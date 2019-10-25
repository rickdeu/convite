package com.fenix.wakonga.convidadoFragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fenix.wakonga.R;
import com.squareup.picasso.Picasso;

public class ViewHolderConvidado extends RecyclerView.ViewHolder {


    View mView;

    public ViewHolderConvidado(@NonNull View itemView) {
        super(itemView);
        mView=itemView;
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
    //set details to recycler view item_convidado
    public void setDetails(Context ctx, String nomeC, String sobreoneC, String img, String telefoneC, String emailC, String acompanhanteC, int
                            quantidade){
        //views
        TextView nome=mView.findViewById(R.id.nomeC);
        TextView sobrenome=mView.findViewById(R.id.sobrenomeC);
       final  ImageView mImageTv=mView.findViewById(R.id.imgC);
        //set data to views
        nome.setText(nomeC);
        sobrenome.setText(sobreoneC);
    if (img!=null){
        //Picasso.get().load(img).into(mImageTv);

        Glide.with(ctx)
                .load(img)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //rootContent.setBackground(resource);
                            mImageTv.setImageDrawable(resource);

                        }
                    }
                });


    }else {
        Picasso.get().load(R.drawable.logoeditado).into(mImageTv);
    }
}
    private ViewHolderConvidado.ClickListener mClickListener;
    //interface to send callbeck
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(ViewHolderConvidado.ClickListener clickListener){
        mClickListener=clickListener;
    }




}
