package com.fenix.wakonga.servicos;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.fenix.wakonga.R;

public class SlidingImage_Adapter extends RecyclerView.ViewHolder {

    //public TextView txtMenuName;
    public ImageView imageView;

    private AdapterView.OnItemClickListener itemClickListener;


    public SlidingImage_Adapter(@NonNull View itemView) {
        super(itemView);
        //txtMenuName=(TextView)itemView.findViewById(R.id.imageslide);
        imageView=(ImageView)itemView.findViewById(R.id.imageslide);

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });
    }



    private SlidingImage_Adapter.ClickListener mClickListener;


    //interface to send callbeck
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(SlidingImage_Adapter.ClickListener clickListener){
        mClickListener=clickListener;
    }
}
