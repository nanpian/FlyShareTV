package cn.fxdata.tv.activity.user;

import com.zgntech.core.ActivityStack;

import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.fxdata.tv.R;
import cn.fxdata.tv.application.FxApplication;
import cn.fxdata.tv.base.BaseTitleBarActivity;
import cn.fxdata.tv.utils.SharedPreferencePersonUtil;
import cn.fxdata.tv.utils.LoginUserInfo;

public class UserInformationActivity extends BaseTitleBarActivity implements
        OnClickListener {
    private Button exit;
    private ImageView left, right2;
    private TextView tv_title, username, modfiyusername, phone;
    private LoginUserInfo loginInfo;
    private RelativeLayout modfiypw;
    private SharedPreferencePersonUtil sp;
    private Context context;
    SharedPreferences sharedPreferences;
    private String mName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinformation);
        context = getApplicationContext();
        initview();
        getPersonSetting();
    }

    private void initview() {
        tv_title = (TextView) findViewById(R.id.title);
        tv_title.setText("个人信息");
        right2 = (ImageView) findViewById(R.id.iv_right2);
        right2.setVisibility(right2.GONE);
        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(this);
        left = (ImageView) findViewById(R.id.iv_left);
        left.setOnClickListener(this);
        modfiyusername = (TextView) findViewById(R.id.modfiyuserName);
        modfiyusername.setOnClickListener(this);
        // UserLoginedActivity页面传 用户名 到 UserInformationActivity
        Intent intent = getIntent();
        username = (TextView) findViewById(R.id.userName);
        username.setText(intent.getStringExtra("username"));
        phone = (TextView) findViewById(R.id.phone);
        // phone.setText(intent.getStringExtra("phone"));
        sharedPreferences = getSharedPreferences("UserInformationActivity",
                Context.MODE_PRIVATE);
        mName = sharedPreferences.getString("Username", "Name");
        username.setText(mName);

        modfiypw = (RelativeLayout) findViewById(R.id.modfiypw);
        modfiypw.setOnClickListener(this);
    }

    private void ShowEditDialog() {
        // TODO Auto-generated method stub
        final EditText edt = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入用户姓名").setView(edt)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Editor editor = sharedPreferences.edit();
                        editor.putString("Username", edt.getText().toString());
                        editor.commit();
                        username.setText(edt.getText().toString());
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void getPersonSetting() {
        sp = new SharedPreferencePersonUtil(context);
        loginInfo = ((FxApplication) FxApplication.getInstance())
                .getLoginInfo();

        username.setText(loginInfo.getUsername());
        phone.setText(loginInfo.getMobile());
        // avatar.setTag(loginInfo.getAvatar());
    }

    /**
     * 清除系统账户信息
     */
    private void clearConfig() {
        FxApplication.getInstance().info = null;
        SharedPreferencePersonUtil sp = new SharedPreferencePersonUtil(context);
        sp.clearUserInfo();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.iv_left:
            finish();
            break;
        case R.id.modfiypw:
            startActivity(new Intent(UserInformationActivity.this,
                    UserInformationModfiyActivity.class));
            break;
        case R.id.exit:
            clearConfig();
            Intent intent = new Intent();
            intent.setClass(UserInformationActivity.this, UserActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
            startActivity(intent);
            finish();
            ActivityStack.create().finishActivity(UserLoginedActivity.class);
            
            break;
        case R.id.modfiyuserName:
            ShowEditDialog();
            break;
        default:
            break;
        }
    }
}
