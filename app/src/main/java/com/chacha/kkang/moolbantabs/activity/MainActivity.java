package com.chacha.kkang.moolbantabs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chacha.kkang.moolbantabs.R;
import com.chacha.kkang.moolbantabs.RollingScrollView;
import com.chacha.kkang.moolbantabs.adapter.Adapter_Pager;
import com.kkang.mbtabs.component.MBTab;
import com.kkang.mbtabs.component.CUSTOM_TAB_DATA;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static void Debug(String msg) {
        Log.d("KKANG", buildLogMsg(msg));
    }

    private static String buildLogMsg(String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ste.getFileName());
        sb.append(" > ");
        sb.append(ste.getMethodName());
        sb.append(" > #");
        sb.append(ste.getLineNumber());
        sb.append("] ");
        sb.append(message);

        return sb.toString();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvTab = (TextView) findViewById(R.id.tvTab);
        TextView tvRolling = (TextView) findViewById(R.id.tvRolling);

        tvTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
            }
        });
        tvRolling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RollingSampleActivity.class));
            }
        });
    }

}
