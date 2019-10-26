package com.fenix.wakonga.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.io.File;

public class FontView extends androidx.appcompat.widget.AppCompatTextView{

    public FontView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(context, attrs);
    }

    public FontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context, attrs);
    }

    public FontView(Context context) {
        super(context);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts" + File.separator + "Roboto-Light.ttf"));
    }
}
