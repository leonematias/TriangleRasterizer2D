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
        this.W = W;
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
    
    public static Vector4 interpolate(Vector4 v1, float s1, Vector4 v2, float s2, Vector4 out) {
        out.X = v1.X * s1 + v2.X * s2;
        out.Y = v1.Y * s1 + v2.Y * s2;
        out.Z = v1.Z * s1 + v2.Z * s2;
        out.W = v1.W * s1 + v2.W * s2;
        return out;
    }
    
    public static Vector4 interpolate(Vector4 v1, float s1, Vector4 v2, float s2, Vector4 v3, float s3, Vector4 out) {
        out.X = v1.X * s1 + v2.X * s2 + v3.X * s3;
        out.Y = v1.Y * s1 + v2.Y * s2 + v3.Y * s3;
        out.Z = v1.Z * s1 + v2.Z * s2 + v3.Z * s3;
        out.W = v1.W * s1 + v2.W * s2 + v3.W * s3;
        return out;
    }
    
    @Override
    public String toString() {
        return "(" + X + ", " + Y + ", " + Z + ", " + W + ")";
    }

}
