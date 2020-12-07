package com.fighter.common;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import androidx.annotation.NonNull;

/**
 * Created by fighter_lee on 19/07/18.
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    /**
     * 删除文件或文件夹
     *
     * @param path 文件夹名称
     */
    public static void deleteDir(@NonNull String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            return;
        }
        if (dir.isFile()) {
            dir.delete();
        } else if (dir.isDirectory()) {
            File[] tmp = dir.listFiles();
            if (tmp == null) {
                dir.delete();
                return;
            }
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i].isDirectory()) {
                    deleteDir(tmp[i].getAbsolutePath());
                } else {
                    tmp[i].delete();
                }
            }
            dir.delete();
        }
    }

    // 创建文件
    public static boolean createOrExistsFile(@NonNull String filePath) {
        File path = new File(filePath);
        if (path.exists() && path.isFile()) {
            return true;
        } else {
            try {
                return path.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Trace.e(TAG, "createOrExistsFile() e = " + e);
            }
        }
        return false;
    }

    // 创建文件夹
    public static boolean createOrExistsDir(@NonNull String dirPath) {
        File path = new File(dirPath);
        return path.exists() ? path.isDirectory() : path.mkdirs();
    }

    /**
     * 重命名文件
     */
    public static boolean rename(@NonNull String srcPath, @NonNull String destPath) {
        File file1 = new File(srcPath);
        File file2 = new File(destPath);
        return file1.renameTo(file2);
    }

    /**
     * 拷贝文件
     */
    public static boolean copyFile(@NonNull String srcPath, @NonNull String destPath) {
        if (srcPath.equals(destPath)) {
            return true;
        }
        FileInputStream fi = null;
        FileOutputStream fo = null;
        try {
            fi = new FileInputStream(srcPath);
            fo = new FileOutputStream(destPath);
            FileChannel in = fi.getChannel();//得到对应的文件通道
            FileChannel out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Trace.e(TAG, "copyFile() e = " + e);
            return false;
        } finally {
            closeQuietly(fo, fi);
        }
    }

    public static boolean writeFile(File file, String content) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(content.getBytes());
            os.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(os);
        }
    }

    public static void closeQuietly(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    if (closeable != null)
                        closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
