package com.example.demo.arithmetic;

import java.util.Arrays;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class GetLongestPalindrome {
    public static void main(String[] args) {
        // 对于长度为n的一个字符串A（仅包含数字，大小写英文字母），请设计一个高效算法，计算其中最长回文子串的长度
        // 3
        System.out.println(test("cababcd"));
        // 5
        System.out.println(test("abbba"));
        // 6
        System.out.println(test("cabbac"));
        // 5
        System.out.println(test("abacbca"));
        // 7
        System.out.println(test("ababcabacb"));
        // 5
        System.out.println(test("bbcbcb"));

    }

    public static int test(String arr) {

        // 至少大于2个字符
        if (arr.length() < 2) {
            return 0;
        }
        int MAX = 0;
        int[] r = new int[arr.length()];
        Arrays.fill(r, 1);
        if (arr.charAt(0) == arr.charAt(1)) {
            r[0] = 2;
            r[1] = 2;
            MAX = 2;
        }
        for (int i = 1; i < arr.length(); i++) {
            for (int j = 0; j < i; j++) {
                if ((j + r[j]) < i) {
                    if (r[j] < MAX) {
                        continue;
                    }
                    if (r[j] == MAX) {
                        j = j + r[j] - 1;
                        continue;
                    }
                }
                if ((j + r[j]) > i) {
                    continue;
                }

                if ((j + r[j]) == i) {
                    if (j > 0 && arr.charAt(j - 1) == arr.charAt(j + r[j])) {
                        r[j] = r[j] + 2;
                        r[j - 1] = Math.max(r[j - 1], r[j]);
                        for (int k = j + 1; k < i + 1; k++) {
                            if (arr.charAt(k) != arr.charAt(j)) {
                                r[k] = Math.max(r[k], r[j]);
                            }
                        }
                        MAX = Math.max(MAX, r[j]);
                        break;
                    }
                    // 与自己对比
                    if (arr.charAt(j) == arr.charAt(j + r[j])) {
                        boolean flag = true;
                        for (int k = 0; k < r[j] / 2 + 1; k++) {
                            if (arr.charAt(j + k) != arr.charAt(j + r[j] - k)) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            r[j] = r[j] + 1;
                            for (int k = j + 1; k < i + 1; k++) {
                                if (arr.charAt(k) != arr.charAt(j)) {
                                    r[k] = Math.max(r[k], r[j]);
                                }
                            }
                            MAX = Math.max(MAX, r[j]);
                            break;
                        }

                    }
                }

            }
        }
        return MAX;
    }


}




