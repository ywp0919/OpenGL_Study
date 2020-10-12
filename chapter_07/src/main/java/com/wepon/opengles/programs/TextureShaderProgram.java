package com.wepon.opengles.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.wepon.opengles.R;

public class TextureShaderProgram extends ShaderProgram {
    // Uniform locations

    private final int uMatrixLocation;
    private final int uTextureUnitLocation_0;
    private final int uTextureUnitLocation_1;

    // Attribute locations

    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation_0 = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT_0);
        uTextureUnitLocation_1 = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT_1);

        // Retrieve
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId, int unitIndex) {
        // Pass the matrix into the shader program.
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + unitIndex);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        // Tell the texture uniform sampler to use this texture in the shader
        // by telling it to read from texture unit 0.
        // 使用glUniform1i，我们可以给纹理采样器分配一个位置值，这样的话我们能够在一个片段着色器中设置多个纹理。
        // 一个纹理的位置值通常称为一个纹理单元(Texture Unit)。
        // 一个纹理的默认纹理单元是0，它是默认的激活纹理单元
        // 对应上面GLES20.GL_TEXTURE0，如果有多个，设置为1，2，3的话就是在GLES20.GL_TEXTURE0基础上+1，+2，+3
        GLES20.glUniform1i(uTextureUnitLocation_0, unitIndex);
    }

    public int getPositionLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesLocation() {
        return aTextureCoordinatesLocation;
    }

}
