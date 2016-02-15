package cn.fxdata.tv.activity.hotplay;

import java.util.List;

import cn.fxdata.tv.R;
import cn.fxdata.tv.fragment.home.hotplay.FragmentAnime;
import cn.fxdata.tv.fragment.home.hotplay.FragmentDrama;
import cn.fxdata.tv.fragment.home.hotplay.FragmentMovie;
import cn.fxdata.tv.fragment.home.hotplay.FragmentVariety;
import cn.fxdata.tv.fragment.home.hotplay.IndicatorFragmentActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class HotplayClassifyActivity extends IndicatorFragmentActivity {

    public static final int FRAGMENT_1_VARIETY = 0;
    public static final int FRAGMENT_2_DRAMA = 1;
    public static final int FRAGMENT_3_MOVIE = 2;
    public static final int FRAGMENT_4_ANIME = 3;

    public static final int getIntFromString(String str, Resources res) {
        if (str.equals(res.getString(R.string.fragment_1_variety))) {
            return FRAGMENT_1_VARIETY;
        } else if (str.equals(res.getString(R.string.fragment_2_drama))) {
            return FRAGMENT_2_DRAMA;
        } else if (str.equals(res.getString(R.string.fragment_3_movie))) {
            return FRAGMENT_3_MOVIE;
        } else if (str.equals(res.getString(R.string.fragment_4_anime))) {
            return FRAGMENT_4_ANIME;
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            Integer index = data.getInt("key");
            this.navigate(index);
        }
    }

    @Override
    protected int supplyTabs(List<TabInfo> tabs) {
        tabs.add(new TabInfo(FRAGMENT_1_VARIETY,
                getString(R.string.fragment_1_variety), FragmentVariety.class));
        tabs.add(new TabInfo(FRAGMENT_2_DRAMA,
                getString(R.string.fragment_2_drama), FragmentDrama.class));
        tabs.add(new TabInfo(FRAGMENT_3_MOVIE,
                getString(R.string.fragment_3_movie), FragmentMovie.class));
        tabs.add(new TabInfo(FRAGMENT_4_ANIME,
                getString(R.string.fragment_4_anime), FragmentAnime.class));
        return FRAGMENT_2_DRAMA;
    }

    @Override
    protected void initViews() {
        // TODO Auto-generated method stub
        super.initViews();
        ImageView back = (ImageView) this.findViewById(R.id.iv_left);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText(getResources().getString(R.string.hotplay_classify_title));
        ((View) this.findViewById(R.id.btn_right).getParent())
                .setVisibility(View.INVISIBLE);
    }
}
