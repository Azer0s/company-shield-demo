package com.companyshield.demo.util;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class RingBuffer<T> {
    private final T[] buffer;
    private int head;

    public RingBuffer(int size) {
        this.head = 0;

        @SuppressWarnings("unchecked")
        final var buffer = (T[]) new Object[size];

        this.buffer = buffer;
    }

    public void add(T element) {
        if (head == buffer.length) {
            head = 0;
        }

        buffer[head++] = element;
    }

    public List<T> getBuffer() {
        return Arrays.asList(buffer);
    }
}
