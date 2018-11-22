package com.chacha.kkang.moolbantabs.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chacha.kkang.moolbantabs.R;
import com.chacha.kkang.moolbantabs.RollingScrollView;
import com.chacha.kkang.moolbantabs.TALK_NOTI_LIST;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class RollingSampleActivity extends AppCompatActivity {

    RollingScrollView rollingScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rolling_sample);

        rollingScrollView = (RollingScrollView) findViewById(R.id.RollingScrollView);

        ArrayList<TALK_NOTI_LIST> list = new ArrayList<>();
        TALK_NOTI_LIST data = new TALK_NOTI_LIST();
        data.tc_name = "공지";
        data.tl_subject = "123123123123123";
        list.add(data);

        data = new TALK_NOTI_LIST();
        data.tc_name = "공지";
        data.tl_subject = "reweqeqrqwerq";
        list.add(data);

        data = new TALK_NOTI_LIST();
        data.tc_name = "공지";
        data.tl_subject = "ㅈㄷㄱㅈㄱㅈㄱㅈㄱㄷㅈㄷㄱ";
        list.add(data);

        rollingScrollView.setData(list);

    }
}
