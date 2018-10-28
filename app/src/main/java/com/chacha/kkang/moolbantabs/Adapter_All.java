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

public class Adapter_All extends RecyclerView.Adapter<Adapter_All.ViewHolder> {
    Context context;
    ArrayList<TAB_DATA> dataList = new ArrayList<>();
    OnSubAreaItemClickListener listener;
    boolean isReserve;
    boolean isTalk = false;

    public Adapter_All(Context context,ArrayList<TAB_DATA> dataList, OnSubAreaItemClickListener listener) {
        this.context = context;
        this.isReserve = isReserve;
        this.dataList = dataList;
        this.listener = listener;
        this.isTalk = isTalk;
    }

    public void setData(ArrayList<TAB_DATA> dataList) {
        this.dataList = dataList;
    }

    @Override
    public Adapter_All.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tabview, parent, false);
        return new Adapter_All.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(dataList.get(position), position);
    }


    @Override
    public int getItemCount() {
        if(dataList == null){
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
            itemView.setOnClickListener(this);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        public void setData(TAB_DATA _data, int pos) {
            data = _data;
            position = pos;

            tv.setText(data.name);
            if (data.isSelect) {
                tv.setTextColor(Color.parseColor("#e84418"));
                tv.setBackgroundResource(R.drawable.shape_round_tomato_trnas_7);
            }  else {
                tv.setTextColor(Color.parseColor("#231916"));
                tv.setBackgroundResource(R.drawable.shape_round_231916_trnas_7);

            }

        }

        @Override
        public void onClick(View v) {
            YoYo.with(Techniques.StandUp)
                    .duration(700)
                    .playOn(v);
            listener.onSubItemClick(position, data);

        }
    }

    public interface OnSubAreaItemClickListener {
        void onSubItemClick(int position, TAB_DATA data);
        void onSubItemClick(int position, TAB_DATA data, View v);
    }
}

