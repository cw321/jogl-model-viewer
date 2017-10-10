public class Face {

    // init class vars
    private static int counter = 0;

    public int id, vertices;
    public int[] id_list;
    public Vertex[] vertex_list, global_list;

    public Face(int v0, int[] id_list0, Vertex[] glob0) {
        // count number of faces
        id = counter;
        counter++;

        // save vals from construction
        vertices = v0;
        id_list = id_list0;

        // save reference to global vertex list
        global_list = glob0;
        for (int i = 0; i < vertices; i++) {
            vertex_list[i] = this.findVertexById(id_list[i]);
        }
    }

    public Vertex findVertexById(int find) {
        Vertex out;

        for (int i = 0; i < this.global_list.length; i++) {
            if (this.global_list[i].id == find) {
                out = this.global_list[i];
                return out;
            }
        }

        return null;
    }
}
