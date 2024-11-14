package com.example.demo.arithmetic;

import java.util.Arrays;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class LIS {
    public static void main(String[] args) {
        // 给定一个长度为 n 的数组 arr，求它的最长严格上升子序列的长度。
        //所谓子序列，指一个数组删掉一些数（也可以不删）之后，形成的新数组。例如 [1,5,3,7,3] 数组，其子序列有：[1,3,3]、[7] 等。但 [1,6]、[1,3,5] 则不是它的子序列。
        //我们定义一个序列是 严格上升 的，当且仅当该序列不存在两个下标
        // [6,3,1,5,2,3,7] 该数组最长上升子序列为 [1,2,3,7] ，长度为4
        System.out.println(test(new int[]{6, 3, 1, 5, 2, 3, 7}));
        System.out.println(test(new int[]{1, 5, 2, 3, 4, 7}));

    }

    public static int test(int[] arr) {
        int[] dp = new int[arr.length];
        //设置数组长度大小的动态规划辅助数组
        Arrays.fill(dp, 1);
        int res = 0;
        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j < i; j++) {
                //可能j不是所需要的最大的，因此需要dp[i] < dp[j] + 1
                if (arr[i] > arr[j] && dp[i] < dp[j] + 1) {
                    //i点比j点大，理论上dp要加1
                    dp[i] = dp[j] + 1;
                    //找到最大长度
                    res = Math.max(res, dp[i]);
                }
            }
        }
        return res;
    }
}




