package trianglerasterizer2d;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author matias.leone
 */
public class Rasterizer {
    
    public final static int WHITE = Color.WHITE.getRGB();
    
    private int screenWidth;
    private int screenHeight;
    private int[] renderBuffer;
    private TriangleClipping clipping;
    
    private Vector3 barycentric;
    private Vector4 vColor;
    
    public Rasterizer(int screenWidth, int screenHeight, int[] renderBuffer) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.renderBuffer = renderBuffer;
        this.clipping = new TriangleClipping(screenWidth, screenHeight);
        
        barycentric = new Vector3();
        vColor = new Vector4();
    }

    public void drawTriangle(Vertex a, Vertex b, Vertex c) {
        TriangleClipping.Result res = clipping.clip(a, b, c);
        for (int i = 0; i < res.trianglesCount; i++) {
            doDrawTriangle(res.vertexList[i * 3], res.vertexList[i * 3 + 1], res.vertexList[i * 3 + 2]);
        }
    }
    
    private void doDrawTriangle(Vertex a, Vertex b, Vertex c) {
        //Compute triangle's bounding box
        int xMin = (int)min(a.position.X, b.position.X, c.position.X);
        int xMax = (int)max(a.position.X, b.position.X, c.position.X);
        int yMin = (int)min(a.position.Y, b.position.Y, c.position.Y);
        int yMax = (int)max(a.position.Y, b.position.Y, c.position.Y);
        
        /*
        //Clipping
        if(xMin < 0 || yMin < 0 || xMax >= screenWidth || yMax >= screenHeight)
            return;
        */
        
        //Scan line rendering
        int index = 0;
        for (int y = yMin; y < yMax; y++) {
            index = y * screenWidth;
            for (int x = xMin; x < xMax; x++) {
                
                computeBarycentric(a.position.X, a.position.Y, b.position.X, b.position.Y, c.position.X, c.position.Y, x, y, barycentric);
                
                //Inside triangle
                if((barycentric.X >= 0) && (barycentric.Y >= 0) && (barycentric.X + barycentric.Y <= 1)) {
                    
                    //Gouraud shading
                    /*
                    vColor.set(
                            barycentric.X * a.color.X + barycentric.Y * b.color.X + barZ * c.color.X,
                            barycentric.X * a.color.Y + barycentric.Y * b.color.Y + barZ * c.color.Y,
                            barycentric.X * a.color.Z + barycentric.Y * b.color.Z + barZ * c.color.Z,
                            barycentric.X * a.color.W + barycentric.Y * b.color.W + barZ * c.color.W
                    );
                    */
                    Vector4.interpolate(a.color, barycentric.X, b.color, barycentric.Y, c.color, barycentric.Z, vColor);
                    int color = vector4ToAWTColor(vColor);
                    
                    
                    renderBuffer[index + x] = color;

                    
                }
            }
        }
        
    }
    
    
    public void clear(int color) {
        Arrays.fill(renderBuffer, color);
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
    
    private float min(float a, float b, float c) {
        return Math.min(Math.min(a, b), c);
    }
    
    private float max(float a, float b, float c) {
        return Math.max(Math.max(a, b), c);
    }
    
    public static Vector3 computeBarycentric(float v1x, float v1y, float v2x, float v2y, float v3x, float v3y , float px, float py, Vector3 out) {
	float A = v1x - v3x;
	float B = v2x - v3x;
	float C = v3x - px;
        
	float D = v1y - v3y;
	float E = v2y - v3y;
	float F = v3y - py;
        
	float G = 0;
	float H = 0;
	float I = 0;
        
	if(A == 0 && B == 0) {
		float temp = A;
		A = D;
		D = temp;
                
		temp = B;
		B = E;
		E = temp;
                
		temp = C;
		C = F;
		F = temp;
	}
	out.X = (B*(F+I) - C*(E+H)) / (A*(E+H) - B*(D+G));
	out.Y = (A*(F+I) - C*(D+G)) / (B*(D+G) - A*(E+H));
        out.Z = 1 - out.X - out.Y;
	return out;
    }
    
    private int vector4ToAWTColor(Vector4 v) {
        int r = (int)(v.X * 255);
        int g = (int)(v.Y * 255);
        int b = (int)(v.Z * 255);
        int a = (int)(v.W * 255);
        return ((a & 0xFF) << 24) |((r & 0xFF) << 16) | ((g & 0xFF) << 8)  | ((b & 0xFF) << 0);
    }
    

    
    
}
