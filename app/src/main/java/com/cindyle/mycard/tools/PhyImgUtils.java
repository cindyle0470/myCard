package com.cindyle.mycard.tools;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mycard.R;

public class PhyImgUtils {
    public static int src;

    public static int getPhy(int phy) {
        switch (phy){
            case 1:
                src = R.drawable.physical2;
                break;
            case 2:
                src = R.drawable.physical3;
                break;
            case 3:
                src = R.drawable.physical4;
                break;
            case 4:
                src = R.drawable.physical5;
                break;
            case 5:
                src = R.drawable.physical6;
                break;
            case 6:
                src = R.drawable.physical7;
                break;
            case 7:
                src = R.drawable.physical8;
                break;
            case 8:
                src = R.drawable.physical9;
                break;
            case 9:
                src = R.drawable.physical10;
                break;
            default:
                src = R.drawable.physical_back;
                break;
        }
        return src;
    }

    public static int getPpy(int ppy) {
        switch (ppy){
            case 1:
                src = R.drawable.ppy1;
                break;
            case 2:
                src = R.drawable.ppy2;
                break;
            case 3:
                src = R.drawable.ppy3;
                break;
            case 4:
                src = R.drawable.ppy4;
                break;
            case 5:
                src = R.drawable.ppy5;
                break;
            case 6:
                src = R.drawable.ppy6;
                break;
            case 7:
                src = R.drawable.ppy7;
                break;
            case 8:
                src = R.drawable.ppy8;
                break;
            case 9:
                src = R.drawable.ppy9;
                break;
            default:
                src = R.drawable.ppy_back;
                break;
        }
        return src;
    }

    public static int getStar(int star){
        switch (star){
            case 1:
                src = R.drawable.star1;
                break;
            case 2:
                src = R.drawable.star2;
                break;
            case 3:
                src = R.drawable.star3;
                break;
            case 4:
                src = R.drawable.star4;
                break;
            case 5:
                src = R.drawable.star5;
                break;
            case 6:
                src = R.drawable.star6;
                break;
            case 7:
                src = R.drawable.star7;
                break;
            case 8:
                src = R.drawable.star8;
                break;
            default:
                src = R.drawable.star_back;
                break;
        }
        return src;
    }

    public static int getGod(int god){
        switch (god){
            case 1:
                src = R.drawable.god1;
                break;
            case 2:
                src = R.drawable.god2;
                break;
            case 3:
                src = R.drawable.god3;
                break;
            case 4:
                src = R.drawable.god4;
                break;
            case 5:
                src = R.drawable.god5;
                break;
            case 6:
                src = R.drawable.god6;
                break;
            case 7:
                src = R.drawable.god7;
                break;
            case 8:
                src = R.drawable.god8;
                break;
            default:
                src = R.drawable.god_back;
                break;
        }
        return src;
    }

    public static int getDoor(int door){
        switch (door){
            case 1:
                src = R.drawable.door1;
                break;
            case 2:
                src = R.drawable.door2;
                break;
            case 3:
                src = R.drawable.door3;
                break;
            case 4:
                src = R.drawable.door4;
                break;
            case 5:
                src = R.drawable.door5;
                break;
            case 6:
                src = R.drawable.door6;
                break;
            case 7:
                src = R.drawable.door7;
                break;
            case 8:
                src = R.drawable.door8;
                break;
            default:
                src = R.drawable.door_back;
                break;
        }
        return src;
    }

    public static int getSpace(int space){
        switch (space){
            case 1:
                src = R.drawable.space1;
                break;
            case 2:
                src = R.drawable.space2;
                break;
            case 3:
                src = R.drawable.space3;
                break;
            case 4:
                src = R.drawable.space4;
                break;
            case 5:
                src = R.drawable.space5;
                break;
            case 6:
                src = R.drawable.space6;
                break;
            case 7:
                src = R.drawable.space7;
                break;
            case 8:
                src = R.drawable.space8;
                break;
            default:
                src = R.drawable.space_back;
                break;
        }
        return src;
    }

    public static int getOther(int other, boolean isHorseShow){
        switch (other) {
            case 1:
                src = R.drawable.none;
                break;
            case 2:
                if (isHorseShow){
                    src = R.drawable.horse;
                } else {
                    src = R.drawable.nothing;
                }
                break;
            case 3:
                src = R.drawable.nothing;
                break;
            default:
                src = R.drawable.other_back;
                break;
        }
        return src;
    }

}
