package com.fenix.wakonga.convites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenix.wakonga.R;

public class ConviteAdapter  extends RecyclerView.ViewHolder{


    public TextView txtMenuName;
    public ImageView imageView;

    private AdapterView.OnItemClickListener itemClickListener;


    public ConviteAdapter(@NonNull View itemView) {
        super(itemView);
        txtMenuName=(TextView)itemView.findViewById(R.id.texte_view_name);
        imageView=(ImageView)itemView.findViewById(R.id.image_flag);

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



    private ConviteAdapter.ClickListener mClickListener;


    //interface to send callbeck
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(ConviteAdapter.ClickListener clickListener){
        mClickListener=clickListener;
    }
}
