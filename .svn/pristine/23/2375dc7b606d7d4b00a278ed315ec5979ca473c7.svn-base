package cn.fxdata.tv.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import com.android.volley.image.ImageLoader;
import com.android.volley.toolbox.BaseImageLoader.ImageListener;

import cn.fxdata.tv.R;
import cn.fxdata.tv.adapter.HotCommentAdapter.OnUserClickZanClickListener;
import cn.fxdata.tv.adapter.HotCommentAdapter.ViewHolder;
import cn.fxdata.tv.bean.HotCommentReturn.MovieComments;
import cn.fxdata.tv.bean.comment.HotComment;
import cn.fxdata.tv.bean.comment.UpdateComment;
import cn.fxdata.tv.http.VolleyRequestManager;
import cn.fxdata.tv.utils.StringUtils;

public class CommentAdapter extends BaseAdapter {
    private Context mContext;

    public CommentAdapter(Context context, List<Object> comments) {
        this.mContext = context;
        this.mAllComments = comments;
    }
    
    private List<Object> mAllComments;
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mAllComments == null ? 0 : mAllComments.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mAllComments == null ? null : mAllComments.get(arg0);
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
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.row_comment_item, null);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.userName = (TextView) convertView
                    .findViewById(R.id.userName);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.zan = (TextView) convertView.findViewById(R.id.zan);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String userName = null;
        String created = null;
        String praiseTimes = null;
        String avatar = null;
        String content = null;
        
        Object o = mAllComments.get(position);
        if(o instanceof HotComment) {
        	HotComment hotComment = (HotComment) o;
        	userName = hotComment.getUsername();
        	created = hotComment.getCreated();
        	praiseTimes = hotComment.getPraiseTimes();
        	avatar = hotComment.getAvatar();
        	content = hotComment.getContent();
        }
        
        if(o instanceof UpdateComment) {
        	UpdateComment updateComment = (UpdateComment) o;
        	userName = updateComment.getUsername();
        	created = updateComment.getCreated();
        	praiseTimes = updateComment.getPraiseTimes();
        	avatar = updateComment.getAvatar();
        	content = updateComment.getContent();
        }
        
        if (false == StringUtils.isNullOrEmpty(avatar)) {
            ImageListener imageListener = ImageLoader.getImageListener(
                    holder.avatar, R.drawable.avatar_default,
                    R.drawable.avatar_default);
            VolleyRequestManager.getInstance().getImageLoader()
                    .get(avatar, imageListener);
        }
        holder.userName.setText(userName == null ? "null"
                : userName);
        holder.time.setText(created == null ? "null"
                : created);
        holder.zan.setText(praiseTimes == null ? "0"
                : praiseTimes);
        holder.zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if(mZanListener != null) {
                    mZanListener.onZanClick(position);	
            	}
            }
        });
        holder.content.setText(content);
        return convertView;
    }

    private OnUserClickZanClickListener mZanListener;
    public void setOnUserClickZanClickListener(
            OnUserClickZanClickListener listener) {
        this.mZanListener = listener;
    }
    
    public class ViewHolder {
        public ImageView avatar;
        public TextView userName;
        public TextView time;
        public TextView zan;
        public TextView content;
    }
}
