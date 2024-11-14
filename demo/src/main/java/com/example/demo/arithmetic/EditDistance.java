package com.example.demo.arithmetic;

/**
 * @author LJP
 * @create 2024/10/16 10:02
 */
public class EditDistance {
    public static void main(String[] args) {
        // 给定两个字符串 str1 和 str2 ，请你算出将 str1 转为 str2 的最少操作数。你可以对字符串进行3种操作：
        //1.插入一个字符
        //2.删除一个字符
        //3.修改一个字符。

        // "nowcoder","new"     ，6
        System.out.println(test("new", "nowcoder"));
        System.out.println(test("newrs", "ewrsnowrsen"));
        System.out.println(test("newrs", "srs"));
        System.out.println(test("srs", "newrs"));
    }

    public static int test(String s1, String s2) {
        int[][] r = new int[s1.length()][s2.length()];
        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++) {
                int i1 = Math.max(i - 1, 0);
                int j1 = Math.max(j - 1, 0);
                if(s1.charAt(i)==s2.charAt(j)){
                    r[i][j]=r[i1][j1];
                    continue;
                }
                if(i==0){
                    r[i][j]=r[i][j1]+1;
                    continue;
                }
                if(j==0){
                    r[i][j]=r[i1][j]+1;
                    continue;
                }
                r[i][j]=Math.min(Math.min(r[i1][j],r[i][j1]),r[i1][j1])+1;
            }
        }
        return r[s1.length()-1][s2.length()-1];
    }


}




