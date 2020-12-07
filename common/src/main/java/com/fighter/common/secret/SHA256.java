package com.fighter.common.secret;

import com.fighter.common.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * SHA256算法
 * Created by fighter_lee on 19/12/03.
 */
public class SHA256 {

    //计算文件的SHA-256
    public static String sha256(File file) {
        FileInputStream fis = null;
        StringBuilder buf = new StringBuilder();
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024 * 256];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                sha.update(buffer, 0, len);
            }
            byte[] bytes = sha.digest();
            for (int i = 0; i < bytes.length; i++) {
                String shaStr = Integer.toHexString(bytes[i] & 0xff);
                if (shaStr.length() == 1) {
                    buf.append("0");
                }
                buf.append(shaStr);
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            FileUtil.closeQuietly(fis);
        }
    }
}
