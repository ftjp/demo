package com.example.demo.arithmetic;



import java.util.HashMap;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class MinCostClimbingStairs {
    public static void main(String[] args) {
        // 给定一个整数数组cost，其中cost[i]  是从楼梯第 i 个台阶向上爬需要支付的费用，下标从0开始。一旦你支付此费用，即可选择向上爬一个或者两个台阶。
        //你可以选择从下标为 0 或下标为 1 的台阶开始爬楼梯。请你计算并返回达到楼梯顶部的最低花费。
        int[] n = {1, 2, 5, 2, 1, 2, 4, 2, 3};
        System.out.println(test(n));
    }

    public static int test(int[] n) {
        // f(i) = f(i-1) + cost[i]  or  f(i-2) + cost[i]
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n.length; i++) {
            if (i < 2) {
                map.put(i, n[i]);
                continue;
            }
            map.put(i, Math.min(map.get(i - 1), map.get(i - 2)) + n[i]);
        }
        return map.get(n.length - 1);
    }


}


