package com.fenix.wakonga.slide;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fenix.wakonga.R;

public class SlideAdapter  extends PagerAdapter{
    Context context;
    LayoutInflater layoutInflater;
    public SlideAdapter(Context context){
        this.context=context;
    }

    //Arrays
    public int[] slide_images={
            R.drawable.introactivity2,
            R.drawable.flores3,
            R.drawable.convitesdecasamento
    };
    public String[] slide={
            "As condições para o seu casamento na palma da mão!",
            "Temos diversos fornecedores a sua escolha!",
            "Notifique os seus convidados com apenas uma mensagem!"
    };
    public String[] slide_headings={
            "Encontre tudo o que precisa para o seu casamento!",
            "Espaços para casamentos, Fotografos, Música, Carros, Lojas de noivas, Arranjos florais, Fatos de noivos!",
            "Amar-te é o melhor que aconteceu na minha vida, por isso sou feliz e tenho prazer de me sentir parte ti!",
    };
    public String[] slide_descs={
            "Facil, simples e seguro, tenha a lista dos convidados no seu telefone, evite os convites em papel e gaste menos para o seu casamento!",
            "Envie para os convidados do seu casamento um e-mail ou uma mensagem de texto!",
            "Tudo que procura para o seu casamento, você encontra aqui, elabore os requisitos do seu casamento atraves da nossa aplicação, aqui temos as melhores opções para ter o casamento que sempre sonhou!"
    };
    @Override
    public int getCount() {
        return slide_headings.length;
    }
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view==(RelativeLayout) o;
    }
    @NonNull
    @Override
    public Object instantiateItem( ViewGroup container, int position){
       layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
       View view=layoutInflater.inflate(R.layout.slide_layout,container, false);
        ImageView slideImageView=(ImageView)view.findViewById(R.id.slide_image);
        TextView slideSlide=(TextView)view.findViewById(R.id.slide);
        TextView slideHeading=(TextView)view.findViewById(R.id.slide_heading);
        TextView slideDescription=(TextView)view.findViewById(R.id.slide_desc);
        slideImageView.setImageResource(slide_images[position]);
        slideSlide.setText(slide[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);
        Animation animation= AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        slideImageView.startAnimation(animation);
        container.addView(view);
       return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object o) {
        container.removeView((RelativeLayout)o);
    }


}
