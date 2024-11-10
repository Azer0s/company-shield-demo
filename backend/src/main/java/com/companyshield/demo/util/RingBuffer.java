package com.companyshield.demo.util;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RingBuffer<T> {
    private final List<T> buffer;
    private final int size;

    public RingBuffer(int size) {
        this.size = size;
        this.buffer = new ArrayList<>(size);
    }

    public void add(T element) {
        if (buffer.size() == size) {
            buffer.removeFirst();
        }
        buffer.add(element);
    }
}
