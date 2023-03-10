package com.example.itda.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.example.itda.MainActivity;
import com.example.itda.R;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashHandler(), 3000);  //1초 후에 hd handler 실행 3000ms = 3초
    }

    private class splashHandler implements Runnable{
        @Override
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class));
            SplashActivity.this.finish();   //로딩페이지 Activity Stack에서 제거
        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈 때 뒤로가기 버튼 못누르게 함
    }
}
