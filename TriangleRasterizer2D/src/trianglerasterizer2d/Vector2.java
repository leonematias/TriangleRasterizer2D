package trianglerasterizer2d;

/**
 * A 2D vector
 * 
 * @author matias.leone
 */
public class Vector2 {
    
    public float X;
    public float Y;
    
    public Vector2() {
        X = 0;
        Y = 0;
    }

    public Vector2(float X, float Y) {
        this.X = X;
        this.Y = Y;
    }
    
    public Vector2 set(float X, float Y) {
        this.X = X;
        this.Y = Y;
        return this;
    }
    
    public Vector2 set(Vector2 v) {
        return set(v.X, v.Y);
    }
    
    public static Vector2 interpolate(Vector2 v1, float s1, Vector2 v2, float s2, Vector2 out) {
        out.X = v1.X * s1 + v2.X * s2;
        out.Y = v1.Y * s1 + v2.Y * s2;
        return out;
    }
    
    public static Vector2 interpolate(Vector2 v1, float s1, Vector2 v2, float s2, Vector2 v3, float s3, Vector2 out) {
        out.X = v1.X * s1 + v2.X * s2 + v3.X * s3;
        out.Y = v1.Y * s1 + v2.Y * s2 + v3.Y * s3;
        return out;
    }
    
    public static float length(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }
    
}
