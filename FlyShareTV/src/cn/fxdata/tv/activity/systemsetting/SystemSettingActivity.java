package cn.fxdata.tv.activity.systemsetting;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cn.fxdata.tv.activity.user.OwnerMsgEditActivity;
import cn.fxdata.tv.base.BaseActivity;
import cn.fxdata.tv.utils.PortraitManger;
import cn.fxdata.tv.version.UpdateManager;
import cn.fxdata.tv.view.RoundedImageView;
import cn.fxdata.tv.web.WebActivity;
import cn.fxdata.tv.R;

/**
 * Created by Jianyong on 15/6/13.
 */
public class SystemSettingActivity extends BaseActivity implements
        OnClickListener {

    private ImageView ivLeft;
    private TextView tvTitle;
    private ImageView ivRight;
    private RoundedImageView ivAvatar;
    private TextView tvNickName;
    private TextView tvEdit;
    private ImageView ivAvatarSetting;
    private Switch wifiSwitch;
    SharedPreferences sharedPreferences;
    private boolean isWifiOnly;
    private String TvNickname;
    private TextView mVersion;
    private PortraitManger pmgr;
    private TextView aboutFx;
    private TextView userRequst;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        initView();
    }

    private void initView() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("设置");
        ivRight = (ImageView) findViewById(R.id.iv_right2);
        ivRight.setVisibility(View.GONE);

        ivAvatar = (RoundedImageView) findViewById(R.id.avatar);
        tvNickName = (TextView) findViewById(R.id.tv_nick_name);
        sharedPreferences = getSharedPreferences("OwnerMsgEditActivity",
                Context.MODE_PRIVATE);
        TvNickname = sharedPreferences.getString("Username", "昵称");
        tvNickName.setText(TvNickname);
        tvEdit = (TextView) findViewById(R.id.tv_edit);
        ivAvatarSetting = (ImageView) findViewById(R.id.btn_avatar_setting);
        ivAvatarSetting.setOnClickListener(this);
        wifiSwitch = (Switch) findViewById(R.id.switch1);
        sharedPreferences = getSharedPreferences("SystemSetting",
                Context.MODE_PRIVATE);
        isWifiOnly = sharedPreferences.getBoolean("wifionly", true);
        wifiSwitch.setChecked(isWifiOnly);
        wifiSwitch.setOnCheckedChangeListener(mChangelistener);
        mVersion = (TextView) findViewById(R.id.version_update);
        mVersion.setOnClickListener(this);
        
        aboutFx = (TextView)findViewById(R.id.about_fx);
        aboutFx.setOnClickListener(this);
        userRequst = (TextView)findViewById(R.id.user_request);
        userRequst.setOnClickListener(this);
        
        pmgr = new PortraitManger(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        String path = pmgr.getProtraitPhoto();
        if (!path.isEmpty()) {
            File imagefile = new File(path);
            if (imagefile.exists()) {
                ivAvatar.setImageURI(Uri.parse(path));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.iv_left:
            finish();
            break;
        case R.id.btn_avatar_setting:
            startActivity(new Intent(SystemSettingActivity.this,
                    OwnerMsgEditActivity.class));
            break;
        case R.id.version_update:
            new Handler().post(new Runnable() {
                public void run() {
                    UpdateManager um = new UpdateManager(
                            SystemSettingActivity.this);
                    um.checkRemoteVersion();
                }
            });
            break;
        case R.id.user_request:
            Intent intent2 = new Intent(this,WebActivity.class);
            intent2.putExtra("Url", "http://www.baidu.com");
            intent2.putExtra("Title", "用户反馈");
            startActivity(intent2);
            break;
        case R.id.about_fx:
            Intent intent1 = new Intent(this,WebActivity.class);
            intent1.putExtra("Url", "http://www.baidu.com");
            intent1.putExtra("Title", "关于飞享");
            startActivity(intent1);
            break;
        default:
            break;
        }
    }

    private void reflashWifistate() {
        // TODO Auto-generated method stub
        isWifiOnly = !isWifiOnly;
        wifiSwitch.setChecked(isWifiOnly);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("wifionly", isWifiOnly);
        editor.commit();
    }

    OnCheckedChangeListener mChangelistener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            // TODO Auto-generated method stub
            reflashWifistate();
        }

    };
}