package com.moje.onlyu.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.moje.onlyu.GApplication;


public class TextViewTF extends TextView {

    public TextViewTF(Context context) {
        super(context);
    }

    public TextViewTF(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewTF(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTypeface(Typeface tf) {
       // setTextSize();
        if (isInEditMode()) {
            super.setTypeface(tf);
            return;
        } else {
            super.setTypeface(GApplication.getInstance().getIconfont());
        }
    }
}
