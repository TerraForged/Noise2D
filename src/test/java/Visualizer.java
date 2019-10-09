import me.dags.noise.Module;
import me.dags.noise.Source;
import me.dags.noise.source.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;

public class Visualizer {

    private static final JSlider scale = slider(8, 1, 50);
    private static final JSlider octaves = slider(1, 1, 5);
    private static final JSlider strength = slider(16, 0, 100);
    private static final JComboBox<String> type = new JComboBox<>();
    private static final ImageIcon icon = new ImageIcon();
    private static final JLabel view = new JLabel(icon);
    private static final Map<String, Class<? extends Module>> TYPES = new LinkedHashMap<String, Class<? extends Module>>(){{
        put("billow", FastBillow.class);
        put("cell", FastCell.class);
        put("cubic", FastCubic.class);
        put("perlin", FastPerlin.class);
        put("ridge", FastRidge.class);
        put("simplex", FastSimplex.class);
    }};

    static {
        type.setPreferredSize(new Dimension(200, 20));
        type.setModel(new DefaultComboBoxModel<>(TYPES.keySet().toArray(new String[0])));
        type.setSelectedItem("perlin");
        type.addActionListener(e -> render(null));
    }

    private static float posX = 0F;
    private static float posZ = 0F;
    private static int zoom = 1;

    public static void main(String[] args) {
        render(null);

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{80, 200};

        JLabel source = new JLabel("Source ");
        source.setPreferredSize(new Dimension(80, 20));

        JPanel panel = new JPanel();
        panel.add(source);

        JPanel controls = new JPanel(layout);
        add(controls, panel, 0, 0);
        add(controls, type, 1, 0);
        add(controls, label("Scale", scale), 0, 1);
        add(controls, scale, 1, 1);
        add(controls, label("Octaves", octaves), 0, 2);
        add(controls, octaves, 1, 2);
        add(controls, label("Distance", strength), 0, 3);
        add(controls, strength, 1, 3);

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
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(60, 20));

        JLabel value = new JLabel(String.format("%02d", slider.getValue()));
        value.setPreferredSize(new Dimension(20, 20));

        slider.addChangeListener(e -> value.setText(String.format("%02d", slider.getValue())));

        JPanel root = new JPanel();
        root.add(label);
        root.add(value);

        return root;
    }

    private static JSlider slider(int value, int min, int max) {
        JSlider slider = new JSlider();
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
        int strength = Visualizer.strength.getValue();
        Class<? extends Module> noiseType = TYPES.get(type.getSelectedItem() + "");

        Module x = Source.build(456, scale, octaves).build(noiseType);
        Module y = Source.build(789, scale, octaves).build(noiseType);
        Module source = Source.cell(123, 180)
                .warp(101112, 90, 1, 70)
                .warp(x, y, strength);
        BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);

        visit(image, (img, ix, iy, px, pz) -> {
            float value = source.getValue(px, pz);
            int color = color(value, 0, 0.7F);
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
        return Color.HSBtoRGB(hue, 0.7F, 0.7F);
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

    public interface Visitor {

        void visit(BufferedImage img, int ix, int iy, float px, float pz);
    }
}
