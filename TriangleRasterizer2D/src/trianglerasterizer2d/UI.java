package trianglerasterizer2d;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 *
 * @author matias.leone
 */
public class UI {

    private final static int WIN_WIDTH = 1200;
    private final static int WIN_HEIGHT = 720;
    
    private JFrame frame;
    private JTextArea logArea;
    private Canvas renderPanel;
    private BufferedImage renderImg;
    private Graphics2D renderG;
    private Dimension graphDim;
    
    
    private Rasterizer rasterizer;
    
    public static void main(String[] args) {
        new UI().renderLoop();
    }
    
    public UI() {
        frame = new JFrame("Triangle Rasterizer 2D - Matias Leone");
        frame.setMinimumSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        renderPanel = new Canvas();
        frame.add(renderPanel, BorderLayout.CENTER);
        
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenDim.width / 2 - WIN_WIDTH / 2, screenDim.height / 2 - WIN_HEIGHT / 2);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        frame.setVisible(true);
        
        
        //Create rasterizer
        graphDim = renderPanel.getSize();
        renderImg = (BufferedImage)renderPanel.createImage(graphDim.width, graphDim.height);
        renderG = renderImg.createGraphics();
        int[] pixelData = ((DataBufferInt)renderImg.getRaster().getDataBuffer()).getData();
        rasterizer = new Rasterizer(graphDim.width, graphDim.height, pixelData);
        
    }
    
    /**
     * Do render loop
     */
    public void renderLoop() {
        init();
        
        long startTime = System.nanoTime();
        long currentTime;
	float lastFps = startTime;
        int frameCount = 0;
        float elapsedTime = 0;
        int framesPerSecond = 0;
        Graphics g = renderPanel.getGraphics();
        
        while(true) {

            //Compute elapsed time and FPS
            currentTime = System.nanoTime();
            elapsedTime = (currentTime - startTime) / 1000000000.0f;
            startTime = currentTime;
            if(currentTime - lastFps >= 1000000000) {
		framesPerSecond = frameCount;
		frameCount = 0;
		lastFps = currentTime;
            }
            frameCount++;
            
            //Render
            render(elapsedTime);
            
            //Draw FPS
            renderG.setColor(Color.BLACK);
            renderG.drawString("FPS: " + framesPerSecond, 10, 20);
            
            //Draw buffer
            g.drawImage(renderImg, 0, 0, null);
            
            
            Thread.yield();
	} 
    }
    
    
    
    
    
    //Scene data
    private Vertex[] vertices = new Vertex[2 * 3];
    private int sizeCount = 0;
    private float sizeSpeed = 100;
    private float dir = 1;
    private Triangle triangle;
    
    
    /**
     * Init scene
     */
    private void init() {
        int i = 0;
        
        Vector4 red = new Vector4(1, 0, 0, 0);
        Vector4 green = new Vector4(0, 1, 0, 0);
        Vector4 blue = new Vector4(0, 0, 1, 0);
        Vector4 yellow = new Vector4(1, 1, 0, 0);
        
        vertices[i++] = new Vertex(new Vector3(300, 400, 0), yellow, new Vector3(), new Vector2());
        vertices[i++] = new Vertex(new Vector3(300, 100, 0), red, new Vector3(), new Vector2());
        vertices[i++] = new Vertex(new Vector3(600, 100, 0), green, new Vector3(), new Vector2());
        
        vertices[i++] = new Vertex(new Vector3(600, 100, 0), green, new Vector3(), new Vector2());
        vertices[i++] = new Vertex(new Vector3(600, 400, 0), blue, new Vector3(), new Vector2());
        vertices[i++] = new Vertex(new Vector3(300, 400, 0), yellow, new Vector3(), new Vector2());
        
        
        triangle = new Triangle(
                new Vector3(500, 0, 0), new Vector4(1, 0, 0, 1),
                new Vector3(WIN_WIDTH + 200, 500, 0), new Vector4(0, 1, 0, 1),
                new Vector3(500, 500, 0), new Vector4(0, 0, 1, 1)
        );
    }
    
    
    /**
     * Main render method
     */
    private void render(float elapsedTime) {
        rasterizer.clear(Rasterizer.WHITE);
        

        //Change size
        sizeCount++;
        if(sizeCount > 350) {
            sizeCount = 0;
            dir *= -1;
        }
        float change = sizeSpeed * elapsedTime;
        
        vertices[1].position.X += change * -dir;
        vertices[1].position.Y += change * -dir;
        
        vertices[2].position.X += change * dir;
        vertices[2].position.Y += change * -dir;
        vertices[3].position.X += change * dir;
        vertices[3].position.Y += change * -dir;
        
        vertices[0].position.X += change * -dir;
        vertices[0].position.Y += change * dir;
        vertices[5].position.X += change * -dir;
        vertices[5].position.Y += change * dir;
        
        vertices[4].position.X += change * dir;
        vertices[4].position.Y += change * dir;
        
        
        
        
        //Render triangles
        for (int i = 0; i < vertices.length; i += 3) {
            rasterizer.drawTriangle(vertices[i], vertices[i + 1], vertices[i + 2]);
        }
        
        
        //rasterizer.drawTriangle(triangle.a, triangle.b, triangle.c);
        
    }
    
    private class Triangle {
        public Vertex a;
        public Vertex b;
        public Vertex c;

        public Triangle(Vertex a, Vertex b, Vertex c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
        
        public Triangle(Vector3 aPos, Vector4 aColor, Vector3 bPos, Vector4 bColor, Vector3 cPos, Vector4 cColor) {
            this(
                    new Vertex(aPos, aColor, new Vector3(), new Vector2()),
                    new Vertex(bPos, bColor, new Vector3(), new Vector2()),
                    new Vertex(cPos, cColor, new Vector3(), new Vector2())
            );
        }
    }

}
