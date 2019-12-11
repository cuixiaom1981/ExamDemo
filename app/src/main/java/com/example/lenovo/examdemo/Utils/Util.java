package com.example.lenovo.examdemo.Utils;

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static final String ws = "ws://172.81.253.56:8080/demo/websocket/stu/lisijia";//websocket测试地址

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
}
