public class Face {

    // init class vars
    private static int counter = 0;

    public int id, vertices;
    public Vertex[] vertex_list;
    public NormalVector normal;

    public Face(int v0, Vertex[] v_list0, Reader r0) {
        // count number of faces
        id = counter;
        counter++;

        // save vals from construction
        vertices = v0;
        vertex_list = new Vertex[vertices];

        // deep copy values
        for (int i = 0; i < vertices; i++) {
            vertex_list[i] = v_list0[i];
        }

        normal = this.calcNormal();
    }

    public NormalVector calcNormal() {
        Vertex v1 = vertex_list[0];
        Vertex v2 = vertex_list[1];
        Vertex v3 = vertex_list[2];
        float u1, u2, u3, w1, w2, w3, outx, outy, outz;

        // get the values of v1 - v2 and v2 - v3
        u1 = v1.x - v2.x; w1 = v2.x - v3.x;
        u2 = v1.y - v2.y; w2 = v2.y - v3.y;
        u3 = v1.z - v2.z; w3 = v2.z - v3.z;

        // cross product them
        outx = u2 * w3 - w2 * u3;
        outy = w1 * u3 - u1 * w3;
        outz = u1 * w2 - u2 * w1;

        // normalize the length
        double len = Math.sqrt(Math.pow(outx, 2) + Math.pow(outy, 2) + Math.pow(outz, 2));
        outx /= len;
        outy /= len;
        outz /= len;

        NormalVector out = new NormalVector(outx, outy, outz);

        return out;
    }
}
