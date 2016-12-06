package com.ybao.statusbar;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class SystemConfig {


    public static int[] getScreenSize(Window window) {
        Display mDisplay = window.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        mDisplay.getMetrics(metrics);
        int[] size = new int[2];
        size[0] = metrics.widthPixels;
        size[1] = metrics.heightPixels;
        return size;
    }

    public static int getScreenWidth(Window window) {
        Display mDisplay = window.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        mDisplay.getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 屏幕分辨率高
     **/
    public static int getScreenHeight(Window window) {
        Display mDisplay = window.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        mDisplay.getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int[] getScreenSize(Context context) {
        Display mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        mDisplay.getMetrics(metrics);
        int[] size = new int[2];
        size[0] = metrics.widthPixels;
        size[1] = metrics.heightPixels;
        return size;
    }

    public static int getScreenWidth(Context context) {
        Display mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        mDisplay.getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 屏幕分辨率高
     **/
    public static int getScreenHeight(Context context) {
        Display mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        mDisplay.getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int get1080ScaleValue(Context context, int value) {
        double scale = ((double) getScreenWidth(context)) / 1080.0;
        return (int) (scale * value);
    }

    public static int get720ScaleValue(Context context, int value) {
        double scale = ((double) getScreenWidth(context)) / 720.0;
        return (int) (scale * value);
    }


    public static int get1080ScaleDip2px(Context context, int value) {
        double scale = ((double) getScreenWidth(context)) / 1080.0;
        return (int) (scale * value);
    }

    public static int get720ScaleDip2px(Context context, int value) {
        double scale = ((double) getScreenWidth(context)) / 720.0;
        return (int) (scale * value);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int getdip(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static float get480Bdip(Context context) {
        return 480 / getdip(context);
    }

    // 获取当前版本号
    public static String getAppVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (Exception e) {

            e.printStackTrace();
            return "1";
        }
    }

    // 获取当前版本号
    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }

    public static int getSystemVerdionInt() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public static boolean appIsOpen(Context context) {
        boolean app_open = false;
        ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            String packageName = context.getPackageName();
            if (info.topActivity.getPackageName().equals(packageName)
                    && info.baseActivity.getPackageName().equals(packageName)) {
                app_open = true;
                break;
            }
        }
        return app_open;
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

    public static int getKeyboardHeight(Activity paramActivity) {
        int height = getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity) - getAppHeight(paramActivity);
        SharedPreferences mPreferences = paramActivity.getSharedPreferences("SizeUtils", Context.MODE_PRIVATE);
        if (height == 0) {
            height = mPreferences.getInt("KeyboardHeight", 0);
        } else {
            SharedPreferences.Editor mEditor = mPreferences.edit();
            mEditor.putInt("KeyboardHeight", height);
            mEditor.commit();
        }
        return height;
    }

    /**
     * statusBar高度
     **/
    public static int getStatusBarHeightByTop(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;

    }

    /**
     * 可见屏幕高度
     **/
    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }

    /**
     * 关闭键盘
     **/
    public static void hideSoftInput(View paramEditText) {
        ((InputMethodManager) paramEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    // below actionbar, above softkeyboard
    public static int getAppContentHeight(Activity paramActivity) {
        return getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity)
                - getActionBarHeight(paramActivity) - getKeyboardHeight(paramActivity);
    }

    /**
     * 获取actiobar高度
     **/
    public static int getActionBarHeight(Activity paramActivity) {
        if (true) {
            return dip2px(paramActivity, 56);
        }
        int[] attrs = new int[]{android.R.attr.actionBarSize};
        TypedArray ta = paramActivity.obtainStyledAttributes(attrs);
        return ta.getDimensionPixelSize(0, dip2px(paramActivity, 56));
    }


    /**
     * 键盘是否在显示
     **/
    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity)
                - getAppHeight(paramActivity);
        return height > 0;
    }

    /**
     * 显示键盘
     **/
    public static void showKeyBoard(final View paramEditText) {
        paramEditText.requestFocus();
        paramEditText.post(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) paramEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(paramEditText, 0);
            }
        });
    }


    /**
     * @param context
     * @return 判断网络是否连接
     */
    public static boolean isConn(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network network = connMgr.getActiveNetwork();
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            return networkInfo != null && networkInfo.isConnected();
        } else {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobileInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return (mobileInfo != null && mobileInfo.isConnected()) || (wifiInfo != null && wifiInfo.isConnected());
        }
    }


    public static boolean isWifiConn(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (wifiInfo != null && wifiInfo.isConnected());
    }

    public static void getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

//    public static void getTotalHeightofListView(ListView listView, int height) {
//        ListAdapter mAdapter = listView.getAdapter();
//        if (mAdapter == null) {
//            return;
//        }
//        int totalHeight = 0;
//        for (int i = 0; i < mAdapter.getCount(); i++) {
//            View mView = mAdapter.getView(i, null, listView);
//            mView.measure(
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//            //mView.measure(0, 0);
//            totalHeight += mView.getMeasuredHeight();
//            Log.w("HEIGHT" + i, String.valueOf(totalHeight));
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        int lvHeight = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
//        Log.i("CalculationRuleActivity", "lvtotal=" + lvHeight);
//        if (lvHeight > height) {
//            params.height = height;
//        } else {
//            params.height = lvHeight;
//        }
//
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//    }

    public static void getTotalHeightofGridView(GridView gridView, int cou, int num) {
        ListAdapter mAdapter = gridView.getAdapter();
        if (mAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < num; i += cou) {
            View mView = mAdapter.getView(i, null, gridView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
//        params.height = totalHeight + (gridView.getVerticalSpacing() * (num / cou - 1));
        params.height = totalHeight;
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }
}