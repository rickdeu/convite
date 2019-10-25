package com.fenix.wakonga.servicos;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.fenix.wakonga.R;
import com.fenix.wakonga.model.Estilos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CountryAdapterSlide extends ArrayAdapter <Estilos>{
    public CountryAdapterSlide(Context context, ArrayList<Estilos> countryItems) {
        super(context, 0, countryItems);

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.slidingimages_layout, parent, false
            );
        }
        ImageView imageViewFlag = convertView.findViewById(R.id.image);
        Estilos currentItem = getItem(position);
        if (currentItem != null){
            //imageViewFlag.setImageResource(currentItem.getmFlagImage());
            Picasso.get().load(currentItem.getConvite()).into(imageViewFlag);
        }
        return convertView;
    }
}
