package com.wepon.opengles.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * TextureHelper
 *
 * @author wepon
 * create time  2020/9/27 7:43 PM
 * des
 */
public class TextureHelper {

    private static final String TAG = "TextureHelper";


    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectIds = new int[1];
        // 生成纹理 id
        GLES20.glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            Log.d(TAG, "Could not generate a new OpenGL texture object.");
            return 0;
        }

        // OpenGL 不能直接读取PNG或者JPEG的文件的数据，需要非压缩形式的原始数据
        // 所以使用android内置的位图解码器解压缩

        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 不缩放
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            Log.d(TAG, "Resource ID " + resourceId + " could not be decoded.");
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        // 作为二维纹理使用，绑定到指定的纹理 id上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);

        // 设置每个过滤器， 缩小 和 放大 两种情况下分别允许使用的纹理过滤模式是不一样的，可以参考书上说明。
        // 缩小的情况   三线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        // 放大的情况   双线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // 加载位图到OpenGL里
        /// 读入bitmap定义的位图数据，并复制到当前绑定的纹理对象，这个时候这个bitmap就可以不需要了
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();

        // 生成MIP贴图
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        // 传 0 表示与当前的纹理解除绑定，为什么要解除绑定呢？这样就不会用其他纹理方法调用意外地改变这个纹理了。
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }

}
