package com.wepon.opengles.utils;

import android.util.Log;

import static android.opengl.GLES20.*;

/**
 * ShaderHelper
 *
 * @author wepon
 * create time  2020/9/22 8:06 PM
 * des
 */
public class ShaderHelper {

    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    /**
     * 编译着色器
     *
     * @param type       类型
     * @param shaderCode glsl的代码
     * @return shaderObjectId, 为0表示失败。
     */
    private static int compileShader(int type, String shaderCode) {

        // 创建一个shader对象，返回id
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            // 为0即表示失败
            Log.d(TAG, "Could not create new shader.");
            return 0;
        }
        // 读入源码，通过id给到创建的这个shader对象
        glShaderSource(shaderObjectId, shaderCode);

        // 编译这个shader
        glCompileShader(shaderObjectId);

        // 取出编译状态，检查是否能成功编译这个shader
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        // 不能编译成功的话为0
        if (compileStatus[0] == 0) {
            glDeleteShader(shaderObjectId);
            Log.d(TAG, "Compilation of shader failed.");
            return 0;
        }

        return shaderObjectId;
    }

    /**
     * 链接着色器
     *
     * @param vertexShaderId   顶点着色器id
     * @param fragmentShaderId 片段着色器id
     * @return 返回程序id
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        // 创建程序对象，返回id
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            Log.d(TAG, "Could not create new program.");
            return 0;
        }

        // 附上着色器，两个都附加到程序对象上去。
        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);

        // 链接程序
        glLinkProgram(programObjectId);
        // 检查成功还是失败，跟前面差不多
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        Log.d(TAG, "Results of linking program:\n"
                + glGetProgramInfoLog(programObjectId));
        if (linkStatus[0] == 0) {
            // 如果链接失败就删除程序对象
            glDeleteProgram(programObjectId);
            Log.d(TAG, "Linking of program failed.");
            return 0;
        }

        return programObjectId;
    }

    /**
     * 验证OpenGL程序的对象
     *
     * @param programObjectId 程序对象id
     * @return true or false
     */
    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.d(TAG, "Results of validating program: " + validateStatus[0]
                + "\nLog: " + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }
}
