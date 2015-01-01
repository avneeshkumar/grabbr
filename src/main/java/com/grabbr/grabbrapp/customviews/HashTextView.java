package com.grabbr.grabbrapp.customviews;

import java.util.StringTokenizer;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

public class HashTextView extends TextView {
	 
    public HashTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

   public HashTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

   public HashTextView(Context context) {
        super(context);
   }

	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(text.toString());
		String output = "";
		while (st.hasMoreTokens()) {
	    	 String s = st.nextToken();
	         if(s.startsWith("#")){
	        	 if(s.substring(1).length()!=0){
	        		output+=" "+"<font color=\"#1182ae\">"+s.substring(0)+"</font>";
	        	 }
	         }else{
	        	 output+=" "+s;
	         }
	    } 
		super.setText(Html.fromHtml(output), BufferType.SPANNABLE);
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