package com.moje.onlyu.utils;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.moje.onlyu.GApplication;
import com.moje.onlyu.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ImmersiveFullScreenUtils {
    @TargetApi(11)
    public static void addAnimForView(View rootView) {

        ViewGroup vg;
        if (DeviceUtil.getSDKVersionInt() >= 11 && rootView instanceof ViewGroup) {
            vg = (ViewGroup) rootView;
            LayoutTransition layoutTransition = new LayoutTransition();
            layoutTransition.setDuration(400);
            vg.setLayoutTransition(layoutTransition);
        }
    }

    /**
     * 设置沉浸式
     * 针对有titleview的情况
     *
     * @param mA
     * @param rootView
     */
    public static int uiSystemBarTint(Activity mA, View rootView) {
        RelativeLayout titleView = (RelativeLayout) rootView.findViewById(R.id.title_root_layout);
        titleView = titleView == null ? (RelativeLayout) rootView.findViewById(R.id.top_title_contentview) : titleView;
        if (titleView != null) {
            int titleHeight = mA.getResources().getDimensionPixelOffset(R.dimen._48);
            return setTitleheight(mA, titleView, titleHeight);
        }
        return 0;
    }

    public static int uiSystemBarTint(Activity mA, View rootView, int titleHeight) {
        RelativeLayout titleView = (RelativeLayout) rootView.findViewById(R.id.title_root_layout);
        titleView = titleView == null ? (RelativeLayout) rootView.findViewById(R.id.top_title_contentview) : titleView;
        if (titleView != null) {
            return setTitleheight(mA, titleView, titleHeight);
        }
        return 0;
    }

    /**
     * 针对没有titleview的情况
     *
     * @param mA
     * @param rootView
     */
    public static int uiSystemBarTintNoTitle(Activity mA, View rootView) {
        if (rootView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (GApplication.statusBarHeight == 0) {
                    GApplication.statusBarHeight = getStatusBarHeight(mA);
                }
                rootView.setPadding(0, GApplication.statusBarHeight, 0, 0);
                return GApplication.statusBarHeight;
            }
        }
        return 0;
    }

    public static int setTitleheight(Activity mA, RelativeLayout titleView, int titleHeight) {
        // LogUtil.d("shejian", "setTitleheight" + titleHeight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (GApplication.statusBarHeight == 0) {
                GApplication.statusBarHeight = getStatusBarHeight(mA);
            }
            ViewGroup.LayoutParams layoutParams = titleView.getLayoutParams();
            layoutParams.height = titleHeight + GApplication.statusBarHeight;
            titleView.setLayoutParams(layoutParams);
            RelativeLayout relativeLayout = (RelativeLayout) titleView.findViewById(R.id.top_title_contentview);
            if (relativeLayout != null) {
                relativeLayout.setPadding(0, GApplication.statusBarHeight, 0, 0);
            }
            return layoutParams.height;
        }
        return titleHeight;
    }

    public static int getTitleHeight(Activity mA) {
        RelativeLayout titleView = (RelativeLayout) mA.findViewById(R.id.title_root_layout);
        titleView = titleView == null ? (RelativeLayout) mA.findViewById(R.id.top_title_contentview) : titleView;
        if (titleView != null) {
            int titleHeight = mA.getResources().getDimensionPixelOffset(R.dimen._48);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (GApplication.statusBarHeight == 0) {
                    GApplication.statusBarHeight = getStatusBarHeight(mA);
                }
                return titleHeight + GApplication.statusBarHeight;
            }
            return titleHeight;
        } else {
            return 0;
        }
    }


    public static void miDarkSystemBar(Activity mActivity) {
        String vString = android.os.Build.MANUFACTURER;
        if ("Xiaomi".equals(vString)) {// 是小米设备
            String miuiV = getSystemProperty("ro.miui.ui.version.name");
            switch (miuiV) {
                case "V5":
                    break;
                case "V6":
                case "V7":
                    miuiDarkSystemBar(mActivity);// MIUI的V6沉浸式
                    break;
            }
        }
    }

    public static void miWhiteSystemBar(Activity mActivity) {
        String vString = android.os.Build.MANUFACTURER;
        if ("Xiaomi".equals(vString)) {// 是小米设备
            String miuiV = getSystemProperty("ro.miui.ui.version.name");
            switch (miuiV) {
                case "V5":
                    break;
                case "V6":
                case "V7":
                    miuiWhiteSystemBar(mActivity);// MIUI的V6沉浸式
                    break;
            }
        }
    }


    public static boolean initSystemBar(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = mActivity.getWindow().getDecorView();
            if (decorView.getTag() == null) {
                mActivity.getWindow().getDecorView().setTag("");
                SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(R.color.black_20_color);
            }

            return true;
        }
        return true;
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            //Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    //     Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * +
     * 设置Miui的沉浸式
     * Method name: miuiDarkSystemBar <BR>
     * Description: miuiDarkSystemBar <BR>
     * Remark: <BR>
     *
     * @param mActivity void<BR>
     */
    public static void miuiDarkSystemBar(Activity mActivity) {
        Window window = mActivity.getWindow();
        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);

            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);

            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            //只需要状态栏透明
            //    extraFlagField.invoke(window, tranceFlag, tranceFlag);
            //或
            //状态栏透明且黑色字体
            extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
            //清除黑色字体
            // extraFlagField.invoke(window, 0, darkModeFlag);
            GApplication.isMIUIv6 = true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public static void miuiWhiteSystemBar(Activity mActivity) {
        Window window = mActivity.getWindow();
        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);

            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);

            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            //只需要状态栏透明
            extraFlagField.invoke(window, tranceFlag, tranceFlag);
            //或
            //状态栏透明且黑色字体
            //  extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
            //清除黑色字体
            extraFlagField.invoke(window, 0, darkModeFlag);
            GApplication.isMIUIv6 = true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
