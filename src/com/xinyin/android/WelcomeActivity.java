package com.xinyin.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class WelcomeActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        View view = findViewById(R.id.launcher);
        AlphaAnimation aa=new AlphaAnimation(0.8f, 1f);
		aa.setDuration(4000);
        view.startAnimation(aa);
        aa.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		});
    }
}
