package com.ch.redis;

import org.apache.poi.ss.formula.functions.T;

import javax.xml.crypto.Data;

/**
 * @author chenghao
 * @purpose：
 * @备注：
 * @data 2023年02月28日 22:25
 */
public class ObjectVO<T> {
    private T targetObj;
    private String time;

    public ObjectVO() {
    }

    public ObjectVO(T targetObj, String time) {
        this.targetObj = targetObj;
        this.time = time;
    }

    public T getTargetObj() {
        return targetObj;
    }

    public void setTargetObj(T targetObj) {
        this.targetObj = targetObj;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
