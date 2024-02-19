package com.cindyle.mycard;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.cindyle.mycard.tools.RandomUtils;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void getNum(){
        int cardNum = new RandomUtils().getRandomNum(8);
        System.out.print(cardNum);
    }

    public void useAppContext() {
        // Context of the app under test.
        int a = getRandomNum(8);
        System.out.print(a);
    }

    private int getRandomNum(int num){
        int randomNum;
        randomNum = (int)(Math.random()*num)+1;
        return randomNum;
    }
}

