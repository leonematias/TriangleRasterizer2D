package trianglerasterizer2d;

/**
 * A vertex from a triangle
 * 
 * @author matias.leone
 */
public class Vertex {
    
    public Vector3 position;
    public Vector4 color;
    public Vector3 normal;
    public Vector2 texCoord;

    public Vertex() {
        this.position = new Vector3();
        this.color = new Vector4();
        this.normal = new Vector3();
        this.texCoord = new Vector2();
    }
    
    public Vertex(Vector3 position, Vector4 color, Vector3 normal, Vector2 texCoord) {
        this.position = position;
        this.color = color;
        this.normal = normal;
        this.texCoord = texCoord;
    }
    
    public Vertex(float x, float y, float z, float r, float g, float b, float a, float nx, float ny, float nz, float u, float v) {
        this(new Vector3(x, y, z), new Vector4(r, g, b, a), new Vector3(nx, ny, nz), new Vector2(u, v));
    }

    @Override
    public String toString() {
        return position.toString();
    }
    
    
    
}
