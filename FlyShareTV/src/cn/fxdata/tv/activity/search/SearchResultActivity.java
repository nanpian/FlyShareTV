
package cn.fxdata.tv.activity.search;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Listener;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyTools;
import com.android.volley.request.StringRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.fxdata.tv.R;
import cn.fxdata.tv.base.BaseActivity;
import cn.fxdata.tv.bean.HotSearchResult;
import cn.fxdata.tv.bean.HotSearchResult.DataEntity.HotMovieEntity;
import cn.fxdata.tv.constants.Constants;
import cn.fxdata.tv.db.SQLiteHelper;
import cn.fxdata.tv.db.SearchUtils;
import cn.fxdata.tv.utils.Log;
import cn.fxdata.tv.utils.MJsonUtil;
import cn.fxdata.tv.view.CustomDialog;
import cn.fxdata.tv.view.SearchEditText;
import cn.fxdata.tv.view.XGridView;

public class SearchResultActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "SearchResultActivity";
    private ImageView backImageView;
    private SearchEditText searchEditText;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private ImageView mImgSearchView;
    private ArrayList<String> mSearchHistory;
    private static final int REFRESH_SEARCH_DATA = 1;
    public static final String SEARCH_KEY = "search_key";
    public static final String SEARCH_SCHOOL_ID = "search_school_id";
    private String mSearchKey;
    private int mSearchSchoolId = 2;
    //搜索显示
    private RelativeLayout mSearchHotContent;
    private GridView mHistoryGridView = null;
    private ArrayAdapter mSearchHisAdapter;
    private LinearLayout mSearchHistoryLayout;
    //热播
    private GridView mHotSearchGridView = null;
    private HotSearchAdapter mHotSeachAdapter;
    private TextView mTvSearchDel;

    //搜素结果显示
    private RelativeLayout mSrLayout;
    private ImageView mImgSr; //电视图片
    private TextView mTvSrTitle; //名称
    private TextView mTvSrActor; //演员
    private TextView mTvSrCatogery; //类型
    private TextView mTvSrDate; // 时间
    private TextView mTvSrCountry; // 时间
    private TextView mTvSrPlayTime; //播放次数
    private TextView mImgStartPlay;
    private RelativeLayout mSrColseLayout;
    private ImageView mImgClose;

    private XGridView mVideGrid;
    private XGridView mVideList;
    private VideoListAdapter mVideoListAdapter;
    private VideoGridAdapter mVideoGridAdapter;
    private ArrayList<VideoGridItems> mVideoGridsList = new ArrayList<VideoGridItems>();
    private ArrayList<String> mVideoData = new ArrayList<String>();

    private LinearLayout mShowMoreLayout;
    private TextView mTvShowMore;

    private static final int SHOW_SEARCH_HIS = 1;
    private static final int SHOW_SEARCH_RESULT = 2;
    private static final int SHOW_SEARCH_MORE = 3;
    private static final int VIDEO_TOTIL_NUMBER = 75;
    private static final int VIDEO_SHOW = 15;
    private static final int VIDEO_NUMBER_PER = 25;

    private android.os.Handler mUpdateHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_SEARCH_DATA:
                    updateSearchList();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search_result);
        initView();
        setup();
        initData();
    }

    public void initView() {
        backImageView = (ImageView) findViewById(R.id.image_back);
        mImgSearchView = (ImageView) findViewById(R.id.image_down_back);
        searchEditText = (SearchEditText) findViewById(R.id.edit_search);
        Bundle bundle = getIntent().getExtras();
        mSearchKey = bundle.getString(SEARCH_KEY);
        mSearchSchoolId = bundle.getInt(SEARCH_SCHOOL_ID);
        Log.i(TAG, "mSearchKey is : " + mSearchKey + "mSearchSchoolId is : " + mSearchSchoolId);
        searchEditText.setText(mSearchKey);
        backImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        //初始化search view
        mSearchHotContent = (RelativeLayout) findViewById(R.id.search_his_layout);
        mImgSearchView.setBackgroundResource(R.drawable.arrow_down_left);
        mImgSearchView.setOnClickListener(this);
        mTvSearchDel = (TextView) findViewById(R.id.search_del_history);
        mTvSearchDel.setOnClickListener(this);
        mSearchHistoryLayout = (LinearLayout) findViewById(R.id.search_history);

        //初始化search result
        mSrLayout = (RelativeLayout) findViewById(R.id.search_rst_layout);
        mImgSr = (ImageView) findViewById(R.id.src_des_image);
        mTvSrActor = (TextView) findViewById(R.id.src_des_actor);
        mTvSrCountry = (TextView) findViewById(R.id.src_des_country);
        mTvSrCatogery = (TextView) findViewById(R.id.src_des_catogery);
        mTvSrDate = (TextView) findViewById(R.id.src_des_year);
        mTvSrTitle = (TextView) findViewById(R.id.src_des_title);
        mTvSrPlayTime = (TextView) findViewById(R.id.src_des_play_time);
        mImgStartPlay = (TextView) findViewById(R.id.src_dec_play);
        mSrColseLayout = (RelativeLayout) findViewById(R.id.src_close_content);
        mImgClose = (ImageView) findViewById(R.id.src_rut_close);
        mImgClose.setOnClickListener(this);
        mVideGrid = (XGridView) findViewById(R.id.src_video_grid);
        mVideList = (XGridView) findViewById(R.id.src_video_list);
        mVideoListAdapter = new VideoListAdapter(this);
        mVideoGridAdapter = new VideoGridAdapter(this);
        mVideGrid.setAdapter(mVideoGridAdapter);
        mVideList.setAdapter(mVideoListAdapter);
        mVideGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mVideoGridAdapter != null) {
                    mVideoGridAdapter.setSelectPos(position);
                    mVideoListAdapter.setDataRefresh(mVideoGridsList.get(position).mVideoLists, true, position * VIDEO_NUMBER_PER);
                }
            }
        });
        mShowMoreLayout = (LinearLayout) findViewById(R.id.src_show_more_content);
        mTvShowMore = (TextView) findViewById(R.id.src_show_more);
        mTvShowMore.setOnClickListener(this);
        switchContent(SHOW_SEARCH_HIS);
    }

    public void switchContent(int type) {
        switch (type) {
            case SHOW_SEARCH_HIS:
                mSearchHotContent.setVisibility(View.VISIBLE);
                mSrLayout.setVisibility(View.GONE);
                break;
            case SHOW_SEARCH_RESULT:
                mSearchHotContent.setVisibility(View.GONE);
                mSrLayout.setVisibility(View.VISIBLE);
                mSrColseLayout.setVisibility(View.GONE);
                mVideGrid.setVisibility(View.GONE);
                mShowMoreLayout.setVisibility(View.VISIBLE);
                break;
            case SHOW_SEARCH_MORE:
                mSearchHotContent.setVisibility(View.GONE);
                mSrLayout.setVisibility(View.VISIBLE);
                mSrColseLayout.setVisibility(View.VISIBLE);
                mVideGrid.setVisibility(View.VISIBLE);
                mShowMoreLayout.setVisibility(View.GONE);
                break;
        }
    }

    public void initData() {
        if (mSearchHistory == null) {
            mSearchHistory = new ArrayList<String>();
        }
        updateSearchList();
        mSearchHisAdapter = new ArrayAdapter<String>(this, R.layout.row_textview, mSearchHistory);
        mHistoryGridView.setAdapter(mSearchHisAdapter);
        mHistoryGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });
        //获得热搜索接口
        getHotSearchMovies();
    }

    public void getHotSearchMovies() {
        Log.i(TAG, "getHotSearchMovies");
        VolleyTools tools = VolleyTools.getInstance(this);
        StringRequest hotMovieRequest = new StringRequest(Request.Method.GET, Constants.ServerConfig.HOT_SEARCH_URL,
                new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "hot movie result is : " + response);
                HotSearchResult hotSearchResult = MJsonUtil.gson.fromJson(response, HotSearchResult.class);
                Log.i(TAG, "get result success size is : " + hotSearchResult.getData().getHot_movie().size());
                if (hotSearchResult.getError_code() == 0){
                    if (mHotSeachAdapter != null){
                        mHotSeachAdapter.setHotSearchData(hotSearchResult.getData().getHot_movie());
                        mHotSearchGridView.setAdapter(mHotSeachAdapter);
                    }
                }else {
                    Log.i(TAG, "error from server");
                }
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                Log.i(TAG, "get hot search result error");
            }

            @Override
            public void onCancel() {
                super.onCancel();
                Log.i(TAG, "cancel hot search result error");
            }
        });
        tools.addToRequestQueue(hotMovieRequest, VolleyTools.VolleyTAG);
    }

    private void setupViews() {
        mHistoryGridView = (GridView) this.findViewById(R.id.hot_play_history_gv);
        mHotSearchGridView = (GridView) this.findViewById(R.id.hot_play_hot_search_gv);
        mHotSeachAdapter = new HotSearchAdapter(this);
    }

    private void setup() {
        setupViews();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (mSrLayout.getVisibility() == View.VISIBLE){
                switchContent(SHOW_SEARCH_HIS);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_down_back:
                startSearch();
                break;
            case R.id.search_del_history:
                delSearchHistory();
                break;
            case R.id.src_show_more:
                showMoreVideos();
                break;
            case R.id.src_rut_close:
                hideMoreVideos();
                break;
            default:
                break;
        }
    }

    public void showMoreVideos(){
        switchContent(SHOW_SEARCH_MORE);
        if (mVideoGridAdapter != null && mVideoListAdapter != null && mVideoGridsList != null){
            mVideoListAdapter.setDataRefresh(mVideoGridsList.get(0).mVideoLists, true, 0);
        }
    }

    public void hideMoreVideos(){
        switchContent(SHOW_SEARCH_RESULT);
        if (mVideoGridAdapter != null && mVideoListAdapter != null && mVideoGridsList != null){
            mVideoListAdapter.setDataRefresh(mVideoGridsList.get(0).mVideoLists, false, 0);
            mVideoGridAdapter.setSelectPos(0);
        }
    }

    public void startSearch() {
        String name = searchEditText.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            int number = SearchUtils.getInstance(this).getNumberByName(name);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String curTime = formatter.format(curDate);
            if (number == -1) {
                //没有任何数据
                ContentValues contentValues = new ContentValues();
                contentValues.put(SQLiteHelper.SEARCH_NAME, name);
                contentValues.put(SQLiteHelper.SEARCH_NUMBER, 1);
                //当前时间
                contentValues.put(SQLiteHelper.SEARCH_TIME, curTime);
                SearchUtils.getInstance(this).insertSearchHis(contentValues);
            } else {
                //有数据在里面
                number = number + 1;
                ContentValues contentValues = new ContentValues();
                contentValues.put(SQLiteHelper.SEARCH_NAME, name);
                contentValues.put(SQLiteHelper.SEARCH_NUMBER, number);
                //当前时间
                contentValues.put(SQLiteHelper.SEARCH_TIME, curTime);
                String selection = SQLiteHelper.SEARCH_NAME + " = ?";
                String[] selectionArgs = new String[]{name};
                SearchUtils.getInstance(this).updateSearchHis(contentValues, selection, selectionArgs);
            }

            //更新搜索数据列表
            mUpdateHandler.obtainMessage(REFRESH_SEARCH_DATA).sendToTarget();
            //增加网络搜索,成功后显示
            startSearchFromServer();

        }
    }

    public void startSearchFromServer(){
        //网络搜索
        switchContent(SHOW_SEARCH_RESULT);

        if (mVideoData != null){
            mVideoData.clear();
        }

        if (mVideoGridsList != null){
            mVideoGridsList.clear();
        }

        for (int i = 0; i < 73; i++) {
            mVideoData.add("aa");
        }

        int size = mVideoData.size();
        int number = size / VIDEO_NUMBER_PER;
        int left = size % VIDEO_NUMBER_PER;
        for (int i = 0; i < number; i++) {
            ArrayList<String> list = new ArrayList<String>();
            int startPos = VIDEO_NUMBER_PER * i + 1;
            int videoSize = VIDEO_NUMBER_PER;
            for (int j = startPos - 1; j < VIDEO_NUMBER_PER * (i + 1); j++) {
                list.add(mVideoData.get(j));
            }
            VideoGridItems items = new VideoGridItems(startPos, videoSize, list);
            mVideoGridsList.add(items);
        }

        if (left != 0) {
            int start = number * VIDEO_NUMBER_PER + 1;
            int vSize = left;
            ArrayList<String> list1 = new ArrayList<String>();
            for (int m = number * VIDEO_NUMBER_PER; m < number * VIDEO_NUMBER_PER + vSize; m++) {
                list1.add(mVideoData.get(m));
            }
            VideoGridItems perItems = new VideoGridItems(start, vSize, list1);
            mVideoGridsList.add(perItems);
        }

        if (mVideoGridAdapter != null && mVideoListAdapter != null && mVideoGridsList != null){
            mVideoListAdapter.setDataRefresh(mVideoGridsList.get(0).mVideoLists, false, 0);
            mVideoGridAdapter.setDataRefresh(mVideoGridsList);
        }
    }



    public void delSearchHistory() {
        final CustomDialog dialog = new CustomDialog(getContext(), "童鞋，确定要删除记录吗", "确定", "取消");
        dialog.setOnButtonClickListener(new CustomDialog.OnButonClickListener() {

            @Override
            public void onLeftButtonClick() {
                if (dialog != null && dialog.isShowing()) {
                    //删除数据，隐藏view
                    if (mSearchHistory != null) {
                        mSearchHistory.clear();
                        SearchUtils.getInstance(SearchResultActivity.this).deleteSearchHis(null, null);
                        mSearchHisAdapter.notifyDataSetChanged();
                    }

                    mSearchHistoryLayout.setVisibility(View.GONE);
                    mTvSearchDel.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }

            @Override
            public void onRightButtonClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void updateSearchList() {
        if (mSearchHistoryLayout.getVisibility() == View.GONE) {
            mSearchHistoryLayout.setVisibility(View.VISIBLE);
            mTvSearchDel.setVisibility(View.VISIBLE);
        }
        String orderBy = SQLiteHelper.SEARCH_TIME + " DESC";
        String[] columns = new String[]{SQLiteHelper.SEARCH_NAME, SQLiteHelper.SEARCH_TIME};
        Cursor cursor = SearchUtils.getInstance(this).querySearchHis(columns, null, null, null, null, orderBy, Integer.toString(9));
        Log.i(TAG, "count is : " + cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            if (mSearchHistory != null) {
                mSearchHistory.clear();
//                cursor.moveToFirst();
                //会少一条？
//                while (cursor.moveToNext()){
//                    mSearchHistory.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.SEARCH_NAME)));
//                }
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    mSearchHistory.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.SEARCH_NAME)));
                }
                Log.i(TAG, "mSearchHistory size is : " + mSearchHistory.size());
            }
            if (mSearchHisAdapter != null) {
                mSearchHisAdapter.notifyDataSetChanged();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    public class HotSearchAdapter extends BaseAdapter{

        private List<HotMovieEntity> mDataList;
        private LayoutInflater mInflater;

        public HotSearchAdapter(Context context){
            mInflater = LayoutInflater.from(context);
        }

        public void setHotSearchData(List<HotMovieEntity> dataList){
            this.mDataList = dataList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mDataList != null){
                return mDataList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mDataList != null){
                return mDataList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) mInflater.inflate(R.layout.row_textview, null);
            HotMovieEntity hotSearchMovieEntity = mDataList.get(position);
            textView.setText(hotSearchMovieEntity.getMovie_name());
            return textView;
        }
    }

    public class VideoGridAdapter extends BaseAdapter{

        public Context mContext;
        public LayoutInflater mInflater;
        public ArrayList<VideoGridItems> mGridLists;
        private int mSelectPos = 0;

        public void setSelectPos(int pos){
            mSelectPos = pos;
            notifyDataSetChanged();
        }

        public VideoGridAdapter(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        public void setDataRefresh(ArrayList<VideoGridItems> gridLists){
            this.mGridLists = gridLists;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mGridLists != null){
                return mGridLists.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mGridLists != null){
                return mGridLists.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VideoGridItems items = (VideoGridItems) getItem(position);
            String text = Integer.toString(items.mStartPos) + "~" + Integer.toString((items.mStartPos + items.mListSize -1));
            TextView textView = (TextView) mInflater.inflate(R.layout.row_grid_episode_item, null);
            textView.setText(text);
            if (position == mSelectPos){
                textView.setBackground(getResources().getDrawable(R.drawable.bg_pressed));
            }else {
                textView.setBackground(getResources().getDrawable(R.drawable.bg_normal));
            }
            return textView;
        }
    }

    public class VideoListAdapter extends BaseAdapter{

        public Context mContext;
        public LayoutInflater mInflater;
        public ArrayList<String> mVideoItemLists;
        public int mStartPos = 0;
        public int mItemNumer = 0;

        public VideoListAdapter(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        public void setDataRefresh(ArrayList<String> videoItemLists, boolean isShowMore, int startPos){
            this.mVideoItemLists = videoItemLists;
            this.mStartPos = startPos;
            if(!isShowMore){
                if(videoItemLists != null){
                    if(videoItemLists.size() < 15){
                        mItemNumer = videoItemLists.size();
                    }else{
                        mItemNumer = 15;
                    }
                }
            }else{
                if(videoItemLists != null){
                    mItemNumer = videoItemLists.size();
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {

            return mItemNumer;
        }

        @Override
        public Object getItem(int position) {
            if (mVideoItemLists != null){
                return mVideoItemLists.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) mInflater.inflate(R.layout.row_grid_episode_item, null);
            textView.setText(Integer.toString(mStartPos + position + 1));
            return textView;
        }
    }

    public class VideoGridItems {
        public int mStartPos;
        public int mListSize;
        ArrayList<String> mVideoLists;

        public VideoGridItems(int startPos, int listSize, ArrayList<String> videoLists){
            this.mStartPos = startPos;
            this.mListSize = listSize;
            this.mVideoLists = videoLists;
        }
    }
}
