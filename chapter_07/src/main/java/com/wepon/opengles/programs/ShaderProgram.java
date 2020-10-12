package com.wepon.opengles.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.wepon.opengles.utils.ShaderHelper;
import com.wepon.opengles.utils.TextResourceReader;


public class ShaderProgram {
    // Uniform constants

    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT_0 = "u_TextureUnit_0";
    protected static final String U_TEXTURE_UNIT_1 = "u_TextureUnit_1";


    // Attribute constants

    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";


    // Shader program

    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId,
                            int fragmentShaderResourceId) {
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId)
        );
    }

    public void useProgram() {
        // Set the current OpenGL shader program to this program.
        GLES20.glUseProgram(program);
    }

}
