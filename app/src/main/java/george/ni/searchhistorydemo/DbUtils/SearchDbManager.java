package george.ni.searchhistorydemo.DbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



import java.util.ArrayList;

import george.ni.searchhistorydemo.bean.SearchHistoryBean;

/**
 * Created by quan on 2016/11/17.
 * 搜索历史 数据库管理器
 */
public class SearchDbManager {

    private Context context;
    public SearchDbManager(Context context) {
        this.context=context;
    }
    /**
     * 插入数据，若数据已满5条，则跟新最老的一条记录
     * @param tableName
     * @return
     */
    public void insertSearchInfo(String tableName,SearchHistoryBean bean) {

        SQLiteDatabase db = context.openOrCreateDatabase( "search_history.db", Context.MODE_PRIVATE,null);

        createTable(db, tableName);
        if(hasFiveItem(db,tableName)){
            ContentValues cValue = new ContentValues();
            cValue.put("content", bean.getContent());
            cValue.put("create_time", bean.getCreateTime());
            db.update(tableName,cValue,"create_time=(select min(create_time) from "+tableName+")",null);

        }else {
            ContentValues cValue = new ContentValues();

            cValue.put("content", bean.getContent());

            cValue.put("create_time", bean.getCreateTime());
            //调用insert()方法插入数据
            db.insert(tableName, null, cValue);

        }
        db.close();

    }

    public void updateHistory(String tableName,SearchHistoryBean bean){
        SQLiteDatabase db = context.openOrCreateDatabase( "search_history.db", Context.MODE_PRIVATE,null);
        ContentValues cValue = new ContentValues();
//        cValue.put("content", bean.getContent());
        cValue.put("create_time", bean.getCreateTime());
        db.update(tableName,cValue,"content="+"'"+bean.getContent()+"'",null);
    }

    /**
     * 查询表中字段
     * @param tableName
     * @return
     */
    public ArrayList<SearchHistoryBean> quearySearchHistory(String tableName){
        ArrayList<SearchHistoryBean> users=new ArrayList<>();
        SQLiteDatabase db = context.openOrCreateDatabase( "search_history.db", Context.MODE_PRIVATE,null);
        createTable(db, tableName);
        Cursor c = db.rawQuery("SELECT * FROM " + tableName + " order by create_time desc", null);
        while (c.moveToNext()){
            String content=c.getString(c.getColumnIndex("content"));
            long time=c.getLong(c.getColumnIndex("create_time"));
            SearchHistoryBean bean=new SearchHistoryBean(content,time);
            users.add(bean);
        }
        c.close();
        db.close();
        return users;
    }

    /**
     * 清除该表中所有数据
     * @param tableName
     */
    public void clear(String tableName){
        SQLiteDatabase db = context.openOrCreateDatabase( "search_history.db", Context.MODE_PRIVATE,null);
        String sql="delete from "+tableName+"";
        db.execSQL(sql);
    }

    /**
     * 判断，创建表
     * @param db
     * @param tableName
     */
    public void createTable(SQLiteDatabase db,String tableName) {
        if(!tabbleIsExist(db,tableName)){
            //创建表SQL语句
            String stu_table = "create table "+tableName+" (content varchar[30],create_time varchar[30])";
            //执行SQL语句
            db.execSQL(stu_table);
        }

    }

    /**
     * 判断该表是否存在
     * @param db
     * @param tableName
     * @return
     */
    public boolean tabbleIsExist(SQLiteDatabase db,String tableName){
        boolean result = false;
        if(tableName == null){
            return false;
        }

        try {
            //这里表名可以是Sqlite_master
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tableName.trim()+"' ";
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断表中数量是否有5条了
     * @param db
     * @param tableName
     * @return
     */
    public boolean hasFiveItem(SQLiteDatabase db,String tableName){
        boolean result = false;
        if(tableName == null){
            return false;
        }
        String sql = "select count(*) as c from "+tableName+"";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToNext()){
            int count = cursor.getInt(0);
            if(count==5){
                result = true;
            }
        }
        cursor.close();
        return result;
    }

}
