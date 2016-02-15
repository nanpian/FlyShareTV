package cn.fxdata.tv.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.fxdata.tv.R;

/**
 * Created by Jianyong on 15/6/12.
 * <p/>
 * Custom Dialog：
 * <p/>
 * final CustomDialog dialog = new CustomDialog(this, "童鞋，确定不追我了吗", "leftx",
 * "rightx"); dialog.setOnButtonClickListener(new
 * CustomDialog.OnButonClickListener() {
 *
 * @Override public void onLeftButtonClick() { Log.d(TAG, "onLeftClick");
 * if(dialog !=null && dialog.isShowing()){ dialog.dismiss(); } }
 * @Override public void onRightButtonClick() { Log.d(TAG, "onRightClick"); }
 * }); dialog.show();
 */
public class CustomDialog extends Dialog implements View.OnClickListener {

    public interface OnButonClickListener {
        void onLeftButtonClick();

        void onRightButtonClick();
    }

    private Context context;
    private OnButonClickListener onButtonClickListener;
    private TextView tvMessage;
    private Button btnLeft;
    private Button btnRight;

    private String message;
    private String leftText;
    private String rightText;
    private View line;

    public CustomDialog(Context context) {
        super(context, R.style.custom_dialog);
        this.context = context;
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    /**
     * 构造函数
     *
     * @param context  上下文
     * @param message  dialog所显示的message
     * @param btnLeft  左边按钮显示的文字
     * @param btnRight 右边按钮显示的文字
     */
    public CustomDialog(Context context, String message, String btnLeft,
                        String btnRight) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.message = message;
        this.leftText = btnLeft;
        this.rightText = btnRight;
    }

    public CustomDialog(Context context, String message, String btnLeft) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.message = message;
        this.leftText = btnLeft;
        this.rightText = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.custom_dialog);
        this.setCanceledOnTouchOutside(false);
        initLayout();
    }

    private void initLayout() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_dialog, null, false);
        tvMessage = (TextView) view.findViewById(R.id.message);
        btnLeft = (Button) view.findViewById(R.id.btn_left);
        btnLeft.setOnClickListener(this);

        btnRight = (Button) view.findViewById(R.id.btn_right);
        btnRight.setOnClickListener(this);
        line = view.findViewById(R.id.line);
        if (message != null) {
            setMessage(message);
        }
        if (leftText != null) {
            setLeftButtonText(leftText);
        }
        if (rightText != null) {
            setRightButtonText(rightText);
        } else {
            btnRight.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }

        setContentView(view);
    }

    /**
     * 设置左边按钮文字
     *
     * @param text
     */
    public void setLeftButtonText(String text) {
        btnLeft.setText(text);
    }

    /**
     * 设置右边按钮文字
     *
     * @param text
     */
    public void setRightButtonText(String text) {
        btnRight.setText(text);
    }

    /**
     * 设置消息文本
     *
     * @param text
     */
    public void setMessage(String text) {
        tvMessage.setText(text);
    }

    public void setOnButtonClickListener(OnButonClickListener listener) {
        this.onButtonClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                onButtonClickListener.onLeftButtonClick();
                break;
            case R.id.btn_right:
                onButtonClickListener.onRightButtonClick();
                break;
        }

    }
}
