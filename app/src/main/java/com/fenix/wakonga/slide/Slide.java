 package com.fenix.wakonga.slide;

 import android.content.Intent;
 import android.graphics.drawable.AnimationDrawable;
 import android.os.Bundle;
 import androidx.annotation.NonNull;
 import androidx.viewpager.widget.ViewPager;
 import androidx.appcompat.app.AppCompatActivity;
 import android.text.Html;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.WindowManager;
 import android.widget.Button;
 import android.widget.LinearLayout;
 import android.widget.RelativeLayout;
 import android.widget.TextView;

 import com.fenix.wakonga.MainActivity;
 import com.fenix.wakonga.R;

 public class Slide extends AppCompatActivity {
    private ViewPager mSliderViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;

    private SlideAdapter slideAdapter;
     LayoutInflater layoutInflater;


     private TextView entrarLogin;
     private Button mNextBtn;
     private Button mBackBtn;
     RelativeLayout mLayout, fundoPrinciapl;
     AnimationDrawable animationDrawable;
   /*  DatabaseReference fundoPrincipal;
     FirebaseDatabase mFirebaseDatabase;
*/


     private int mCurrentPage;
    // private FirebaseAuth mAuth;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.slide_main);

        mLayout=findViewById(R.id.mylayout);
        fundoPrinciapl=findViewById(R.id.fundo_principal);

        animationDrawable=(AnimationDrawable) mLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        mSliderViewPager=(ViewPager)findViewById(R.id.slideViewPager);
         mDotLayout=findViewById(R.id.dotsLayout);
        mNextBtn=(Button)findViewById(R.id.nextBtn);
        mBackBtn=(Button)findViewById(R.id.prevBtn);

        entrarLogin=findViewById(R.id.entrarLogin);

        slideAdapter=new SlideAdapter(this);
        mSliderViewPager.setAdapter(slideAdapter);

        addDotsIndicator(0);
        mSliderViewPager.addOnPageChangeListener(viewListener);
        mBackBtn.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View  view){
                                            mSliderViewPager.setCurrentItem(mCurrentPage-1);
                                        }
                                    }

        );

        mNextBtn.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View  view){
                                            mSliderViewPager.setCurrentItem(mCurrentPage+1);
                                        }
                                    }

        );


        entrarLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View  view){
                startActivity(new Intent(Slide.this, MainActivity.class));
                finish();
            }
                                    }

        );
 //   servicos();
    }



     public void addDotsIndicator(int position){
         mDotLayout.removeAllViews(); // This right here!
         mDots=new TextView[3];
        for (int i=0; i<mDots.length; i++){
            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.transparent2));
            mDotLayout.addView(mDots[i]);

        }
         if (mDots.length>0){
             mDots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
         }
    }
    ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int i) {

           addDotsIndicator(i);
            mCurrentPage=i;

            if (i==0){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.VISIBLE);;

                mNextBtn.setText("");
                mBackBtn.setText("");
            }
            else
                if(i==mDots.length-1){
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(true);
                    mBackBtn.setVisibility(View.VISIBLE);;

                    mNextBtn.setText("");
                    mBackBtn.setText("");
                }
                else
                if(i==mDots.length-2){
                    mNextBtn.setEnabled(false);
                    mBackBtn.setEnabled(true);
                    mBackBtn.setVisibility(View.VISIBLE);;
                    mNextBtn.setText("");
                    mBackBtn.setText("");
                }
                else{
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(true);
                    mBackBtn.setVisibility(View.VISIBLE);;
                    mNextBtn.setText("");
                    mBackBtn.setText("");
                }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

/*
     public void setFundoPrincipal(){
         fundoPrincipal.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 FundoEstiloConvite fundoEstiloConvite=dataSnapshot.getValue(FundoEstiloConvite.class);
                 if (fundoEstiloConvite!=null){
                     if (fundoEstiloConvite.getFundEstiloConvite()!=null){
                         Glide.with(getApplicationContext())
                                 .load(fundoEstiloConvite.getFundEstiloConvite())
                                 .into(new SimpleTarget<Drawable>() {
                                     @Override
                                     public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                             fundoPrinciapl.setBackground(resource);
                                         }
                                     }
                                 });
                     }
                 } }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
     }

*/
/*private void servicos() {

    View servico=LayoutInflater.from(this).inflate(R.layout.slide_layout,null);
    TextView button=servico.findViewById(R.id.id_servicos_casamento);
    button.setOnClickListener(new View.OnClickListener(){
                                       @Override
                                       public void onClick(View  view){
                                           startActivity(new Intent(Slide.this, CategoriaServico.class));
                                       }
                                   }

    );




}*/
 }
