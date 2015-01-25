package com.example.chai.dreamtrip;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


import com.example.chai.dreamtrip.model.Enemy;
import com.example.chai.dreamtrip.model.GameObject;
import com.example.chai.dreamtrip.opengl.TextureShaderProgram;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

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

    //redfires
    private int[] fireredResId = {R.drawable.fire0, R.drawable.fire1, R.drawable.fire2, R.drawable.fire3};

    //white stars
    private int[] whiteStarsId = {R.drawable.star0, R.drawable.star1, R.drawable.star2, R.drawable.start3};

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

    Enemy enemy;
    Enemy fire;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //load the program
        textureProgram = new TextureShaderProgram(context);


        //game elements
        background = new GameObject(context, -1, -1, 2, 2, R.drawable.sky_background);
        //ration w/h
        background_low0 = new GameObject(context, -1f, -1f, 4f, 0.856f, R.drawable.land_second_small);
        background_low1 = new GameObject(context, 1f, -1f, 4f, 0.856f, R.drawable.land_second_small);
        background_low0.setSpeed(0.002f);
        background_low1.setSpeed(0.002f);


        background_low2 = new GameObject(context, -1f, -1f, 4f, 0.856f, R.drawable.land_first_small_chai);
        background_low3 = new GameObject(context, 1f, -1f, 4f, 0.856f, R.drawable.land_first_small_chai);

        launchStars();

        ship = new GameObject(context, 0f, 0f, 0.2f, 0.18f, shipsResId);
        fire = new Enemy(context, 0f, 0f, 0.10f, 0.15f,R.drawable.fire0 );

    }

    public void launchStars() {
        for (int i = 0; i < 7; i++) {
            float y = -0.3f + i * 0.16666f;
            float randY = (float) Math.random() * (6);
            float x = 1f + 0.3f * randY;
            enemy = new Enemy(context, x, y, 0.10f, 0.15f, whiteStarsId);
            enemy.setLineaMovement(true);
            enemies.add(enemy);
        }
    }

    public void launchFireEnemies() {
        for (int i = 0; i < 6; i++) {
            float y = -0.25f + i * 0.16666f;
            float randY = (float) Math.random() * (10);
            float x = 1f + 0.15f * randY;
            enemy = new Enemy(context, x, y, 0.15f, 0.16f, fireredResId);
            int randomDirection = (int) (Math.random() * (10));
            if (randomDirection >= 5) enemy.changeYDirection();
            enemies.add(enemy);
        }
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

        drawLowBackgroundStrips();
        drawLowBackgroundStrips2();


        for (Enemy en : enemies) {
            drawElement(en);
            en.updatePosition();

            //collision
            if(ship.getX()+ship.getWidth() > en.getX() && (ship.getX()+ship.getWidth())<(en.getX()+en.getWidth())){
                if(ship.getY() + ship.getHeight() > en.getY() && (ship.getY()+ship.getHeight())<(en.getY()+en.getHeight())){
                    fire.setX(ship.getX());
                    fire.setY(ship.getY());
                    showFire = true;
                    pastTimeToFire = System.currentTimeMillis();
                }

            }

            if (en.getX() <= -1) {
                en.setX(1f);

                if (level == 0) {
                    float randY = (float) Math.random() * (6);
                    float y = -0.25f + randY * 0.16666f;
                    en.setY(y);
                } else if (level == 2) {
                    if (en.getY() <= -0.25f) en.changeYDirection();
                    if (en.getY() >= 1f) en.changeYDirection();
                }
            }


        }

        if(showFire) {
            if((System.currentTimeMillis() - pastTimeToFire )<= deltaTimeToFire){
                drawFire();
                showFire = false;
            }

        }
        drawElement(ship);


    }

    long deltaTimeToFire = 500;
    long pastTimeToFire = 0;
boolean showFire = false;

    public void drawFire(){
        drawElement(fire);
    }

    int level = 0;
    boolean startSecondtrip = false;
    boolean starFirstdtrip = true;


    public void drawLowBackgroundStrips() {

        if (starFirstdtrip) {
            drawElement(background_low0);
            background_low0.updatePosition();
            if (background_low0.getX() <= -3f) {
                drawElement(background_low1);
                background_low1.updatePosition();
            }
            if ((background_low0.getX() <= -5f)) {
                background_low0.setX(1);
                startSecondtrip = true;
                starFirstdtrip = false;
            }
        }

        if (startSecondtrip) {
            drawElement(background_low1);
            background_low1.updatePosition();
            if (background_low1.getX() <= -3f) {
                drawElement(background_low0);
                background_low0.updatePosition();
            }
            if ((background_low1.getX() <= -5f)) {
                background_low1.setX(1);
                startSecondtrip = false;
                starFirstdtrip = true;
            }
        }


    }

    boolean starFirstdtrip2 = true;
    boolean startSecondtrip2 = false;

    public void drawLowBackgroundStrips2() {

        if (starFirstdtrip2) {
            drawElement(background_low2);
            background_low2.updatePosition();
            if (background_low2.getX() <= -3f) {
                drawElement(background_low3);
                background_low3.updatePosition();
            }
            if ((background_low2.getX() <= -5f)) {
                background_low2.setX(1);
                startSecondtrip2 = true;
                starFirstdtrip2 = false;
            }
        }

        if (startSecondtrip2) {
            drawElement(background_low3);
            background_low3.updatePosition();
            if (background_low3.getX() <= -3f) {
                drawElement(background_low2);
                background_low2.updatePosition();
            }
            if ((background_low3.getX() <= -5f)) {
                background_low3.setX(1);
                startSecondtrip2 = false;
                starFirstdtrip2 = true;
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

    public void drawElement(Enemy obj) {
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

    private List<Enemy> enemies = new LinkedList<>();

    private final static int MAX_ENEMY_PER_LINE = 6;
    private final static int MIN_ENEMY_PER_LINE = 3;

    private final static int MAX_ENEMY_PER_COL = 1;
    private final static int MIN_ENEMY_PER_COL = 3;


    //deltaTime per enemy appear
    long enemyDelta = 1000;
    long lastTimeGenerated = 0;
    boolean generateNewEnemy = true;

    public void generateEnemyRandom() {

        for (int i = 0; i < 20; i++) {
            lastTimeGenerated = currentTime;
            generateNewEnemy = false;
            int row = (int) (Math.random() * (10)); //scegli una riga a caso
            float y = (row - 1) * 0.02f;
            float x = 1f;//out of screen
            Enemy enemy = new Enemy(context, x, y, 0.02f, 0.02f, fireredResId);
            enemies.add(enemy);
        }


    }

    public LinkedList<Enemy> populateScreenWithRedEnemies() {
        LinkedList<Enemy> enemies = new LinkedList<Enemy>();
        Enemy enemy;

        //number of column = 10
        int columns = 10;
        int rows = 10;

        int min = 1;
        int max = 3;
        int enemy_per_row;
        int enemy_per_column;
        int count = 0;
        int[] enemies_col_num = new int[columns];
        int[] enemies_row_num = new int[rows];
        int[] enemyPos = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i < columns; i++) {
            enemy_per_column = min + (int) (Math.random() * (max - min));
            enemies_col_num[i] = enemy_per_column;
        }

        //for each row I randomly add stars in columns
        for (int j = 0; j < rows; j++) {

            do {
                int x = (int) (Math.random() * (columns));
                if (enemyPos[x] == 0) {
                    enemyPos[x] = 1;

                    float x1 = (float) (x * 0.2f - 1);
                    float y1 = (float) (-0.25 + 0.2f - j * 0.2f);

                    enemy = new Enemy(context, x1, y1, 0.02f, 0.02f, fireredResId);
                    enemies.add(enemy);
                    count++;
                }
                //Enemy(Context context, float posX, float posY, float width, float height, int resID)

            } while (count > MAX_ENEMY_PER_COL);


        }


        return enemies;
    }

    /*
    * Called to update the ship position*/
    public void updateShipPosition(float x, float y) {

        ship.updatePosition(x, y);


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


