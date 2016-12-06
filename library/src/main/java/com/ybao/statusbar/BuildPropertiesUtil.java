package com.ybao.statusbar;

/**
 * android系统配置文件
 * Created by Y-bao on 2015/12/1.
 */

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;


public class BuildPropertiesUtil {

    private final Properties properties;

    private BuildPropertiesUtil() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
    }

    public boolean containsKey(final Object key) {
        return properties.containsKey(key);
    }

    public boolean containsValue(final Object value) {
        return properties.containsValue(value);
    }

    public Set<Entry<Object, Object>> entrySet() {
        return properties.entrySet();
    }

    public String getProperty(final String name) {
        return properties.getProperty(name);
    }

    public String getProperty(final String name, final String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public Enumeration<Object> keys() {
        return properties.keys();
    }

    public Set<Object> keySet() {
        return properties.keySet();
    }

    public int size() {
        return properties.size();
    }

    public Collection<Object> values() {
        return properties.values();
    }

    public static BuildPropertiesUtil newInstance() throws IOException {
        return new BuildPropertiesUtil();
    }

    public static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }


    public static boolean containsKey(String key) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("containsKey", String.class);
            return (boolean) get.invoke(clz, key);
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean containsValue(String value) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("containsValue", String.class);
            return (boolean) get.invoke(clz, value);
        } catch (Exception e) {
            return false;
        }
    }
}