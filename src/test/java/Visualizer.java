import me.dags.noise.Module;
import me.dags.noise.Source;
import me.dags.noise.domain.Domain;
import me.dags.noise.source.Line;
import me.dags.noise.util.NoiseUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class Visualizer {

    private static final JSlider scale = slider(8, 1, 100);
    private static final JSlider octaves = slider(1, 1, 5);
    private static final JSlider distance = slider(16, 0, 200);
    private static final FloatSlider gain = slider(5, 1, 100, 10);
    private static final FloatSlider lacunarity = slider(20, 1, 100, 10);
    private static final JComboBox<Source> type = new JComboBox<>(Source.values());
    private static final ImageIcon icon = new ImageIcon();
    private static final JLabel view = new JLabel(icon);

    static {
        type.setPreferredSize(new Dimension(200, 20));
        type.setSelectedItem(Source.PERLIN);
        type.addActionListener(e -> render(null));
    }

    private static float posX = 0F;
    private static float posZ = 0F;
    private static int zoom = 1;

    public static void main(String[] args) {
        render(null);

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{100, 200};

        JLabel source = new JLabel("Source ");
        source.setPreferredSize(new Dimension(100, 20));

        JPanel panel = new JPanel();
        panel.add(source);

        JPanel controls = new JPanel(layout);
        add(controls, panel, 0, 0);
        add(controls, type, 1, 0);
        add(controls, label("Scale", scale), 0, 1);
        add(controls, scale, 1, 1);
        add(controls, label("Octaves", octaves), 0, 2);
        add(controls, octaves, 1, 2);
        add(controls, label("Gain", gain), 0, 3);
        add(controls, gain, 1, 3);
        add(controls, label("Lacunarity", lacunarity), 0, 4);
        add(controls, lacunarity, 1, 4);
        add(controls, label("Distance", distance), 0, 5);
        add(controls, distance, 1, 5);

        JPanel root = new JPanel();
        root.setAlignmentY(JPanel.TOP_ALIGNMENT);
        root.add(view);
        root.add(controls);

        input(view, "W", "up", move(0, -1));
        input(view, "A", "left", move(-1, 0));
        input(view, "S", "down", move(0, 1));
        input(view, "D", "right", move(1, 0));
        input(view, "Q", "zoomIn", zoom(1));
        input(view, "Z", "zoomOut", zoom(-1));

        JFrame frame = new JFrame();
        frame.add(root);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void add(JPanel root, Component component, int x, int y) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        root.add(component, constraints);
    }

    private static Component label(String text, JSlider slider) {
        return label(text, "", slider);
    }

    private static Component label(String text, String hover, JSlider slider) {
        JLabel label = new JLabel(text);
        label.setToolTipText(hover);
        label.createToolTip().setTipText("pls");

        label.setPreferredSize(new Dimension(60, 20));

        JLabel value = new JLabel(slider.toString());
        value.setPreferredSize(new Dimension(40, 20));

        slider.addChangeListener(e -> value.setText(slider.toString()));

        JPanel root = new JPanel();
        root.add(label);
        root.add(value);

        return root;
    }

    private static FloatSlider slider(int value, int min, int max, int scale) {
        FloatSlider slider = new FloatSlider(scale);
        slider.setValue(value);
        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.addChangeListener(Visualizer::render);
        slider.setPreferredSize(new Dimension(200, 20));
        return slider;
    }

    private static IntSlider slider(int value, int min, int max) {
        IntSlider slider = new IntSlider();
        slider.setValue(value);
        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.addChangeListener(Visualizer::render);
        slider.setPreferredSize(new Dimension(200, 20));
        return slider;
    }

    private static void render(ChangeEvent event) {
        int scale = Visualizer.scale.getValue();
        int octaves = Visualizer.octaves.getValue();
        int strength = Visualizer.distance.getValue();
        float gain = Visualizer.gain.getValueF();
        float lacunarity = Visualizer.lacunarity.getValueF();
        Source noiseType = (Source) type.getSelectedItem();
        if (noiseType == null) {
            return;
        }

        Module x = Source.build(456, scale, octaves)
                .lacunarity(lacunarity)
                .gain(gain)
                .build(noiseType);

        Module y = Source.build(789, scale, octaves)
                .lacunarity(lacunarity)
                .gain(gain)
                .build(noiseType);

        Module source = Source.cell(123, 180)
                .warp(Domain.warp(x, y, Source.constant(strength)).cache());

        Domain domain = Domain.warp(Source.CUBIC, 532, 25, 1, 50)
                .then(Domain.warp(134, 200, 1, 100))
                .then(Domain.warp(134, 500, 1, 200));

        BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        visit(image, (img, ix, iy, px, pz) -> {
            float pxw = domain.getX(px, pz);
            float pyw = domain.getY(px, pz);

            float value = source.getValue(pxw, pyw);

            int color = shade(value, 0, 1);

            img.setRGB(ix, iy, color);
        });

        icon.setImage(image);
        view.repaint();
        view.requestFocusInWindow();
    }

    private static void visit(BufferedImage img, Visitor visitor) {
        int centerX = img.getWidth() / 2 / zoom;
        int centerY = img.getHeight() / 2 / zoom;

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int sx = x / zoom;
                int sz = y / zoom;
                float px = sx + posX - centerX;
                float pz = sz + posZ - centerY;
                visitor.visit(img, x, y, px, pz);
            }
        }
    }

    private static int color(float value, float min, float max) {
        float hue = min + (value * (max - min));
        return Color.HSBtoRGB(hue, 0.8F, 0.8F);
    }

    private static int shade(float value, float min, float max) {
        value = NoiseUtil.clamp(value, min, max);
        float brightness = min + (value * (max - min));
        return Color.HSBtoRGB(0F, 0F, brightness);
    }

    private static void input(JComponent comp, String key, String name, Action action) {
        comp.getInputMap().put(KeyStroke.getKeyStroke(key), name);
        comp.getActionMap().put(name, action);
    }

    private static Action move(int dx, int dy) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                posX += dx * Math.max(1, 8 - zoom);
                posZ += dy * Math.max(1, 8 - zoom);
                render(null);
            }
        };
    }

    private static Action zoom(int direction) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoom = Math.max(1, zoom + direction);
                render(null);
            }
        };
    }

    private static class IntSlider extends JSlider {
        @Override
        public String toString() {
            return String.format("%02d", getValue());
        }
    }

    private static class FloatSlider extends JSlider {

        private final float scale;

        private FloatSlider(float scale) {
            this.scale = scale;
        }

        public float getValueF() {
            return super.getValue() / scale;
        }

        @Override
        public String toString() {
            return String.format("%.1f", getValueF());
        }
    }

    public interface Visitor {

        void visit(BufferedImage img, int ix, int iy, float px, float pz);
    }
}
