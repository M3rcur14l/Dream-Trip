package com.example.chai.dreamtrip.model;

import android.content.Context;

import com.example.chai.dreamtrip.opengl.TextureHelper;
import com.example.chai.dreamtrip.opengl.TextureShaderProgram;
import com.example.chai.dreamtrip.opengl.VertexArray;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by Chai on 24/01/2015.
 */
public class GameObject {

    /**
     * OPENGL stuff
     */
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * 4;


    private final VertexArray vertexArray;
    private final float[] DINAMIC_DATA;

    private float X;
    private float Y;
    private float Width;
    private float Height;
    private static final float SPEED = 0.005f;
    private int resId;
    int[] resIDs;
    private int numberOfFrames = 0;

    public boolean isHasFrames() {
        return hasFrames;
    }

    public int getNumberOfFrames() {
        return numberOfFrames;
    }

    public void setNumberOfFrames(int numberOfFrames) {
        this.numberOfFrames = numberOfFrames;
    }

    public void setHasFrames(boolean hasFrames) {
        this.hasFrames = hasFrames;
    }

    boolean hasFrames = false;


    private Context context;

    public GameObject(Context context, float posX, float posY, float width, float height, int resID) {
        this.context = context;
        setX(posX);
        setY(posY);
        setWidth(width);
        setHeight(height);
        setResId(TextureHelper.loadTexture(context, resID));
        float x = getX();
        float y = getY();


        float[] DINAMIC_VERTEX_DATA = {
                // Order of coordinates: X, Y, S, T
                // Triangle strip
                x, y, 0f, 1f,
                x, y + getHeight(),0f, 0f,
                x + getWidth(),y + getHeight(), 1f, 0f,
                x, y, 0f, 1f,
                x + getWidth(), y , 1f, 1f
               };
        DINAMIC_DATA = DINAMIC_VERTEX_DATA;
        vertexArray = new VertexArray(DINAMIC_DATA);

    }
    public GameObject(Context context, float posX, float posY, float width, float height, int[] resID) {
        this.context = context;
        setX(posX);
        setY(posY);
        setWidth(width);
        setHeight(height);
        //load multiple frames
        hasFrames = true;
        setNumberOfFrames(resID.length);
        resIDs = new int[resID.length];
        loadGraphics(resID);

        float x = getX();
        float y = getY();


        float[] DINAMIC_VERTEX_DATA = {
                // Order of coordinates: X, Y, S, T
                // Triangle strip
                x, y, 0f, 1f,
                x, y + getHeight(),0f, 0f,
                x + getWidth(),y + getHeight(), 1f, 0f,
                x, y, 0f, 1f,
                x + getWidth(), y , 1f, 1f
        };
        DINAMIC_DATA = DINAMIC_VERTEX_DATA;
        vertexArray = new VertexArray(DINAMIC_DATA);

    }

    public void loadGraphics(int[] rIds){
        for(int i = 0 ; i < rIds.length; i++){
            resIDs[i] = TextureHelper.loadTexture(context, rIds[i]);
        }
    }
    public int[] getResIDs() {
        return resIDs;
    }

    public void setResIDs(int[] resIDs) {
        this.resIDs = resIDs;
    }

    public void updatePosition() {
        float newX = getX() - SPEED;
        setX(newX);


        float x = getX();
        float y = getY();

        DINAMIC_DATA[0] = x;
        DINAMIC_DATA[1] = y;
        DINAMIC_DATA[2] = 0f;
        DINAMIC_DATA[3] = 1f;

        DINAMIC_DATA[4] = x;
        DINAMIC_DATA[5] = y + getHeight();
        DINAMIC_DATA[6] = 0f;
        DINAMIC_DATA[7] = 0f;

        DINAMIC_DATA[8] = x + getWidth();
        DINAMIC_DATA[9] = y + getHeight();
        DINAMIC_DATA[10] = 1f;
        DINAMIC_DATA[11] = 0f;

        DINAMIC_DATA[12] = x;
        DINAMIC_DATA[13] = y;
        DINAMIC_DATA[14] = 0f;
        DINAMIC_DATA[15] = 1f;

        DINAMIC_DATA[16] = x + getWidth();
        DINAMIC_DATA[17] = y ;
        DINAMIC_DATA[18] = 1f;
        DINAMIC_DATA[19] = 1f;


        vertexArray.updateBuffer(DINAMIC_DATA, 0, 20);
    }

    public void updatePosition(float posX, float posY) {

        setX(posX);
        setY(posY);

        float x = getX();
        float y = getY();

        DINAMIC_DATA[0] = x;
        DINAMIC_DATA[1] = y;
        DINAMIC_DATA[2] = 0f;
        DINAMIC_DATA[3] = 1f;

        DINAMIC_DATA[4] = x;
        DINAMIC_DATA[5] = y + getHeight();
        DINAMIC_DATA[6] = 0f;
        DINAMIC_DATA[7] = 0f;

        DINAMIC_DATA[8] = x + getWidth();
        DINAMIC_DATA[9] = y + getHeight();
        DINAMIC_DATA[10] = 1f;
        DINAMIC_DATA[11] = 0f;

        DINAMIC_DATA[12] = x;
        DINAMIC_DATA[13] = y;
        DINAMIC_DATA[14] = 0f;
        DINAMIC_DATA[15] = 1f;

        DINAMIC_DATA[16] = x + getWidth();
        DINAMIC_DATA[17] = y ;
        DINAMIC_DATA[18] = 1f;
        DINAMIC_DATA[19] = 1f;


        vertexArray.updateBuffer(DINAMIC_DATA, 0, 20);
    }


    public void bindData(TextureShaderProgram textureProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 5);
    }


    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }


    public float getHeight() {
        return Height;
    }

    public void setHeight(float height) {
        Height = height;
    }

    public float getWidth() {
        return Width;
    }

    public void setWidth(float width) {
        Width = width;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }


}
