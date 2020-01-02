package com.itek.library_core.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Simonzhang on 2018/3/23.
 */
public class FileUtils {


    private static int bufferd = 1024;

    private FileUtils() {
    }


    // =================get SDCard information===================
    public static boolean isSdcardAvailable() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    public static long getSDAllSizeKB() {
        // get path of sdcard
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // get single block size(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数
        long allBlocks = sf.getBlockCount();
        // 返回SD卡大小
        return (allBlocks * blockSize) / 1024; // KB
    }

    /**
     * free size for normal application
     *
     * @return
     */
    public static long getSDAvalibleSizeKB() {
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        long avaliableSize = sf.getAvailableBlocks();
        return (avaliableSize * blockSize) / 1024;// KB
    }

    // =====================File Operation==========================
    public static boolean isFileExist(String director) {
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + director);
        return file.exists();
    }

    /**
     * create multiple director
     *
     * @param director
     * @return
     */
    public static boolean createFile(String director) {
        if (isFileExist(director)) {
            return true;
        } else {
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + director);
            return file.mkdirs();
        }
    }

    public static File writeToSDCardFile(String directory, String fileName,
                                         String content, boolean isAppend) {
        return writeToSDCardFile(directory, fileName, content, "", isAppend);
    }

    /**
     * @param directory (you don't need to begin with
     *                  Environment.getExternalStorageDirectory()+File.separator)
     * @param fileName
     * @param content
     * @param encoding  (UTF-8...)
     * @param isAppend  : Context.MODE_APPEND
     * @return
     */
    public static File writeToSDCardFile(String directory, String fileName,
                                         String content, String encoding, boolean isAppend) {
        // mobile SD card path +path
        File file = null;
        OutputStream os = null;
        try {
            if (!createFile(directory)) {
                return file;
            }
            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + directory + File.separator + fileName);
            os = new FileOutputStream(file, isAppend);
            if (encoding.equals("")) {
                os.write(content.getBytes());
            } else {
                os.write(content.getBytes(encoding));
            }

            os.write("\n".getBytes());
            os.flush();
        } catch (IOException e) {
            KLog.e("FileUtil", "writeToSDCardFile:" + e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * write data from inputstream to SDCard
     */
    public File writeToSDCardFromInput(String directory, String fileName,
                                       InputStream input) {
        File file = null;
        OutputStream os = null;
        try {
            if (createFile(directory)) {
                return file;
            }
            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + directory + fileName);
            os = new FileOutputStream(file);
            byte[] data = new byte[bufferd];
            int length = -1;
            while ((length = input.read(data)) != -1) {
                os.write(data, 0, length);
            }
            // clear cache
            os.flush();
        } catch (Exception e) {
            KLog.e("FileUtil", "" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * this url point to image(jpg)
     *
     * @param url
     * @return image name
     */
    public static String getUrlLastString(String url) {
        String[] str = url.split("/");
        int size = str.length;
        return str[size - 1];
    }

    public static String getFile(Context context, String fileName) {
        String str = "";
        try {
            String mDownloadUrl = context.getExternalCacheDir().getPath();
            File dir = new File(mDownloadUrl);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File mFile = new File(dir.getPath(), fileName);
            FileInputStream fis = new FileInputStream(mFile);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            str = new String(buffer, StandardCharsets.UTF_8);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return str;
    }

    public static void saveFile2(Context context, String toSaveString, String fileName, boolean append) {

        try {
            String mDownloadUrl = context.getExternalCacheDir().getPath();
            File dir = new File(mDownloadUrl);
            if(!dir.exists()){
                dir.mkdirs();
            }
            File mFile = new File(dir.getPath(),fileName);

            FileOutputStream outStream = new FileOutputStream(mFile, append);
            outStream.write(toSaveString.getBytes());
            outStream.write("\n".getBytes());
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }



}
