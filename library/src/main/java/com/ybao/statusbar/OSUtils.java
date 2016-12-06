package com.ybao.statusbar;

import android.os.Build;
import android.text.TextUtils;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 系统判断
 * Created by Y-bao on 2015/12/1.
 */
public class OSUtils {
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    public static final String TYPE_EMUI = "EMUI";
    public static final String TYPE_MIUI = "MIUI";
    public static final String TYPE_FLYME = "FLYME";
    public static final String TYPE_NONE = "NONE";
    private static String TYPE = null;

    private static boolean isPropertiesExist(String... keys) {
        try {
            BuildPropertiesUtil prop = BuildPropertiesUtil.newInstance();
            for (String key : keys) {
                String str = prop.getProperty(key);
                if (str != null)
                    return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }


    private static String getProperties(String key) {
        try {
            BuildPropertiesUtil prop = BuildPropertiesUtil.newInstance();
            return prop.getProperty(key, "");
        } catch (IOException e) {
            return "";
        }
    }

    private static boolean isEMUI() {
        return isPropertiesExist(KEY_EMUI_VERSION_CODE);
    }

    private static boolean isMIUI() {
        return isPropertiesExist(KEY_MIUI_VERSION_CODE, KEY_MIUI_VERSION_NAME, KEY_MIUI_INTERNAL_STORAGE);
    }

    private static boolean isFlyme() {
        String meizuFlymeOSFlag = BuildPropertiesUtil.getSystemProperty("ro.build.display.id", "");
        if (meizuFlymeOSFlag.toLowerCase().contains("flyme")) {
            return true;
        } else {
            try {
                final Method method = Build.class.getMethod("hasSmartBar");
                return method != null;
            } catch (final Exception e) {
                return false;
            }
        }
    }

    public static String getOSName() {
        if (TextUtils.isEmpty(TYPE)) {
            if (isEMUI()) {
                TYPE = TYPE_EMUI;
            } else if (isMIUI()) {
                TYPE = TYPE_MIUI;
            } else if (isFlyme()) {
                TYPE = TYPE_FLYME;
            } else {
                TYPE = TYPE_NONE;
            }
        }
        return TYPE;
    }

}