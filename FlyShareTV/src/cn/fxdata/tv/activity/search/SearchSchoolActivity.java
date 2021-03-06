package cn.fxdata.tv.activity.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.zip.Inflater;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.TextView;
import cn.fxdata.tv.R;
import cn.fxdata.tv.R.id;
import cn.fxdata.tv.adapter.ListViewAdapter;
import cn.fxdata.tv.base.BaseActivity;
import cn.fxdata.tv.constants.Constants;
import cn.fxdata.tv.fragment.home.HotPlayFragment;
import cn.fxdata.tv.utils.MJsonUtil;
import cn.fxdata.tv.utils.StringUtils;
import cn.fxdata.tv.view.SearchEditText;
import cn.fxdata.tv.bean.hotschool.Hotschool;
import cn.fxdata.tv.bean.movie.Movie;
import cn.fxdata.tv.bean.school.*;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Listener;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyTools;
import com.android.volley.request.FJsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zgntech.core.view.SideBar;
import com.zgntech.core.view.SideBar.OnTouchingLetterChangedListener;

public class SearchSchoolActivity extends BaseActivity {
    private final String TAG = "SearchSchoolActivity";

    private ImageView backImageView;
    private SearchEditText searchEditText;
    private GridView mHotSchoolGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search_school);
        backImageView = (ImageView) findViewById(R.id.image_back);
        searchEditText = (SearchEditText) findViewById(R.id.edit_search);
        mHotSchoolGridView = (GridView) findViewById(R.id.school_gv);
        mHotSchoolGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
                String school = (String) arg1
                        .getTag(R.id.tag_hotplay_school_name);
                int schoolId = (Integer) arg1
                        .getTag(R.id.tag_hotplay_school_id);
                Log.i("xixia-1", "school:" + school + ",id:" + schoolId);
                Bundle bundle = new Bundle();
                bundle.putString(
                        HotPlayFragment.SEARCH_SCHOOL_EXTRA_SCHOOL_NAME, school);
                bundle.putInt(
                        HotPlayFragment.SEARCH_SCHOOL_EXTRA_SCHOOL_SCHOOL_ID,
                        schoolId);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        backImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        setup();
    }

    private String[] indexStr = { "#", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };
    private HashMap<String, Integer> selector;// 存放含有索引字母的位置
    private ListView mSchoolListView = null;
    private ListViewAdapter mSchoolListAdapter;
    private List<SchoolData> mSchools = null;
    private List<SchoolData> mNewSchools = new ArrayList<SchoolData>();
    TextView mCurrentSideBarFocus = null;
    com.zgntech.core.view.SideBar mSideBar = null;
    com.zgntech.core.view.SideBar.OnTouchingLetterChangedListener mOnTouchingLetterChangedListener = new OnTouchingLetterChangedListener() {
        @Override
        public void onTouchingLetterChanged(String s, boolean isTouching) {
            // TODO Auto-generated method stub
            if (isTouching) {
                if (mCurrentSideBarFocus.getVisibility() == View.INVISIBLE) {
                    mCurrentSideBarFocus.setVisibility(View.VISIBLE);
                }
                mCurrentSideBarFocus.setText(s);
                if (selector.containsKey(s)) {
                    int pos = selector.get(s);
                    if (mSchoolListView.getHeaderViewsCount() > 0) {// 防止ListView有标题栏，本例中没有。
                        mSchoolListView.setSelectionFromTop(pos
                                + mSchoolListView.getHeaderViewsCount(), 0);
                    } else {
                        mSchoolListView.setSelectionFromTop(pos, 0);// 滑动到第一项
                    }
                }
            } else {
                if (mCurrentSideBarFocus.getVisibility() == View.VISIBLE) {
                    mCurrentSideBarFocus.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    private void makeDataForSchool() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... unused) {
                String[] allNames = sortIndex(mSchools);
                sortList(allNames);
                selector = new HashMap<String, Integer>();
                for (int j = 0; j < indexStr.length; j++) {// 循环字母表，找出newPersons中对应字母的位置
                    for (int i = 0; i < mNewSchools.size(); i++) {
                        if (mNewSchools.get(i).getName().equals(indexStr[j])) {
                            selector.put(indexStr[j], i);
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mSchoolListAdapter = new ListViewAdapter(
                        SearchSchoolActivity.this, mNewSchools);
                mSchoolListView.setAdapter(mSchoolListAdapter);
                // hide progressbar
                SearchSchoolActivity.this.findViewById(
                        R.id.pb_hot_play_school_list_loading).setVisibility(
                        View.INVISIBLE);
            }
        }.execute();
    }

    private void setup() {
        setupViews();
        // setData();
        mSchools = new ArrayList<SchoolData>();
        loadDataFromServer();
        setupHotSchoolData();
    }

    private void setupViews() {
        mSideBar = (SideBar) this.findViewById(R.id.sidebar);
        mSideBar.setOnTouchingLetterChangedListener(mOnTouchingLetterChangedListener);
        mCurrentSideBarFocus = (TextView) this.findViewById(R.id.dialog);
        mSchoolListView = (ListView) this.findViewById(R.id.school_lv);
        mSchoolListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // TODO Auto-generated method stub
                String text = (String) view.getTag();
                if (!text.equals("null")) {
                    String school = (String) view
                            .getTag(R.id.tag_hotplay_school_name);
                    int schoolId = (Integer) view
                            .getTag(R.id.tag_hotplay_school_id);
                    Bundle bundle = new Bundle();
                    bundle.putString(
                            HotPlayFragment.SEARCH_SCHOOL_EXTRA_SCHOOL_NAME,
                            school);
                    bundle.putInt(
                            HotPlayFragment.SEARCH_SCHOOL_EXTRA_SCHOOL_SCHOOL_ID,
                            schoolId);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    public class SchoolData {
        private String name;
        private String pinYinName;
        private int schoolId;

        public SchoolData(String name) {
            super();
            this.name = name;
        }

        public SchoolData(String name, String pinYinName) {
            super();
            this.name = name;
            this.pinYinName = pinYinName;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPinYinName() {
            return pinYinName;
        }

        public void setPinYinName(String pinYinName) {
            this.pinYinName = pinYinName;
        }
    }

    /**
     * 获取排序后的新数据
     * 
     * @param schools
     * @return
     */
    public String[] sortIndex(List<SchoolData> schools) {
        TreeSet<String> set = new TreeSet<String>();
        // 获取初始化数据源中的首字母，添加到set中
        for (SchoolData school : schools) {
            set.add(StringUtils.getPinYinHeadChar(school.getName()).substring(
                    0, 1));
        }
        // 新数组的长度为原数据加上set的大小
        String[] names = new String[schools.size() + set.size()];
        int i = 0;
        for (String string : set) {
            names[i] = string;
            i++;
        }
        String[] pinYinNames = new String[schools.size()];
        for (int j = 0; j < schools.size(); j++) {
            schools.get(j)
                    .setPinYinName(
                            StringUtils.getPingYin(schools.get(j).getName()
                                    .toString()));
            pinYinNames[j] = StringUtils.getPingYin(schools.get(j).getName()
                    .toString());
        }
        // 将原数据拷贝到新数据中
        System.arraycopy(pinYinNames, 0, names, set.size(), pinYinNames.length);
        // 自动按照首字母排序
        Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
        return names;
    }

    /**
     * 重新排序获得一个新的List集合
     * 
     * @param allNames
     */
    private void sortList(String[] allNames) {
        for (int i = 0; i < allNames.length; i++) {
            if (allNames[i].length() != 1) {
                for (int j = 0; j < mSchools.size(); j++) {
                    if (allNames[i].equals(mSchools.get(j).getPinYinName())) {
                        SchoolData p = new SchoolData(
                                mSchools.get(j).getName(), mSchools.get(j)
                                        .getPinYinName());
                        p.setSchoolId(mSchools.get(j).getSchoolId());
                        mNewSchools.add(p);
                    }
                }
            } else {
                mNewSchools.add(new SchoolData(allNames[i]));
            }
            Log.i("xixia", "" + i + ", " + mNewSchools.get(i).getPinYinName());
        }
    }

    public VolleyTools tools;

    private void loadDataFromServer() {
        String url = Constants.ServerConfig.SCHOOL_LIST_URL + "&user_id=1";
        tools = VolleyTools.getInstance(this.getContext());
        tools.addToRequestQueue(new StringRequest(Method.GET, url,
                new Listener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        // TODO Auto-generated method stub
                        cn.fxdata.tv.bean.school.School school = new Gson()
                                .fromJson(response,
                                        cn.fxdata.tv.bean.school.School.class);
                        for (int i = 0; i < school.getData().size(); i++) {
                            String name = school.getData().get(i).getName();
                            SchoolData s = new SchoolData(name);
                            s.setSchoolId(Integer.valueOf(school.getData()
                                    .get(i).getId()));
                            mSchools.add(s);
                        }
                        makeDataForSchool();
                    }
                }));
    }

    class HotSchoolAdapter extends BaseAdapter {

        final String[] schools;
        final int[] ids;
        final Context context;

        HotSchoolAdapter(Context context, String[] schools, int[] ids) {
            this.schools = schools;
            this.ids = ids;
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return schools.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.row_textview, null);
            convertView.setTag(R.id.tag_hotplay_school_id, new Integer(
                    ids[position]));
            convertView.setTag(R.id.tag_hotplay_school_name, schools[position]);
            ((TextView) convertView).setText(schools[position]);
            return convertView;
        }
    }

    /**
     * 准备热门高校的数据
     */
    private void setupHotSchoolData() {
        VolleyTools tools = VolleyTools.getInstance(this);
        // 根据不同学校的Id返回
        String requestUrl = Constants.ServerConfig.HOT_SCHOOL_LIST_URL;
        StringRequest hotRequest = new StringRequest(Method.GET, requestUrl,
                new Listener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d(TAG, "data response --> " + response.toString());
                        Hotschool hotSchool = MJsonUtil.gson.fromJson(response,
                                new TypeToken<Hotschool>() {
                                }.getType());
                        if (hotSchool.getErrorCode() == 0) {
                            if (hotSchool.getData() != null
                                    && hotSchool.getData().getHotSchool() != null
                                    && hotSchool.getData().getHotSchool()
                                            .size() > 0) {
                                String[] schools = new String[hotSchool
                                        .getData().getHotSchool().size()];
                                int[] ids = new int[hotSchool.getData()
                                        .getHotSchool().size()];
                                for (int i = 0; i < schools.length; i++) {
                                    schools[i] = hotSchool.getData()
                                            .getHotSchool().get(i).getName();
                                    ids[i] = Integer.valueOf(hotSchool
                                            .getData().getHotSchool().get(i)
                                            .getSchoolId());
                                    Log.d(TAG, "hotschool[" + i + "]:"
                                            + schools[i]);
                                }
                                HotSchoolAdapter adapter = new HotSchoolAdapter(
                                        getApplicationContext(), schools, ids);
                                mHotSchoolGridView.setAdapter(adapter);
                            }
                        } else {
                            Log.i(TAG, "ERROR CODE");
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        super.onError(error);
                        Log.d(TAG, "the response --> " + error.toString());
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                        Log.d(TAG, "cancelled");
                    }
                });
        tools.addToRequestQueue(hotRequest, tools.VolleyTAG);
    }

    /**
     * 设置模拟数据
     */
    private void setData() {
        mSchools = new ArrayList<SchoolData>();
        SchoolData p1 = new SchoolData("北京大学");
        mSchools.add(p1);
        SchoolData p2 = new SchoolData("北京交通大学");
        mSchools.add(p2);
        SchoolData p3 = new SchoolData("兰州大学");
        mSchools.add(p3);
        SchoolData p4 = new SchoolData("深圳大学");
        mSchools.add(p4);
        SchoolData p5 = new SchoolData("浙江大学");
        mSchools.add(p5);
        SchoolData p6 = new SchoolData("南京大学");
        mSchools.add(p6);
        SchoolData p7 = new SchoolData("中国矿业大学");
        mSchools.add(p7);
        SchoolData p8 = new SchoolData("南京农业大学");
        mSchools.add(p8);
        SchoolData p9 = new SchoolData("延边大学");
        mSchools.add(p9);
        SchoolData p10 = new SchoolData("中山大学");
        mSchools.add(p10);
        SchoolData p11 = new SchoolData("南京理工大学");
        mSchools.add(p11);
        SchoolData p12 = new SchoolData("南京航空航天大学");
        mSchools.add(p12);
        SchoolData p13 = new SchoolData("四川大学");
        mSchools.add(p13);
        SchoolData p14 = new SchoolData("西藏大学");
        mSchools.add(p14);
        SchoolData p15 = new SchoolData("宁夏大学");
        mSchools.add(p15);
        SchoolData p16 = new SchoolData("上海大学");
        mSchools.add(p16);
        SchoolData p17 = new SchoolData("复旦大学");
        mSchools.add(p17);
        SchoolData p18 = new SchoolData("宁波大学");
        mSchools.add(p18);
        SchoolData p19 = new SchoolData("厦门大学");
        mSchools.add(p19);
        SchoolData p20 = new SchoolData("华侨大学");
        mSchools.add(p20);
        SchoolData p21 = new SchoolData("云南大学");
        mSchools.add(p21);

    }
}
