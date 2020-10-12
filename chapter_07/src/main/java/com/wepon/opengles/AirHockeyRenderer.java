package com.wepon.opengles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.wepon.opengles.objects.Mallet;
import com.wepon.opengles.objects.Table;
import com.wepon.opengles.programs.ColorShaderProgram;
import com.wepon.opengles.programs.TextureShaderProgram;
import com.wepon.opengles.utils.MatrixHelper;
import com.wepon.opengles.utils.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

/**
 * AirHockeyRenderer
 *
 * @author wepon
 * create time  2020/9/22 7:52 PM
 * des
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    /**
     * 存储矩阵
     */
    private final float[] projectionMatrix = new float[16];
    /**
     * 使用模型矩阵移动物体
     */
    private final float[] modelMatrix = new float[16];

    private final Context context;

    private Table table;
    private Mallet mallet;

    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;

    private int texture_0;
    private int texture_1;

    public AirHockeyRenderer(Context context) {
        this.context = context;
    }


    @Override

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 设置清空屏幕后填充的颜色
        glClearColor(0, 0, 0, 0);

        table = new Table();
        mallet = new Mallet();

        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);

        texture_0 = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);

        texture_1 = TextureHelper.loadTexture(context, R.drawable.awesomeface);
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

        // Draw the table.
        textureProgram.useProgram();
        textureProgram.setUniforms(projectionMatrix, texture_0,0);
        textureProgram.setUniforms(projectionMatrix, texture_1,1);

        table.bindData(textureProgram);
        table.draw();

        // Draw the mallets.
        colorProgram.useProgram();
        colorProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorProgram);
        mallet.draw();
    }

}
