package com.fenix.wakonga.galeria;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenix.wakonga.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    public List<String> fileNameList;
    public List<String> fileDoneList;
    public List<String> fileImageList;


    public UploadListAdapter(List<String> fileNameList, List<String> fileDoneList, List<String> fileImageList) {
        this.fileNameList = fileNameList;
        this.fileDoneList = fileDoneList;
        this.fileImageList = fileImageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single_photo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String fileName=fileNameList.get(i);
        viewHolder.fileNameView.setText(fileName);

        String imagem=fileImageList.get(i);
        Picasso.get().load(imagem).into(viewHolder.imageView);

       /* String fileDone=fileDoneList.get(i);

        if (fileDone.equals("uploading")){
            viewHolder.fileDoneView.setImageResource(R.drawable.uploadphoto);
        }else {
            viewHolder.fileDoneView.setImageResource(R.drawable.uploadphoto);
        }*/


    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView fileNameView;
        ImageView fileDoneView, imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            fileNameView=mView.findViewById(R.id.upload_filename);
            fileDoneView=mView.findViewById(R.id.upload_loading);
            imageView=mView.findViewById(R.id.upload_icon);


        }
    }
}
