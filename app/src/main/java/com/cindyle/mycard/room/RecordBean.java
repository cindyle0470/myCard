package com.cindyle.mycard.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "data_cards")
public class RecordBean {
    @PrimaryKey(autoGenerate = true)
    public long id;
    // 1 單宮 , 2 雙宮 , 3 宮
    public int type;
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    public String time;
    public String question;
    public int phy1;
    public int phy2;
    public int pp1;
    public int pp2;
    public int star;
    public int god;
    public int door;
    public int space;
    public int other;
    public int phy21;
    public int phy22;
    public int pp21;
    public int pp22;
    public int star2;
    public int god2;
    public int door2;
    public int space2;
    public int other2;
    public int phy31;
    public int phy32;
    public int pp31;
    public int pp32;
    public int star3;
    public int god3;
    public int door3;
    public int space3;
    public int other3;
    public String record;

    public RecordBean(long id, int type, String time, String question, int phy1, int phy2, int pp1, int pp2, int star, int god, int door, int space, int other, int phy21, int phy22, int pp21, int pp22, int star2, int god2, int door2, int space2, int other2, int phy31, int phy32, int pp31, int pp32, int star3, int god3, int door3, int space3, int other3, String record) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.question = question;
        this.phy1 = phy1;
        this.phy2 = phy2;
        this.pp1 = pp1;
        this.pp2 = pp2;
        this.star = star;
        this.god = god;
        this.door = door;
        this.space = space;
        this.other = other;
        this.phy21 = phy21;
        this.phy22 = phy22;
        this.pp21 = pp21;
        this.pp22 = pp22;
        this.star2 = star2;
        this.god2 = god2;
        this.door2 = door2;
        this.space2 = space2;
        this.other2 = other2;
        this.phy31 = phy31;
        this.phy32 = phy32;
        this.pp31 = pp31;
        this.pp32 = pp32;
        this.star3 = star3;
        this.god3 = god3;
        this.door3 = door3;
        this.space3 = space3;
        this.other3 = other3;
        this.record = record;
    }

    @NonNull
    @Override
    public String toString() {
        return "RecordBean{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", time='" + time + '\'' +
                ", question='" + question + '\'' +
                ", record='" + record + '\'' +
                '}';
    }

    public String getTime() {
        return time;
    }

    public String getQuestion() {
        return question;
    }
}
