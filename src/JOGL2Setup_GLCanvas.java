import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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
    private Reader values;
    private float rt_x = 0; private float tr_x = 0;
    private float rt_y = 0; private float tr_y = -0.1f;
    private float rt_z = 0; private float tr_z = -0.5f;
    private int render_mode = GL_FILL;
    private float material = 0;
    private boolean lights_on = true;
    GL2 gl;

    // ------ Implement methods declared in GLEventListener ------

    /**
     * Called back immediately after the OpenGL context is initialized. Can be used
     * to perform one-time initialization. Run only once.
     */
    @Override
    public void init(GLAutoDrawable drawable) {

        addMouseMotionListener(new MouseMotionListener() {

            private int old_x = 0;
            private int old_y = 0;


            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int x = e.getX();
                    int y = e.getY();

                    rt_y -= (old_x - x) / 2;
                    rt_x -= (old_y - y) / 2;

                    old_x = x;
                    old_y = y;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    int x = e.getX();
                    int y = e.getY();

                    tr_x -= ((float)old_x - (float)x) / 1000;
                    tr_y += ((float)old_y - (float)y) / 1000;

                    old_x = x;
                    old_y = y;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                old_x = x;
                old_y = y;
            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    tr_z += 0.01f;
                } else {
                    tr_z -= 0.01f;
                }
            }
        });

        try {
            values = new Reader("resources/bun_zipper.ply");;
        } catch(IOException ie) {
            ie.printStackTrace();
        }

        this.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_W) {
                    rt_x += 1f;
                } else if (key == KeyEvent.VK_X) {
                    rt_x -= 1f;
                } else if (key == KeyEvent.VK_D) {
                    rt_y += 1f;
                } else if (key == KeyEvent.VK_A) {
                    rt_y -= 1f;
                } else if (key == KeyEvent.VK_E) {
                    rt_z += 1f;
                } else if (key == KeyEvent.VK_Z) {
                    rt_z -= 1f;
                } else if (key == KeyEvent.VK_P) {
                    tr_z += 0.01f;
                } else if (key == KeyEvent.VK_O) {
                    tr_z -= 0.01f;
                } else if (key == KeyEvent.VK_K) {
                    if (render_mode == GL_FILL) {
                        render_mode = GL_LINE;
                    } else {
                        render_mode = GL_FILL;
                    }
                } else if (key == KeyEvent.VK_L) {
                    if (lights_on) {
                        lights_on = false;
                    } else {
                        lights_on = true;
                    }
                } else if (key == KeyEvent.VK_M) {
                    material += 1;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

        });

        gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
        glu = new GLU();                         // get GL Utilities
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

    }

    /**
     * Call-back handler for window re-size event. Also called when the drawable is
     * first set to visible.
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
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
    public void display(GLAutoDrawable drawable) {
        render3D(drawable);
    }

    public void render3D(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        float[] light_pos = {1.0f, 1.0f, 1.0f, 1.0f};
        ByteBuffer lbb = ByteBuffer.allocateDirect(16);
        lbb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = lbb.asFloatBuffer();
        fb.put(light_pos);
        fb.position(0);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
        gl.glLoadIdentity();  // reset the model-view matrix

        gl.glTranslatef(tr_x, tr_y, tr_z);
        gl.glPolygonMode(GL_FRONT_AND_BACK, render_mode);

        if (lights_on) {
            gl.glEnable(GL_LIGHT0);
            gl.glLightfv(GL_LIGHT0, GL_POSITION, fb);
        } else {
            gl.glDisable(GL_LIGHT0);
        }

        if (material%3 == 0) {

            float[] ambient = {0.96f, 0.96f, 0.96f, 1.0f};
            float[] diffuse = {0.96f, 0.96f, 0.96f, 1.0f};
            float[] specular = {0.96f, 0.96f, 0.96f, 1.0f};

            lbb = ByteBuffer.allocateDirect(16);
            lbb.order(ByteOrder.nativeOrder());
            fb = lbb.asFloatBuffer();
            fb.put(ambient);
            fb.position(0);

            gl.glLightfv(GL_LIGHT0, GL_AMBIENT, fb);

            lbb = ByteBuffer.allocateDirect(16);
            lbb.order(ByteOrder.nativeOrder());
            fb = lbb.asFloatBuffer();
            fb.put(diffuse);
            fb.position(0);

            gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, fb);

            lbb = ByteBuffer.allocateDirect(16);
            lbb.order(ByteOrder.nativeOrder());
            fb = lbb.asFloatBuffer();
            fb.put(specular);
            fb.position(0);

            gl.glLightfv(GL_LIGHT0, GL_SPECULAR, fb);

        } else if (material%3 == 1) {

            float[] ambient = {1.0f, 0.843f, 0, 1.0f};
            float[] diffuse = {1.0f, 0.843f, 0, 1.0f};
            float[] specular = {1.0f, 0.843f, 0, 1.0f};

            lbb = ByteBuffer.allocateDirect(16);
            lbb.order(ByteOrder.nativeOrder());
            fb = lbb.asFloatBuffer();
            fb.put(ambient);
            fb.position(0);

            gl.glLightfv(GL_LIGHT0, GL_AMBIENT, fb);

            lbb = ByteBuffer.allocateDirect(16);
            lbb.order(ByteOrder.nativeOrder());
            fb = lbb.asFloatBuffer();
            fb.put(diffuse);
            fb.position(0);

            gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, fb);

            lbb = ByteBuffer.allocateDirect(16);
            lbb.order(ByteOrder.nativeOrder());
            fb = lbb.asFloatBuffer();
            fb.put(specular);
            fb.position(0);

            gl.glLightfv(GL_LIGHT0, GL_SPECULAR, fb);

        } else if (material%3 == 2) {

            float[] ambient = {0.804f, 0.5216f, 0.247f, 1.0f};
            float[] diffuse = {0.804f, 0.5216f, 0.247f, 1.0f};
            float[] specular = {0.804f, 0.5216f, 0.247f, 1.0f};

            lbb = ByteBuffer.allocateDirect(16);
            lbb.order(ByteOrder.nativeOrder());
            fb = lbb.asFloatBuffer();
            fb.put(ambient);
            fb.position(0);

            gl.glLightfv(GL_LIGHT0, GL_AMBIENT, fb);

            lbb = ByteBuffer.allocateDirect(16);
            lbb.order(ByteOrder.nativeOrder());
            fb = lbb.asFloatBuffer();
            fb.put(diffuse);
            fb.position(0);

            gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, fb);

            lbb = ByteBuffer.allocateDirect(16);
            lbb.order(ByteOrder.nativeOrder());
            fb = lbb.asFloatBuffer();
            fb.put(specular);
            fb.position(0);

            gl.glLightfv(GL_LIGHT0, GL_SPECULAR, fb);

        }

        float[] light_mode = {0.3f, 0.3f, 0.3f, 1.0f};
        lbb = ByteBuffer.allocateDirect(16);
        lbb.order(ByteOrder.nativeOrder());
        fb = lbb.asFloatBuffer();
        fb.put(light_mode);
        fb.position(0);
        gl.glEnable(GL_LIGHTING);
        gl.glLightModelfv(GL_LIGHT_MODEL_AMBIENT, fb);

        gl.glRotatef(rt_x, 1, 0, 0);
        gl.glRotatef(rt_y, 0, 1, 0);
        gl.glRotatef(rt_z, 0, 0, 1);

        gl.glBegin(GL_TRIANGLES);

        for (int i = 0; i < values.face_list.length; i++) {
            gl.glNormal3f(values.face_list[i].vertex_list[0].normal.x,
                    values.face_list[i].vertex_list[0].normal.y,
                    values.face_list[i].vertex_list[0].normal.z);
            gl.glVertex3f(values.face_list[i].vertex_list[0].x,
                    values.face_list[i].vertex_list[0].y,
                    values.face_list[i].vertex_list[0].z);

            gl.glNormal3f(values.face_list[i].vertex_list[1].normal.x,
                    values.face_list[i].vertex_list[1].normal.y,
                    values.face_list[i].vertex_list[1].normal.z);
            gl.glVertex3f(values.face_list[i].vertex_list[1].x,
                    values.face_list[i].vertex_list[1].y,
                    values.face_list[i].vertex_list[1].z);

            gl.glNormal3f(values.face_list[i].vertex_list[2].normal.x,
                    values.face_list[i].vertex_list[2].normal.y,
                    values.face_list[i].vertex_list[2].normal.z);
            gl.glVertex3f(values.face_list[i].vertex_list[2].x,
                    values.face_list[i].vertex_list[2].y,
                    values.face_list[i].vertex_list[2].z);
        }

        gl.glEnd();
        gl.glLoadIdentity();

    }


    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) { }
}