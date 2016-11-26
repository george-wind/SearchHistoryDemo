package george.ni.searchhistorydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import george.ni.searchhistorydemo.DbUtils.SearchDbManager;
import george.ni.searchhistorydemo.adapter.SearchHistoryAdapter;
import george.ni.searchhistorydemo.bean.SearchHistoryBean;

/**
 * 只有点击搜索按钮后，会将输入字段存入数据库并进行搜索
 * 每在搜索框中输入一个字，都会进行搜索，但不会加入搜索记录
 * 当搜索栏没有输入值时显示搜索历史
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public String TABLE_NAME = null;
    public SearchHistoryAdapter searchHistoryAdapter;
    public List<SearchHistoryBean> historyDatas = new ArrayList();
    public SearchDbManager searchDbManager;
    public EditText etSearch;
    public TextView tvClear;
    private TextView tvSearch;
    private ListView lv;
    private LinearLayout llSearchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        getHistoryDatas();
    }

    public void getHistoryDatas() {
        historyDatas=searchDbManager.quearySearchHistory(TABLE_NAME);
        searchHistoryAdapter.setData(historyDatas);
    }

    private void initViews() {
        TABLE_NAME = "search_history";
        searchDbManager = new SearchDbManager(this);
        llSearchContent= (LinearLayout) findViewById(R.id.ll_search_content);
        tvClear = (TextView) findViewById(R.id.clear_history);
        tvClear.setOnClickListener(this);
        etSearch = (EditText) findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s)){
                    llSearchContent.setVisibility(View.GONE);
                }else {
                    llSearchContent.setVisibility(View.VISIBLE);
                    goSearch();
                }
            }
        });
        tvSearch= (TextView) findViewById(R.id.tv_search);
        tvSearch.setOnClickListener(this);
        //该方法是通过软键盘上的搜索按钮来添加进搜索历史中
//        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    updateTables();
//                    goSearch();
//                }
//                return false;
//            }
//        });

        lv= (ListView) findViewById(R.id.search_history);
        searchHistoryAdapter=new SearchHistoryAdapter(this,historyDatas);
        lv.setAdapter(searchHistoryAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goSearch();
            }
        });
    }

    private void goSearch() {
        //TODO

    }

    /**
     * 如果历史记录未记录，则添加入数据库
     * 如果该历史记录已经有，则更新该记录时间
     */
    public void updateTables() {
        String s=etSearch.getText().toString();
        SearchHistoryBean bean=new SearchHistoryBean(s,System.currentTimeMillis());
        if(hasHistory(s)){
            searchDbManager.updateHistory(TABLE_NAME,bean);
        }else {
            searchDbManager.insertSearchInfo(TABLE_NAME,bean);
        }
        historyDatas=searchDbManager.quearySearchHistory(TABLE_NAME);
        searchHistoryAdapter.setData(historyDatas);
    }

    public void clearHistory(){
        searchDbManager.clear(TABLE_NAME);
        historyDatas.clear();
        searchHistoryAdapter.setData(historyDatas);
    }

    /**
     * 判断该条历史记录是否已经记录
     * @param s
     * @return
     */
    public boolean hasHistory(String s){
        boolean a=false;
        for(SearchHistoryBean bean:historyDatas){
            if(bean.getContent().equals(s)){
                a=true;
                break;
            }
        }
        return a;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                updateTables();
                goSearch();
                break;
            case R.id.clear_history:
                clearHistory();
                break;
        }
    }
}
