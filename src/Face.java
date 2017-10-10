public class Face {

    // init class vars
    private static int counter = 0;

    public int id, vertices;
    public Vertex[] vertex_list;
    public Vertex normal;

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
    }

    public Vertex calcNormal() {
        Vertex v1 = vertex_list[0];
        Vertex v2 = vertex_list[1];
        Vertex v3 = vertex_list[2];



        return out;
    }
}
