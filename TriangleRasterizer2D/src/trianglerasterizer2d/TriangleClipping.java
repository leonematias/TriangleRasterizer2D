package trianglerasterizer2d;

import java.util.Arrays;
import static trianglerasterizer2d.Rasterizer.computeBarycentric;

/**
 *
 * @author matias.leone
 */
public class TriangleClipping {
    
    private final static int MAX_VERT = 10;
    private final static int MAX_TRI = 4;
    
    private float maxX;
    private float maxY;
    private Vertex[] inputList;
    private Vertex[] outputList;
    private Vertex[] auxVertexList;
    private int inId;
    private int outId;
    private int auxId;
    private Result result;
    private Vector3 barycentric;

        
    private enum ClipEdge {LEFT, TOP, RIGHT, BOTTOM}

    public TriangleClipping(float maxX, float maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
        
        this.inputList = new Vertex[MAX_VERT];
        this.outputList = new Vertex[MAX_VERT];
        this.auxVertexList = new Vertex[MAX_VERT];
        for (int i = 0; i < this.auxVertexList.length; i++) {
            this.auxVertexList[i] = new Vertex();
        }
        this.result = new Result();
        this.barycentric = new Vector3();
    }

    
    public Result clip(Vertex a, Vertex b, Vertex c) {        
        auxId = 0;
        outId = resetList(outputList);
        outputList[0] = a;
        outputList[1] = b;
        outputList[2] = c;
        outId = 3;
        
        //Sutherland–Hodgman
        for (ClipEdge clipEdge : ClipEdge.values()) {
            inId = setList(outputList, inputList, outId);
            outId = resetList(outputList);
            if(inId > 0) {
                Vertex s = inputList[inId - 1]; //last
                for (int i = 0; i < inId; i++) {
                    Vertex e = inputList[i];
                    if(inside(e, clipEdge)) {
                        if(!inside(s, clipEdge)) {
                            outputList[outId++] = intersect(s, e, clipEdge, auxVertexList[auxId++]);
                        }
                        outputList[outId++] = e;
                    } else if(inside(s, clipEdge)) {
                        outputList[outId++] = intersect(s, e, clipEdge, auxVertexList[auxId++]);
                    }
                    s = e;
                }
            }
        }
        
        //Split into many triangles
        tesselatte();
        
        //Interpolate vertex values        
        for (int i = 0; i < outId; i++) {
            interpolate(result.vertexList[i], a, b, c);
        }
        
        return result;
    }
    
    private boolean inside(Vertex v, ClipEdge edge) {
        switch(edge) {
            case LEFT:
                return v.position.X >= 0;
            case TOP:
                return v.position.Y >= 0;
            case RIGHT:
                return v.position.X < maxX;
            case BOTTOM:
                return v.position.Y < maxY;
        }
        return false;
    }
    
    private int setList(Vertex[] src, Vertex[] dst, int length) {
        System.arraycopy(src, 0, dst, 0, length);
        return length;
    }
    
    private int resetList(Vertex[] list) {
        Arrays.fill(outputList, null);
        return 0;
    }
    
    private Vertex intersect(Vertex v1, Vertex v2, ClipEdge edge, Vertex out) {
        switch(edge) {
            case LEFT:
                return intersectX(v1, v2, 0, out);
            case TOP:
                return intersectY(v1, v2, 0, out);
            case RIGHT:
                return intersectX(v1, v2, maxX, out);
            case BOTTOM:
                return intersectY(v1, v2, maxY, out);
        }
        return null;
    }
    
    private Vertex intersectX(Vertex v1, Vertex v2, float x, Vertex out) {
        float dx = v2.position.X - v1.position.X;
        float dy = v2.position.Y - v1.position.Y;
        
        //Edge equation: y = a * x + b
        float y = 0;
        if(dx != 0) {
            float a = dy / dx;
            float b = v1.position.Y - a * v1.position.X;
        
            //Compute y for the given x
            y = a * x + b;
            
        } else {
            throw new RuntimeException("Invalid dx: " + dx);
        }
        
        out.position.X = x;
        out.position.Y = y;
        return out;
        
        /*
        //Interpolate values in intersection point
        return interpolate(v1, v2, x, y, out);
        */
    }
    
    private Vertex intersectY(Vertex v1, Vertex v2, float y, Vertex out) {
        float dx = v2.position.X - v1.position.X;
        float dy = v2.position.Y - v1.position.Y;
        
        //Edge equation: y = a * x + b
        float a = 0;
        float b = 0;
        float x = 0;
        if(dx != 0) {
            a = dy / dx;
            b = v1.position.Y - a * v1.position.X;
            
            //Compute x for the given y
            x = y - b;
            if(a != 0) {
                x /= a;
            }
        } else {
            x = v1.position.X;
        }
        
        out.position.X = x;
        out.position.Y = y;
        return out;
        
        /*
        //Interpolate values in intersection point
        return interpolate(v1, v2, x, y, out);
                */
    }
    
    private void interpolate(Vertex v, Vertex a, Vertex b, Vertex c) {
        computeBarycentric(a.position.X, a.position.Y, b.position.X, b.position.Y, c.position.X, c.position.Y, v.position.X, v.position.Y, barycentric);
        
        //TODO: interpolate Z
        v.position.Z = 0;
        
        //Interpolate color
        Vector4.interpolate(a.color, barycentric.X, b.color, barycentric.Y, c.color, barycentric.Z, v.color);
        
        //Interpolate normal
        Vector3.interpolate(a.normal, barycentric.X, b.normal, barycentric.Y, c.normal, barycentric.Z, v.normal);
        
        //Interpolate texCoord
        Vector2.interpolate(a.texCoord, barycentric.X, b.texCoord, barycentric.Y, c.texCoord, barycentric.Z, v.texCoord);
    }
    
    private Vertex interpolate(Vertex v1, Vertex v2, float interX, float interY ,Vertex out) {
        //Compute barycentric on edge
        float length = Vector2.length(v1.position.X, v1.position.Y, v2.position.X, v2.position.Y);
        float lengthV1 =  Vector2.length(v1.position.X, v1.position.Y, interX, interY);
        float s1 = lengthV1 / length;
        float s2 = 1 - s1;
        
        //New position
        out.position.X = interX;
        out.position.Y = interY;
        out.position.Z = 0; //TODO: interpolate Z
        
        //Interpolate color
        Vector4.interpolate(v1.color, s1, v2.color, s2, out.color);
        
        //Interpolate normal
        Vector3.interpolate(v1.normal, s1, v2.normal, s2, out.normal);
        
        //Interpolate texCoord
        Vector2.interpolate(v1.texCoord, s1, v2.texCoord, s2, out.texCoord);
        
        return out;
    }
    
    private void tesselatte() {
        if(outId == 3) {
            result.trianglesCount = 1;
            result.vertexList[0] = outputList[0];
            result.vertexList[1] = outputList[1];
            result.vertexList[2] = outputList[2];
            
        } else if(outId == 4) {
            result.trianglesCount = 2;
            result.vertexList[0] = outputList[0];
            result.vertexList[1] = outputList[1];
            result.vertexList[2] = outputList[2];
            
            result.vertexList[3] = outputList[0];
            result.vertexList[4] = outputList[2];
            result.vertexList[5] = outputList[3];
            
        } else if(outId == 5) {
            result.trianglesCount = 3;
            result.vertexList[0] = outputList[0];
            result.vertexList[1] = outputList[1];
            result.vertexList[2] = outputList[2];
            
            result.vertexList[3] = outputList[0];
            result.vertexList[4] = outputList[2];
            result.vertexList[5] = outputList[3];
            
            result.vertexList[6] = outputList[0];
            result.vertexList[7] = outputList[3];
            result.vertexList[8] = outputList[4];
            
        } else if(outId == 6) {
            result.trianglesCount = 4;
            
            result.trianglesCount = 3;
            result.vertexList[0] = outputList[0];
            result.vertexList[1] = outputList[1];
            result.vertexList[2] = outputList[2];
            
            result.vertexList[3] = outputList[0];
            result.vertexList[4] = outputList[2];
            result.vertexList[5] = outputList[3];
            
            result.vertexList[6] = outputList[0];
            result.vertexList[7] = outputList[3];
            result.vertexList[8] = outputList[4];
            
            result.vertexList[9] = outputList[0];
            result.vertexList[10] = outputList[4];
            result.vertexList[11] = outputList[5];
        }
    }
    
    public static class Result {
        public final Vertex[] vertexList;
        public int trianglesCount;
        
        public Result() {
            vertexList = new Vertex[MAX_TRI * 3];
            for (int i = 0; i < vertexList.length; i++) {
                vertexList[i] = new Vertex();
            }
            trianglesCount = 0;
        }
    }
    
}
