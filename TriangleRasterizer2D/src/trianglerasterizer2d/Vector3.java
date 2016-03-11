package trianglerasterizer2d;

/**
 * A 3D vector
 * 
 * @author matias.leone
 */
public class Vector3 {
    
    public float X;
    public float Y;
    public float Z;
    
    public Vector3() {
        X = 0;
        Y = 0;
        Z = 0;
    }

    public Vector3(float X, float Y, float Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }
    
    public Vector3 set(float X, float Y, float Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        return this;
    }
    
    public Vector3 set(Vector3 v) {
        return set(v.X, v.Y, v.Z);
    }
    
    public static float dot(Vector3 u, Vector3 v) {
        return u.X * v.X + u.Y * v.Y + u.Z * v.Z;
    }
    
    public static float lengthSq(Vector3 v) {
        return v.X * v.X + v.Y * v.Y + v.Z * v.Z;
    }
    
    public static float length(Vector3 v) {
        return (float)Math.sqrt(v.X * v.X + v.Y * v.Y + v.Z * v.Z);
    }
    
    public static Vector3 cross(Vector3 v1, Vector3 v2, Vector3 out) {
        out.set(v1.Y*v2.Z - v1.Z*v2.Y, 
                v1.Z*v2.X - v1.X*v2.Z, 
                v1.X*v2.Y - v1.Y*v2.X);
        return out;
    }
    
    public Vector3 add(float s) {
        X += s;
        Y += s;
        Z += s;
        return this;
    }
    
    public Vector3 sub(float s) {
        X -= s;
        Y -= s;
        Z -= s;
        return this;
    }
    
    public Vector3 mul(float s) {
        X *= s;
        Y *= s;
        Z *= s;
        return this;
    }
    
    public Vector3 div(float s) {
        X /= s;
        Y /= s;
        Z /= s;
        return this;
    }
    
    public Vector3 normalize() {
        float l = length(this);
        div(l);
        return this;
    }
    
    public float get(int i) {
        if(i == 0) return X;
        if(i == 1) return Y;
        if(i == 2) return Z;
        throw new IndexOutOfBoundsException("Invalid index: " + i);
    }
    
}
