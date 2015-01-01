package com.grabbr.grabbrapp.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {
	 
    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

   public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

   public MyTextView(Context context) {
        super(context);
   }


   public void setTypeface(Typeface tf, int style) {
         if (style == Typeface.BOLD) {
              super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/MYRIADPRO-BOLDCOND.OTF")/*, -1*/);
          } else if(style == Typeface.ITALIC){
        	  super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/MYRIADPRO-CONDIT.OTF")/*, -1*/);
          }else if(style == Typeface.BOLD_ITALIC){
        	  super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/MYRIADPRO-BOLDCONDIT.OTF")/*, -1*/);
          }else if(style == Typeface.NORMAL){
        	  super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/MYRIADPRO-REGULAR.OTF")/*, -1*/);
          }else{
             super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/MYRIADPRO-COND.OTF")/*, -1*/);
          }
    }
}
