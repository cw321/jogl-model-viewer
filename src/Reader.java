import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Handler to read from a .ply file and create the vertices and faces stored within the file.
 *
 * @author Curtis White
 * @version 1.0
 * @since 1.0
 */
public class Reader {

    /**
     * String of the filename.
     */
    public String file;

    /**
     * List of all read in vertices.
     */
    public Vertex[] vertex_list;

    /**
     * List of all read in faces.
     */
    public Face[] face_list;

    /**
     * Number of vertices to be read in.
     */
    public int vertex_len;

    /**
     * Number of faces to be read in
     */
    public int face_len;

    /**
     * Constructor for the class.
     *
     * @author Curtis White
     * @since 1.0
     * @param filename The filename to be read from.
     * @throws IOException Exception thrown with file errors
     */
    public Reader(String filename) throws IOException{
        // Save the filename, could allow us to write back later if needed
        file = filename;

        // Flag lets us know when we aren't reading header
        boolean flag = false;

        // Counters for the loop
        int vertex_counter = 0;
        int face_counter = 0;

        // Small list for constructing faces
        Vertex[] vert_list = new Vertex[3];

        // Create the reader and loop over the lines
        BufferedReader read = new BufferedReader(new FileReader(file));
        for (String line = read.readLine(); line != null; line = read.readLine()) {
            // Split the line on spaces
            String[] splits = line.split(" ");
            if (flag) {
                if (vertex_counter < vertex_len) {
                    // Create a vertex from the split line
                    vertex_list[vertex_counter] = new Vertex(Float.parseFloat(splits[0]),
                            Float.parseFloat(splits[1]),
                            Float.parseFloat(splits[2]),
                            Float.parseFloat(splits[3]),
                            Float.parseFloat(splits[4]));
                    vertex_counter++;
                } else if (face_counter < face_len) {
                    // Create a face from the split line, finding the vertices needed for the face
                    vert_list[0] = vertex_list[Integer.parseInt(splits[1])];
                    vert_list[1] = vertex_list[Integer.parseInt(splits[2])];
                    vert_list[2] = vertex_list[Integer.parseInt(splits[3])];

                    face_list[face_counter] = new Face(Integer.parseInt(splits[0]), vert_list);

                    // Add the new face to each vertex's list of adjacent faces
                    vert_list[0].adj_face.add(face_list[face_counter]);
                    vert_list[1].adj_face.add(face_list[face_counter]);
                    vert_list[2].adj_face.add(face_list[face_counter]);
                    face_counter++;
                }
            } else if (line.contains("end_header")){
                // Find the end of the header and flag that we are entering real data
                flag = true;
            } else if (line.contains("element vertex")) {
                // Extracts the number of vertices in the file
                vertex_len = Integer.parseInt(splits[2]);
                vertex_list = new Vertex[vertex_len];
            } else if (line.contains("element face")) {
                // Extracts the number of faces in the file
                face_len = Integer.parseInt(splits[2]);
                face_list = new Face[face_len];
            }
        }
        for (int i = 0; i < vertex_list.length; i++) {
            // Loop over vertices and find their normal vector
            // This has to be done last so we have all the faces
            vertex_list[i].normal = vertex_list[i].calcNormal();
        }
    }
}
