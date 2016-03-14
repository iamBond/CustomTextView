package com.hnulab.Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 屏幕像素等相关值处理工具类
 * Created by Administrator on 2016/3/11.
 */
public class DisplayTools {
    /**
     * @return 获取屏幕宽度像素
     */
    public static int getScreenWidth(Context pContext){
        DisplayMetrics _DM = new DisplayMetrics();
        WindowManager _WM = (WindowManager)pContext.getSystemService(Context.WINDOW_SERVICE);
        _WM.getDefaultDisplay().getMetrics(_DM);
        return _DM.widthPixels;
    }

    /**
     * @return 获取屏幕高度像素
     */
    public static int getScreenHeight(Context pContext){
        DisplayMetrics _DM = new DisplayMetrics();
        WindowManager _WM = (WindowManager)pContext.getSystemService(Context.WINDOW_SERVICE);
        _WM.getDefaultDisplay().getMetrics(_DM);
        return _DM.heightPixels;
    }
}
