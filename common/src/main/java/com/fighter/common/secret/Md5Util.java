package com.fighter.common.secret;

import com.fighter.common.FileUtil;
import com.fighter.common.Trace;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.NonNull;

/**
 * Created by fighter_lee on 19/08/22.
 */
public class Md5Util {

    public static String md5sum(@NonNull File file) {
        FileInputStream fis = null;
        StringBuilder buf = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024 * 256];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                String md5s = Integer.toHexString(bytes[i] & 0xff);
                if (md5s.length() == 1) {
                    buf.append("0");
                }
                buf.append(md5s);
            }
            return buf.toString();
        } catch (Exception ex) {
            Trace.e("Md5Util", "getMd5ByFile() Exception:" + ex.toString());
            ex.printStackTrace();
            return null;
        } finally {
            FileUtil.closeQuietly(fis);
        }
    }

    public static String md5sum(@NonNull String text) {
        return new String(md5sum(text.getBytes()));
    }

    public static byte[] md5sum(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "".getBytes();
        }
        md.update(data);
        byte[] bytes = md.digest();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String md5s = Integer.toHexString(bytes[i] & 0xff);
            if (md5s.length() == 1) {
                buf.append("0");
            }
            buf.append(md5s);
        }
        return buf.toString().getBytes();
    }
}
