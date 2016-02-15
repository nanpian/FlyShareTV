package cn.fxdata.tv.constants;

public class Constants {
    public static class ServerConfig {
        public static final String BASE_URL = "http://fx.72zhe.com/api/server.php?";
        public static final String HOME_URL = "http://fx.72zhe.com/api/server.php?ac=newMovie";
        public static final String HOME_URL2 = "http://fx.72zhe.com/api/server.php?ac=speedMovie";
        public static final String ZHUI_URL = "http://fx.72zhe.com/api/server.php?ac=catchMe";
        public static final String PREVIEW_URL = "http://fx.72zhe.com/api/server.php?ac=preview";
        public static final String GET_COLUMNS_URL = "http://fx.72zhe.com/api/server.php?ac=hotspot";
        public static final String MOVIE_DETAIL_URL = "http://fx.72zhe.com/api/server.php?ac=today";
        public static final String SCHOOL_LIST_URL = "http://fx.72zhe.com/api/server.php?ac=schoolList";
        public static final String UPDATE_VERSION_URL = "http://fx.72zhe.com/api/server.php?ac=getVersion";
        public static final String FOLLOW_ME_URL = "http://fx.72zhe.com/api/server.php?ac=catchList";
        public static final String FOLLOW_ME_URL2 = "http://fx.72zhe.com/api/server.php?ac=catchRecords";
        public static final String DROP_URL = "http://fx.72zhe.com/api/server.php?ac=dropCatch";
        public static final String HOT_PALY_URL = "http://fx.72zhe.com/api/server.php?ac=hotspot";
        public static final String COMMENT_LIST_URL = "http://fx.72zhe.com/api/server.php?ac=commentList&";
        public static final String REQUEST_URL = "http://fx.72zhe.com/api/server.php?";
        public static final String TODAY_MOVIE_URL = "http://fx.72zhe.com/api/server.php?ac=today";
        public static final String HOT_SCHOOL_LIST_URL = "http://fx.72zhe.com/api/server.php?ac=hotSchool";
        public static final String HOT_BROADCAST_URL = "http://fx.72zhe.com/api/server.php?ac=hotBroadcast";
        public static final String HOT_SEARCH_URL = "http://fx.72zhe.com/api/server.php?ac=hotMovie";
    }

    public static class DEFAULT_VALUE {

        /***/
        public static final int TEXT_LINE_TAG_DEFAULT = 0;
        public static final int TEXT_LINE_TAG_MAX = 1;
        public static final int TEXT_LINE_TAG_MIN = 2;

        /** default text show max lines */
        public static final int DEFAULT_MAX_LINE_COUNT = 15;
        /** textv最大字数，超过该字数 显示加载更多 */
        public static final int DEFAULT_MAX_TEXTLENGTH_COUNT = 120;
        /** listView 刷新延迟时间 */
        public static final int DEFAULT_LIST_LAOD_DELAYMILLIS = 400;
    }

    /**
     * 登陆类型 TODO<请描述这个类是干什么的>
     * 
     * @version: V1.0
     */
    public static class PersonLoginType {

        public static final int LOGIN_TYPE_NOLOGIN = 0;

        public static final int LOGIN_TYPE_FX = 1;

        public static final int LOGIN_TYPE_QQ = 2;

        public static final int LOGIN_TYPE_SINA = 3;
    }

    public static class MovieExtra {
        /** 用于在视频播放页面间传递视频的地址 */
        public static final String EXTRA_MOVIE_PATH = "extra_movie_path";
        public static final String EXTRA_MOVIE_ID = "extra_movie_id";
        public static final String EXTRA_MOVIE_THUMB = "extra_movie_pic";
        public static final String EXTRA_MOVIE_NAME = "extra_movie_name";
        public static final String EXTRA_MOVIE_TYPE = "extra_movie_type";
        public static final String EXTRA_MOVIE_AREA = "extra_movie_area";
        public static final String EXTRA_MOVIE_RELEASE_TIME = "extra_movie_release_time";
        public static final String EXTRA_MOVIE_DETAIL = "extra_movie_detail";
        public static final String EXTRA_MOVIE_POSTION ="extra_movie_postion";
    }
    
    public static class ConstantDefault {
    	public static final String CONSTANT_DEFAULT_USER_ID = "user_id";
    	public static final String CONSTANT_DEFAULT_MOVIE_ID = "movie_id";
    }
    
    public static class CommentConstant {
    	/** 评论相关的常量 */
    	public static final String CONSTANT_COMMENT_PAGE = "page";
    	public static final String CONSTANT_COMMENT_RECORDS = "records";
    	public static final String CONSTANT_COMMENT_COMMENT_ID = "comment_id";
    	public static final String CONSTANT_COMMENT_AC = "ac";
    	public static final String CONSTANT_COMMENT_PRAISE = "praise";	
    	public static final String CONSTANT_COMMENT_CONTENT = "content";	
    	public static final String CONSTANT_COMMENT_COMMENT = "comment";	
    	public static final String CONSTANT_COMMENT_COMMENT_LIST = "commentList";
    }
    
    public static class EpisodeConstant {
    	/** 选集相关的常量 */
    	public static final String CONSTANT_EPISODE_SETS = "sets";
    }
}
