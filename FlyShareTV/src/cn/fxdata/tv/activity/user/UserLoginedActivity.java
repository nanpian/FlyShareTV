package cn.fxdata.tv.activity.user;

import android.content.Context;
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
import cn.fxdata.tv.activity.main.MainFragmentActivity;
import cn.fxdata.tv.application.FxApplication;
import cn.fxdata.tv.base.BaseTitleBarActivity;
import cn.fxdata.tv.utils.LoginUserInfo;
import cn.fxdata.tv.utils.SharedPreferencePersonUtil;

public class UserLoginedActivity extends BaseTitleBarActivity implements
        OnClickListener {
    private ImageView left, right2, avatar;
    private TextView setting, username, tvTitle, phone;
    private FrameLayout f;
    private RelativeLayout myInformation;
    private LoginUserInfo loginInfo;
    private SharedPreferencePersonUtil sp;
    private Context context;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_userlogined);
        setContentView(R.layout.act_user);
        context = getApplicationContext();
        initview();
        getPersonSetting();
    }

    private void initview() {
        left = (ImageView) findViewById(R.id.iv_left);
        left.setOnClickListener(this);
        setting = (TextView) findViewById(R.id.user_setting);
        setting.setOnClickListener(this);
        myInformation = (RelativeLayout) findViewById(R.id.user_information);
        myInformation.setOnClickListener(this);
        right2 = (ImageView) findViewById(R.id.iv_right2);
        right2.setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("用户中心");
        f = (FrameLayout) findViewById(R.id.content);
        f.setBackgroundColor(0xe4e4e4);
        // RegisterActivity 页面传的 用户名

        Intent intent = getIntent();
        username = (TextView) findViewById(R.id.userName);
        // username.setText(intent.getStringExtra("username"));
        phone = (TextView) findViewById(R.id.userId);
        // phone.setText("帐号："+intent.getStringExtra("phone"));

    }

    /*********************************************************************************/

    private void getPersonSetting() {
        sp = new SharedPreferencePersonUtil(context);
        loginInfo = ((FxApplication) FxApplication.getInstance())
                .getLoginInfo();
        username.setText(loginInfo.getUsername());
        phone.setText("帐号：" + loginInfo.getMobile());
        // avatar.setTag(loginInfo.getAvatar());
    }

    // 重写后退键，使其跳到指定页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 如果按下返回键
            /*
             * startActivity(new Intent(UserLoginedActivity.this,
             * MainFragmentActivity.class));
             */
            Intent intent2 = new Intent();
            intent2.setClass(UserLoginedActivity.this,
                    MainFragmentActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
            onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    /*************************************************************************************/
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.iv_left:
            Intent intent2 = new Intent();
            intent2.setClass(UserLoginedActivity.this,
                    MainFragmentActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
            startActivity(intent2);
            // finish();
            break;
        case R.id.user_information:
            Intent intent = new Intent();
            intent.setClass(UserLoginedActivity.this,
                    UserInformationActivity.class);
            intent.putExtra("username", username.getText().toString());
            // 没必要传，下一页面可直接获取到 （同上也是） intent.putExtra("phone",
            // phone.getText().toString());
            startActivity(intent);
            
            break;
        case R.id.user_setting:
            startActivity(new Intent(UserLoginedActivity.this,
                    UserSettingActivity.class));
            break;
        default:
            break;
        }
    }
}
