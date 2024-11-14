package com.example.demo.arithmetic;




import java.util.HashMap;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class JumpFloor {
    public static void main(String[] args) {
        // 一只青蛙一次可以跳上1级台阶，也可以跳上2级。求该青蛙跳上一个 n 级的台阶总共有多少种跳法（先后次序不同算不同的结果）
        System.out.println(test(4));
        System.out.println(test(5));
    }

    public static int test(int n) {
        // 1 -- 1
        // 2 -- 1,1 / 2
        // 3 -- 1,1,1 / 1,2 / 2,1
        // 4 -- 1,1,1,1 / 1,2,1 / 2,1,1 / 1,1,2 / 2,2
        // 5 -- 1,1,1,1,1 / 1,2,1,1 / 1,1,2,1 / 2,1,1,1 / 1,1,1,2 / 2,2,1 / 1,2,2 / 2,1,2
        // 1 2 3 5 8
        // 跨2步，则第n步，的时候，n-2 个位置有f(n-2)种，n-1 个位置有f(n-1)种。这两种最终只需要再踏一步即可，也就是包含了之前的两种情况
        // 斐波那契 数列 f(n) = f(n-1) + f(n-2)
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 1; i < n + 1; i++) {
            if (i == 1) {
                map.put(i, 1);
                continue;
            }
            if (i == 2) {
                map.put(i, 2);
                continue;
            }
            map.put(i, map.get(i - 1) + map.get(i - 2));
        }
        return map.get(n);
    }


}


