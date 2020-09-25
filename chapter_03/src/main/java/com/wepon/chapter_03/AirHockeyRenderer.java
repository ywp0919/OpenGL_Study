package com.wepon.chapter_03;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.wepon.chapter_03.utils.ShaderHelper;
import com.wepon.chapter_03.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttrib1f;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * AirHockeyRenderer
 *
 * @author wepon
 * create time  2020/9/22 7:52 PM
 * des
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    /**
     * 对应写在片段着色器glsl代码里面的称为u_Color的uniform，并在main()里面赋值给了gl_FragColor
     */
    private static final String U_COLOR = "u_Color";
    private int uColorLocation;

    private static final int POSITION_COMPONENT_COUNT = 2;

    /**
     * 表示 float 占 32bit == 4 字节
     */
    private static final int BYTES_PER_FLOAT = 4;
    private final Context context;

    private FloatBuffer vertexData;

    private int program;

    public AirHockeyRenderer(Context context) {
        this.context = context;

        // 顶点 unused
        float[] tableVertices = {
                0f, 0f,
                0f, 14f,
                9f, 14f,
                9f, 0f
        };

        // 三角形
//        float[] tableVerticesWithTriangles = {
//                0f, 0f,
//                9f, 14f,
//                0f, 14f,
//
//                0f, 0f,
//                9f, 0f,
//                9f, 14f,
//
//                // Line 1
//                0f, 7f,
//                9f, 7f,
//
//                // Mallets
//                4.5f, 2f,
//                4.5f, 12f
//        };

        float[] tableVerticesWithTriangles = {
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,

                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,

                // Line 1
                -0.5f, 0f,
                0.5f, 0f,

                // Mallets
                0f, -0.25f,
                0f, 0.25f
        };

        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);
    }


    @Override

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 设置清空屏幕后填充的颜色
        glClearColor(0, 0, 0, 0);

        // 读取着色器的代码，即glsl代码
        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.simple_vertex_shader);

        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.simple_fragment_shader);

        // 编译着色器，拿到着色器id
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);

        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        // 链接程序，将着色器附加到程序对象上
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        // 验证程序
        ShaderHelper.validateProgram(program);

        // 调用这个方法告诉OpenGL 在绘制任何东西到屏幕上的时候要使用这里定义的程序
        glUseProgram(program);

        // 程序链接成功后，查这个uniform的位置
        uColorLocation = glGetUniformLocation(program, U_COLOR);

        // 表示从开着开始读
        vertexData.position(0);
        // 这一步是告诉OpenGL到哪里找到属性a_Position对应的数据
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, 0, vertexData);

        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清空屏幕上的所有颜色，并使用之前设置的glClearColor填充。
        glClear(GL_COLOR_BUFFER_BIT);

        // 画三角形
        // 更新着色器代码中的u_Color的值
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        // OpenGL只能画 点、线、三角形，这里第一个参数是告诉它我要画的是三角形
        // 然后从0位置开始读，读入6个顶点，这里就是对应了两个三角形
        glDrawArrays(GL_TRIANGLES, 0, 6);

        // 画线条
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        // 画两个点
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
    }

}
