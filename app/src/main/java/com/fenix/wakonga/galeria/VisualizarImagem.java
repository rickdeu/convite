package com.fenix.wakonga.galeria;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fenix.wakonga.R;
import com.fenix.wakonga.model.FotoCasamento;

import java.util.ArrayList;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class VisualizarImagem extends DialogFragment {

    static VisualizarImagem newInstance() {
        VisualizarImagem f = new VisualizarImagem();
        return f;
    }



    private String TAG = VisualizarImagem.class.getSimpleName();
    private ArrayList<FotoCasamento> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle;
    private int selectedPosition = 0;
    //ImageView imgPartilhar, imgGuardar, imgWalper;
   // private static final int WRITE_EXTERNAL_STORAGE_CODE=1;
    ImageView imageViewPreview;
    //Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.slide_show_imagem, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
       /* imgPartilhar=(ImageView)v.findViewById(R.id.imgPartilhar);
        imgGuardar=(ImageView)v.findViewById(R.id.imgGuardar);
        imgWalper=(ImageView)v.findViewById(R.id.imgWalper);



        imgGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED){
                        String[] permission={Manifest.permission.WRITE_EXTERNAL_STORAGE};

                        requestPermissions(permission, WRITE_EXTERNAL_STORAGE_CODE);
                    }else {
                        salveImage();
                    }
                }else {
                    salveImage();
                }

            }
        });

        imgPartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
shareImage();
            }
        });
        imgWalper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageWalper();
            }
        });*/
        images = (ArrayList<FotoCasamento>) getArguments().getSerializable("images");

       // images=new ArrayList<FotoCasamento>((ArrayList<FotoCasamento>) getArguments().getSerializable("imagemAlbumId"));
        selectedPosition = getArguments().getInt("position");
        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images: " + images.size());
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        setCurrentItem(selectedPosition);
        return v;
    }
   /* private void setImageWalper() {
        WallpaperManager myWalpweManager=WallpaperManager.getInstance(getContext());
        try {
            myWalpweManager.setBitmap(bitmap);
            Toast.makeText(getContext(), " Imagem de fundo definida", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void shareImage() {
        try {
            String s=lblTitle.getText().toString();
            File file=new File( "simple.png");
            FileOutputStream fout=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            fout.close();file.setReadable(true, false);
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, s);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "partilhar com"));
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void salveImage() {
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        File path =Environment.getExternalStorageDirectory();
        File dir=new File(path+"/Casamento/Galeria Fotos");
        dir.mkdirs();
        String imageName=timeStamp+".PNG";
        File file =new File(dir, imageName);
        OutputStream out;
        try {
            out=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(getContext(), imageName+" Guardado em"+dir, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length>0&&grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    salveImage();
                }else {
                    Toast.makeText(getContext(),"Permissao desativada, active para guardar", Toast.LENGTH_LONG).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }
    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private void displayMetaInfo(int position) {
        //lblCount.setText((position + 1) + " de " + images.size());
        FotoCasamento image = images.get(position);
        lblTitle.setText(image.getTitle());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }
    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        public MyViewPagerAdapter() {
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.galeria_image_visualizar, container, false);
             imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);
            FotoCasamento image = images.get(position);
            //Picasso.get().load(image.getFoto()).into(imageViewPreview);
            Glide.with(getContext())
                    .load(image.getFoto())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                //rootContent.setBackground(resource);
                                imageViewPreview.setImageDrawable(resource);
                            }
                        }
                    });
                YoYo.with(Techniques.RotateInUpRight)
                        .duration(500)
                        .repeat(0)//repeat(YoYo.INFINITE)
                        .delay(position)
                        .playOn(imageViewPreview);
           //bitmap=((BitmapDrawable)imageViewPreview.getDrawable()).getBitmap();
            container.addView(view);
            return view;
        }
        @Override
        public int getCount() {
            return images.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}