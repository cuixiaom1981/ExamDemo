package com.example.lenovo.examdemo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lenovo.examdemo.R;
import com.tencent.liteav.demo.play.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerView;
import com.tencent.liteav.demo.play.v3.SuperPlayerVideoId;

public class PlayerDemoActivity extends AppCompatActivity {

    public SuperPlayerView mSuperPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_demo);

        mSuperPlayerView = (SuperPlayerView)findViewById(R.id.videoView3);

//        // 播放器配置
//        SuperPlayerGlobalConfig prefs = SuperPlayerGlobalConfig.getInstance();
//        // 开启悬浮窗播放
//        prefs.enableFloatWindow = true;
//        //设置悬浮窗的初始位置和宽高
//        SuperPlayerGlobalConfig.TXRect rect = new SuperPlayerGlobalConfig.TXRect();
//        rect.x = 0;
//        rect.y = 0;
//        rect.width = 810;
//        rect.height = 540;

        SuperPlayerModel model = new SuperPlayerModel();
        model.appId = 1300414804;// 配置 AppId
        model.videoId = new SuperPlayerVideoId();
        model.videoId.fileId = "5285890797455267129"; // 配置 FileId
        mSuperPlayerView.playWithModel(model);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuperPlayerView.resetPlayer();
    }

}
