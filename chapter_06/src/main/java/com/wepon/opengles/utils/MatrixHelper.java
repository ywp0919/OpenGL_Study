package com.wepon.opengles.utils;

/**
 * MatrixHelper
 *
 * @author wepon
 * create time  2020/9/27 4:56 PM
 * des
 */
public class MatrixHelper {


    /**
     * 与 Matrix.perspectiveM 非常相似，做了一些小改动。
     */
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        // OpenGL把矩阵数据按照以列为主的顺序存储，以下分为4列，而不是4行

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }

}
