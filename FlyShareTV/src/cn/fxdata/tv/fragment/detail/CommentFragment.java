package cn.fxdata.tv.fragment.detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Listener;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.zgntech.core.util.UiTools;
import com.zgntech.core.view.pullrefresh.PullToRefreshBase;
import com.zgntech.core.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zgntech.core.view.pullrefresh.PullToRefreshListView;

import cn.fxdata.tv.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import cn.fxdata.tv.adapter.CommentAdapter;
import cn.fxdata.tv.adapter.HotCommentAdapter;
import cn.fxdata.tv.adapter.HotCommentAdapter.OnUserClickZanClickListener;
import cn.fxdata.tv.base.BaseFragment;
import cn.fxdata.tv.bean.HotCommentReturn;
import cn.fxdata.tv.bean.MovieCommentsDataReturn;
import cn.fxdata.tv.bean.MovieCommentsDataReturn.MovieComments;
import cn.fxdata.tv.bean.comment.Comment;
import cn.fxdata.tv.bean.comment.HotComment;
import cn.fxdata.tv.bean.comment.UpdateComment;
import cn.fxdata.tv.constants.Constants;
import cn.fxdata.tv.http.VolleyRequestManager;
import cn.fxdata.tv.utils.StringUtils;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Video detail comments fragment in video detail activity.
 */
public class CommentFragment extends BaseFragment implements OnItemClickListener, View.OnClickListener {

    private final static String TAG = "CommentFragment";

    private CommentAdapter mCommentAdapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    /** 电影评论数据 */
    protected LinkedList<MovieComments> movieCommentsDatas = new LinkedList<MovieComments>();
    private com.zgntech.core.view.pullrefresh.PullToRefreshListView pullToRefreshListView;
    private ListView mListview;
    private int page;
    private String mMovieId;
    private EditText mUserCommentEditText = null;

    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        /*mHotCommentAdapter = new HotCommentAdapter(this.getContext(), mHotComments);
        mHotCommentAdapter.setOnUserClickZanClickListener(mOnUserClickZanListener);*/
        Bundle bundle = getArguments();
        // 启动参数解析
        parseArguments(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail_comment, container, false);
        // commentListView = (ListView)
        // view.findViewById(R.id.video_detail_comment_listview);
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.person_comments_pulltorefreshlistview);
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setScrollLoadEnabled(true);
        // pullToRefreshListView.setPullRefreshEnabled(false);

        mListview = pullToRefreshListView.getRefreshableView();
        mListview.setFadingEdgeLength(0);
        mListview.setDividerHeight(0);
        mListview.setSelector(getResources().getDrawable(R.drawable.transparent));
        mListview.setOnItemClickListener(this);

        pullToRefreshListView.setOnRefreshListener(mOnPullToRefreshListener);
        loadData();

        mUserCommentEditText = (EditText) view.findViewById(R.id.comment_edit);
        // 发送评论按钮
        view.findViewById(R.id.btn_send).setOnClickListener(this);
        // commentListView.setAdapter(adapter);
        // UiTools.setListViewHeightBasedOnChildren(commentListView);
        return view;
    }

    /** 下拉刷新ListView的监听器 */
    OnRefreshListener mOnPullToRefreshListener = new OnRefreshListener<View>() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<View> refreshView) {
            // TODO Auto-generated method stub
            // 下拉刷新评论
            Log.i(TAG, "onPullDownToRefresh");
            // refreshCommentData();
            firstLoadComment();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<View> refreshView) {
            // TODO Auto-generated method stub
            // 上拉加载更多评论
            Log.i(TAG, "onPullUpToRefresh");
            String page = getLoadMoreCommentsPage();
            if (page == null) {
                showToast("已经没有更多评论了!");
                pullToRefreshListView.onPullUpRefreshComplete();
                return;
            }
            loadMoreCommentData(page);
        }

    };

    @Override
    public void loadData() {
        // TODO Auto-generated method stub
        super.loadData();
        if (isShouldLoadData()) {
            setFirstLoad(false);
            if (movieCommentsDatas != null) {
                // 清空已保存的评论数据
                movieCommentsDatas.clear();
            }
            pullToRefreshListView.doPullRefreshing(true, Constants.DEFAULT_VALUE.DEFAULT_LIST_LAOD_DELAYMILLIS);
        }
    }

    private void fillParams(Map<String, String> map, String ac, String movieId, String userId, String page, String records, String commentId, String content) {
        if (ac != null)
            map.put(Constants.CommentConstant.CONSTANT_COMMENT_AC, ac);

        if (movieId != null)
            map.put(Constants.ConstantDefault.CONSTANT_DEFAULT_MOVIE_ID, movieId);
        if (userId != null)
            map.put(Constants.ConstantDefault.CONSTANT_DEFAULT_USER_ID, userId);
        if (page != null)
            map.put(Constants.CommentConstant.CONSTANT_COMMENT_PAGE, page);
        if (records != null)
            map.put(Constants.CommentConstant.CONSTANT_COMMENT_RECORDS, records);

        if (commentId != null)
            map.put(Constants.CommentConstant.CONSTANT_COMMENT_COMMENT_ID, commentId);
        /*
         * if(praise != null)
         * map.put(Constants.CommentConstant.CONSTANT_COMMENT_AC, praise);
         */
        // 发送评论
        if (content != null)
            map.put(Constants.CommentConstant.CONSTANT_COMMENT_CONTENT, content);
    }

    private void firstLoadComment() {
    	// 初次加载评论，最前面是3条热门评论，然后是后面的其他评论，
    	// 注意后面的评论中包含有前面的3条热门评论
        Map<String, String> param = new HashMap<String, String>();
        fillParams(param, Constants.CommentConstant.CONSTANT_COMMENT_COMMENT_LIST, mMovieId, getUserId(), "1", sPerPageRecords, null, null);
        
        sendVolleyRequest(param, Constants.ServerConfig.BASE_URL, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "The response data string -- >> " + response);
                pullToRefreshListView.onPullDownRefreshComplete();
                try {
					Comment commentResponse = cn.fxdata.tv.utils.MJsonUtil.gson
							.fromJson((String) response,
									new TypeToken<Comment>() {
									}.getType());
					
					if(commentResponse.getErrorCode() == 0) {
						mAllComments.clear();
						mAllCommentsIds.clear();
						mCurrentPage = 1;
						
						if(commentResponse.getData() != null && commentResponse.getData().getHotComment() != null) {
							loadHotComments(commentResponse.getData().getHotComment());
						}
						
						if(commentResponse.getData() != null && commentResponse.getData().getUpdateComment() != null) {
							loadUpdateComments(commentResponse.getData().getUpdateComment(), false);
							updateTotalPages(commentResponse.getData().getTotalPage());
						}
						
						if(null == mCommentAdapter) {
							mCommentAdapter = new CommentAdapter(getContext(), mAllComments);
							mCommentAdapter.setOnUserClickZanClickListener(mOnUserClickZanListener);
							mListview.setAdapter(mCommentAdapter);
						}
					}
                } catch (JsonSyntaxException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

            public void onError(VolleyError error) {
                Log.d(TAG, "The response data string -- >> " + error.toString());
                pullToRefreshListView.onPullDownRefreshComplete();
            }

        });
    }

    Handler mRefreshCommentHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
    };
    
    private void loadHotComments(List<HotComment> hotComments) {
    	if(hotComments.size() > 0) {
			// 热门评论列表不为空
			for(int i=0; i<hotComments.size(); i++) {	
				HotComment hotComment = hotComments.get(i);
				if(!mAllCommentsIds.contains(hotComment.getCommentId()) && !mAllComments.contains(hotComment)) {
					mAllCommentsIds.add(hotComment.getCommentId());
					mAllComments.add(hotComment);
				}
			}
			Log.d(TAG, "Hot Comment size is : " + hotComments.size());
		}
    }
    
    private void loadUpdateComments(List<UpdateComment> updateComments, boolean move2Top) {
    	if(updateComments.size() > 0) {
			// 热门评论列表不为空
			for(int i=0; i<updateComments.size(); i++) {	
				UpdateComment updateComment = updateComments.get(i);
				if(!mAllCommentsIds.contains(updateComment.getCommentId()) && !mAllComments.contains(updateComment)) {
					mAllCommentsIds.add(updateComment.getCommentId());
					if(move2Top) {
						// 因为前面三条是热门评论
						mAllComments.add(3, updateComment);;	
					} else {
						mAllComments.add(updateComment);
					}
				}
			}
			Log.d(TAG, "Update Comment size is : " + updateComments.size());
		}
    }
    
    /***
     * 发表完评论以后调用
     * 获取最新发表的评论
     */
    private void loadLatestComment() {
    	// 一开始先加载最顶部的热门评论
        Map<String, String> param = new HashMap<String, String>();
        fillParams(param, Constants.CommentConstant.CONSTANT_COMMENT_COMMENT_LIST, mMovieId, getUserId(), "1", sPerPageRecords, null, null);
        
        sendVolleyRequest(param, Constants.ServerConfig.BASE_URL, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "The response data string -- >> " + response);
                pullToRefreshListView.onPullDownRefreshComplete();
                try {
					Comment commentResponse = cn.fxdata.tv.utils.MJsonUtil.gson
							.fromJson((String) response,
									new TypeToken<Comment>() {
									}.getType());
					
					if(commentResponse.getErrorCode() == 0) {
						if(commentResponse.getData() != null 
								&& commentResponse.getData().getUpdateComment() != null 
								&& commentResponse.getData().getUpdateComment().size() > 0) {
							// 热门评论列表不为空
							
							loadUpdateComments(commentResponse.getData().getUpdateComment(), true);
							updateTotalPages(commentResponse.getData().getTotalPage());
							
							if(null == mCommentAdapter) {
								mCommentAdapter = new CommentAdapter(getContext(), mAllComments);
								mCommentAdapter.setOnUserClickZanClickListener(mOnUserClickZanListener);
								mListview.setAdapter(mCommentAdapter);
							} else {
								mCommentAdapter.notifyDataSetChanged();
							}
						}
					}
                } catch (JsonSyntaxException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

            public void onError(VolleyError error) {
                Log.d(TAG, "The response data string -- >> " + error.toString());
                pullToRefreshListView.onPullDownRefreshComplete();
            }

        });
    }
    
    private HotComment getHotCommentFromUpdateComment(UpdateComment comment) {
    	HotComment rt = new HotComment();
    	rt.setAvatar(comment.getAvatar());
    	rt.setCommentId(comment.getCommentId());
    	rt.setContent(comment.getContent());
    	rt.setCreated(comment.getCreated());
    	rt.setUserId(comment.getUserId());
    	rt.setPraiseTimes(comment.getPraiseTimes());
    	rt.setUsername(comment.getUsername());
    	return rt;
    }
    
    /**
     * 加载 更多 电影评论 数据
     */
    private void loadMoreCommentData(String page) {
        Map<String, String> param = new HashMap<String, String>();

        fillParams(param, Constants.CommentConstant.CONSTANT_COMMENT_COMMENT_LIST, mMovieId, getUserId(), 
        		page, sPerPageRecords, null, null);
        
        sendVolleyRequest(param, new Listener<String>() {

            @Override
            public void onSuccess(String response) {
                // TODO Auto-generated method stub
                pullToRefreshListView.onPullUpRefreshComplete();

                try {
					Comment commentResponse = cn.fxdata.tv.utils.MJsonUtil.gson
							.fromJson((String) response,
									new TypeToken<Comment>() {
									}.getType());
					
					if(commentResponse.getErrorCode() == 0) {
						if(commentResponse.getData() != null 
								&& commentResponse.getData().getUpdateComment() != null 
								&& commentResponse.getData().getUpdateComment().size() > 0) {
							// 热门评论列表不为空
							
							loadUpdateComments(commentResponse.getData().getUpdateComment(), false);
							updateTotalPages(commentResponse.getData().getTotalPage());
							
							if(null == mCommentAdapter) {
								mCommentAdapter = new CommentAdapter(getContext(), mAllComments);
								mCommentAdapter.setOnUserClickZanClickListener(mOnUserClickZanListener);
								mListview.setAdapter(mCommentAdapter);
							} else {
								mCommentAdapter.notifyDataSetChanged();
							}
						}
					}
                } catch (JsonSyntaxException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

            public void onError(VolleyError error) {

            }
        });
    }

    /** volley相关 */
    public void sendVolleyRequest(Map<String, String> params, Listener<String> listener) {
        if (checkNet()) {
            Map<String, String> reqParams = getReqParam(params);
            com.zgntech.core.util.StringParamsRequest request = new com.zgntech.core.util.StringParamsRequest(Method.GET, Constants.ServerConfig.COMMENT_LIST_URL, listener, reqParams);
            request.setRetryPolicy(getRetryPolicy());
            VolleyRequestManager.getInstance().addRequest(request, this);
        }
    }

    public void sendVolleyRequest(Map<String, String> params, String server, Listener<String> listener) {
        if (checkNet()) {
            Map<String, String> reqParams = getReqParam(params);
            com.zgntech.core.util.StringParamsRequest request = new com.zgntech.core.util.StringParamsRequest(Method.GET, server, listener, reqParams);
            request.setRetryPolicy(getRetryPolicy());
            VolleyRequestManager.getInstance().addRequest(request, this);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

    }

    private void updateTotalPages(int total) {
    	// http://fx.72zhe.com/api/server.php?ac=commentList&movie_id=5&page=3&records=10）
    	// TotalPage 表明当前UpdateHotcomment共有多少页
    	// 最新发表的评论都在第一页
    	// 因此loadLatestComments获取的都是第一页
    	// loadMoreComments获取的都是page1+1页
    	// 如果页面数大于totalPages的计数,则提示用户已无评论
    	if(total != mTotalPage) {
    		mTotalPage = total;
    	}
    }
    
    String getLoadMoreCommentsPage() {
        synchronized (this) {
            mCurrentPage = mCurrentPage + 1;
            if (mCurrentPage > mTotalPage) {
                return null;
            }
            return String.valueOf(mCurrentPage);
        }
    }

    // 评论数据总共的页数
    public int mTotalPage = 0; // 表示当前电影共有多少页评论
    // 评论数据当前页数,
    // 如果是第一次加载,则是1,同时填充到mHotComments之中
    // 后面则是这个值加1,然后填充到mHotComments的最后
    public int mCurrentPage = 0;
    /** 
     * 存放所有评论的记录集合,一开始放的是热门评论
     * 然后放的是UpdateComment,也就是更新的评论
     * 实际上这两个评论的项目都是一样的,因此只使用HotComment即可
     */
    private List<Object> mAllComments = new ArrayList<Object>();
    private HotCommentAdapter mHotCommentAdapter;
    
    // 保存所有的评论ID,用于排除已经加过的评论
    private List<String> mAllCommentsIds = new ArrayList<String>();
    // 存放评论的列表
    // public List<HotCommentReturn.MovieComments> mHotComments = new ArrayList<HotCommentReturn.MovieComments>();
    // 每一页固定读取10条评论记录
    private static String sPerPageRecords = "10";
    // 是否正在刷新
    private Boolean isRefreshTime = false;
    // 用户点赞的监听
    private final OnUserClickZanClickListener mOnUserClickZanListener = new OnUserClickZanClickListener() {
        @Override
        public void onZanClick(int position) {
            // TODO Auto-generated method stub
            Log.d(TAG, "user click zan");
            // 首先得到comment id,movie id ,赞接口需要
            if (isRefreshTime == true) {
                showToast("请稍后点赞");
                return;
            }
            showToast("赞");
            synchronized (isRefreshTime) {
            	String commentId = null;
            	Object o = mAllComments.get(position);
            	if(o instanceof HotComment) {
            		commentId = ((HotComment)o).getCommentId();
            	} 
            	
            	if(o instanceof UpdateComment) {
            		commentId = ((UpdateComment)o).getCommentId();
            	}
            	
                if (commentId != null) {
                    if (!StringUtils.isNullOrEmpty(commentId) && !StringUtils.isNullOrEmpty(mMovieId)) {
                        isRefreshTime = true;
                        zanRequest(commentId, position);
                    }
                }

            }
        }
    };

    @Override
    public String getUserId() {
        // TODO Auto-generated method stub
        boolean overrideSuper = StringUtils.isNullOrEmpty(super.getUserId());
        return overrideSuper ? "2" : super.getUserId();
    }

    private final static boolean DEBUG_ZAN = true;
    private final static boolean DEBUG_COMMENT = true;
    
    @Override
	protected void showToast(String msg) {
		// TODO Auto-generated method stub
		// super.showToast(msg);
    	Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
	}

	/**
     * 赞接口
     */
    private synchronized void zanRequest(String commentId, final int postion) {
        Map<String, String> param = new HashMap<String, String>();
        if (DEBUG_ZAN) {
            Log.i(TAG, "commentId : " + commentId + ", userId : " + getUserId());
        }
        String ac = Constants.CommentConstant.CONSTANT_COMMENT_PRAISE;
        fillParams(param, ac, null, getUserId(), null, null, commentId, null);
        sendVolleyRequest(param, Constants.ServerConfig.BASE_URL, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "zanResponse:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Integer error_code = jsonObject.getInt("error_code");
                    String error_msg = jsonObject.getString("error_msg");
                    Log.d(TAG, "debug zan: error code is -->> " + error_code + " \n error msg is -->> " + error_msg);
                    if (error_code == 0) {
                        if (jsonObject != null) {
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            if (jsonData != null) {
                                String result = jsonData.getString("result");
                                if (result.equals("1")) {
                                    showToast("点赞成功!");
                                    
                                    Object o = mAllComments.get(postion);
                                    if(o instanceof HotComment) {
                                    	HotComment hotComment = (HotComment) o;
                                        String oldTimes = hotComment.getPraiseTimes();
                                        hotComment.setPraiseTimes(String.valueOf(Integer.parseInt(oldTimes) + 1));
                                        mCommentAdapter.notifyDataSetChanged();
                                    }
                                    
                                    if(o instanceof UpdateComment) {
                                    	UpdateComment updateComment = (UpdateComment) o;
                                        String oldTimes = updateComment.getPraiseTimes();
                                        updateComment.setPraiseTimes(String.valueOf(Integer.parseInt(oldTimes) + 1));
                                        mCommentAdapter.notifyDataSetChanged();
                                    }
                                    
                                    isRefreshTime = false;
                                } else if(result.equals("2")) {
                                    showToast("取消点赞!");
                                    
                                    Object o = mAllComments.get(postion);
                                    if(o instanceof HotComment) {
                                    	HotComment hotComment = (HotComment) o;
                                        String oldTimes = hotComment.getPraiseTimes();
                                        hotComment.setPraiseTimes(String.valueOf(Integer.parseInt(oldTimes) - 1));
                                        mCommentAdapter.notifyDataSetChanged();
                                    }
                                    
                                    if(o instanceof UpdateComment) {
                                    	UpdateComment updateComment = (UpdateComment) o;
                                        String oldTimes = updateComment.getPraiseTimes();
                                        updateComment.setPraiseTimes(String.valueOf(Integer.parseInt(oldTimes) - 1));
                                        mCommentAdapter.notifyDataSetChanged();
                                    }
                                    isRefreshTime = false;
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("点赞失败!");
                    isRefreshTime = false;
                }
            }

            public void onError(VolleyError error) {
                Log.d(TAG, "The response data string -- >> " + error.toString());
                isRefreshTime = false;
                showToast("点赞失败!");
            }
        });
    }

    private void parseArguments(Bundle bundle) {
        if (null != bundle) {
            // 获取MovieId
            mMovieId = bundle.getString(Constants.MovieExtra.EXTRA_MOVIE_ID);
        } else {
            mMovieId = "13";
        }
    }

    private String getComment() {
        // 返回用户输入框中待发表的评论
        if (null == mUserCommentEditText) {
            return null;
        }
        String str = mUserCommentEditText.getText().toString();
        if (StringUtils.isNullOrEmpty(str)) {
            return null;
        }
        return str;
    }

    private void sendCommentRequest(String content) {
        final String userId = getUserId();
        final String movieId = mMovieId;
        final String ac = Constants.CommentConstant.CONSTANT_COMMENT_COMMENT;
        Map<String, String> param = new HashMap<String, String>();
        fillParams(param, ac, movieId, userId, null, null, null, content);

        sendVolleyRequest(param, Constants.ServerConfig.BASE_URL, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "zanResponse:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Integer error_code = jsonObject.getInt("error_code");
                    String error_msg = jsonObject.getString("error_msg");
                    Log.d(TAG, "debug zan: error code is -->> " + error_code + " \n error msg is -->> " + error_msg);
                    if (error_code == 0) {
                        if (jsonObject != null) {
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            if (jsonData != null) {
                                String result = jsonData.getString("result");
                                if (result.equals("1")) {
                                    // 发表评论成功
                                    if (DEBUG_COMMENT) {
                                        Log.i(TAG, "comment success");
                                    }
                                    if (null != mUserCommentEditText) {
                                        mUserCommentEditText.setText("");
                                    }
                                    loadLatestComment();
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // showToast("点赞失败!");
                }
            }

            public void onError(VolleyError error) {
                Log.d(TAG, "The response data string -- >> " + error.toString());
                // showToast("点赞失败!");
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        final int id = v.getId();
        switch (id) {
        case R.id.btn_send:
            // 发送评论按钮
            String comment = getComment();
            if (null == comment) {
                return;
            }
            if (DEBUG_COMMENT) {
                Log.i(TAG, "comment : " + comment);
            }
            sendCommentRequest(comment);
            break;
        }
    }
}
