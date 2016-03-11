package trianglerasterizer2d;

/**
 * A 4D vector or RGBA color
 * 
 * @author matias.leone
 */
public class Vector4 {
    
    public float X;
    public float Y;
    public float Z;
    public float W;
    
    public Vector4() {
        X = 0;
        Y = 0;
        Z = 0;
        W = 0;
    }

    public Vector4(float X, float Y, float Z, float W) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.W = 0;
    }
    
    public Vector4 set(float X, float Y, float Z, float W) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.W = W;
        return this;
    }
    
    public Vector4 set(Vector4 v) {
        return set(v.X, v.Y, v.Z, v.W);
    }
    
    public Vector4 mul(float s) {
        X *= s;
        Y *= s;
        Z *= s;
        W *= s;
        return this;
    }

}
