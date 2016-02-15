
package com.zgntech.core.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @title : FontHelper.java
 * @package : com.zgntech.core.util
 * @description :设置应用字体
 * @author: lyu
 * @date:2015-4-20
 */
public class FontHelper {
    private static final String TAG = "FontHelper";

    private FontHelper() {
    }

    private static Typeface typeFace;

    private static void initTypeface(Context c) {
        if (null == typeFace) {
            typeFace = Typeface.createFromAsset(c.getAssets(), "fonts/STXIHEI.TTF");
        }
    }

    public static void setFont(TextView textView) {
        initTypeface(textView.getContext());
        // 应用字体
        textView.setTypeface(typeFace);
    }

    /*
     * Sets the font on all TextViews in the ViewGroup. Searches recursively for
     * all inner ViewGroups as well. Just add a check for any other views you
     * want to set as well (EditText, etc.)
     */
    public static void applyFont(final Context context, final View root, final String fontName) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    applyFont(context, viewGroup.getChildAt(i), fontName);
            } else if (root instanceof TextView || root instanceof EditText
                    || root instanceof Button)
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(),
                        fontName));
        } catch (Exception e) {
            Log.e(TAG, String.format("Error occured when trying to apply %s font for %s view",
                    fontName, root));
            e.printStackTrace();
        }
    }

    /**
     * 加粗
     * 
     * @param tv
     */
    public static void bold(TextView tv) {
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
    }
}
