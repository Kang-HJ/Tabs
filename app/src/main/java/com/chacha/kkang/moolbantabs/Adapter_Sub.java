package com.chacha.kkang.moolbantabs;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

/**
 * Created by kkang on 2018. 10. 28..
 */

public class Adapter_Sub extends RecyclerView.Adapter<Adapter_Sub.ViewHolder> {
    Context context;
    ArrayList<TAB_DATA> dataList;
    OnSubAreaItemClickListener listener;

    public Adapter_Sub(Context context, ArrayList<TAB_DATA> dataList, OnSubAreaItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
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

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        public void setData(TAB_DATA _data, int pos) {
            data = _data;
            position = pos;
            itemView.setOnClickListener(this);

            tv.setText(data.name);
            if (data.isSelect) {
                tv.setTextColor(Color.parseColor("#3e3e3e"));
            } else {
                tv.setTextColor(Color.parseColor("#878787"));

            }

        }

        @Override
        public void onClick(View v) {

            listener.onSubItemClick(position, data, itemView);

        }
    }

    public interface OnSubAreaItemClickListener {
        void onSubItemClick(int position, TAB_DATA data);

        void onSubItemClick(int position, TAB_DATA data, View v);
    }
}

