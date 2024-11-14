package com.example.demo.arithmetic;

import java.util.HashMap;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class Solve {
    public static void main(String[] args) {
        // 有一种将字母编码成数字的方式：'a'->1, 'b->2', ... , 'z->26'。现在给一串数字，返回有多少种可能的译码结果

        System.out.println(test("12"));
        System.out.println(test("131"));
        System.out.println(test("1311"));
        System.out.println(test("31717126241541717"));
    }

    public static int test(String num) {
        int limit = 26;
        // 新增一个数字，只会和前一个做结合
        // f("x*") = f（"x*-2"） // f("x*-1")
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < num.length(); i++) {
            if (i == 0) {
                map.put(i, 1);
                continue;
            }
            if ((Integer.valueOf(String.valueOf(num.charAt(i - 1))) * 10 + Integer.valueOf(String.valueOf(num.charAt(i)))) > limit) {
                map.put(i, map.get(i - 1));
                continue;
            }
            if (i < 2) {
                map.put(i, 2);
                continue;
            }
            map.put(i, map.get(i - 1) + map.get(i - 2));
        }
        return map.get(num.length() - 1);
    }


}


