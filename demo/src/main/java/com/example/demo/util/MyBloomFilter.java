package com.example.demo.util;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * @author caili
 * @create 2024/11/11 9:53
 */
public class MyBloomFilter {

    /**
     * 预计插入的数据
     */
    private static Integer expectedInsertions = 100000000;
    /**
     * 误判率
     */
    private static Double fpp = 0.01;
    /**
     * 布隆过滤器
     */
    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), expectedInsertions, fpp);

    public static void main(String[] args) {


        long numBits = (long) ((double) (-expectedInsertions) * Math.log(fpp) / (Math.log(2.0) * Math.log(2.0)));
        int numHashFunctions = Math.max(1, (int) Math.round((double) numBits / (double) expectedInsertions * Math.log(2.0)));

        // 插入 1千万数据
        for (int i = 0; i < expectedInsertions; i++) {
            bloomFilter.put(i);
        }

        // 用1千万数据测试误判率
        int count = 0;
        for (int i = expectedInsertions; i < expectedInsertions * 2; i++) {
            if (bloomFilter.mightContain(i)) {
                count++;
            }
        }

        System.out.println("一共误判了：" + count);
        System.out.println("误判率：" + (double) count / expectedInsertions);

    }


}
