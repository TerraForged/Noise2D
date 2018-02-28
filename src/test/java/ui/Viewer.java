package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dags <dags@dags.me>
 */
public class Viewer extends JFrame implements KeyListener {

    private static final int xInc = 10;
    private static final int zInc = 10;

    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final JLabel label = new JLabel();
    private final BufferedImage image;

    private boolean rendering = false;
    private int xOffset = 0;
    private int zOffset = 0;
    private Renderer renderer = (img, x, z) -> {};

    public Viewer(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.label.addKeyListener(this);
        this.addKeyListener(this);
        this.add(label);
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public synchronized void setRenderer(Renderer renderer) {
        this.renderer = renderer;
        doUpdate();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (rendering) {
            return;
        }
        switch (keyEvent.getKeyCode()) {
            case 38:
                zOffset -= zInc;
                doUpdate();
                return;
            case 40:
                zOffset += zInc;
                doUpdate();
                return;
            case 37:
                xOffset -= xInc;
                doUpdate();
                return;
            case 39:
                xOffset += xInc;
                doUpdate();
                return;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    public static void clear(BufferedImage image) {
        Graphics2D g = image.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();
    }

    private void doUpdate() {
        if (rendering) {
            return;
        }

        rendering = true;
        final int x = xOffset;
        final int z = zOffset;
        executor.submit(() -> {
            try {
                renderer.accept(image, x, z);
                setImage(image);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private synchronized void setImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        SwingUtilities.invokeLater(() -> {
            label.setIcon(icon);
            label.repaint();
            rendering = false;
        });
    }

    public interface Renderer {

        void accept(BufferedImage buffer, int xOff, int zOff);
    }
}
