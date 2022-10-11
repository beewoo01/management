package com.example.rfid.ui.activity;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.rfid.R;
import com.example.rfid.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity {
    ActivitySplashBinding binding;
    Animation fadeInAnimationImg, fadeOutAnimationImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mContext = this;

        //액션바 투명
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        fadeOutAnimationImg = AnimationUtils.loadAnimation(this, R.anim.logo_fade_out);
        fadeOutAnimationImg.setAnimationListener(fadeOutAnimationListener2);
        fadeInAnimationImg = AnimationUtils.loadAnimation(this, R.anim.logo_fade_in);
        fadeInAnimationImg.setAnimationListener(fadeInAnimationListener2);

        binding.logoImv.startAnimation(fadeInAnimationImg);
    }



    /*스플래시 로고 페이드 아웃 리스너*/
    public Animation.AnimationListener fadeOutAnimationListener2 = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {

            binding.logoImv.setVisibility(View.GONE);
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_fade_out, R.anim.activity_fade_in);
            finish();

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };


    /*스플래시 로고 페이드 인 리스너*/
    public Animation.AnimationListener fadeInAnimationListener2 = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            binding.logoImv.startAnimation(fadeOutAnimationImg);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    };
}