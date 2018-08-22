package com.lovely3x.common.utils;

/**
 * gps信号等级
 */
public enum GPSSignalLevel {
    /**
     * 无gps信号
     */
    non(-1),
    /**
     * 差
     */
    low(1),
    /**
     * 中
     */
    middle(2),
    /**
     * 优秀
     */
    high(3);

    GPSSignalLevel(int value) {

    }
}