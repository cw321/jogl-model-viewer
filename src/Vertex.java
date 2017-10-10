import java.util.ArrayList;
import java.util.List;

public class Vertex {

    // init class vars
    private static int counter = 0;

    public float x, y, z, confidence, intensity;
    public int id;
    public List<Face> adj_face = new ArrayList<>();
    public NormalVector normal;

    public Vertex(float x0, float y0, float z0, float conf0, float int0) {
        // count number of vertices
        id = counter;
        counter++;

        // assign vals from construction
        x = x0;
        y = y0;
        z = z0;
        confidence = conf0;
        intensity = int0;
    }

    public NormalVector calcNormal() {
        float sumx = 0;
        float sumy = 0;
        float sumz = 0;

        for (int i = 0; i < adj_face.size(); i++) {
            sumx += adj_face.get(i).normal.x;
            sumy += adj_face.get(i).normal.y;
            sumz += adj_face.get(i).normal.z;
        }

        NormalVector out = new NormalVector(sumx/adj_face.size(),
                sumy/adj_face.size(),
                sumz/adj_face.size());

        return out;
    }
}
