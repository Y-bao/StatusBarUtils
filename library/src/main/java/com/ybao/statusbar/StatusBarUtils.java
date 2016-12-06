package com.ybao.statusbar;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Y-bao on 2015/12/1.
 */
public class StatusBarUtils {

    /**
     * 设置兼容的沉浸式布局
     *
     * @return 沉浸式偏移量
     */
    public static int setImmerseLayout(Activity activity) {
        TypedArray activityStyle = activity.getTheme().obtainStyledAttributes(R.styleable.ImmerseLayout);
        boolean isImmerseLayout = activityStyle.getBoolean(R.styleable.ImmerseLayout_immerseLayout, false);
        boolean statusBarDarkMode = activityStyle.getBoolean(R.styleable.ImmerseLayout_statusBarDarkMode, false);
        activityStyle.recycle();
        return setImmerseLayout(activity, isImmerseLayout, statusBarDarkMode);
    }

    public static int setImmerseLayout(Activity activity, boolean isImmerseLayout, boolean statusBarDarkMode) {
        if (isImmerseLayout && canStatusBarTransparent(activity)) {
            int statuBarHeigthInLayout = SystemConfig.getStatusBarHeight(activity);
            if (statusBarDarkMode) {
                setStatusBarLightMode(activity, statusBarDarkMode);
            }
            return statuBarHeigthInLayout;
        }
        return 0;
    }


    @SuppressLint({"InlinedApi", "NewApi"})
    public static boolean canStatusBarTransparent(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.getOSName().equals(OSUtils.TYPE_EMUI)) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            return true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return true;
        }
        return false;
    }

    private static boolean hasDarkMode = true;

    /**
     * 改变状态栏字体颜色  暂时只支持安卓6.0以及小米魅族4.4以上
     *
     * @return
     */
    public static void setStatusBarLightMode(Activity activity, boolean darkmode) {
        Window window = activity.getWindow();
        if (hasDarkMode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && OSUtils.getOSName().equals(OSUtils.TYPE_NONE)) {
                //6.0通过此代码改为亮色背景的深色字体
                window.getDecorView().setSystemUiVisibility(darkmode ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                return;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && OSUtils.getOSName().equals(OSUtils.TYPE_FLYME)) {
                if (window != null) {
                    try {
                        WindowManager.LayoutParams lp = window.getAttributes();
                        Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                        Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                        darkFlag.setAccessible(true);
                        meizuFlags.setAccessible(true);
                        int bit = darkFlag.getInt(null);
                        int value = meizuFlags.getInt(lp);
                        if (darkmode) {
                            value |= bit;
                        } else {
                            value &= ~bit;
                        }
                        meizuFlags.setInt(lp, value);
                        window.setAttributes(lp);
                        return;
                    } catch (Exception e) {
                        hasDarkMode = false;
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && OSUtils.getOSName().equals(OSUtils.TYPE_MIUI)) {
                if (window != null) {
                    Class clazz = window.getClass();
                    try {
                        int darkModeFlag = 0;
                        Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                        Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                        darkModeFlag = field.getInt(layoutParams);
                        Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                        extraFlagField.invoke(window, darkmode ? darkModeFlag : 0, darkModeFlag);
                        return;
                    } catch (Exception e) {
                        hasDarkMode = false;
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.getOSName().equals(OSUtils.TYPE_EMUI)) {
            window.setStatusBarColor(darkmode ? 0x33000000 : 0x00000000);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup viewGroup = (ViewGroup) window.getDecorView();
            if (darkmode) {
                View view = new View(activity);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity)));
                view.setBackgroundColor(0x33000000);
                view.setTag("StatusBarColor");
                viewGroup.addView(view);
            } else {
                View view = viewGroup.findViewWithTag("StatusBarColor");
                viewGroup.removeView(view);
            }
        }
    }

    private static int result = 0;

    public static int getStatusBarHeight(Context context) {
        if (result == 0) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }
}
