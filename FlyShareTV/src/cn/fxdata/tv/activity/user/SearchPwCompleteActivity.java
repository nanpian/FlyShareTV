package cn.fxdata.tv.activity.user;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.fxdata.tv.R;
import cn.fxdata.tv.base.BaseTitleBarActivity;
import cn.fxdata.tv.base.BaseUserActivity.StringFormatUtil;

public class SearchPwCompleteActivity extends BaseTitleBarActivity implements
        OnClickListener {
    private ImageView back, right2;
    private Button next, topright;
    private TextView tv_show, tv_title;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchpw_complete);
        initview();
    }

    // 重写后退键，使其跳到指定页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 如果按下返回键
            Intent intent2 = new Intent();
            intent2.setClass(SearchPwCompleteActivity.this, LoginActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
            onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void initview() {
        tv_title = (TextView) findViewById(R.id.title);
        tv_title.setText("找回密码");
        right2 = (ImageView) findViewById(R.id.iv_right2);
        right2.setVisibility(right2.GONE);
        back = (ImageView) findViewById(R.id.iv_left);
        back.setOnClickListener(this);
        next = (Button) findViewById(R.id.complete);
        next.setOnClickListener(this);
        tv_show = (TextView) findViewById(R.id.show3);
        // 根据状态改颜色（手动）
        tv_show.setGravity(Gravity.CENTER);
        String wholeStr = "确认账号······修改密码······完成";
        StringFormatUtil spanStr = new StringFormatUtil(this, wholeStr,
                "确认账号······修改密码······完成", R.color.text_red_color).fillColor();
        tv_show.setText(spanStr.getResult());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.iv_left:
            Intent intent2 = new Intent();
            intent2.setClass(SearchPwCompleteActivity.this, LoginActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
            onBackPressed();
            break;
        case R.id.complete:
            Intent intent = new Intent();
            /*
             * startActivity(new Intent(SearchPwCompleteActivity.this,
             * UserActivity.class));
             */
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(SearchPwCompleteActivity.this, UserActivity.class);
            startActivity(intent);
            break;
        default:
            break;
        }
    }
}
