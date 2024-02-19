package com.cindyle.mycard.record;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cindyle.mycard.room.RecordBean;
import com.cindyle.mycard.room.RecordDb;

import java.util.ArrayList;
import java.util.List;

public class RecordViewModel extends ViewModel {
    public MutableLiveData<List<RecordBean>> list = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<Boolean> isEmpty = new MutableLiveData<>(false);
    private List<RecordBean> platformList;

    public void getRecordList(RecordDb db ){
        platformList = db.recordDao().getAllInfo();
        Log.i(TAG, "getCardsImg_getRoomList: " + platformList.size());
        list.setValue(platformList);

        boolean listEmpty = platformList.size() == 0;

        isEmpty.setValue(listEmpty);
    }

}