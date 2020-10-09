package com.wepon.opengles.objects;

import android.opengl.GLES20;

import com.wepon.opengles.data.VertexArray;
import com.wepon.opengles.programs.TextureShaderProgram;

import static com.wepon.opengles.Constants.BYTES_PER_FLOAT;

/**
 * Table
 *
 * @author wepon
 * create time  2020/9/29 7:33 PM
 * des 存储桌子数据
 */
public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // X, Y, S, T

            0f, 0f,          0.5f, 0.5f,
            -0.5f, -0.8f,    0f, 0.9f,
            0.5f, -0.8f,     1f, 0.9f,
            0.5f, 0.8f,      1f, 0.1f,
            -0.5f, 0.8f,     0f, 0.1f,
            -0.5f, -0.8f,    0f, 0.9f
    };

    private final VertexArray vertexArray;

    public Table() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
    }
}
