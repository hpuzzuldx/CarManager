package com.shumeipai.mobilecontroller.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.IntRange;
import androidx.core.content.FileProvider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * @author wangzhiyuan
 * @since 2017/9/3
 */

public class FileUtils {
    private static final String[] IMAGE_SUPPORT_EXTS = {"png", "jpg", "jpeg", "bmp"};
    private static final String[] VIDEO_SUPPORT_EXTS = {"mp4"};

    public static String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeSilently(in);
        }
        return base64;
    }


    /**
     * 创建一个用于拍照图片输出路径的Uri (FileProvider)
     */
    public static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, getFileProviderName(context), file);
    }

    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileprovider";
    }

    /**
     * 把Uri 解析出文件绝对路径
     */
    public static String parseOwnUri(Context context, Uri uri) {
        if (uri == null || uri.getPath() == null) return null;
        String path;
        if (TextUtils.equals(uri.getAuthority(), getFileProviderName(context))) {
            path = new File(uri.getPath()).getAbsolutePath();
        } else {
            path = uri.getPath();
        }
        return path;
    }

    public static void copy(final String from, final String to, final FileCopyListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                File file = null;
                try {
                    file = new File(from);
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onFail(e);
                    }
                    return;
                }
                if (!file.isFile()) {
                    if (listener != null) {
                        listener.onFail(new Exception(String.format("%s is not a file", from)));
                        return;
                    }
                }
                if (!file.exists()) {
                    if (listener != null) {
                        listener.onFail(new FileNotFoundException(String.format("%s is not exists.", from)));
                        return;
                    }
                }

                if (listener != null) {
                    listener.onStart();
                }

                long fileSize = file.length();
                long process = 0;

                try {
                    FileInputStream fis = new FileInputStream(file);

                    byte[] buff = new byte[1024];
                    int rc = 0;

                    File toFile = new File(to);
                    if (!toFile.getParentFile().exists()) {
                        toFile.getParentFile().mkdirs();
                    }

                    FileOutputStream fos = new FileOutputStream(toFile);

                    while ((rc = fis.read(buff, 0, 1024)) > 0) {
                        process += rc;
                        fos.write(buff, 0, rc);
                        if (listener != null) {
                            listener.onProcess(((int) (((float) process) * 100 / fileSize)));
                        }
                    }

                    fos.flush();
                    fos.close();
                    fis.close();

                } catch (Exception e) {
                    if (listener != null) {
                        listener.onFail(e);
                        return;
                    }
                }

                if (listener != null) {
                    listener.onFinish(to);
                }
            }
        }).start();
    }

    public static void copy(final InputStream is, final String to, final FileCopyListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (listener != null) {
                    listener.onStart();
                }

                try {

                    long fileSize = is.available();
                    long process = 0;

                    byte[] buff = new byte[1024];
                    int rc = 0;

                    File toFile = new File(to);
                    if (!toFile.getParentFile().exists()) {
                        toFile.getParentFile().mkdirs();
                    }

                    FileOutputStream fos = new FileOutputStream(toFile);

                    while ((rc = is.read(buff, 0, 1024)) > 0) {
                        process += rc;
                        fos.write(buff, 0, rc);
                        if (listener != null) {
                            listener.onProcess(((int) (((float) process) * 100 / fileSize)));
                        }
                    }

                    fos.flush();
                    fos.close();
                    is.close();

                } catch (Exception e) {
                    if (listener != null) {
                        listener.onFail(e);
                        return;
                    }
                }

                if (listener != null) {
                    listener.onFinish(to);
                }
            }
        }).start();
    }

    public interface FileCopyListener {
        void onStart();

        void onFail(Exception e);

        void onProcess(@IntRange(from = 0, to = 100) int process);

        void onFinish(String toPath);
    }

    public static void createPath(String file) {
        File f = new File(file);
        if (f.exists()) {
            return;
        }
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
    }

    public static byte[] read(String file) {

        File f = new File(file);
        if (!f.exists() || f.length() == 0) {
            return null;
        }

        try {
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[((int) f.length())];
            fis.read(buffer);
            IOUtils.closeSilently(fis);
            return buffer;
        } catch (Exception e) {
            return null;
        }
    }

    public static String toBase64(byte[] buffer) {
        if (buffer == null || buffer.length == 0) {
            return null;
        }
        try {
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取不带扩展名的文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileNameNoEx(String filePath) {
        try {
            if ((filePath != null) && (filePath.length() > 0)) {
                int index = filePath.lastIndexOf("/") + 1;
                int dot = filePath.lastIndexOf(".");
                if ((dot > -1) && (dot < (filePath.length()))) {
                    if (index != -1 && index < filePath.length()) {
                        return filePath.substring(index, dot);
                    }
                }
            }
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    public static boolean isVideo(String path) {
        String extName = FileUtils.getExtensionName(path);
        if (TextUtils.isEmpty(extName)) {
            return false;
        }
        return Arrays.asList(VIDEO_SUPPORT_EXTS).contains(extName.toLowerCase());
    }

    public static boolean isImage(String filePath) {
        String extName = FileUtils.getExtensionName(filePath);
        if (TextUtils.isEmpty(extName)) {
            return false;
        }
        return Arrays.asList(IMAGE_SUPPORT_EXTS).contains(extName.toLowerCase());
    }

    public static boolean isExist(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        try {
            File file = new File(path);
            return file.exists() && file.isFile() && getFileSize(path) != 0;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isExist(File file) {
        if (file == null) {
            return false;
        }
        try {
            return file.exists();
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean deleteFile(String path) {
        try {
            if (TextUtils.isEmpty(path)) {
                return false;
            }
            File file = new File(path);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static long getFileSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        try {
            return new File(path).length();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Uri getFileProviderUri(Context context, File file) {
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } else {
            data = Uri.fromFile(file);
        }
        return data;
    }

    public static String getDownloadImagePath(String fileName) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.ZD_DOWNLOADS_IMAGE_PATH + fileName;
        }
        return "";
    }


    public static void saveBitmapToLocal(Bitmap bitmap, String fileName, OnBitmapToLocalListener onBitmapToLocalListener) {
        String path = getDownloadImagePath(fileName + ".jpg");
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
            if (onBitmapToLocalListener != null) {
                onBitmapToLocalListener.saveSuccess(path);
            }
        } catch (Exception e) {
            if (onBitmapToLocalListener != null) {
                onBitmapToLocalListener.saveFailed();
            }
            e.printStackTrace();
        }
    }


    public static void saveBitmapToCache(Bitmap bitmap, String path, OnBitmapToLocalListener onBitmapToLocalListener) {
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
            if (onBitmapToLocalListener != null) {
                onBitmapToLocalListener.saveSuccess(path);
            }
        } catch (Exception e) {
            if (onBitmapToLocalListener != null) {
                onBitmapToLocalListener.saveFailed();
            }
            e.printStackTrace();
        }
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()) && context.getExternalCacheDir() != null) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public static String getCachePath(Context context) {
        File targetFile = getDiskCacheDir(context, Constants.IMAGE_COMPRESS_PATH);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        return targetFile.getAbsolutePath();
    }

    public static String formatFileSize(long size) {
        DecimalFormat formatter = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024L) {
            float kbSize = size / 1024f;
            return formatter.format(kbSize) + "KB";
        } else if (size < 1024 * 1024 * 1024L) {
            float mbSize = size / 1024f / 1024f;
            return formatter.format(mbSize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024L) {
            float gbSize = size / 1024f / 1024f / 1024f;
            return formatter.format(gbSize) + "GB";
        } else {
            return "0KB";
        }
    }

    public static String getFileWithEx(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        int index = filePath.lastIndexOf("/");
        try {
            return filePath.substring(index + 1, filePath.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void writeToFile(String content, String logPath) {

        if (TextUtils.isEmpty(logPath)) {
            return;
        }

        String fileName = logPath + "/location.txt";//log日志名，使用时间命名，保证不重复
        //如果父路径不存在
        File file = new File(logPath);
        if (!file.exists()) {
            file.mkdirs();//创建父路径
        }

        FileOutputStream fos;//FileOutputStream会自动调用底层的close()方法，不用关闭
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(fileName, true);//这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(content + "\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();//关闭缓冲流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取文件扩展名
     *
     * @param filePath
     * @return
     */
    public static String getExtensionName(String filePath) {
        if ((filePath != null) && (filePath.length() > 0)) {
            int dot = filePath.lastIndexOf('.');
            if ((dot > -1) && (dot < (filePath.length() - 1))) {
                return filePath.substring(dot + 1);
            }
        }
        return filePath;
    }
}
