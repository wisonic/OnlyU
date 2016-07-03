package com.moje.onlyu.tools.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by gongqibing on 16/5/7.
 */
public class MemoryUtils {
    /**
     * 获取总内存
     * @return
     */
    public static String getTotalMemory(Context cxt){
        String sysMemInfoPath = "/proc/meminfo";//系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initialMemory = 0;
        int bufferSize = 8192;
        try{
            FileReader localFileReader = new FileReader(sysMemInfoPath);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader,bufferSize);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            initialMemory = Integer.valueOf(arrayOfString[1]) * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e){
        }
        return Formatter.formatFileSize(cxt,initialMemory);
    }

    public static String getAvailMemory(Context cxt) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(cxt, mi.availMem);// 将获取的内存大小规格化
    }

    public static String getAllocationMemory(Context cxt) {
        // 获取系统分配的内存大小
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        //开启了android:largeHeap="true",米4系统能分配的内存为512M，不开启为128M
        return  "large: "+am.getLargeMemoryClass()+" normal: " + am.getMemoryClass();
        //return  am.getMemoryClass()+"";
    }
}
