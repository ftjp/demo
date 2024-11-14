package com.example.demo.arithmetic;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class Rob {
    public static void main(String[] args) {
        // 你是一个经验丰富的小偷，准备偷沿街的一排房间，每个房间都存有一定的现金，为了防止被发现，你不能偷相邻的两家，
        // 即，如果偷了第一家，就不能再偷第二家；如果偷了第二家，那么就不能偷第一家和第三家。
        // 给定一个整数数组nums，数组中的元素表示每个房间存有的现金数额，请你计算在不被发现的前提下最多的偷窃金额。
        // [1,2,3,4]  6
        System.out.println(test(new int[]{1, 2, 3, 4}));
        System.out.println(test(new int[]{1, 2, 0, 1}));
        System.out.println(test(new int[]{1, 2, 0, 5, 7}));
    }

    public static int test(int[] arr) {
        // 简化 f（x） = max(f(x-1),f（x-2）+ arr[x])
        int max = 0;
        int[] amount = new int[arr.length];
        amount[0] = arr[0];
        amount[1] = Math.max(arr[0], arr[1]);
        amount[2] = Math.max(arr[1], arr[0] + arr[2]);
        for (int i = 3; i < arr.length; i++) {
            amount[i] = Math.max(amount[i - 1], amount[i - 2] + arr[i]);
            max = Math.max(max, amount[i]);
        }
        return max;
    }


}




