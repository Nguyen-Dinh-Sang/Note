package com.example.mynote.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mynote.R;
import com.example.mynote.iterface.OnItemClickListener;


public class ColorChooseAdapter extends RecyclerView.Adapter<ColorChooseAdapter.ViewHolder> {

    private String TAG = "AdapterColorPicker";
    private String[] colorList;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public ColorChooseAdapter(String[] colorList, Context context) {
        this.colorList = colorList;
        this.context =context;
        Log.d(TAG, "AdapterColorPicker: " + colorList.length);
        if (colorList == null) {
            this.colorList = new String[]{"#000000"};
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public String[] getColorList() {
        return colorList;
    }

    @Override
    public ColorChooseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_colorpicker_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ColorChooseAdapter.ViewHolder holder, int position) {
        holder.view.setBackgroundColor(Color.parseColor(colorList[position]));
    }

    @Override
    public int getItemCount() {
        return colorList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.itemColorPicker);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener!=null) onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
