/**
 * A Fractal Explorer created in Java
 * @author Vaseem Naazleen Shaik, shaikvaseemnaazleen@gmail.com
 **/

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.security.Key;
import java.awt.event.*;
import javax.swing.*;


public class FractalExplorer extends JFrame {
	
	static final int WIDTH  = 600;
	static final int HEIGHT = 600;
	
	Canvas canvas;
	BufferedImage fractalImage;
	
	static final int MAX_ITER = 200;
	
	static final double DEFAULT_ZOOM       = 100.0;
	static final double DEFAULT_TOP_LEFT_X = -3.0;
	static final double DEFAULT_TOP_LEFT_Y = +3.0;
	
	double zoomFactor = DEFAULT_ZOOM;
	double topLeftX   = DEFAULT_TOP_LEFT_X;
	double topLeftY   = DEFAULT_TOP_LEFT_Y;

// -------------------------------------------------------------------
	public FractalExplorer() {
		setInitialGUIProperties();
		addCanvas();
		canvas.addKeyStrokeEvents();
		updateFractal();
		this.setVisible(true);
	}
	
// -------------------------------------------------------------------

	public static void main(String[] args) {
		new FractalExplorer();
	}
	
// -------------------------------------------------------------------

	private void addCanvas() {

		canvas = new Canvas();
		fractalImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		canvas.setVisible(true);
		this.add(canvas, BorderLayout.CENTER);

	} // addCanvas

// -------------------------------------------------------------------
		
		private void setInitialGUIProperties() {
			
			this.setTitle("Fractal Explorer");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(WIDTH, HEIGHT);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
			
		} // setInitialGUIProperties

// -------------------------------------------------------------------
	private double getXPos(double x) {
		return x/zoomFactor + topLeftX;
	} // getXPos
// -------------------------------------------------------------------
	private double getYPos(double y) {
		return y/zoomFactor - topLeftY;
	} // getYPos
// -------------------------------------------------------------------
	
	/**
	 * Updates the fractal by computing the number of iterations
	 * for each point in the fractal and changing the color
	 * based on that.
	 **/
	
	public void updateFractal() {
		
		for (int x = 0; x < WIDTH; x++ ) {
			for (int y = 0; y < HEIGHT; y++ ) {
				
				double c_r = getXPos(x);
				double c_i = getYPos(y);
				
				int iterCount = computeIterations(c_r, c_i);
				
				int pixelColor = makeColor(iterCount);
				fractalImage.setRGB(x, y, pixelColor);
				
			}
		}
		
		canvas.repaint();
		
	} // updateFractal
// -------------------------------------------------------------------	
	/** Returns a posterized color based off of the iteration count
	    of a given point in the fractal **/
	private int makeColor( int iterCount ) {
		
		int color = 0b10111010101010111010011; 
		int mask  = 0b000000000000010101110111; 
		int shiftMag = iterCount / 13;
		
		if (iterCount == MAX_ITER) 
			return Color.BLACK.getRGB();
		
		return color | (mask << shiftMag);
		
	} // makeColor

// -------------------------------------------------------------------

	private int computeIterations(double c_r, double c_i) {
		
		/*
		
		Let c = c_r + c_i
		Let z = z_r + z_i
		
		z' = z*z + c
		   = (z_r + z_i)(z_r + z_i) + (c_r + c_i)
		   = z_r² + 2*z_r*z_i - z_i² + c_r + c_i

		     z_r' = z_r² - z_i² + c_r
		     z_i' = 2*z_i*z_r + c_i
		     
		*/

		double z_r = 0.0;
		double z_i = 0.0;
		
		int iterCount = 0;

		// Modulus (distance) formula:
		// √(a² + b²) <= 2.0
		// a² + b² <= 4.0
		while ( z_r*z_r + z_i*z_i <= 4.0 ) {
			
			double z_r_tmp = z_r;
			
			z_r = z_r*z_r - z_i*z_i + c_r;
			z_i = 2*z_i*z_r_tmp + c_i;
			
			// Point was inside the Mandelbrot set
			if (iterCount >= MAX_ITER) 
				return MAX_ITER;
			
			iterCount++;
			
		}
		
		// Complex point was outside Mandelbrot set
		return iterCount;
		
	} // computeIterations
// -------------------------------------------------------------------
	private void moveUp() {
		double curHeight = HEIGHT / zoomFactor;
		topLeftY -= curHeight / 6;
		updateFractal();
	} // moveUp
// -------------------------------------------------------------------
	private void moveDown() {
		double curHeight = HEIGHT / zoomFactor;
		topLeftY += curHeight / 6;
		updateFractal();
	} // moveDown
// -------------------------------------------------------------------
	private void moveLeft() {
		double curWidth = WIDTH / zoomFactor;
		topLeftX += curWidth / 6;
		updateFractal();
	} // moveLeft
// -------------------------------------------------------------------
	private void moveRight() {
		double curWidth = WIDTH / zoomFactor;
		topLeftX -= curWidth / 6;
		updateFractal();
	} // moveRight
// -------------------------------------------------------------------		

	private void adjustZoom( double newX, double newY, double newZoomFactor ) {
		
		topLeftX += newX/zoomFactor;
		topLeftY -= newY/zoomFactor;
		
		zoomFactor = newZoomFactor;
		
		topLeftX -= ( WIDTH/2) / zoomFactor;
		topLeftY += (HEIGHT/2) / zoomFactor;
		
		updateFractal();
		
	} // adjustZoom

// -------------------------------------------------------------------	
	
	private class Canvas extends JPanel implements MouseListener {
		
		public Canvas() {
			addMouseListener(this);
		} 
		
		@Override public Dimension getPreferredSize() {
			return new Dimension(WIDTH, HEIGHT);
		} // getPreferredSize
		
		@Override public void paintComponent(Graphics drawingObj) {
			drawingObj.drawImage( fractalImage, 0, 0, null );
		} // paintComponent
		
		@Override public void mousePressed(MouseEvent mouse) {
			
			double x = (double) mouse.getX();
			double y = (double) mouse.getY();
			
			switch( mouse.getButton() ) {
				
				// Left
				case MouseEvent.BUTTON1:
					adjustZoom( x, y, zoomFactor*2 );
					break;
				
				// Right
				case MouseEvent.BUTTON3:
					adjustZoom( x, y, zoomFactor/2 );
					break;
				
			}
			
		} // mousePressed
		
		public void addKeyStrokeEvents() {
			
			KeyStroke uKey = KeyStroke.getKeyStroke(KeyEvent.VK_U, 0 );
			KeyStroke dKey = KeyStroke.getKeyStroke(KeyEvent.VK_D, 0 );
			KeyStroke lKey = KeyStroke.getKeyStroke(KeyEvent.VK_L, 0 );
			KeyStroke rKey = KeyStroke.getKeyStroke(KeyEvent.VK_R, 0 );
			
			Action uPressed = new AbstractAction() {
				@Override public void actionPerformed(ActionEvent e) {
					moveUp();
				}
			};
			
			Action lPressed = new AbstractAction() {
				@Override public void actionPerformed(ActionEvent e) {
					moveLeft();
				}
			};
			
			Action dPressed = new AbstractAction() {
				@Override public void actionPerformed(ActionEvent e) {
					moveDown();
				}
			};
			
			Action rPressed = new AbstractAction() {
				@Override public void actionPerformed(ActionEvent e) {
					moveRight();
				}
			}; 
			
			this.getInputMap().put( uKey, "u_key" );
			this.getInputMap().put( rKey, "r_key" );
			this.getInputMap().put( lKey, "l_key" );
			this.getInputMap().put( dKey, "d_key" );
			
			this.getActionMap().put( "u_key", uPressed );
			this.getActionMap().put( "r_key", rPressed );
			this.getActionMap().put( "l_key", lPressed );
			this.getActionMap().put( "d_key", dPressed );
			
		} // addKeyStrokeEvents
		
		@Override public void mouseReleased(MouseEvent mouse){ }
		@Override public void mouseClicked(MouseEvent mouse) { }
		@Override public void mouseEntered(MouseEvent mouse) { }
		@Override public void mouseExited (MouseEvent mouse) { }
		
	} // Canvas
	
} // FractalExplorer
