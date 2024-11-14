package com.example.demo.arithmetic;



import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class Fibonacci {
    public static void main(String[] args) {
        // 1 1 2 3 5 8
        System.out.println(test(40));
        System.out.println(test1(40));

    }

    public static int test(int n) {
        HashMap<Integer, Integer> map = new HashMap<>();
        // 斐波那契 f(i) = f(i-1) + f(i-2)
        for (int i = 1; i < n + 1; i++) {
            if (i <= 2) {
                map.put(i, 1);
                continue;
            }
            map.put(i, map.get(i - 1) + map.get(i - 2));
        }
        return map.get(n);
    }

    public static int test1(int n) {
        LinkedList<Integer> linkedList = Lists.newLinkedList();
        for (int i = 1; i < n + 1; i++) {
            if (i <= 2) {
                linkedList.add(1);
                continue;
            }
            if (i == 3) {
                linkedList.add(2);
                continue;
            }
            linkedList.add(linkedList.get(1) + linkedList.get(2));
            linkedList.removeFirst();
        }
        return linkedList.getLast();
    }


}


