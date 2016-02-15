package cn.fxdata.tv.activity.search;

import cn.fxdata.tv.base.BaseActivity;
import cn.fxdata.tv.view.SearchEditText;
import cn.fxdata.tv.view.SearchEditText.SearchClickListener;
import cn.fxdata.tv.R;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * @description：TODO<请描述这个类是干什么的>
 * @author：lyu
 * @date:2015-5-30
 * @version：V1.0
 */
public class SearchHistoryActivity extends BaseActivity {
    private GridView historyGridView;
    private GridView hotGridView;
    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;

    private String[] histories = new String[] { "两天一夜", "妻子的谎言", "嘿，老头",
            "两天一夜", "妻子的谎言", "嘿，老头", "两天一夜", "妻子的谎言", "嘿，老头" };
    private ImageView backImageView;
    private SearchEditText searchEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search_history);
        historyGridView = (GridView) findViewById(R.id.history_gv);
        hotGridView = (GridView) findViewById(R.id.hotsearch_gv);
        backImageView = (ImageView) findViewById(R.id.image_back);
        searchEditText = (SearchEditText) findViewById(R.id.edit_search);
        adapter1 = new ArrayAdapter<String>(this, R.layout.row_textview,
                histories);
        historyGridView.setAdapter(adapter1);
        // UiTools.setGridViewHeightBasedOnChildren(historyGridView);
        adapter2 = new ArrayAdapter<String>(this, R.layout.row_textview,
                histories);
        hotGridView.setAdapter(adapter2);
        // UiTools.setGridViewHeightBasedOnChildren(hotGridView);
        backImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        searchEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int actionId,
                    KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Bundle bundle = new Bundle();
                    bundle.putString("key", searchEditText.getText().toString());
                    showActivity(SearchResultActivity.class, bundle);
                    return true;
                }
                return false;
            }
        });
    }

}
