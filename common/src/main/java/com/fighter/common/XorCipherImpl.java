package com.fighter.common;


import com.fighter.common.interfaces.ICipher;

import androidx.annotation.NonNull;

/**
 * 默认的加密算法：异或
 * Created by fighter_lee on 19/07/17.
 */
public class XorCipherImpl implements ICipher {

    @Override
    public byte[] encrypt(@NonNull byte[] plainData) {
        return encryptData(plainData);
    }

    @Override
    public byte[] decrypt(@NonNull byte[] cipherData) {
        return encrypt(cipherData);
    }

    @Override
    public String encrypt(@NonNull String plainData) {
        return new String(encrypt(plainData.getBytes()));
    }

    @Override
    public String decrypt(@NonNull String cipherData) {
        return encrypt(cipherData);
    }

    @Override
    @NonNull
    public byte[] getKey() {
        return new byte[]{0x23};
    }

    private byte[] encryptData(byte[] text) {
        for (int i = 0; i < text.length; i++) {
            text[i] = (byte) (text[i] ^ getKey()[0]);
        }
        return text;
    }
}
