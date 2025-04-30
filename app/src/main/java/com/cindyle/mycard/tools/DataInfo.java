package com.cindyle.mycard.tools;

import static android.content.ContentValues.TAG;

import android.util.Log;

public class DataInfo {
    // 因亂數起始碼為1，故data[0]皆暫放入空數
    public static final String[] phy = {"","乙", "丙" , "丁", "戊", "已", "庚", "辛", "壬", "癸", "寄干"};
    public static final String[]  pp = {"","乙庚", "丙已" , "丁戊", "戊辛", "已壬", "庚癸", "辛丁", "壬丙", "癸乙", "寄干"};
    public static final String[]  star = {"","天輔", "天沖" , "天任", "天蓬", "天英", "天芮", "天柱", "天心"};
    public static final String[]  god = {"","六合", "太陰" , "騰蛇", "值符", "白虎", "玄武", "九地", "九天"};
    public static final String[]  door = {"","休", "生" , "傷", "杜", "景", "死", "驚", "開"};
    public static final String[]  space = {"","乾", "兌" , "離", "震", "巽", "坎", "艮", "坤"};
    public static final String[]  other = {"","空亡", "馬星" , ""};
}
