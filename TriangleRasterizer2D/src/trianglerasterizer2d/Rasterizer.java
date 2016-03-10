package trianglerasterizer2d;

import java.awt.Color;
import java.util.Arrays;


/**
 *
 * @author matias.leone
 */
public class Rasterizer {
    
    private int screenWidth;
    private int screenHeight;
    private int[] renderBuffer;
    
    public Rasterizer(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.renderBuffer = new int[screenWidth * screenHeight];
    }
    
    int offset = 0;
    
    public int[] render() {
        
        int clearColor = Color.WHITE.getRGB();
        clear(clearColor);
        
        int color = Color.RED.getRGB();
        for (int i = offset; i < offset + 100; i++) {
            for (int j = offset; j < offset + 100; j++) {
                renderBuffer[i * screenWidth + j] = color;
            }
        }
        
        offset += 10;
        offset %= 500;
        
        return renderBuffer;
    }
    
    private void clear(int color) {
        Arrays.fill(renderBuffer, color);
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
    
    
    
}
