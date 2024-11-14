package com.example.demo.arithmetic;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class MinPathSum {
    public static void main(String[] args) {
        // 给定一个 n * m 的矩阵 a，从左上角开始每次只能向右或者向下走，最后到达右下角的位置，路径上所有的数字累加起来就是路径和，输出所有的路径中最小的路径和。
        int[][] num = {{1, 3, 5, 9}, {8, 1, 3, 4}, {5, 0, 6, 1}, {8, 8, 4, 0}};
        System.out.println(test(num));
    }
    // 1,3,5,9
    // 8,1,3,4
    // 5,0,6,1
    // 8,8,4,0

    public static int test(int[][] num) {
        int[][] r = new int[num.length][num[0].length];
        for (int i = 0; i < num.length; i++) {
            for (int j = 0; j < num[0].length; j++) {
                if (i == 0 && j == 0) {
                    r[i][j] = num[i][j];
                    continue;
                }
                if (i == 0) {
                    r[i][j] = r[i][j - 1] + num[i][j];
                    continue;
                }
                if (j == 0) {
                    r[i][j] = r[i - 1][j] + num[i][j];
                    continue;
                }
                int min = Math.min(r[i - 1][j], r[i][j - 1]);
                int nu = num[i][j];
                r[i][j] = min + nu;
            }
        }
        return r[num.length - 1][num[0].length - 1];
    }


}


