package com.example.chai.dreamtrip;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


import com.example.chai.dreamtrip.model.GameObject;
import com.example.chai.dreamtrip.opengl.TextureShaderProgram;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.Matrix.orthoM;


/**
 * Created by Chai on 20/01/2015.
 */
public class GameRenderer implements GLSurfaceView.Renderer {

    Context context;
    private TextureShaderProgram textureProgram;


    private static final String theme = "WHAT DO WE DO NOW?";


    private final float[] projectionMatrix = new float[16];


    private int screenWidth;
    private int screenHeight;

    //GameObject for the game
    private GameObject ship;
    private int[] shipsResId = {R.drawable.ship_0, R.drawable.ship_1, R.drawable.ship_2, R.drawable.ship_3, R.drawable.ship_4};


    private GameObject background_low0;
    private GameObject background_low1;
    private GameObject background_low2;
    private GameObject background_low3;
    private ArrayList<GameObject> backgroundStripList = new ArrayList<>();
    private GameObject background;

    private float unitX;
    private float unitY;
    private float normalize;

    public GameRenderer(Context c) {
        context = c;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        unitX = 2 / screenWidth;
        unitY = 2 / screenHeight;
        normalize = unitX / unitY;

    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.520f, 0.80f, 0.92f, 5.0f);
        //load the program
        textureProgram = new TextureShaderProgram(context);


        //game elements
        background = new GameObject(context, -1, -1, 2, 2, R.drawable.sky_background);
        //ration w/h
        background_low0 = new GameObject(context, -1f, -1f, 4f, 0.65f, R.drawable.land_second_1part);
        background_low1 = new GameObject(context, 3f, -1f, 4f, 0.65f, R.drawable.land_second_2part);

        background_low2 = new GameObject(context, -1f, -1f, 4f, 0.65f, R.drawable.land_first_1part);
        background_low3 = new GameObject(context, 1f, -1f, 4f, 0.65f, R.drawable.land_first_2part);

        backgroundStripList.add(background_low0);
        backgroundStripList.add(background_low1);
        backgroundStripList.add(background_low2);
        backgroundStripList.add(background_low3);

        ship = new GameObject(context, 0f, 0f, 0.4f, 0.38f, shipsResId);


    }


    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        orthoM(projectionMatrix, 0, -1, 1, -1, 1, -1f, 1f);


    }


    @Override
    public void onDrawFrame(GL10 gl10) {
        /*first method to call*/
        initDrawingSetting();
        drawElement(background);

        drawLowBackgroundStrips();

        drawElement(ship);


    }

    boolean startSecondtrip = false;
    boolean starFirstdtrip = true;

    public void drawLowBackgroundStrips() {

        if (starFirstdtrip) {
            drawElement(background_low0);
            background_low0.updatePosition();
            if ((background_low0.getX()-background_low0.getWidth()) == -1) {
                background_low0.setX(1);
                startSecondtrip = true;
                starFirstdtrip = false;
            }
        }
        if (startSecondtrip) {
            drawElement(background_low1);
            background_low1.updatePosition();
            if ((background_low1.getX()- background_low1.getWidth()) == -1) {
                background_low1.setX(1);
                startSecondtrip = false;
                starFirstdtrip = true;
            }
        }


    }

    public void handleChangePosition(float normalizedX, float normalizedY) {

        ship.updatePosition(normalizedX, normalizedY);

    }

    int indexCounter = 0;
    long currentTime = 0;
    long pastTime = 0;
    final static long deltaTime = 150;
    boolean drawNextFrame = true;

    public void drawElement(GameObject obj) {
        if (obj.isHasFrames()) {
            if (indexCounter > obj.getNumberOfFrames() - 1) indexCounter = 0;
            textureProgram.setUniforms(projectionMatrix, (obj.getResIDs())[indexCounter]);
            obj.bindData(textureProgram);
            obj.draw();
            currentTime = System.currentTimeMillis();
            if (drawNextFrame) {
                pastTime = currentTime;
                drawNextFrame = false;
            }
            if (currentTime - pastTime >= deltaTime) {
                indexCounter++;
                drawNextFrame = true;
            }
        } else {
            textureProgram.setUniforms(projectionMatrix, obj.getResId());
            obj.bindData(textureProgram);
            obj.draw();
        }
    }

    ;


    public void initDrawingSetting() {
        GLES20.glClear(GL_COLOR_BUFFER_BIT);
        // enable transparency of .png background
        GLES20.glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //prepare the program shader to start drawing
        textureProgram.useProgram();
    }

    /*





    * Called to update the ship position*/
    public void updateShipPosition(int x, int y) {


    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("GameRenderer", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }


    public class FPSCounter {
        long startTime = System.nanoTime();
        public int frames = 0;

        public void measureFrame() {
            frames++;
            if (System.nanoTime() - startTime >= 1000000000) {
                frames = 0;
                startTime = System.nanoTime();
            }
        }
    }

}


