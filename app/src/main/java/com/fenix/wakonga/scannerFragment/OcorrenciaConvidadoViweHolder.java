package com.fenix.wakonga.scannerFragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenix.wakonga.R;

public class OcorrenciaConvidadoViweHolder extends RecyclerView.ViewHolder  {

    public TextView txtMenuName;
    public ImageView imageView;


    public OcorrenciaConvidadoViweHolder(@NonNull View itemView) {
        super(itemView);

        txtMenuName=(TextView)itemView.findViewById(R.id.idocorrencia);
        imageView=(ImageView) itemView.findViewById(R.id.imgocorrencia);

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

    private OcorrenciaConvidadoViweHolder.ClickListener mClickListener;


    //interface to send callbeck
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(OcorrenciaConvidadoViweHolder.ClickListener clickListener){
        mClickListener=clickListener;

    }
}
