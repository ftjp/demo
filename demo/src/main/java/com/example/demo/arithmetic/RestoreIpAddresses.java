package com.example.demo.arithmetic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class RestoreIpAddresses {
    public static void main(String[] args) {
        // 现在有一个只包含数字的字符串，将该字符串转化成IP地址的形式，返回所有可能的情况。
        // 给出的字符串为"25525522135",返回["255.255.22.135", "255.255.221.35"]. (顺序没有关系)
        // ip地址是由四段数字组成的数字序列，格式如 "x.x.x.x"，其中 x 的范围应当是 [0,255]。
        System.out.println(test("25525522135"));
    }

    public static List<String> test(String arr) {
        List<String> result = new ArrayList<>();
        if (arr.length() < 4 || arr.length() > 12) {
            return result;
        }
        for (int i = 1; i < 4; i++) {
            if ("0".equals(String.valueOf(arr.charAt(0))) && i > 1) {
                continue;
            }
            for (int j = 1; j < 4; j++) {
                if ("0".equals(String.valueOf(arr.charAt(i))) && j > 1) {
                    continue;
                }
                for (int k = 1; k < 4; k++) {
                    if ("0".equals(String.valueOf(arr.charAt(i + j))) && k > 1) {
                        continue;
                    }
                    int l = arr.length() - i - j - k;
                    if (l < 1 || l > 3) {
                        continue;
                    }
                    String str1 = arr.substring(0, i);
                    if (Integer.parseInt(str1) > 255) {
                        continue;
                    }
                    String str2 = arr.substring(i, i + j);
                    if (Integer.parseInt(str2) > 255) {
                        continue;
                    }
                    String str3 = arr.substring(i + j, i + j + k);
                    if (Integer.parseInt(str3) > 255) {
                        continue;
                    }
                    String str4 = arr.substring(i + j + k, i + j + k + l);
                    if (Integer.parseInt(str3) > 255) {
                        continue;
                    }
                    result.add(str1 + "." + str2 + "." + str3 + "." + str4);
                }
            }

        }

        return result;
    }


}




