package com.example.demo.arithmetic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class Knapsack3 {
    public static void main(String[] args) {
        //     给定一个正整数n，请找出最少个数的完全平方数，使得这些完全平方数的和等于n。
        //    完全平方指用一个整数乘以自己例如1*1，2*2，3*3等，依此类推。若一个数能表示成某个整数的平方的形式，则称这个数为完全平方数。
        //    例如:1，4，9，和16都是完全平方数，但是2，3，5，8，11等等不是

        System.out.println(test(6));

    }

    public static int test(int n) {
        n++;
        List<Integer> squareList = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            if (i * i > n) {
                break;
            }
            squareList.add(i * i);
        }
        int[][] dp = new int[n][squareList.size()];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < squareList.size(); j++) {
                for (int k = 0; k < n / squareList.get(j); k++) {

                    if (j - squareList.get(j) * k >= 0) {
                        if (j - squareList.get(j) * k == 0) {
                            dp[i][j] = Math.min(k, dp[i][Math.max(j - 1, 0)] == 0 ? dp[i][Math.max(j - 1, 0)] : k);
                        }
                        if (dp[i][j - squareList.get(j) * k] != n) {
                            dp[i][j] = Math.min(dp[i][j], dp[i][j - squareList.get(j) * k] + k);
                        }
                    }
                }
            }
        }
        return dp[n][squareList.size() - 1];
    }


}




