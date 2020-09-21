package com.wepon.chapter_01;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(this);
        // 设置GL 版本
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new GLSurfaceView.Renderer() {
            // 这几个回调函数里面的GL10 gl参数是unused，GL10版本遗留下来的。
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                // 设置清空屏幕后填充的颜色
                glClearColor(1, 0, 0, 0);
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                glViewport(0, 0, width, height);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                // 清空屏幕上的所有颜色，并使用之前设置的glClearColor填充。
                glClear(GL_COLOR_BUFFER_BIT);
            }
        });
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }
}
