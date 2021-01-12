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

    static final int MAX_ITER = 200;

    static final double DEFAULT_ZOOM = 100.0;
    static final double  DEFAULT_TOP_LEFT_X = -3.0;
    static final double  DEFAULT_TOP_LEFT_Y = -3.0;
    
    
    double zoomFactor = DEFAULT_ZOOM;
    double topLeftX   = DEFAULT_TOP_LEFT_X;
    double topLeftY   = DEFAULT_TOP_LEFT_Y;

    Canvas canvas;
    BufferedImage fractalImage;

    public FractalExplorer() {
        setInitialGUIProperties();
        addCanvas();
        updateFractal();
    }

    private void updateFractal() {

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {

                double c_real = getXPos(x);
                double c_img = getYPos(y);

                int iterCount = computeIterations(c_real, c_img);

                int pixelColor = makeColor(iterCount);
                fractalImage.setRGB(x, y, pixelColor);

            }
        }

        canvas.repaint();
    }

    private int makeColor(int iterCount) {
        if (iterCount == MAX_ITER) {
            return Color.BLACK.getRGB();
        }

        return Color.PINK.getRGB();
    }

    private double getXPos(int x) {
        return x / zoomFactor + topLeftX;
    }

    private double getYPos(int y) {
        return y / zoomFactor - topLeftY;
    }

    private int computeIterations(double c_real, double c_img) {
        /*
            Let c = c_real + c_img
            Let z = z_real + z_img

            z' =  z * z + c
               = (z_real + z_img)(z_real + z_img) + c_real + c_img
               = z_real ** 2 + 2 * z_real * z_img - z_img ** 2 + c_real + c_img
            
            z_real' = z_real ** 2 - z_img ** 2 + c_real
            z_img' = 2 * z_real * z_img + c_img
        */

        double z_real = 0.0;
        double z_img = 0.0;

        int iterCount = 0;

        // calculating modulus or dist from origin  √(a**2 + b**2) <= 2.0 ==> (a**2 + b**2) <= (2.0) ** 2

        while ( z_real * z_real + z_img * z_img <= 4.0) {
            double z_real_temp = z_real;

            z_real = z_real * z_real - z_img * z_img + c_real;
            z_img = 2 * z_real_temp * z_img + c_img;

            if (iterCount >= MAX_ITER) {
                return MAX_ITER;
            }

            iterCount++;

        }

        return iterCount;
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
