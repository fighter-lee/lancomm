package com.fighter.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import androidx.annotation.NonNull;

/**
 * 文件的压缩和解压
 * Created by fighter_lee on 19/08/22.
 */
public class ZipUtil {

    private static final String TAG = "ZipUtil";

    /**
     * 解压文件到指定文件夹
     *
     * @param zip      源文件
     * @param destPath 目标文件夹路径
     * @throws Exception 解压失败
     */
    public static void decompress(@NonNull String zip, @NonNull String destPath) throws Exception {
        //参数检查
        File zipFile = new File(zip);
        if (!zipFile.exists()) {
            throw new FileNotFoundException("zip file is not exists");
        }
        File destFolder = new File(destPath);
        if (!destFolder.exists()
                && !destFolder.mkdirs()) {
            throw new FileNotFoundException("destPath mkdirs is failed:" + destFolder);
        }
        ZipInputStream zis = null;
        BufferedOutputStream bos = null;
        try {
            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)));
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                //得到解压文件在当前存储的绝对路径
                String filePath = destPath + File.separator + ze.getName();
                if (ze.isDirectory()) {
                    new File(filePath).mkdirs();
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int count;
                    while ((count = zis.read(buffer)) != -1) {
                        baos.write(buffer, 0, count);
                    }
                    byte[] bytes = baos.toByteArray();
                    File entryFile = new File(filePath);
                    //创建父目录
                    if (!entryFile.getParentFile().exists()
                            && !entryFile.getParentFile().mkdirs()) {
                        throw new FileNotFoundException("zip entry mkdirs is failed:" + entryFile.getParent());
                    }
                    //写文件
                    bos = new BufferedOutputStream(new FileOutputStream(entryFile));
                    bos.write(bytes);
                    bos.flush();
                }
            }
        } finally {
            FileUtil.closeQuietly(zis, bos);
        }
    }

    /**
     * @param srcPath  源文件的绝对路径，可以为文件或文件夹
     * @param destPath 目标文件的绝对路径  /sdcard/.../file_name.zip
     * @throws Exception 解压失败
     */
    public static void compress(@NonNull String srcPath, @NonNull String destPath) throws Exception {
        //参数检查
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException("srcPath file is not exists:" + srcPath);
        }
        File destFile = new File(destPath);
        if (destFile.exists()
                && !destFile.delete()) {
            throw new IllegalArgumentException("destFile is exist and do not delete:" + destPath);
        }

        CheckedOutputStream cos;
        ZipOutputStream zos = null;
        try {
            // 对目标文件做CRC32校验，确保压缩后的zip包含CRC32值
            cos = new CheckedOutputStream(new FileOutputStream(destPath), new CRC32());
            //装饰一层ZipOutputStream，使用zos写入的数据就会被压缩啦
            zos = new ZipOutputStream(cos);
            zos.setLevel(3);//设置压缩级别 0-9,0表示不压缩，1表示压缩速度最快，9表示压缩后文件最小；默认为6，速率和空间上得到平衡。
            if (srcFile.isFile()) {
                compressFile("", srcFile, zos);
            } else if (srcFile.isDirectory()) {
                compressFolder("", srcFile, zos);
            }
        } finally {
            FileUtil.closeQuietly(zos);
        }
    }

    private static void compressFolder(String prefix, File srcFolder, ZipOutputStream zos) throws IOException {
        String new_prefix = prefix + srcFolder.getName() + "/";
        File[] files = srcFolder.listFiles();
        //支持空文件夹
        if (files.length == 0) {
            compressFile(prefix, srcFolder, zos);
        } else {
            for (File file : files) {
                if (file.isFile()) {
                    compressFile(new_prefix, file, zos);
                } else if (file.isDirectory()) {
                    compressFolder(new_prefix, file, zos);
                }
            }
        }
    }

    private static void compressFile(String prefix, File src, ZipOutputStream zos) throws IOException {
        //若是文件,那肯定是对单个文件压缩
        //ZipOutputStream在写入流之前，需要设置一个zipEntry
        //注意这里传入参数为文件在zip压缩包中的路径，所以只需要传入文件名即可
        String relativePath = prefix + src.getName();
        if (src.isDirectory()) relativePath += "/";//处理空文件夹
        ZipEntry entry = new ZipEntry(relativePath);
        //写到这个zipEntry中，可以理解为一个压缩文件
        zos.putNextEntry(entry);
        InputStream is = null;
        try {
            if (src.isFile()) {
                is = new FileInputStream(src);
                byte[] buffer = new byte[1024 << 3];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                }
            }
            //该文件写入结束
            zos.closeEntry();
        } finally {
            FileUtil.closeQuietly(is);
        }
    }
}
