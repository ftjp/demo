package com.example.demo.arithmetic;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class UniquePaths {
    public static void main(String[] args) {
        // 一个机器人在m×n大小的地图的左上角（起点）。机器人每次可以向下或向右移动。机器人要到达地图的右下角（终点）。可以有多少种不同的路径从起点走到终点？
        System.out.println(test(5, 7));
        System.out.println(test(2, 2));
        System.out.println(test(3, 3));
    }
    // 1 1 1 1 1
    // 1 1 1 1 1
    // 1 1 1 1 1
    // 1 1 1 1 1
    // 1 1 1 1 1
    // 1 1 1 1 1
    // 1 1 1 1 1


    public static int test(int n, int m) {
        int[][] r = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == 0 && j == 0) {
                    r[i][j] = 0;
                    continue;
                }
                if (i == 0 || j == 0) {
                    r[i][j] = 1;
                    continue;
                }
                r[i][j] = r[i - 1][j] + r[i][j - 1];
            }
        }
        return r[n - 1][m - 1];
    }


}


