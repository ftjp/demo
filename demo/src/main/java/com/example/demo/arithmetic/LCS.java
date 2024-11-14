package com.example.demo.arithmetic;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class LCS {
    public static void main(String[] args) {
        // 给定两个字符串str1和str2，输出两个字符串的最长公共子序列。如果最长公共子序列为空，则返回"-1"。目前给出的数据，仅仅会存在一个最长的公共子序列
        String s1 = "1A2C3D4B56";
        String s2 = "B1D23A456A";
        // 返回 123456
        System.out.println(test(s1, s2));
        System.out.println(test("3321322", "11344322"));
        System.out.println(test(s1, "1A2C3B56"));
        System.out.println(test("32", "31"));

    }

    /**
     * longest common subsequence
     *
     * @param s1 string字符串 the string
     * @param s2 string字符串 the string
     * @return string字符串
     */
    public static String test(String s1, String s2) {

        // 得出不同长度的数组的最长公共子序列长度
        int[][] r = new int[s1.length()][s2.length()];
        r[0][0] = s1.charAt(0) == s2.charAt(0) ? 1 : 0;
        r[1][0] = s1.charAt(1) == s2.charAt(0) ? 1 : 0;
        r[0][1] = s1.charAt(0) == s2.charAt(1) ? 1 : 0;
        // r[i][j] = r[i-1][j-1] + 1 / r[i-1][j]  r[i][j-1] /
        for (int i = 1; i < s1.length(); i++) {
            for (int j = 1; j < s2.length(); j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    r[i][j] = r[i - 1][j - 1] + 1;
                    continue;
                }
                // 长度递推
                r[i][j] = Math.max(r[i - 1][j], r[i][j - 1]);
            }
        }
        // 反向构建最长公共子序列
        StringBuilder lcs = new StringBuilder();
        int i = s1.length() - 1, j = s2.length() - 1;
        while (i >= 0 && j >= 0) {
            if (s1.charAt(i) == s2.charAt(j)) {
                lcs.insert(0, s1.charAt(i));
                i--;
                j--;
                continue;
            }
            if (i == 0) {
                j--;
                continue;
            }
            if (j == 0) {
                i--;
                continue;
            }
            if (r[i - 1][j] > r[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        // 如果最长公共子序列为空，则返回"-1"
        if (lcs.length() == 0) {
            return "-1";
        }
        return lcs.toString();
    }


}


