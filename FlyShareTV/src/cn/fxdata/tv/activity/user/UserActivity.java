package cn.fxdata.tv.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.fxdata.tv.R;
import cn.fxdata.tv.base.BaseTitleBarActivity;

public class UserActivity extends BaseTitleBarActivity implements
        OnClickListener {
    private TextView t, tvTitle;
    private ImageView topright, back;
    private FrameLayout f;
    private RelativeLayout lxhc, setting;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user);
        initview();
    }

    private void initview() {
        back = (ImageView) findViewById(R.id.iv_left);
        back.setOnClickListener(this);
        // 标题名称
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("用户中心");
        // 右上角 隐藏
        topright = (ImageView) findViewById(R.id.iv_right2);
        topright.setVisibility(topright.GONE);
        // 下面FrameLayout 背景色 改成与 大LinearLayout一致
        f = (FrameLayout) findViewById(R.id.content);
        f.setBackgroundColor(0xe4e4e4);
        // lxhc = (RelativeLayout) findViewById(R.id.lxhc);
        t = (TextView) findViewById(R.id.userName);
        setting = (RelativeLayout) findViewById(R.id.setting);
        setting.setOnClickListener(this);
        t.setOnClickListener(this);

        /*
         * lxhc.setOnTouchListener(new OnTouchListener() {
         * 
         * @Override public boolean onTouch(View v, MotionEvent event) { if
         * (event.getAction() == MotionEvent.ACTION_DOWN) { // 更改为按下时的背景色
         * v.setBackgroundColor(0xe4e4e4); } else if (event.getAction() ==
         * MotionEvent.ACTION_UP) { // 改为抬起时的背景色 v.setBackgroundColor(0xffffff);
         * } return true; }
         * 
         * });
         */
    }

    // 重写后退键，使其跳到指定页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 如果按下返回键
            // startActivity(new
            // Intent(UserActivity.this,MainFragmentActivity.class));
            onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.iv_left:
            onBackPressed();
            break;
        case R.id.userName:
            startActivity(new Intent(UserActivity.this, LoginActivity.class));
            break;
        case R.id.setting:
            startActivity(new Intent(UserActivity.this,
                    UserSettingActivity.class));
            break;
        default:
            break;
        }
    }
}
