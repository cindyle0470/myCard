package com.cindyle.mycard.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecordDao {

    @Query("select * from data_cards")
    List<RecordBean> getAllInfo();

    //根据用户id查询数据,数据表的字段id对应方法的id值
    //查询语句里可以通过冒号方法变量名方式使用
    @Query("select * from data_cards where time = :time")
    RecordBean getInfo(String time);

    //根据用户id查询数据,数据表的字段id对应方法的id值
    //查询语句里可以通过冒号方法变量名方式使用
    @Query("select * from data_cards where id = :id")
    RecordBean getInfo(long id);


    //onConflict = OnConflictStrategy.REPLACE
    //表明插入一条数据如果主键已经存在，则可以直接替换旧的数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RecordBean info);

    @Update
    void update(RecordBean info);

    @Delete
    void deleteBean(RecordBean info);

    @Query("DELETE FROM data_cards where time = :time")
    void deleteBean(String time);

    @Query("DELETE FROM data_cards")
    void clearAll();


}

