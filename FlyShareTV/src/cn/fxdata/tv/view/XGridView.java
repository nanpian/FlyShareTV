package cn.fxdata.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 这个GridView主要用于解决 在ScrollView中嵌套GridView 导致GridView显示不全的问题
 * 
 * @author zhaoxin5
 * 
 */
public class XGridView extends GridView {
    public XGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XGridView(Context context) {
        super(context);
    }

    public XGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
