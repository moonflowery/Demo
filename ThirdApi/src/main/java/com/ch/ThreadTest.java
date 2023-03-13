package com.ch;

import org.apache.poi.ss.formula.functions.T;

/**
 * @author chenghao
 * @purpose：
 * @备注：
 * @data 2023年03月10日 02:18
 */
public class ThreadTest {
    public static void main(String[] args) {
        ThreadTest test  = new ThreadTest();
        for (int i = 0; i < 5; i++) {
            Thread a= new Runnable() -> {
                int next = test.getNext();
                System.out.println(next);
           //todo
            });

        }

    }
    private volatile int count = 0;

    public int getNext() {
        return ++count;
    }
}
