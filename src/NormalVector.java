/**
 * A class representing a normal vector of either a vertex or a face.
 *
 * @author Curtis White
 * @version 1.0
 * @since 1.0
 */
public class NormalVector {

    /**
     * x value of the vector.
     */
    public float x;

    /**
     * y value of the vector.
     */
    public float y;

    /**
     * z value of the vector.
     */
    public float z;

    /**
     * Constructor for the class.
     * <p>
     * NOTE: A length is not stored for these vector since they are all assumed to be of unit length, as this eases
     * the lighting calculation in the RendererMain and Canvas classes.
     *
     * @author Curtis White
     * @since 1.0
     * @param x0 x value of the vector.
     * @param y0 y value of the vector.
     * @param z0 z value of the vector.
     */
    public NormalVector(float x0, float y0, float z0) {
        x = x0;
        y = y0;
        z = z0;
    }
}
