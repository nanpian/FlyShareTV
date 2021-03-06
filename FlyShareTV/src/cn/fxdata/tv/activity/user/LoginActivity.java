package cn.fxdata.tv.activity.user;

import java.util.HashMap;
import java.util.Map;

import com.zgntech.core.listener.VolleyListener;
import com.zgntech.core.listener.VolleyListener.VolleyJsonCallBack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.fxdata.tv.R;
import cn.fxdata.tv.base.BaseTitleBarActivity;
import cn.fxdata.tv.bean.LoginInfoReturn;
import cn.fxdata.tv.utils.Log;
import cn.fxdata.tv.utils.SharedPreferencePersonUtil;
import cn.fxdata.tv.utils.StringUtils;

public class LoginActivity extends BaseTitleBarActivity implements
        OnClickListener {
    private TextView t1, tvTitle;
    private ImageView back;
    private Button login, topright;
    private Context mContext;
    private EditText username, password;
    String phone;
    private static final String ACTIVITY_TAG = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        initview();
    }

    private void initview() {
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("登录");
        topright = (Button) findViewById(R.id.btn_right);
        topright.setVisibility(topright.VISIBLE);
        topright.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.iv_left);
        back.setOnClickListener(this);
        t1 = (TextView) findViewById(R.id.forgetpw);
        t1.setOnClickListener(this);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        username = (EditText) findViewById(R.id.username);
        username.setOnClickListener(this);
        password = (EditText) findViewById(R.id.password);
        password.setOnClickListener(this);

    }

    /*
     * 页面格式判断
     */
    private void Page() {
        String Username = username.getText().toString();
        String Password = password.getText().toString();

        if (StringUtils.isNullOrEmpty(Username)) {
            final String s1 = ("请输入手机号或用户名");
            Toast t1 = Toast.makeText(getApplicationContext(), s1, 1);
            t1.show();
            return;
        }
        if (StringUtils.isNullOrEmpty(Password)) {
            final String s2 = ("请输入密码");
            Toast t2 = Toast.makeText(getApplicationContext(), s2, 1);
            t2.show();
            return;
        }

        LoginWithMobile(Username, Password);
    }

    private void setToast(String string) {
        // TODO Auto-generated method stub

    }

    /**
     * 登录
     * 
     * @param <LoginInfoReturn>
     */
    private void LoginWithMobile(String Username, String Password) {
        // TODO Auto-generated method stub
        // showDialog("登录");
        Map<String, String> param = new HashMap<String, String>();
        param.put("ac", "login");
        param.put("username", Username);
        param.put("password", Password);
        Log.i(LoginActivity.ACTIVITY_TAG, "正在准备调用接口中！");
        sendVolleyRequest(param, new VolleyListener(LoginInfoReturn.class,
                new VolleyJsonCallBack() {

                    @Override
                    public void onResonpse(Object object) {
                        // TODO Auto-generated method stub
                        LoginInfoReturn loginInfoReturn = (LoginInfoReturn) object;
                        LoginSuccess(loginInfoReturn.getData());
                        loginInfoReturn = null;
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        // TODO Auto-generated method stub
                        Log.i(LoginActivity.ACTIVITY_TAG, "调用接口成功，接口返回错误信息");
                        finish();
                    }
                }));
    }

    /**
     * 登录成功
     * 
     * @param info
     * @param jsonArray
     */
    private void LoginSuccess(LoginInfoReturn.LoginInfo info) {
        username.setText(info.getUserInfo().getUsername());
        phone = info.getUserInfo().getMobile();
        SharedPreferencePersonUtil sp = new SharedPreferencePersonUtil(mContext);
        sp.setLoginInfo(info);
        /*
         * 测试log Log.i(RegisterActivity.ACTIVITY_TAG,
         * "5555555555555555555555555555555 ");
         */
        // showDialog("注册成功！");
        // dismissDialog();
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, UserLoginedActivity.class);
        intent.putExtra("username", username.getText().toString());
        intent.putExtra("phone", phone);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.iv_left:
            finish();
            break;
        case R.id.btn_right:
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            break;
        case R.id.forgetpw:
            startActivity(new Intent(LoginActivity.this, SearchPwActivity.class));
            break;
        case R.id.login:
            Page();
            // startActivity(new
            // Intent(LoginActivity.this,UserInformationActivity.class));
            break;
        default:
            break;
        }
    }

}