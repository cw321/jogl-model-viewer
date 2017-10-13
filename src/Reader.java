import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

    public String file;
    public Vertex[] vertex_list;
    public Face[] face_list;
    public int vertex_len, face_len;

    public Reader(String filename) throws IOException{
        file = filename;

        // this flag lets us know whether we are reading assignment, vertices or faces
        // the counters are for the lists
        boolean flag = false;
        int vertex_counter = 0;
        int face_counter = 0;

        Vertex[] vert_list = new Vertex[3];

        BufferedReader read = new BufferedReader(new FileReader(file));
        for (String line = read.readLine(); line != null; line = read.readLine()) {
            String[] splits = line.split(" ");
            if (flag) {
                if (vertex_counter < vertex_len) {
                    // create vertex from line
                    vertex_list[vertex_counter] = new Vertex(Float.parseFloat(splits[0]),
                            Float.parseFloat(splits[1]),
                            Float.parseFloat(splits[2]),
                            Float.parseFloat(splits[3]),
                            Float.parseFloat(splits[4]));
                    vertex_counter++;
                } else if (face_counter < face_len) {
                    // else we make a face from it
                    vert_list[0] = vertex_list[Integer.parseInt(splits[1])];
                    vert_list[1] = vertex_list[Integer.parseInt(splits[2])];
                    vert_list[2] = vertex_list[Integer.parseInt(splits[3])];
                    face_list[face_counter] = new Face(Integer.parseInt(splits[0]), vert_list);
                    vert_list[0].adj_face.add(face_list[face_counter]);
                    vert_list[1].adj_face.add(face_list[face_counter]);
                    vert_list[2].adj_face.add(face_list[face_counter]);
                    face_counter++;
                }
            } else if (line.contains("end_header")){
                // this finds end of header
                flag = true;
            } else if (line.contains("element vertex")) {
                vertex_len = Integer.parseInt(splits[2]);
                vertex_list = new Vertex[vertex_len];
            } else if (line.contains("element face")) {
                face_len = Integer.parseInt(splits[2]);
                face_list = new Face[face_len];
            }
        }
        for (int i = 0; i < vertex_list.length; i++) {
            vertex_list[i].normal = vertex_list[i].calcNormal();
        }
    }
}
