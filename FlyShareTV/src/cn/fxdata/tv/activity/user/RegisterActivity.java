package cn.fxdata.tv.activity.user;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import cn.fxdata.tv.bean.LoginInfoReturn.LoginInfo;
import com.android.volley.Listener;
import com.android.volley.VolleyError;
import com.zgntech.core.listener.VolleyListener;
import com.zgntech.core.listener.VolleyListener.VolleyJsonCallBack;

public class RegisterActivity<LoginInfo, LoginUserInfo, loginInforeturn>
        extends BaseTitleBarActivity implements OnClickListener, TextWatcher {
    private static final String ACTIVITY_TAG = null;
    private Button btn;
    private ImageView left;

    private EditText username, phone, password, passwordconfirm, verify_code;
    private TextView getCode, toastText;
    private Context mContext;
    private Context context;
    private String mobileNumber;
    private int num = 60;
    private Handler handler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        initview();
    }

    private void initview() {
        btn = (Button) findViewById(R.id.btn_submit);
        btn.setOnClickListener(this);
        left = (ImageView) findViewById(R.id.iv_left);
        left.setOnClickListener(this);
        username = (EditText) findViewById(R.id.username);
        username.setOnClickListener(this);
        phone = (EditText) findViewById(R.id.phone);
        phone.setOnClickListener(this);
        password = (EditText) findViewById(R.id.password);
        password.setOnClickListener(this);
        passwordconfirm = (EditText) findViewById(R.id.passwordconfirm);
        passwordconfirm.setOnClickListener(this);
        verify_code = (EditText) findViewById(R.id.verify_code);
        verify_code.setOnClickListener(this);
        getCode = (TextView) findViewById(R.id.sendverify_code);
        getCode.setOnClickListener(this);
        toastText = (TextView) findViewById(R.id.tv_toast);
        Page();

    }

    /**
     * 清除系统账户信息
     */
    private void clearConfig() {
        SharedPreferencePersonUtil sp = new SharedPreferencePersonUtil(context);
        sp.clearUserInfo();
        // RuyDbUtils.clearDB(context);
    }

    private void setMsgcodeView() {
        getCode.setText(num + "s  重新发送");
        getCode.setEnabled(false);
        num = 60;
        runnable.run();
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (num != 0) {
                getCode.setText(num-- + "s  重新发送");
                handler.postDelayed(runnable, 1 * 1000);
            } else {
                getCode.setEnabled(true);
                getCode.setText("重新发送");
            }
        }
    };
    protected Throwable error;

    /**
     * 用手机号码获取短信验证码
     */
    private void getMsgCodeWithPhone() {

        mobileNumber = phone.getText().toString();

        if (TextUtils.isEmpty(mobileNumber)) {
            setToast("请输入您的手机号码");

            Log.i(RegisterActivity.ACTIVITY_TAG, "请输入您的手机号码 ");
        } else if (!StringUtils.isPhone(mobileNumber)) {
            setToast("请您输入正确的手机号码");
            mobileNumber = null;
            Log.i(RegisterActivity.ACTIVITY_TAG, "请您输入正确的手机号码");
        } else {
            sendMobileForCode(mobileNumber);

        }
    }

    private void setToast(String text) {
        toastText.setVisibility(View.VISIBLE);
        toastText.setText(text);
    }

    /**
     * 请求获取短信验证码
     * 
     * @param mobileNumber
     */
    private void sendMobileForCode(String mobileNumber) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("mobile", mobileNumber);
        param.put("type", "register");
        param.put("ac", "sendCode");
        showDialog("正在获取验证码");

        sendVolleyRequest(param, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                dismissDialog();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        jsonObject = jsonObject.getJSONObject("data");
                        // messageCode = jsonObject.getInt("code") + "";
                        setMsgcodeView();
                    } else {
                        String error_msg = jsonObject.getString("error_msg");
                        // mobileNumber = null;
                        if (!TextUtils.isEmpty(error_msg)) {
                            setToast(error_msg);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            /*
             * @Override public void onSuccess(String response) { ActionReturn
             * actionReturn = (entity.ActionReturn) response; try { if
             * (actionReturn.getData().getResult().equals("1")) {
             * Log.i(RegisterActivity.ACTIVITY_TAG,
             * "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ "); showDialog("验证码已发送！");
             * finish(); } } catch (Exception e) {
             * Log.i(RegisterActivity.ACTIVITY_TAG,
             * "******************************** "); showDialog("验证码发送失败！");
             * 
             * } }
             */

            @Override
            public void onError(VolleyError error) {
                dismissDialog();
                showHttpError(error.getMessage());

            }
        });

    }

    /*
     * 页面格式判断
     */
    private void Page() {
        String Username = username.getText().toString();
        String Password = password.getText().toString();
        String PasswordConfirm = passwordconfirm.getText().toString();
        String Verify_code = verify_code.getText().toString();
        if (StringUtils.isNullOrEmpty(Username)) {
            setToast("请输入用户名");
            String s1 = "请输入用户名";
            Toast t1 = Toast.makeText(getApplicationContext(), s1, 1);
            t1.show();
            return;
        }
        if (StringUtils.isNullOrEmpty(Password)) {
            setToast("请输入密码");
            String s2 = "请输入密码";
            Toast t2 = Toast.makeText(getApplicationContext(), s2, 1);
            t2.show();
            return;
        }
        if (Password.length() < 6) {
            setToast("密码过于简单，请重新修改");
            String s3 = "密码过于简单，请重新修改";
            Toast t3 = Toast.makeText(getApplicationContext(), s3, 1);
            t3.show();
            return;
        }
        if (StringUtils.isNullOrEmpty(PasswordConfirm)) {
            setToast("请输入确认密码");
            String s4 = "请输入确认密码";
            Toast t4 = Toast.makeText(getApplicationContext(), s4, 1);
            t4.show();
            return;
        }
        if (!StringUtils.equalsIgnoreCase(Password, PasswordConfirm)) {
            setToast("两次密码不一致，请重新修改");
            String s5 = "两次密码不一致，请重新修改";
            Toast t5 = Toast.makeText(getApplicationContext(), s5, 1);
            t5.show();
            return;
        }
        if (StringUtils.isNullOrEmpty(Verify_code)) {
            setToast("请输入验证码");
            String s6 = "请输入验证码";
            Toast t6 = Toast.makeText(getApplicationContext(), s6, 1);
            t6.show();
            return;
        }
        if (Verify_code.length() != 6) {
            setToast("验证码不符合规则，请重新输入");
            String s7 = "验证码不符合规则，请重新输入";
            Toast t7 = Toast.makeText(getApplicationContext(), s7, 1);
            t7.show();
            return;
        }
        registerWithMobile(Username, Password, PasswordConfirm, mobileNumber,
                Verify_code);
    }

    /**
     * 注册
     * 
     * @param <LoginInfoReturn>
     */
    private void registerWithMobile(String Username, String Password,
            String PasswordConfirm, String mobileNumber, String Verify_code) {
        // TODO Auto-generated method stub
        showDialog("注册");
        Map<String, String> param = new HashMap<String, String>();
        param.put("ac", "register");
        param.put("username", Username);
        param.put("mobile", mobileNumber);
        param.put("password", Password);
        param.put("repassword", PasswordConfirm);
        param.put("verify_code", Verify_code);
        Log.i(RegisterActivity.ACTIVITY_TAG,
                "2222222222222222222222222222222222222 ");
        showDialog("验证码已发送！");
        sendVolleyRequest(param, new VolleyListener(LoginInfoReturn.class,
                new VolleyJsonCallBack() {

                    @Override
                    public void onResonpse(Object object) {
                        // TODO Auto-generated method stub
                        destroyProgressDialog();
                        LoginInfoReturn loginInfoReturn = (LoginInfoReturn) object;
                        RegisterSuccess(loginInfoReturn.getData());
                        loginInfoReturn = null;
                        Log.i(RegisterActivity.ACTIVITY_TAG,
                                "注册成功！直接跳入已登录页面！注册成功！ ");
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        showHttpError(error.getMessage());
                        Log.i(RegisterActivity.ACTIVITY_TAG,
                                "4444444444444444444444444444444 ");
                    }

                }));
    }

    /**
     * 注册成功
     * 
     * @param info
     * @param jsonArray
     */
    private void RegisterSuccess(LoginInfoReturn.LoginInfo info) {
        username.setText(info.getUserInfo().getUsername());
        SharedPreferencePersonUtil sp = new SharedPreferencePersonUtil(mContext);
        sp.setLoginInfo(info);
        /*
         * 测试log Log.i(RegisterActivity.ACTIVITY_TAG,
         * "5555555555555555555555555555555 ");
         */
        // showDialog("注册成功！");
        // dismissDialog();
        Intent intent = new Intent();
        intent.setClass(RegisterActivity.this, UserLoginedActivity.class);
        intent.putExtra("username", username.getText().toString());
        intent.putExtra("phone", phone.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.iv_left:
            finish();
            break;
        case R.id.sendverify_code:
            // SystemTool.closeKeybord(getCode, mContext);

            if (StringUtils.isNullOrEmpty(mobileNumber)) {
                getMsgCodeWithPhone();
            } else {
                sendMobileForCode(mobileNumber);
            }
            break;

        case R.id.btn_submit:
            Page();
            // startActivity(new Intent(RegisterActivity.this,
            // UserActivity.class));
            break;
        default:
            break;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }
}