package com.chacha.kkang.moolbantabs.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.chacha.kkang.moolbantabs.component.PagerView;
import com.chacha.kkang.moolbantabs.TAB_DATA;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kkang on 2018. 10. 29..
 */

public class Adapter_Pager extends PagerAdapter {
    private Context context;

    private ArrayList<TAB_DATA> categories;

    private HashMap<String, PagerView> viewHashMap = new HashMap<>();

    public Adapter_Pager(Context context,ArrayList<TAB_DATA> categories ) {
        this.context = context;
        this.categories = categories;
    }


    public void setCategories(ArrayList<TAB_DATA> categories ){
        viewHashMap.clear();
        this.categories = categories;
    }


    public PagerView getView(int position) {
        if(viewHashMap == null){
            viewHashMap = new HashMap<>();
        }

        if ( !viewHashMap.containsKey("" + categories.get(position).key)) {
            PagerView view = new PagerView(context, position, categories.get(position));
            String key = "" + categories.get(position).key;
            viewHashMap.put(key, view);
        }
        return viewHashMap.get(categories.get(position).key + "");
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String key = "" + categories.get(position).key;
        if (!viewHashMap.containsKey(key)) {
            PagerView view = new PagerView(context, position, categories.get(position));
            viewHashMap.put(key, view);
        }

        container.addView(viewHashMap.get(key));
        return viewHashMap.get(key);
    }


    @Override
    public int getCount() {
        if (categories == null) {
            return 0;
        }
        return categories.size();
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).name;
    }
}
