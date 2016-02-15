package cn.fxdata.tv.activity.videorecords;

import cn.fxdata.tv.R;
import cn.fxdata.tv.activity.video.ForplayVideoViewActivity;
import cn.fxdata.tv.adapter.PhotoAdapter;
import cn.fxdata.tv.adapter.RecordsAdapter;
import cn.fxdata.tv.base.BaseActivity;
import cn.fxdata.tv.bean.VideoItem;
import cn.fxdata.tv.constants.Constants;
import cn.fxdata.tv.view.CustomDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

/**
 * 播放记录页面
 */
public class MoviePlayRecordsActivity extends BaseActivity implements
        OnClickListener {

    private final static String TAG = "MoviePlayRecordsActivity";
    public final static int SELEC_ALL_RECORD = 0;
    private ImageView ivLeft;
    private TextView tvTitle;
    private ImageView ivRight;
    // 底部的控制栏,平时不显示
    private LinearLayout controlBar;
    private ListView mListView;
    private RecordsAdapter mRecordsAdapter;
    private Button mSelect;
    private Button mRemove;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d(TAG, "getmessage 11");
            if (msg.what == SELEC_ALL_RECORD) {
                if (mRecordsAdapter.isAllRecordSelected()) {
                    mSelect.setText("取消全选");
                    //mSelect.setBackgroundColor(R.color.grey);
                } else {
                    mSelect.setText("全选");
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_record);
        initView();
        initAdapter();
    }

    private void initView() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("播放记录");
        ivRight = (ImageView) findViewById(R.id.iv_right2);
        ivRight.setImageResource(R.drawable.remove);
        ivRight.setOnClickListener(this);
        // 控制全选删除的显示，刚开始不显示，点击删除后显示
        controlBar = (LinearLayout) findViewById(R.id.act_records_control);
        controlBar.setVisibility(View.GONE);

        mSelect = (Button) findViewById(R.id.records_select);
        mSelect.setOnClickListener(this);
        mRemove = (Button) findViewById(R.id.records_remove);
        mRemove.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.record_lists);
        mListView.setOnItemClickListener(mListListener);
    }

    private void initAdapter() {
        mRecordsAdapter = new RecordsAdapter(MoviePlayRecordsActivity.this, mHandler);
        mListView.setAdapter(mRecordsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right2:
                // 点击删除的时候，显示底部控制栏和复选框
                controlBar.setVisibility(View.VISIBLE);
                mRecordsAdapter.setDeleteMode(true);
                break;
            case R.id.records_select:
                updateSelectAllRecord();
                // mSelect.setBackgroundColor(R.color.grey);
                break;
            case R.id.records_remove:
                ArrayList<VideoItem> deleteList = mRecordsAdapter.getmDeleteList();
                if (null == deleteList || deleteList.size() == 0) {
                    final CustomDialog dialog1 = new CustomDialog(MoviePlayRecordsActivity.this, "请选择要删除的记录", "确定");
                    dialog1.setOnButtonClickListener(new CustomDialog.OnButonClickListener() {
                        @Override
                        public void onLeftButtonClick() {
                            dialog1.dismiss();
                        }

                        @Override
                        public void onRightButtonClick() {
                            dialog1.dismiss();
                        }
                    });
                    dialog1.show();

                } else {
                    final CustomDialog dialog = new CustomDialog(MoviePlayRecordsActivity.this, "确定要删除播放记录吗?", "取消", "确定");
                    dialog.setOnButtonClickListener(new CustomDialog.OnButonClickListener() {
                        @Override
                        public void onLeftButtonClick() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onRightButtonClick() {
                            dialog.dismiss();
                            mRecordsAdapter.deleteSelectRecord();
                            controlBar.setVisibility(View.GONE);
                            mRecordsAdapter.setDeleteMode(false);
                        }
                    });
                    dialog.show();
                }
                break;

        }

    }

    private void updateSelectAllRecord() {
        if (!mRecordsAdapter.isAllRecordSelected()) {
            mRecordsAdapter.selectAllRecord();
            mSelect.setText("取消全选");
            //mSelect.setBackgroundColor(R.color.grey);
        } else {
            Log.d(TAG, "delete all select");
            mRecordsAdapter.dropSelectAll();
            mSelect.setText("全选");
        }
    }

    /* 进入播放主界面 */
    private OnItemClickListener mListListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            final Context context = view.getContext();
            final VideoItem video = (VideoItem) parent
                    .getItemAtPosition(position);
            Bundle mBundle = new Bundle();
            mBundle.putString(Constants.MovieExtra.EXTRA_MOVIE_PATH,
                    video.getVideoUri());
            mBundle.putInt(Constants.MovieExtra.EXTRA_MOVIE_POSTION, video.getVideoPostion());
            showActivity(ForplayVideoViewActivity.class, mBundle);
        }
    };

}
