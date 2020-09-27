package com.wepon.opengles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.wepon.opengles.utils.MatrixHelper;
import com.wepon.opengles.utils.ShaderHelper;
import com.wepon.opengles.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
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

    private static final String U_MATRIX = "u_Matrix";
    private int uMatrixLocation;

    /**
     * 表示 float 占 32bit == 4 字节
     */
    private static final int BYTES_PER_FLOAT = 4;

    private static final String A_POSITION = "a_Position";
    private static final int POSITION_COMPONENT_COUNT = 2;
    private int aPositionLocation;


    private static final String A_COLOR = "a_Color";
    private static final int COLOR_COMPONENT_COUNT = 3;
    private int aColorLocation;

    /**
     * 这个表示OpenGL读入一个顶点的位置之后，如果它想读入下一个顶点的位置，需要跨过多少字节后读取。
     */
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    /**
     * 存储矩阵
     */
    private final float[] projectionMatrix = new float[16];
    /**
     * 使用模型矩阵移动物体
     */
    private final float[] modelMatrix = new float[16];

    private final Context context;

    private FloatBuffer vertexData;

    private int program;

    public AirHockeyRenderer(Context context) {
        this.context = context;
        // 顶点数据
        // Order of coordinates: X, Y, Z, W,  R, G, B
        float[] tableVerticesWithTriangles = {
                0f, 0f,     /*  0f,    1.5f,   */        1f, 1f, 1f,
                -0.5f, -0.8f,  /*  0f,    1f,     */        0.7f, 0.7f, 0.7f,
                0.5f, -0.8f,  /*  0f,    1f,     */        0.7f, 0.7f, 0.7f,
                0.5f, 0.8f,   /*  0f,    2f,     */        0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f,   /*  0f,    2f,     */        0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f,  /*  0f,    1f,     */        0.7f, 0.7f, 0.7f,

                // Line
                -0.5f, 0f,     /*  0f,    1.5f,   */        1f, 0f, 0f,
                0.5f, 0f,     /*  0f,    1.5f,   */        1f, 0f, 0f,

                // Mallets
                0f, -0.4f,  /*  0f,    1.25f,  */        0f, 0f, 1f,
                0f, 0.4f,   /*  0f,    1.75f,  */        1f, 0f, 0f
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

//        // 程序链接成功后，查这个uniform的位置
//        uColorLocation = glGetUniformLocation(program, U_COLOR);
        // 获取这个属性attribute a_Color的位置
        aColorLocation = glGetAttribLocation(program, A_COLOR);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        // 表示从开着开始读
        vertexData.position(0);
        // 这一步是告诉OpenGL到哪里找到属性a_Position对应的数据
        // 加入STRIDE，告诉OpenGL从vertexData读取一个位置的数据后，再跨过多少字节就可以找到下一个位置的数据
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);

        glEnableVertexAttribArray(aPositionLocation);

        // 找到a_Color对应的数据位置
        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);

        glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        // 使用45度的视野创建一个透视投影。
        // 这个视椎体从z值为 -1 的位置开始，在z值为-10的位置结果。
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);

        // 现在物体在z上为0的位置，而我们设置的范围是 -1f 到 -10f，所以下面通过模型矩阵将物体平移出来

        // 把模型矩阵设置为单位矩阵，再沿着z轴平移-2
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2f);

        // 下面要旋转的话，会导致近端显示超大，显示不完整，所以z轴再平移一点
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -1.5f);
        // 沿x轴旋转
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        // 投影矩阵和模型矩阵相乘   就包含了模型矩阵和投影矩阵的组合效应了
        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清空屏幕上的所有颜色，并使用之前设置的glClearColor填充。
        glClear(GL_COLOR_BUFFER_BIT);

        // 传递这个 投影矩阵
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        // 画三角形
        // 更新着色器代码中的u_Color的值
        // glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        // OpenGL只能画 点、线、三角形，这里第一个参数是告诉它我要画的是三角形
        // 然后从0位置开始读，读入6个顶点，这里就是对应了两个三角形
        // 现在使用的方式为GL_TRIANGLE_FAN，不明白的话去网上查一下，有三种画三角形的方式
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        // 画线条
        // glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        // 画两个点
        // glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        // glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
    }

}
