package cn.fxdata.tv.fragment.home.hotplay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 动漫
 * 
 * @author zhaoxin5
 * 
 */
public class FragmentAnime extends HotplayBaseFragment {

    public FragmentAnime() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public String getClassType() {
        // TODO Auto-generated method stub
        return CLASS_ANIME;
    }

}
