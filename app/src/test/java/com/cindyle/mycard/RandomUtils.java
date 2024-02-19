package com.cindyle.mycard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomUtils {
    int randomNum = 0;

    public int getRandomNum(int num) {
        // 設定字母出現的權重, Map<放入的目標, 機率>
        Map<Integer, Integer> map = new HashMap<>();
        if (num == 3) {
            // 1：空亡   2：馬星   3：無
            map.put(1, 10);
            map.put(2, 10);
            map.put(3, 20);
        } else {
            for (int i = 1; i < num + 1; i++) {
                map.put(i, 10);
            }
        }

        int totalWeight = 0; // 總權重
        // 用有排序的Map將原本的Map裝入，但key為權重累加值，value為放入的目標
        NavigableMap<Integer, Integer> sortedMap = new TreeMap<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int weight = entry.getValue();
            Integer letter = entry.getKey();
            totalWeight += weight;

            sortedMap.put(totalWeight, letter);
        }

        // sortedMap : 各權重累加對應的字母
        List<Integer> randomList = genRandomListByWeight(1, sortedMap);
        randomNum = randomList.get(0);
        return randomNum;
    }

    // 依權重表取出指定數量的List
    private <T> List<T> genRandomListByWeight(int size, NavigableMap<Integer, T> weightTable) {
        List<T> randomList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            randomList.add(getRamdomElementByWeight(weightTable));
        }
        return randomList;
    }

    // 依權重表取出元素
    private <V> V getRamdomElementByWeight(NavigableMap<Integer, V> weightTable) {
        Random random = new Random();
        double randomDouble = random.nextDouble() * weightTable.lastKey();
        return weightTable.ceilingEntry((int) randomDouble).getValue();
    }
}
