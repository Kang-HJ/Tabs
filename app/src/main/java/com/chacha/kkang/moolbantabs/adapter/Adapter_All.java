package com.chacha.kkang.moolbantabs.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chacha.kkang.moolbantabs.R;

/**
 * Created by kkang on 2018. 10. 28..
 */

public class Adapter_All extends RecyclerView.Adapter<Adapter_All.ViewHolder> {
   Context context;
//    ArrayList<TAB_DATA> dataList;
//    setOnTabClickListener listener;
public Adapter_All(Context context){

}
//    public Adapter_All(Context context, ArrayList<TAB_DATA> dataList, setOnTabClickListener listener) {
//        this.context = context;
//        this.dataList = dataList;
//        this.listener = listener;
//    }

//    public void setData(ArrayList<TAB_DATA> dataList) {
//        this.dataList = dataList;
//    }

    @Override
    public Adapter_All.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tabview, parent, false);
        return new Adapter_All.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.setData(dataList.get(position), position);
    }


    @Override
    public int getItemCount() {
//        if (dataList == null) {
//            return 0;
//        }
//        return dataList.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        TAB_DATA data;
//        int position;
//        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
           // tv = (TextView) itemView.findViewById(R.id.tv);
        }

//        public void setData(TAB_DATA _data, int pos) {
//            data = _data;
//            position = pos;
//
//            tv.setText(data.name);
//
//            if (data.isSelect) {
//                tv.setTextColor(Color.parseColor("#e84418"));
//                tv.setBackgroundResource(R.drawable.shape_round_tomato_trans_7);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    tv.setTextAppearance(R.style.FontBoldTextViewStyle);
//                } else {
//                    tv.setTextAppearance(context, R.style.FontBoldTextViewStyle);
//                }
//            } else {
//                tv.setTextColor(Color.parseColor("#231916"));
//                tv.setBackgroundResource(R.drawable.shape_round_line01_white_7);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    tv.setTextAppearance(R.style.FontTextViewStyle);
//                } else {
//                    tv.setTextAppearance(context, R.style.FontTextViewStyle);
//                }
//            }
//        }
//
        @Override
        public void onClick(View v) {
   //         listener.onTabClick(position, data);

        }
    }

 //   public interface setOnTabClickListener {
     //   void onTabClick(int position, TAB_DATA data);
 //   }
}

