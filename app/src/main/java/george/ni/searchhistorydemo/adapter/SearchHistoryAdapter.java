package george.ni.searchhistorydemo.adapter;

import android.content.Context;


import java.util.List;

import george.ni.searchhistorydemo.R;
import george.ni.searchhistorydemo.bean.SearchHistoryBean;

/**
 * Created by quan on 2016/11/16.
 * 搜索-历史记录适配器
 */
public class SearchHistoryAdapter extends CommonAdapter<SearchHistoryBean> {
    public SearchHistoryAdapter(Context context, List<SearchHistoryBean> datas) {
        super(context, datas);
        layoutId= R.layout.adapter_search_history;
    }

    @Override
    public void convert(ViewHolder holder, SearchHistoryBean bean) {
        holder.setText(R.id.content,bean.getContent());
    }
}
