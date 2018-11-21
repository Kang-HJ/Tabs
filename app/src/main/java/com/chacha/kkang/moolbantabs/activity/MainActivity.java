package com.chacha.kkang.moolbantabs.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.chacha.kkang.moolbantabs.R;
import com.chacha.kkang.moolbantabs.adapter.Adapter_Pager;
import com.kkang.mbtabs.component.MBTab;
import com.kkang.mbtabs.component.TAB_DATA;

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

    private MBTab mbTab;
    private ArrayList<TAB_DATA> tabList;

    public ViewPager pager;
    private Adapter_Pager adapterPager;
    private LinearLayout llAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        setUI();
        setView();
    }

    private void initData() {
        tabList = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            TAB_DATA data = new TAB_DATA();
            data.key = i + "";
            data.name = i + " TAB";
            if (i == 3) {
                data.name += "BBBBB";
            }

            if (i == 0) {
                data.isSelect = true;
            }

            data.subList = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                TAB_DATA sub = new TAB_DATA();
                sub.key = i + "";
                sub.name = i + " SUB TAB";
                data.isSelect = false;
                if (j == 0) {
                    sub.isSelect = true;
                }
                data.subList.add(sub);
            }
            tabList.add(data);
        }

        adapterPager = new Adapter_Pager(MainActivity.this, tabList);
    }

    private void setUI() {
        pager = (ViewPager) findViewById(R.id.pager);
        mbTab = (MBTab) findViewById(R.id.mbTab);
        llAll = (LinearLayout) findViewById(R.id.llAll);
    }

    private void setView() {
        llAll.setVisibility(View.GONE);
        pager.setAdapter(adapterPager);
        pager.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);

        mbTab.setMainTabResource(R.drawable.shape_round_tomato_trans_7, R.drawable.shape_round_line01_white_7);
        mbTab.setMainTabMargin(intToDp(MainActivity.this, 3));
        mbTab.setMainTabSetting("#e84418", "#231916", 12);
        mbTab.setMainTabNoImgVisible(false);

        mbTab.setSubTabResource(R.drawable.shape_round_line01_white_7);
        mbTab.setSubTabSetting("#3e3e3e", "#878787", 12);
        mbTab.setSubTabMargin(floatToDp(MainActivity.this, 1.5f));
        mbTab.setSubTabPadding(intToDp(MainActivity.this, 10));
        mbTab.setSubTabNoImgVisible(true);
        mbTab.setSubTabNoImgResource(R.drawable.sketch_fish_180927);
        mbTab.setSubTabNoImgPadding(intToDp(MainActivity.this, 10));

        mbTab.setData(tabList, llAll);
        mbTab.tabBar.setViewPager(pager);
        mbTab.setMainTabCount(3);
        mbTab.setSubTabCount(3);
        mbTab.setFadingResource(R.drawable.sketch_g_181112);
        mbTab.setVisibleFading(true);
        mbTab.setVisibleAll(true);
        mbTab.setSubTabBackground("#f7f7f7");
        mbTab.setCollectTabSetting("모아보기", "#000000", 13);
        int dp6_5 = floatToDp(MainActivity.this, 6.5f);
        int dp11_5 = floatToDp(MainActivity.this, 11.5f);
        mbTab.setSubTabPadding(dp11_5, dp6_5, dp11_5, dp6_5);
    }

    public static int intToDp(Context context, int value) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }

    public static int floatToDp(Context context, float value) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }
}
