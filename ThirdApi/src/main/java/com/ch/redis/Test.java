package com.ch.redis;

import java.util.HashMap;

/**
 * @author chenghao
 * @purpose：
 * @备注：
 * @data 2023年03月01日 10:34
 */
public class Test {
    public static void main(String[] args) {
        HashMap<String,String> map = new HashMap<>();
        map.put("1","ha");
        String s = map.get("2");
        System.out.println(s);

    }
}
