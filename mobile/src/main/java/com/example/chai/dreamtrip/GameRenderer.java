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
import com.google.android.gms.games.Game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    enum GameState {GAME_OVER, GAME_PAUSED, GAME_STARTED}

    ;

    Context context;

    private GameState state;
    private TextureShaderProgram textureProgram;


    private static final String theme = "WHAT DO WE DO NOW?";


    private final float[] projectionMatrix = new float[16];


    private int screenWidth;
    private int screenHeight;

    private int counterToLevelUp = 0;

    //GameObject for the game
    private GameObject ship;
    private int[] shipsResId = {R.drawable.ship_0, R.drawable.ship_1, R.drawable.ship_2, R.drawable.ship_3, R.drawable.ship_4};

    //redfires
    private int[] fireredResId = {R.drawable.fire0, R.drawable.fire1, R.drawable.fire2, R.drawable.fire3};

    //white stars
    //private int[] whiteStarsId = {R.drawable.star0, R.drawable.star1, R.drawable.star2, R.drawable.start3};
    private int[] whiteStarsId = {R.drawable.b_star0, R.drawable.b_star1, R.drawable.b_star2, R.drawable.b_star3};
    //yellow star
    private int[] yellowStarsId = {R.drawable.star_1, R.drawable.star_2, R.drawable.star_3, R.drawable.star_4};

    //plazma
    private int[] plasmaIds = {R.drawable.plazma0, R.drawable.plazma1, R.drawable.plazma2, R.drawable.plazma3, R.drawable.plazma5, R.drawable.plazma6, R.drawable.plazma7,
            R.drawable.plazma8, R.drawable.plazma9};

    //monsters
    private int[] monsterIds = {R.drawable.monster1, R.drawable.monster2, R.drawable.monster3, R.drawable.monster4, R.drawable.monster5, R.drawable.monster6, R.drawable.monster7,
            R.drawable.monster8, R.drawable.monster9};


    private GameObject background_low0;
    private GameObject background_low1;
    private GameObject background_low2;
    private GameObject background_low3;

    //monster
    private LinkedList<GameObject> monsterList = new LinkedList<>();
    private GameObject monster;
    //life bar
    private GameObject lifeBar;

    private ArrayList<GameObject> backgroundStripList = new ArrayList<>();
    private GameObject background;

    private GameObject bucoNerissimo;

    private Enemy single_yellow_star;

    private List<Enemy> yellowStars = new LinkedList<>();

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

    private int gamePoint = 0;

    private GameObject sfondoGameOver;
    private GameObject sfondoGamePaused;
    private GameObject backButton;
    private GameObject againButton;
    private GameObject menuButton;
    private GameObject againButtonPressed;
    private GameObject backButtonPressed;
    private GameObject menuButtonPressed;


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //load the program
        textureProgram = new TextureShaderProgram(context);

        state = GameState.GAME_STARTED;

        //game elements
        background = new GameObject(context, -1, -1, 2, 2, R.drawable.sky_background);
        //ration w/h
        background_low0 = new GameObject(context, -1f, -1f, 4f, 0.856f, R.drawable.land_second_small);
        background_low1 = new GameObject(context, 1f, -1f, 4f, 0.856f, R.drawable.land_second_small);
        background_low0.setSpeed(0.002f);
        background_low1.setSpeed(0.002f);


        background_low2 = new GameObject(context, -1f, -1f, 4f, 0.856f, R.drawable.land_first_small_chai);
        background_low3 = new GameObject(context, 1f, -1f, 4f, 0.856f, R.drawable.land_first_small_chai);

        if (level == 0)
            launchStars();
        if (level == 1)
            launchFireEnemies();

        launchYellowStars();


        lifeBar = new GameObject(context, -1f, 0.75f, 0.5f, 0.25f, R.drawable.life_bar);
        populateMonsters();

        ship = new GameObject(context, 0f, 0f, 0.2f, 0.18f, shipsResId);
        //fire = new Enemy(context, 0f, 0f, 0.18f, 0.2f, R.drawable.fire0);
        bucoNerissimo = new GameObject(context, 1f, 0f, 0.2f, 0.22f, plasmaIds);


        //items for game screen : GAME_OVER and GAME_PAUSED
        sfondoGameOver = new GameObject(context, -1f, -1f, 2f, 2f, R.drawable.game_over);
        sfondoGamePaused = new GameObject(context, -1f, -1f, 2f, 2f, R.drawable.back_paused);

        againButton = new GameObject(context, -0.25f, -0.25f, 0.5f, 0.5f, R.drawable.again_button);
        againButtonPressed = new GameObject(context, -0.25f, -0.25f, 0.5f, 0.5f, R.drawable.again_button_pressed);

        backButton = new GameObject(context, -0.25f, -0.25f, 0.5f, 0.5f, R.drawable.back_button);
        backButtonPressed = new GameObject(context, -0.25f, -0.25f, 0.5f, 0.5f, R.drawable.back_button_pressed);

        menuButton = new GameObject(context, 0.25f, 0.25f, 0.5f, 0.5f, R.drawable.menu_button);
        menuButtonPressed = new GameObject(context, 0.25f, 0.25f, 0.5f, 0.5f, R.drawable.menu_button_pressed);

    }

    public void populateMonsters() {
        for (int i = 0; i < monsterIds.length; i++) {
            monster = new GameObject(context, -1f, 0.75f, 0.5f, 0.25f, monsterIds[i]);
            monsterList.add(monster);
        }
    }

    public void launchYellowStars() {
        for (int i = 0; i < 5; i++) {
            float y = -0.3f + i * 0.16666f;
            float randY = (float) Math.random() * (6);
            float x = 1f + 0.3f * randY;
            single_yellow_star = new Enemy(context, x, y, 0.10f, 0.15f, yellowStarsId);
            single_yellow_star.setLineaMovement(true);
            yellowStars.add(single_yellow_star);
        }
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
        //rinizialize the
        enemies = null;
        enemies = new LinkedList<>();
        for (int i = 0; i < 6; i++) {
            float y = -0.25f + i * 0.16666f;
            float randY = (float) Math.random() * (6);
            float x = 1f + 0.4f * randY;
            enemy = new Enemy(context, x, y, 0.10f, 0.15f, fireredResId);
            enemy.setLineaMovement(false);
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

        if (state == GameState.GAME_STARTED) {
            drawLowBackgroundStrips();
            drawLowBackgroundStrips2();


            for (Enemy en : enemies) {
                drawElement(en);
                en.updatePosition();
                if (en.getX() <= -1)
                    en.setX(1f);

                //for level 1
                if (level == 1) {
                    if (en.getY() <= -0.25f) en.changeYDirection();
                    if (en.getY() >= 1f) en.changeYDirection();
                }

                if (((en.getX() < (ship.getX() + ship.getWidth())) && (en.getX() > ship.getX())) || ((en.getX() + en.getWidth()) > ship.getX() && (en.getX() + en.getWidth()) < ship.getX())) {
                    if (en.getY() > ship.getY() && en.getY() < (ship.getY() + ship.getHeight()) || en.getY() + en.getHeight() > ship.getY() && (en.getY() + en.getHeight()) < ship.getY()) {
                    /*fire.setX(ship.getX() + ship.getWidth() / 2);
                    fire.setY(ship.getY() + ship.getHeight() / 2);*/
                        isBlinking = true;
                        gamePoint = gamePoint + 1;
                        if (gamePoint >= 8) {
                            gamePoint = 8;
                            state = GameState.GAME_OVER;
                            return;
                        }
                    }
                }


            }

            //yellow stars
            for (Enemy en : yellowStars) {
                drawElement(en);
                en.updatePosition();
                if (en.getX() <= -1) {
                    en.setX(1f);
                    if (level == 0) {
                        float randY = (float) Math.random() * (6);
                        float y = -0.25f + randY * 0.16666f;
                        en.setY(y);
                    } else if (level == 1) {
                        if (en.getY() <= -0.25f) en.changeYDirection();
                        if (en.getY() >= 1f) en.changeYDirection();
                    }
                }

                if (((en.getX() < (ship.getX() + ship.getWidth())) && (en.getX() > ship.getX())) || ((en.getX() + en.getWidth()) > ship.getX() && (en.getX() + en.getWidth()) < ship.getX())) {
                    if (en.getY() > ship.getY() && en.getY() < (ship.getY() + ship.getHeight()) || en.getY() + en.getHeight() > ship.getY() && (en.getY() + en.getHeight()) < ship.getY()) {
                        float randValue = (float) (Math.random() * 10) / 10;
                        en.setX(1f + randValue);
                        gamePoint = gamePoint - 1;
                        if (gamePoint < 0) gamePoint = 0;

                    }
                }
            }


            //expleoosion
            if (isBlinking) {
                shipBlickAnimationStart();
            } else {
                drawElement(ship);
            }


            // drawElement(ship);

            if (initializeBlackHole) {
                pastTimeToBlackHole = System.currentTimeMillis();
                initializeBlackHole = false;
            }
            if (!showBlackHole) {
                if (System.currentTimeMillis() - pastTimeToBlackHole > deltaTimeToBlackHole) {
                    showBlackHole = true;
                    pastTimeToBlackHole = System.currentTimeMillis();
                    bucoNerissimo.setX(1f);
                    bucoNerissimo.setY(0f);
                    bucoNerissimo.updatePosition();

                }
            }


            if (showBlackHole) {
                bucoNerissimo.updatePosition();
                drawElement(bucoNerissimo);
                if (System.currentTimeMillis() - pastTimeToBlackHole > 2000) {
                    showBlackHole = false;
                    initializeBlackHole = true;
                }
            }

            drawElement(lifeBar);

            for (int j = 0; j <= gamePoint; j++) {
                drawElement(monsterList.get(j));
            }

        } else if (state == GameState.GAME_PAUSED) {
            //handle game pause state
            drawElement(sfondoGamePaused);
            drawElement(backButton);
            drawElement(menuButton);
            drawElement(againButton);
        } else if (state == GameState.GAME_OVER) {
            //handle game over state
            drawElement(sfondoGameOver);
            drawElement(againButton);
        }
    }

    long deltaBlink = 200;
    long lastBlink = 0;
    int countBlink = 0;
    int MAX_BLINK = 3;
    boolean starBlink = true;

    public void shipBlickAnimationStart() {

        if (starBlink) {
            lastBlink = System.currentTimeMillis();
            starBlink = false;
        }
        if (System.currentTimeMillis() - lastBlink > deltaBlink && countBlink < MAX_BLINK) {
            drawElement(ship);
            lastBlink = System.currentTimeMillis();
            countBlink++;
        }
        if (countBlink == MAX_BLINK) {
            isBlinking = false;
            return;
        }

    }


    long deltaTimeToBlackHole = 8000;
    long pastTimeToBlackHole = 0;
    boolean initializeBlackHole = true;
    boolean showBlackHole = false;
    boolean isBlinking = false;


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
                //end of strips
                counterToLevelUp = counterToLevelUp + 1;

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


