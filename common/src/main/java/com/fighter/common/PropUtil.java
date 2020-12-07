package com.fighter.common;

import android.text.TextUtils;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 非product环境使用
 * 属性文件格式：
 * a=123
 * b={"a":"33"}
 * Created by fighter_lee on 18/10/16.
 */
public class PropUtil {

    private static final Map<String, String> propMap = new HashMap<>();

    public static void loadPropFile(String path) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(path);
            Properties properties = new Properties();
            properties.load(is);
            Set<String> propertyNames = properties.stringPropertyNames();
            for (String propertyName : propertyNames) {
                propMap.put(propertyName, properties.getProperty(propertyName));
                Trace.i("PropUtil", "loadPropFile() key=" + propertyName
                        + ",value=" + propMap.get(propertyName));
            }
        } catch (Exception e) {
            Trace.e("PropUtil", e.getMessage());
        } finally {
            FileUtil.closeQuietly(is);
        }
    }

    /**
     * 获取属性文件value字段
     *
     * @param key 属性文件的key
     * @return value 默认为null
     */
    public static String get(String key) {
        return get(key, null);
    }

    public static boolean hasKey(String key) {
        return !TextUtils.isEmpty(get(key));
    }

    /**
     * 获取属性文件value字段
     *
     * @param key          属性文件的key
     * @param defaultValue 默认值
     * @return value
     */
    public static String get(String key, String defaultValue) {
        if (!propMap.containsKey(key)) {
            return defaultValue;
        }
        return propMap.get(key);
    }

}
