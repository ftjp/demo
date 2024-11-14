package com.example.demo.arithmetic;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class Rob2 {
    public static void main(String[] args) {
        // 你是一个经验丰富的小偷，准备偷沿湖的一排房间，每个房间都存有一定的现金，为了防止被发现，你不能偷相邻的两家，
        // 即，如果偷了第一家，就不能再偷第二家，如果偷了第二家，那么就不能偷第一家和第三家。沿湖的房间组成一个闭合的圆形，即第一个房间和最后一个房间视为相邻。
        //给定一个长度为n的整数数组nums，数组中的元素表示每个房间存有的现金数额，请你计算在不被发现的前提下最多的偷窃金额。
        // [1,2,3,4]  6
//        System.out.println(test(new int[]{1, 2, 3, 4}));
//        System.out.println(test(new int[]{1, 2, 0, 1}));
//        System.out.println(test(new int[]{1, 2, 0, 5, 7}));
        System.out.println(test(new int[]{2, 0, 1, 0, 0, 1, 0, 5, 7, 6}));
    }

    public static int test(int[] arr) {
        // 简化 f（x） = max(f(x-1),f（x-2）+ arr[x])
        int[] amount = new int[arr.length];
        amount[0] = arr[0];
        amount[1] = Math.max(arr[0], arr[1]);
//        amount[2] = Math.max(arr[1], arr[0] + arr[2]);
        for (int i = 2; i < arr.length; i++) {
            amount[i] = Math.max(amount[i - 1], amount[i - 2] + arr[i]);
        }
        if (amount[arr.length - 2] >= amount[arr.length - 1]) {
            return amount[arr.length - 2];
        }
        boolean haveFirst = false;
        int max = amount[arr.length - 1];
        for (int i = 1; i < arr.length + 1; i++) {
            if (amount[arr.length - i] == max) {
                if (i == arr.length) {
                    haveFirst = true;
                    break;
                }
                if (amount[arr.length - i] == amount[arr.length - 1 - i]) {
                    continue;
                }
                max = max - arr[arr.length - i];
            }
        }
        if (!haveFirst) {
            return amount[arr.length - 1];
        }

        if (amount[1] == amount[2]) {
            return amount[arr.length - 1];
        }

        return Math.max(amount[arr.length - 1] - arr[0], amount[arr.length - 2]);
    }


//    public int rob(int[] nums) {
//        //dp[i]表示长度为i的数组，最多能偷取多少钱
//        int[] dp = new int[nums.length + 1];
//        //选择偷了第一家
//        dp[1] = nums[0];
//        //最后一家不能偷
//        for (int i = 2; i < nums.length; i++)
//            //对于每家可以选择偷或者不偷
//            dp[i] = Math.max(dp[i - 1], nums[i - 1] + dp[i - 2]);
//        int res = dp[nums.length - 1];
//        //清除dp数组，第二次循环
//        Arrays.fill(dp, 0);
//        //不偷第一家
//        dp[1] = 0;
//        //可以偷最后一家
//        for (int i = 2; i <= nums.length; i++)
//            //对于每家可以选择偷或者不偷
//            dp[i] = Math.max(dp[i - 1], nums[i - 1] + dp[i - 2]);
//        //选择最大值
//        return Math.max(res, dp[nums.length]);
//    }


}




