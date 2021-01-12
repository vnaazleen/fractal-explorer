/**
 * A Fractal Explorer created in Java
 * @author Vaseem Naazleen Shaik, shaikvaseemnaazleen@gmail.com
 **/

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;

public class FractalExplorer extends JFrame{

    static final int WIDTH = 600;
    static final int HEIGHT = 600;

    Canvas canvas;
    BufferedImage fractalImage;

    public FractalExplorer() {
        setInitialGUIProperties();
        addCanvas();
    }

    public void setInitialGUIProperties() {

        // properties of window
        this.setTitle("Fractal Explorer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    private void addCanvas() {

        canvas = new Canvas();
        fractalImage = new BufferedImage(HEIGHT, WIDTH, BufferedImage.TYPE_INT_RGB);
        canvas.setVisible(true);
        this.add(canvas, BorderLayout.CENTER);

    }

    public static void main(String[] args) {

        new FractalExplorer();

    }

    private class Canvas extends JPanel {

        @Override public Dimension getPreferredSize() {
            return new Dimension(WIDTH, HEIGHT);
        }

        @Override public void paintComponent(Graphics drawingObj) {
            drawingObj.drawImage(fractalImage, 0, 0, null);
        }

    }
}
