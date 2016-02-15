package cn.fxdata.tv.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;

import cn.fxdata.tv.utils.DPIUtil;

public class DateDrawable extends Drawable {
    private CharSequence hh = "00", mm = "00", ss = "00";
    private final String PREFIX = ":";

    private TextPaint paint;
    private int text_color = Color.BLACK;
    private int prefix_color = Color.BLACK;
    private int background_color = Color.WHITE;

    private int background_width = 0;
    private int background_height = 0;
    private Typeface mTypeFace;

    public DateDrawable() {
        paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setTextSize(18);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setStyle(Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            Typeface typeFace = (mTypeFace == null) ? Typeface.DEFAULT_BOLD
                    : mTypeFace;
            paint.setTypeface(typeFace);
            final Rect rect = getBounds();
            final Rect padding = new Rect();
            Log.d("DateDrawable", " rect -->> x = " + rect.left + " r "
                    + rect.right + " t " + rect.top + " b " + rect.bottom);
            getPadding(padding);
            // final int space = DPIUtil.dip2px(2);
            final int space = 4;
            // float end = rect.left + padding.left + 9 * space + 3 *
            // background_width;
            Log.d("DateDrawable", " background_width " + background_width
                    + " space width is " + space);
            float end = 3 * background_width + 8 * space;
            Log.d("DateDrawable", " end -->>  = " + end);
            float drawableWidth = end;
            float start = rect.right - end;
            float x = start / 2;
            // if(Log.D){
            Log.d("DateDrawable", " -->> x = " + x);
            // }
            // float y = rect.top + padding.top + paint.getTextSize() + 1;
            // float textX = background_width / 2 - paint.getTextSize() / 2 + x;
            float textY = (background_height - paint.getTextSize()) / 2
                    + rect.top + padding.top + paint.getTextSize() + 1;// rect.top
                                                                       // +
                                                                       // padding.top
                                                                       // +
                                                                       // space;

            float bgLeft_1 = x;
            float bgLeft_2 = x + background_width + 4 * space;
            float bgLeft_3 = x + 2 * background_width + 8 * space;

            float bgRight_1 = x + background_width;
            float bgRight_2 = x + 2 * background_width + 4 * space;
            float bgRight_3 = x + 3 * background_width + 8 * space;

            float textX_1 = x + (measureX(paint, hh));
            Log.d("DateDrawable", " -->> x = " + x);
            float textX_2 = (x + background_width + 4 * space)
                    + measureX(paint, mm);
            float textX_3 = (x + 2 * background_width + 8 * space)
                    + measureX(paint, ss);
            if (DPIUtil.getWidth() <= 240 && DPIUtil.getHeight() <= 320) {
                textX_1--;
                textX_2--;
                textX_3--;
            }

            float tagX_1 = x + background_width + space;
            float tagX_2 = x + 2 * background_width + 5 * space;
            Log.d("DateDrawable", "-->> x1 " + textX_1 + " -->> " + textX_2
                    + " -->> " + textX_3);

            // 画hh背景框
            paint.setColor(background_color);
            paint.setStyle(Style.FILL);
            canvas.drawOval(new RectF(bgLeft_1, space, bgRight_1, space
                    + background_height), paint);
            // 画hh时
            paint.setStyle(Style.STROKE);
            paint.setColor(text_color);

            canvas.drawText(hh, 0, hh.length(), textX_1, textY
                    + measureY(paint), paint);
            // 画hh冒号
            paint.setColor(prefix_color);
            canvas.drawText(PREFIX, 0, PREFIX.length(), tagX_1, textY
                    + measureY(paint), paint);
            // 画mm背景框
            paint.setColor(background_color);
            paint.setStyle(Style.FILL);
            canvas.drawOval(new RectF(bgLeft_2, space, bgRight_2, space
                    + background_height), paint);
            // 画mm分
            paint.setStyle(Style.STROKE);
            paint.setColor(text_color);
            canvas.drawText(mm, 0, mm.length(), textX_2, textY
                    + measureY(paint), paint);
            // 画mm冒号
            /*
             * paint.setColor(prefix_color); canvas.drawText(PREFIX, 0,
             * PREFIX.length(), tagX_2, textY + measureY(paint), paint); //
             * 画ss背景框 paint.setColor(background_color);
             * paint.setStyle(Style.FILL); canvas.drawOval(new RectF(bgLeft_3,
             * space, bgRight_3, space + background_height), paint); // 画ss秒
             * paint.setStyle(Style.STROKE); paint.setColor(text_color);
             * canvas.drawText(ss, 0, ss.length(), textX_3, textY +
             * measureY(paint), paint);
             */

        } catch (Exception e) {
        }
    }

    private float measureX(Paint paint, CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            float textWidth = paint.measureText(text.toString());
            return (background_width - textWidth) / 2;
        }
        return 0;
    }

    private float measureY(Paint paint) {
        // FontMetrics fm = paint.getFontMetrics();
        // float fontHeight = (float) (Math.ceil(fm.descent - fm.ascent) / 2);
        return 0;// (background_height - fontHeight) ;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    public void setHour(CharSequence text) {
        this.hh = text;
    }

    public void setMinute(CharSequence text) {
        this.mm = text;
    }

    public void setSecond(CharSequence text) {
        this.ss = text;
    }

    public int getTextColor() {
        return text_color;
    }

    public void setTextColor(int color) {
        this.text_color = color;
    }

    public void setPointColor(int color) {
        this.prefix_color = color;
    }

    public void setBackgroundColor(int color) {
        this.background_color = color;
    }

    public void setBackgroundWidth(int width) {
        this.background_width = width;
    }

    public void setBackgroundHeight(int height) {
        this.background_height = height;
    }

    public void setTextSize(float textSize) {
        if (paint != null) {
            paint.setTextSize(textSize);
        }
    }

    public void setTypeFace(Typeface typeFace) {
        mTypeFace = typeFace;
        if (paint != null) {
            paint.setTypeface(typeFace);
        }
    }

}
