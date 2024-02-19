package com.cindyle.mycard.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RecordBean.class},version = 1,exportSchema = false)
public abstract class RecordDb extends RoomDatabase {
    public abstract RecordDao recordDao();
    private static RecordDb INSTANCE;

    public static RecordDb getDatabase(final Context context) {
        if (INSTANCE == null){
            synchronized (RecordDb.class) {
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RecordDb.class, "data_cards.db")
                            .fallbackToDestructiveMigration()
//                            .addCallback(callback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
