package com.shumeipai.mobilecontroller.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;

import com.shumeipai.mobilecontroller.PiCarApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * @author wangzhiyuan
 * @since 2017/8/30
 */

public class BitmapHelper {
    private static final String TAG = "BitmapHelper";

    /**
     * 根据原图添加圆角
     *
     * @param source
     * @return
     */
    public static Bitmap createRoundCornerImage(Bitmap source, float corner) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rect, corner, corner, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream bos = null;
        byte[] result = null;

        try {
            bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            result = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        } finally {
            IOUtils.closeSilently(bos);
        }

        return result;
    }

    /**
     * Use quality compression to compress bitmap's size to be smaller than a max size, and convert it to bytes thereafter.
     * Note that this method will not report compressing ratio related data.
     *
     * @param bitmap  data source
     * @param maxSize unit in kb
     * @return bytes after compressing bitmap to a size smaller than a specific max size.
     */
    public static byte[] bitmapToBytes(Bitmap bitmap, int maxSize) {
        final long start = System.currentTimeMillis();

        if (bitmap == null) {
            return null;
        }

        final int maxSizeOfBytes = maxSize * Constants.ONE_KB;
        ByteArrayOutputStream bos = null;
        byte[] result = null;

        try {
            bos = new ByteArrayOutputStream();
            int quality = 100;
            int fullSize = 0;

            do {
                bos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                if (quality == 100) {
                    fullSize = bos.size();
                }
               // Logger.i(TAG, "quality<---->size, " + quality + "<---->" + bos.size() / 1024);
            }
            while (bos.size() > maxSizeOfBytes && (quality -= (fullSize > Constants.ONE_MB) ? 10 : 5) >= 0);

            result = bos.toByteArray();

            final long end = System.currentTimeMillis();
           /* Logger.i(TAG,
                    "bitmap to bytes costs " + (end - start) + "ms, \n" +
                            "bitmap full size is " + (fullSize / 1024) + "kb, \n" +
                            "bitmap final size is " + (bos.size() / 1024) + "kb, \n" +
                            "bitmap quality is " + quality);*/
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        } finally {
            IOUtils.closeSilently(bos);
        }

        return result;
    }

    /**
     * Use quality compression to compress bitmap to be smaller than a specific max size.
     *
     * @param bitmap  data source
     * @param maxSize a specific max size which's unit is kb.
     * @return a compressed bitmap smaller than the max size.
     */
    public static void compressBitmap(Bitmap bitmap, int maxSize, final OnCompressListener listener) {
        if (bitmap == null || bitmap.isRecycled() || listener == null) {
            return;
        }
        listener.onBeforeCompress();
        ByteArrayOutputStream bos = null;
        Bitmap target = null;

        try {
            bos = new ByteArrayOutputStream();
            int quality = 100;
            int step = 5;
            int fullSize = 0;

            do {
                bos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                if (quality == 100) {
                    fullSize = bos.size();
                }
                if (quality <= 10) {
                    step = 2;
                }
            }
            while (bos.size() / 1024 > maxSize && (quality -= (fullSize > Constants.ONE_MB) ? 10 : step) > 0);

            byte[] result = bos.toByteArray();
            //target = bytesToBitmap(result);
            listener.onCompressSuccess(result);

        } catch (Exception e) {
            e.printStackTrace();
//            target = null;
            listener.onCompressFailed("压缩失败");

        } finally {
            IOUtils.closeSilently(bos);
        }

        return;
    }


    public static Bitmap compressBitmap(Bitmap bitmap, int maxSize) {
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream bos = null;
        Bitmap target = null;

        try {
            bos = new ByteArrayOutputStream();
            int quality = 100;

            do {
                bos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            }
            while (bos.size() / 1024 > maxSize && (quality -= 5) >= 0);

            byte[] result = bos.toByteArray();
            target = bytesToBitmap(result);
        } catch (Exception e) {
            e.printStackTrace();
            target = null;
        } finally {
            IOUtils.closeSilently(bos);
        }

        return target;
    }

    public static byte[] compress(File bitmapFile, int maxSize) {
        if (bitmapFile == null || !bitmapFile.exists()) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath());
        int degree = readPictureDegree( bitmapFile.getAbsolutePath() );
        if ( degree != 0 ) {
            Matrix matrix = new Matrix();
            matrix.reset();
            matrix.setRotate( degree );
            bitmap = Bitmap.createBitmap(bitmap,0,0, bitmap.getWidth(), bitmap.getHeight(),matrix, true);
        }

        ByteArrayOutputStream bos = null;
        byte[] target = null;

        try {
            bos = new ByteArrayOutputStream();
            int quality = 100;

            do {
                bos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            }
            while (bos.size() / 1024 > maxSize && (quality -= 5) >= 0);

            target = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            target = null;
        } finally {
            IOUtils.closeSilently(bos);
        }

        return target;
    }

    /**
     * Decode an immutable bitmap from the specified byte array.
     *
     * @param b byte array of compressed image data
     * @return an immutable bitmap or null in case of exception.
     */
    public static Bitmap bytesToBitmap(byte[] b) {
        if (b != null && b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * Decode an immutable bitmap from the specified byte array.
     *
     * @param b       byte array of compressed image data
     * @param options Options that control downsampling and whether the
     *                image should be completely decoded, or just is size returned.
     * @return an immutable bitmap or null in case of exception.
     */
    public static Bitmap bytesToBitmap(byte[] b, BitmapFactory.Options options) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length, options);
        } else {
            return null;
        }
    }

    /**
     * Get max supported image size which will differ from different devices.
     *
     * @return max size related to the device.
     */
    public static int getMaxSupportedImageSize() {
        int textureLimit = getMaxTextureSize();
        if (textureLimit == 0) {
            return Constants.SIZE_DEFAULT;
        } else {
            return Math.min(textureLimit, Constants.SIZE_LIMIT);
        }
    }

    public static int getMaxTextureSize2() {
        // The OpenGL texture size is the maximum size that can be drawn in an ImageView
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }

    /**
     * duplicated from
     */
    public static int computeSampleSize(InputStream is, boolean close) {
        // Just decode image size into options
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {
            BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (close) IOUtils.closeSilently(is);
        }

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;

        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);

        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide / 1280 == 0 ? 1 : longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }


    /**
     * Decode a bitmap's input stream to find a proper inSampleSize according to device's max supported size.
     *
     * @param is    bitmap's data source
     * @param close whether to close input stream after work is done.
     * @return a proper inSampleSize
     */
    public static int findProperInSampleSize(InputStream is, boolean close) {
        // Just decode image size into options
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {
            BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (close) IOUtils.closeSilently(is);
        }

        int maxSize = getMaxSupportedImageSize();
        int sampleSize = 1;

        while (options.outHeight / sampleSize > maxSize || options.outWidth / sampleSize > maxSize) {
            sampleSize = sampleSize << 1;
        }

      //  Logger.i(TAG, "sample size is " + sampleSize);
        return sampleSize;
    }

    /**
     * Read a picture's degree from a file.
     *
     * @param file data source of a picture
     * @return degrees range from 0 to 360
     */
    public static int readPictureDegree(File file) {
        return readPictureDegree(file.getAbsolutePath());
    }

    /**
     * Read a picture's degree from a file, we use {@link ExifInterface} instead of {@link ExifInterface}
     * to avoid some unexpected bugs.
     *
     * @param filePath file's absolute path which we can read data source of a picture from.
     * @return degrees range from 0 to 360
     */
    public static int readPictureDegree(String filePath) {
        int degree = 0;

        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       // Logger.i(TAG, "ExifInterface, degree is " + degree);

        return degree;
    }

    /**
     * Rotate an bitmap to a specific angle.
     *
     * @param angle  target angle
     * @param bitmap data source
     * @return Returns an immutable bitmap from subset of the source bitmap,
     * transformed by the optional matrix. The new bitmap may be the
     * same object as source, or a copy may have been made. It is
     * initialized with the same density as the original bitmap.
     * <p>
     * If the source bitmap is immutable and the requested subset is the
     * same as the source bitmap itself, then the source bitmap is
     * returned and no new bitmap is created.
     */
    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.preRotate(angle);
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    /**
     * Get picture's absolute path according to its uri.
     *
     * @param context context
     * @param uri     picture's uri
     * @return absolute path of uri.
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) {
            return getRealPathFromUriAboveApi19(context, uri);
        } else {
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * Create a default {@link BitmapFactory.Options} .
     * Note this options use rgb_565 and a proper inSampleSize in order to save memory.
     *
     * @param is    data source of picture
     * @param close whether to close data source
     * @return options containing rgb_565 config and a proper inSampleSize.
     */
    public static BitmapFactory.Options newDefaultOptions(InputStream is, boolean close) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = BitmapHelper.findProperInSampleSize(is, close);

        return options;
    }

    /**
     * Save picture to local file.
     *
     * @param bitmap data source
     * @param file   local file to store picture.
     */
    public static void savePicture(Bitmap bitmap, File file) {
        final long start = System.currentTimeMillis();

        if (bitmap == null || file == null) {
           // Logger.i(TAG, "保存失败, bitmap or file is null.");
            return;
        }
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            final FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            if (file.exists()) {
               // Logger.(TAG, "保存成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Logger.i(TAG, "saving picture costs " + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;

        try {
            // 如果是document类型的 uri, 则通过document id来进行处理
            if (DocumentsContract.isDocumentUri(context, uri)) {
                String documentId = DocumentsContract.getDocumentId(uri);
                if (isMediaDocument(uri)) {
                    // 使用':'分割
                    String id = documentId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = {id};
                    filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
                } else if (isDownloadsDocument(uri)) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                    filePath = getDataColumn(context, contentUri, null, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                filePath = getDataColumn(context, uri, null, null);
            } else if ("file".equals(uri.getScheme())) {
                filePath = uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;
        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static int getMaxTextureSize() {
        try {
            // Safe minimum default size
            final int IMAGE_MAX_BITMAP_DIMENSION = Constants.SIZE_DEFAULT;

            // Get EGL Display
            EGL10 egl = (EGL10) EGLContext.getEGL();
            EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

            // Initialise
            int[] version = new int[2];
            egl.eglInitialize(display, version);

            // Query total number of configurations
            int[] totalConfigurations = new int[1];
            egl.eglGetConfigs(display, null, 0, totalConfigurations);

            // Query actual list configurations
            EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
            egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

            int[] textureSize = new int[1];
            int maximumTextureSize = 0;

            // Iterate through all the configurations to located the maximum texture size
            for (int i = 0; i < totalConfigurations[0]; i++) {
                // Only need to check for width since opengl textures are always squared
                egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

                // Keep trackCustomEvent of the maximum texture size
                if (maximumTextureSize < textureSize[0])
                    maximumTextureSize = textureSize[0];
            }

            // Release
            egl.eglTerminate(display);

            // Return largest texture size found, or default
            return Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 如需二次计算,请用 {@link #dip2pxF}
     */
    private static int dip2px(Context context, float dp) {
        return (int) (convertUnitToPixel(context, TypedValue.COMPLEX_UNIT_DIP, dp) + 0.5f);
    }

    /**
     * dip2px的返回float版
     *
     * @see #dip2px
     */
    private static float dip2pxF(Context context, float dp) {
        return convertUnitToPixel(context, TypedValue.COMPLEX_UNIT_DIP, dp);
    }

    private static float px2dip(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return px / scale;
    }

    private static int px(Context context, float dp) {
        return (int) (dip2px(context, dp) + 0.5f);
    }

    private static float convertUnitToPixel(Context context, int unit, float in) {
        return TypedValue.applyDimension(unit, in, context.getResources().getDisplayMetrics());
    }

    private static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        try {
            if (bitmap != null) {
                result = Base64.encodeToString(bitmapToBytes(bitmap), Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String bitmapArrayToBase64(byte[] data) {
        String result = null;
        try {
            if (data != null) {
                result = Base64.encodeToString(data, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 在系统返回的intent中获取图片信息，并转化为uri
     *
     * @param data
     * @return
     */
    public static Uri convertUri(Context context, Intent data) {
        if (data == null || data.getData() == null) {
            return null;
        }
        Uri localUri = data.getData();
        String scheme = localUri.getScheme();
        String imagePath = "";
        if ("content".equals(scheme)) {
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = context.getContentResolver().query(localUri, filePathColumns, null, null, null);
            if (c != null) {
                try {
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    imagePath = c.getString(columnIndex);
                    c.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    c.close();
                    imagePath = "";
                }
            }
        } else if ("file".equals(scheme)) {//小米4选择云相册中的图片是根据此方法获得路径
            imagePath = localUri.getPath();
        }
        if (TextUtils.isEmpty(imagePath)) {
            return localUri;
        }
        Uri uri = Uri.fromFile(new File(imagePath));
        return uri != null ? uri : localUri;
    }

    public static Bitmap colorToBitmap(int colorResId) {// drawable 转换成bitmap
        Bitmap.Config config = Bitmap.Config.ARGB_8888;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(1, 1, config);// 建立对应bitmap
        bitmap.eraseColor(PiCarApplication.getContext().getResources().getColor(colorResId));
        return bitmap;
    }

    public static String getAlphaHexValue(float alpha) {
        String color = Integer.toHexString((int) alpha * 255);
        return TextUtils.isEmpty(color) ? color : color.toUpperCase();
    }

    /**
     * 抓取本地视频缩略图（操作可能耗时，尽量异步进行）
     *
     * @param filePath
     * @return
     */
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap b = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            b = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    public static BitmapFactory.Options getBitmapOptions(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        return options;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int targetWidth, int targetHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int size = 1;
        if (height > targetHeight || width > targetWidth) {
            int scaleHeight = Math.round((float) height / (float) targetHeight);
            int scaleWidth = Math.round((float) width / (float) targetWidth);
            size = scaleHeight > scaleWidth ? scaleHeight : scaleWidth;
        }

        return size;
    }

    public static Bitmap decodeScaleImage(String path, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = getBitmapOptions(path);
        options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int degree = readPictureDegree(path);
        Bitmap rotateBitmap;
        if (bitmap != null && degree != 0) {
            rotateBitmap = rotateBitmap(degree, bitmap);
            bitmap.recycle();
            return rotateBitmap;
        } else {
            return bitmap;
        }
    }

    public static Bitmap getImage(String fileName) {

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(fileName);
            FileDescriptor fd = stream.getFD();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            if (options.mCancel || options.outWidth == -1
                    || options.outHeight == -1) {
                return null;
            }

            // 1.换算合适的图片缩放值，以减少对JVM太多的内存请求。
            options.inSampleSize = calculateInSampleSize(options, options.outWidth,
                    options.outHeight);
            options.inJustDecodeBounds = false;

            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            // 2. inPurgeable 设定为 true，可以让java系统, 在内存不足时先行回收部分的内存
            options.inPurgeable = true;
            // 与inPurgeable 一起使用
            options.inInputShareable = true;

            try {
                // 4. inNativeAlloc 属性设置为true，可以不把使用的内存算到VM里
                BitmapFactory.Options.class.getField("inNativeAlloc")
                        .setBoolean(options, true);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            // 5. 使用decodeStream 解码，则利用NDK层中，利用nativeDecodeAsset（）
            // 进行解码，不用CreateBitmap
            return BitmapFactory.decodeStream(stream, null, options);

        } catch (IOException ex) {
           // Logger.e(TAG, "", ex);
        } catch (OutOfMemoryError oom) {
          //  Logger.e(TAG, "Unable to decode file " + fileName
                  //  + ". OutOfMemoryError.", oom);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }


    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.WHITE);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(c);
        return bitmap;
    }

    public static boolean checkBitmapIsLegal(Bitmap bitmap) {
        return bitmap != null && bitmap.getByteCount() > 0 && bitmap.getWidth() > 0 && bitmap.getHeight() > 0;
    }

    public static File saveToTempFile(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String compressPath = FileUtils.getCachePath(PiCarApplication.getContext());
        String md5 = Md5Utils.hexdigest(bytes);
        File tempFile = new File(compressPath, md5 + ".temp");

        if (!tempFile.exists()) {
            tempFile.getParentFile().mkdirs();
        } else if (tempFile.length() > 0) {
            return tempFile;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(tempFile);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    /**
     * convert px to its equivalent sp
     * <p>
     * 将px转换为sp
     */
    private static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * convert sp to its equivalent px
     * <p>
     * 将sp转换为px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
