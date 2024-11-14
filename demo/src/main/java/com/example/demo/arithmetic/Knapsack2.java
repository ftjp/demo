package com.example.demo.arithmetic;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class Knapsack2 {
    public static void main(String[] args) {
        // 你有一个背包，最多能容纳的体积是V。
        // 现在有n个物品，第i个物品的体积为 Vi,价值为 Wi，     能提供m个

        //（1）求这个背包至多能装多大价值的物品？
        //（2）若背包恰好装满，求至多能装多大价值的物品？
        // 做法一，直接把数量摊开，变成01背包问题，{2, 10, 3} = {2, 10, 1}{2, 10, 1}{2, 10, 1}
        // 做法二，三维数组
//        System.out.println(test(5, new int[][][]{{2, 10, 3}, {4, 5, 2}, {1, 4, 4}}));
//        System.out.println(test(5, new int[][]{{2, 10, 3}, {4, 5, 2}, {1, 4, 4}}));
        System.out.println(test2(6, new int[][]{{2, 10, 3}, {4, 5, 2}, {1, 4, 4}}));

    }


    // 价值最高
    public static int test(int V, int[][] arr) {
        V++;
        int[][] dp = new int[V][arr.length];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < arr.length; j++) {
                for (int k = 0; k < arr[j][2] + 1; k++) {
                    if (i - k * arr[j][0] >= 0) {
                        // 没放
                        if (k == 0) {
                            dp[i][j] = Math.max(dp[i][j], dp[i][j < 1 ? 0 : j - 1]);
                        }
                        dp[i][j] = Math.max(dp[i][j], dp[i - k * arr[j][0]][j] + k * arr[j][1]);
                    }
                }
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
                dp[i][j] = Math.max(dp[i][j], dp[i][j < 1 ? 0 : j - 1]);
                for (int k = 0; k < arr[j][2] + 1; k++) {
                    if (i - k * arr[j][0] == 0) {
                        dp[i][j] = Math.max(dp[i][j], k * arr[j][1]);
                    }
                    if (i - k * arr[j][0] > 0) {
                        if (dp[i - k * arr[j][0]][j] == 0) {
                            continue;
                        }
                        dp[i][j] = Math.max(dp[i][j], dp[i - k * arr[j][0]][j] + k * arr[j][1]);
                    }
                }
            }
        }
        return dp[V - 1][arr.length - 1];
    }


}




