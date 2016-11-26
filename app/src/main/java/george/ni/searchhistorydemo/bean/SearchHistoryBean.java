package george.ni.searchhistorydemo.bean;

/**
 * Created by quan on 2016/11/17.
 * 历史搜索实体类
 */
public class SearchHistoryBean {
    private String content;
    private long createTime;

    public SearchHistoryBean(String content, long createTime) {
        this.content = content;
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
