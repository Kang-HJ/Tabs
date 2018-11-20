package com.chacha.kkang.moolbantabs.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chacha.kkang.moolbantabs.R;
import com.kkang.mbtabs.component.TAB_DATA;

import java.util.ArrayList;

/**
 * Created by kkang on 2018. 10. 28..
 */

public class Adapter_Sub extends RecyclerView.Adapter<Adapter_Sub.ViewHolder> {

    Context context;
    ArrayList<TAB_DATA> dataList;
    setOnSubTabClickListener listener;

    public Adapter_Sub(Context context, setOnSubTabClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(ArrayList<TAB_DATA> dataList) {
        this.dataList = dataList;
    }

    @Override
    public Adapter_Sub.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subtabview, parent, false);
        return new Adapter_Sub.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(dataList.get(position), position);
    }


    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TAB_DATA data;
        int position;
        TextView tv;
        ImageView ivNon;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
            ivNon = (ImageView) itemView.findViewById(R.id.ivNon);
        }

        public void setData(TAB_DATA _data, int pos) {
            data = _data;
            position = pos;
            itemView.setOnClickListener(this);

            if (data == null) {
                tv.setVisibility(View.GONE);
                ivNon.setVisibility(View.VISIBLE);
            } else {
                tv.setVisibility(View.VISIBLE);
                ivNon.setVisibility(View.GONE);
                tv.setText(data.name);
                if (data.isSelect) {
                    tv.setTextColor(Color.parseColor("#3e3e3e"));
                    tv.setTypeface(null, Typeface.BOLD);
                } else {
                    tv.setTextColor(Color.parseColor("#878787"));
                    tv.setTypeface(null, Typeface.NORMAL);
                }
            }
        }

        @Override
        public void onClick(View v) {
            listener.onSubTabClick(position, data);
        }
    }

    public interface setOnSubTabClickListener {
        void onSubTabClick(int position, TAB_DATA data);
    }
}

