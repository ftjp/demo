package com.example.demo.arithmetic;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class LCS2 {
    public static void main(String[] args) {
        // 给定两个字符串str1和str2,输出两个字符串的最长公共子串,保证str1和str2的最长公共子串存在且唯一。
        String s1 = "1AB2345CD";
        String s2 = "12345EF";
        // 返回 2345
        System.out.println(test(s1, s2));
        System.out.println(test("3321322", "11344322"));
        System.out.println(test(s1, "1A2C3B56"));
        System.out.println(test("32", "31"));
        System.out.println(test("32123", "3213212"));
    }

    /**
     * longest common subsequence
     *
     * @param s1 string字符串 the string
     * @param s2 string字符串 the string
     * @return string字符串
     */
    public static String test(String s1, String s2) {
        // s1 是短的
        if (s1.length() > s2.length()) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
        }
        String substring = "";
        for (int i = s1.length(); i > 0; i--) {
            for (int j = 0; j < s1.length() - i + 1; j++) {
                substring = s1.substring(j, j + i);
                if (s2.contains(substring)) {
                    return substring;
                }
            }
        }
        return substring;
    }

}


