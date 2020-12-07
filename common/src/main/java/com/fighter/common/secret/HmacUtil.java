package com.fighter.common.secret;

import com.fighter.common.Trace;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.NonNull;

/**
 * 用于：物联网后端生成HmacMD5算法
 * Created by fighter_lee on 19/09/04.
 */
public class HmacUtil {

    public static String encode(@NonNull String content, @NonNull String key) {
        String result = null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec((key).getBytes("UTF-8"), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(keySpec);
            byte[] bytes = mac.doFinal(content.getBytes("ASCII"));
            StringBuffer hash = new StringBuffer();

            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            result = hash.toString();
        } catch (Exception e) {
            Trace.e("HmacUtil", "encode() e = " + e);
        }
        return result;
    }
}
