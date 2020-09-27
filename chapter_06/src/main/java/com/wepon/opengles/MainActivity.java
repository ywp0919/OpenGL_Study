package com.wepon.opengles;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        glSurfaceView = new GLSurfaceView(this);

        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new AirHockeyRenderer(this));

        setContentView(glSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        glSurfaceView.onPause();
    }
}
