package com.cindyle.mycard;

import org.junit.Test;

import com.cindyle.mycard.tools.RandomUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        for (int i = 0; i < 10; i++) {
            int cardNum = new RandomUtils().getRandomNum(3);
            System.out.println("num = " + cardNum);
        }

    }

}