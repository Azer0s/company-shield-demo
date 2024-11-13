package com.companyshield.demo;

import com.companyshield.demo.util.RingBuffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RingBufferTest {

    @Test
    void testRingBuffer() {
        var ringBuffer = new RingBuffer<Integer>(3);

        ringBuffer.add(1);
        ringBuffer.add(2);
        ringBuffer.add(3);
        ringBuffer.add(4);
        ringBuffer.add(5);

        Assertions.assertEquals(List.of(4, 5, 3), ringBuffer.getBuffer());
    }
}
