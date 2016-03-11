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
    
}
