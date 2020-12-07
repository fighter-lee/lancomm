package com.fighter.common.secret;

import android.text.TextUtils;
import android.util.Base64;

import com.fighter.common.FileUtil;
import com.fighter.common.interfaces.ICipher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.NonNull;

/**
 * Created by fighter_lee on 19/08/21.
 */
public class AESUtil implements ICipher {

    private static final String CIPHER_MODE = "AES/ECB/PKCS5Padding";
    private static final int FILE_BLOCK_SIZE = 1024 * 1024; // 1M

    /**
     * AES文件加密
     *
     * @param password null if 使用默认密钥
     */
    public void decryptFile(@NonNull File encryptedFile, @NonNull String decryptedFile, String password) throws IOException {
        byte[] psw;
        if (!TextUtils.isEmpty(password)) {
            psw = password.getBytes();
        } else {
            psw = getKey();
        }

        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(encryptedFile);
            os = new FileOutputStream(new File(decryptedFile));
            // ???为什么要加16
            byte[] buf = new byte[FILE_BLOCK_SIZE];
//            byte[] buf = new byte[FILE_BLOCK_SIZE + 16];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                byte[] data = new byte[len];
                System.arraycopy(buf, 0, data, 0, len);
                byte[] decrypt = decryptData(data, psw);
                os.write(decrypt);
            }
        } finally {
            FileUtil.closeQuietly(is, os);
        }
    }

    public void encryptFile(@NonNull File srcFile, @NonNull String encryptedFile, String password) throws IOException {
        byte[] psw;
        if (!TextUtils.isEmpty(password)) {
            psw = password.getBytes();
        } else {
            psw = getKey();
        }

        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(srcFile);
            os = new FileOutputStream(encryptedFile);
            byte[] buffer = new byte[FILE_BLOCK_SIZE];
            int len;
            while ((len = is.read(buffer)) != -1) {
                byte[] readData = new byte[len];
                System.arraycopy(buffer, 0, readData, 0, len);
                byte[] encryptData = encryptData(readData, psw);
                os.write(encryptData);
            }
        } finally {
            FileUtil.closeQuietly(is, os);
        }
    }

    private byte[] encryptData(@NonNull byte[] text, @NonNull byte[] key) {
        try {
            // 密钥补位
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(text);
            return Base64.encode(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    private byte[] decryptData(@NonNull byte[] text, @NonNull byte[] key) {
        try {
            // 密钥补位
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decode = Base64.decode(text, Base64.DEFAULT);
            return cipher.doFinal(decode);
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    @NonNull
    @Override
    public byte[] encrypt(@NonNull byte[] plainData) {
        return encryptData(plainData, getKey());
    }

    @NonNull
    @Override
    public byte[] decrypt(@NonNull byte[] cipherData) {
        return decryptData(cipherData, getKey());
    }

    @NonNull
    @Override
    public String encrypt(@NonNull String plainData) {
        return new String(encrypt(plainData.getBytes()));
    }

    @NonNull
    @Override
    public String decrypt(@NonNull String cipherData) {
        return new String(decrypt(cipherData.getBytes()));
    }

    /**
     * 伪算法
     */
    @NonNull
    @Override
    public byte[] getKey() {
        byte[] keyArray = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        for (int i = 0; i < keyArray.length; i++) {
            if (i < 8) {
                keyArray[i] = keyArray[i * 2];
            } else {
                keyArray[i] = keyArray[i / 2];
            }
        }
        keyArray[5] = 0x05;
        return keyArray;
    }
}
