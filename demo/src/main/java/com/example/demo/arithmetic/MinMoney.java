package com.example.demo.arithmetic;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class MinMoney {
    public static void main(String[] args) {
        // 给定数组arr，arr中所有的值都为正整数且不重复。每个值代表一种面值的货币，每种面值的货币可以使用任意张，再给定一个aim，代表要找的钱数，求组成aim的最少货币数。
        //如果无解，请返回-1.
        // [5,2,3],20  输出 4
        System.out.println(test(new int[]{5, 2, 3}, 20));
        System.out.println(test(new int[]{5, 2, 3}, 21));
        System.out.println(test(new int[]{5, 2, 3}, 22));
        System.out.println(test(new int[]{5, 2, 3}, 25));
        System.out.println(test(new int[]{5, 2, 3}, 0));
        System.out.println(test(new int[]{5, 2, 3}, 1));
    }

    public static int test(int[] arr, int aim) {
        // 假设能组成，则有几种解法，每个数字都可以选择，所以最少的解法就是最少选择次数，即最少货币数
        // f（n）= min{f（n-arr[i]）+1}
        if (aim == 0) {
            return 0;
        }
        int[] r = new int[aim + 1];
        for (int i = 0; i < aim + 1; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[j] == i) {
                    r[i] = 1;
                    continue;
                }
                if (i > arr[j]) {
                    if (r[i - arr[j]] > 0) {
                        if (r[i] > 0) {
                            r[i] = Math.min(r[i], r[i - arr[j]] + 1);
                            continue;
                        }
                        r[i] = r[i - arr[j]] + 1;
                    }
                }
            }
        }
        return r[aim] > 0 ? r[aim] : -1;
    }


}


