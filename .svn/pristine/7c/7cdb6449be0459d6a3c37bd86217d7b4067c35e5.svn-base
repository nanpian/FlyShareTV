package cn.fxdata.tv.adapter;

import java.util.List;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.image.ImageLoader;
import com.android.volley.toolbox.BaseImageLoader.ImageListener;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import cn.fxdata.tv.R;
import cn.fxdata.tv.adapter.FollowMeAdapter.OnFollowMeItemClickListener;
import cn.fxdata.tv.adapter.HotPlayMovieAdapter.ViewHolder;
import cn.fxdata.tv.bean.HotCommentReturn.MovieComments;
import cn.fxdata.tv.http.VolleyRequestManager;
import cn.fxdata.tv.utils.StringUtils;

/**
 * 预告详情页面的热门评论适配器
 */

public class HotCommentAdapter extends BaseAdapter {
    private Context context;
    private List<MovieComments> hotComments;
    private LayoutInflater inflater;
    private Handler mHandler = new Handler();
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    private OnUserClickZanClickListener onUserClickZanClickListener;

    public HotCommentAdapter(Context context, List<MovieComments> hotComments) {
        this.context = context;
        this.hotComments = hotComments;
        inflater = LayoutInflater.from(context);
    }

    public void setHotComments(final List<MovieComments> hotComments1) {
        mHandler.post(new Runnable() {
            public void run() {
                // hotComments.clear();
                // hotComments.addAll(hotComments1);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return hotComments == null ? 0 : hotComments.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return hotComments == null ? null : hotComments.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.row_comment_item, null);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.userName = (TextView) convertView
                    .findViewById(R.id.userName);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.zan = (TextView) convertView.findViewById(R.id.zan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MovieComments movieComment = hotComments.get(position);
        Log.d("zhudewei",
                "movie comment avatar -->>" + movieComment.getAvatar()
                        + " movie comment username"
                        + movieComment.getUsername()
                        + " moview comment prasie time -->> "
                        + movieComment.getPraise_times());
        if (false == StringUtils.isNullOrEmpty(movieComment.getAvatar())) {
            ImageListener imageListener = ImageLoader.getImageListener(
                    holder.avatar, R.drawable.avatar_default,
                    R.drawable.avatar_default);
            VolleyRequestManager.getInstance().getImageLoader()
                    .get(movieComment.getAvatar(), imageListener);
        }
        holder.userName.setText(movieComment.getUsername() == null ? "null"
                : movieComment.getUsername());
        holder.time.setText(movieComment.getCreated() == null ? "null"
                : movieComment.getCreated());
        holder.zan.setText(movieComment.getPraise_times() == null ? "0"
                : movieComment.getPraise_times());
        holder.zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUserClickZanClickListener.onZanClick(position);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        public ImageView avatar;
        public TextView userName;
        public TextView time;
        public TextView zan;
    }

    public interface OnUserClickZanClickListener {
        void onZanClick(int position);
    }

    public void setOnUserClickZanClickListener(
            OnUserClickZanClickListener listener) {
        this.onUserClickZanClickListener = listener;
    }
}
