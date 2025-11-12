package com.example.fe.myapplication.model;

import java.io.Serializable;

/**
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
public class PaginationEn implements Serializable {
    public PaginationEn() {
    }

    public PaginationEn(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }

    /**
     * count : 9
     * offset : 0
     * length : 20
     */

    private int count;

    private int offset;

    private int length;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
