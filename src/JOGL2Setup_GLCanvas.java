import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import java.io.IOException;

import static com.jogamp.opengl.GL.*;  // GL constants
import static com.jogamp.opengl.GL2.*; // GL2 constants

/**
 * Based on :http://www3.ntu.edu.sg/home/ehchua/programming/opengl/JOGL2.0.html with minor modifications
 * JOGL 2.0 Program Template (GLCanvas)
 * This is a "Component" which can be added into a top-level "Container".
 * It also handles the OpenGL events to render graphics.
 */
@SuppressWarnings("serial")
public class JOGL2Setup_GLCanvas extends GLCanvas implements GLEventListener {
    private GLU glu;  // for the GL Utility
    /** Constructor to setup the GUI for this Component */
    public JOGL2Setup_GLCanvas() {
        this.addGLEventListener(this);
    }

    // ------ Implement methods declared in GLEventListener ------

    /**
     * Called back immediately after the OpenGL context is initialized. Can be used
     * to perform one-time initialization. Run only once.
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
        glu = new GLU();                         // get GL Utilities
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        // ----- Your OpenGL initialization code here -----
    }

    /**
     * Call-back handler for window re-size event. Also called when the drawable is
     * first set to visible.
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        System.out.println(width);
        System.out.println(height);
        if (height == 0) height = 1;   // prevent divide by zero
        float aspect = (float)width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar
        //glu.gluOrtho2D(0.0f,width,height,0.0f);
        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }

    /**
     * Called back by the animator to perform rendering.
     */
    @Override
    public void display(GLAutoDrawable drawable){
        try {
            render3D(drawable);
        } catch(IOException ie) {
            ie.printStackTrace();
        } catch(InterruptedException inter) {
            inter.printStackTrace();
        }
    }

    public void render3D(GLAutoDrawable drawable) throws IOException, InterruptedException{
        GL2 bunny = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        bunny.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
        bunny.glLoadIdentity();  // reset the model-view matrix

        bunny.glTranslatef(0.0f,0.0f,-1.0f);

        Reader values = new Reader("resources/bun_zipper.ply");

        bunny.glBegin(GL_TRIANGLES);

        for (int i = 0; i < values.face_list.length; i++) {
            bunny.glNormal3f(values.face_list[i].vertex_list[0].normal.x,
                    values.face_list[i].vertex_list[0].normal.y,
                    values.face_list[i].vertex_list[0].normal.z);
            bunny.glVertex3f(values.face_list[i].vertex_list[0].x,
                    values.face_list[i].vertex_list[0].y,
                    values.face_list[i].vertex_list[0].z);

            bunny.glNormal3f(values.face_list[i].vertex_list[1].normal.x,
                    values.face_list[i].vertex_list[1].normal.y,
                    values.face_list[i].vertex_list[1].normal.z);
            bunny.glVertex3f(values.face_list[i].vertex_list[1].x,
                    values.face_list[i].vertex_list[1].y,
                    values.face_list[i].vertex_list[1].z);

            bunny.glNormal3f(values.face_list[i].vertex_list[2].normal.x,
                    values.face_list[i].vertex_list[2].normal.y,
                    values.face_list[i].vertex_list[2].normal.z);
            bunny.glVertex3f(values.face_list[i].vertex_list[2].x,
                    values.face_list[i].vertex_list[2].y,
                    values.face_list[i].vertex_list[2].z);
        }

        bunny.glEnd();
        bunny.glLoadIdentity();

    }


    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) { }
}