package com.example.demo.arithmetic;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class Knapsack {
    public static void main(String[] args) {
        // 你有一个背包，最多能容纳的体积是V。每种物品只提供一个
        // 现在有n个物品，第i个物品的体积为 Vi,价值为 Wi

        //（1）求这个背包至多能装多大价值的物品？
        //（2）若背包恰好装满，求至多能装多大价值的物品？

        // 3 5
        // 2 10
        // 4 5
        // 1 4
        // 答案 14 ,9
//        System.out.println(test(5, new int[][]{{2, 10}, {4, 5}, {1, 4}}));
//        System.out.println(test(2, new int[][]{{2, 10}, {4, 5}, {1, 4}}));

//        System.out.println(test2(5, new int[][]{{2, 10}, {4, 5}, {1, 4}}));
//        System.out.println(test2(2, new int[][]{{2, 10}, {4, 5}, {1, 4}}));
        System.out.println(test2(6, new int[][]{{2, 10}, {4, 5}, {1, 4}, {1, 5}}));
    }

    public static int test(int V, int[][] arr) {
        V++;
        int[][] dp = new int[V][arr.length];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (i == 0) {
                    dp[i][j] = 0;
                    continue;
                }
                if (j == 0) {
                    dp[i][j] = arr[j][0] > i ? 0 : arr[j][1];
                    continue;
                }
                // 没有放/放了
                if (i - arr[j][0] < 0) {
                    dp[i][j] = dp[i][j - 1];
                    continue;
                }
                dp[i][j] = Math.max(dp[i][j - 1], dp[i - arr[j][0]][j - 1] + arr[j][1]);
            }
        }
        return dp[V - 1][arr.length - 1];
    }

    // 放满背包
    public static int test2(int V, int[][] arr) {
        V++;
        int[][] dp = new int[V][arr.length];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (i == 0) {
                    dp[i][j] = 0;
                    continue;
                }
                if (j == 0) {
                    if (arr[j][0] == i) {
                        dp[i][j] = arr[j][1];
                    }
                    continue;
                }
                if (i - arr[j][0] == 0 && dp[i - arr[j][0]][j - 1] == 0) {
                    dp[i][j] = arr[j][1];
                    continue;
                }
                if (i - arr[j][0] >= 0 && dp[i - arr[j][0]][j - 1] != 0) {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - arr[j][0]][j - 1] + arr[j][1]);
                } else {
                    dp[i][j] = dp[i][j - 1];
                }

            }
        }
        return dp[V - 1][arr.length - 1];
    }


}




