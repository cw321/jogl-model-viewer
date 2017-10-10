import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

    public String file;
    public Vertex[] vertex_list = new Vertex[35947];
    public Face[] face_list = new Face[69451];

    public Reader(String filename) throws IOException{
        file = filename;

        // this flag lets us know whether we are reading assignment, vertices or faces
        // the counters are for the lists
        boolean flag = false;
        int vertex_counter = 0;
        int face_counter = 0;

        Vertex[] vert_list = new Vertex[3];

        // loop over lines and read
        BufferedReader read = new BufferedReader(new FileReader(file));
        for (String line = read.readLine(); line != null; line = read.readLine()) {
            if (flag) {
                String[] splits = line.split(" ");
                if (splits.length == 5) {
                    // create vertex from line
                    vertex_list[vertex_counter] = new Vertex(Float.parseFloat(splits[0]),
                            Float.parseFloat(splits[1]),
                            Float.parseFloat(splits[2]),
                            Float.parseFloat(splits[3]),
                            Float.parseFloat(splits[4]));
                    vertex_counter++;
                } else if (splits.length == 4) {
                    // else we make a face from it
                    vert_list[0] = vertex_list[Integer.parseInt(splits[1])];
                    vert_list[1] = vertex_list[Integer.parseInt(splits[2])];
                    vert_list[2] = vertex_list[Integer.parseInt(splits[3])];
                    face_list[face_counter] = new Face(Integer.parseInt(splits[0]), vert_list, this);
                    vert_list[0].adj_face.add(face_list[face_counter]);
                    vert_list[1].adj_face.add(face_list[face_counter]);
                    vert_list[2].adj_face.add(face_list[face_counter]);
                    face_counter++;
                }
            } else if (line.contains("end_header")){
                // this finds end of header
                flag = true;
            }
        }
    }
}
