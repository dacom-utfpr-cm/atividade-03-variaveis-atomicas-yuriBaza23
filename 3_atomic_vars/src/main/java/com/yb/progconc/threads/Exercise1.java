package com.yb.progconc.threads;

import java.util.concurrent.atomic.AtomicLong;

public class Exercise1 {

    private final AtomicLong counter;

    public Exercise1(long initialValue) {
        this.counter = new AtomicLong(initialValue);
    }

    public long next() {
        long current;
        long next;
        do {
            current = counter.get();
            next = current + 1;
        } while (!counter.compareAndSet(current, next));

        return next;
    }

    public static void main(String[] args) {
        Exercise1 generator = new Exercise1(0);

        // Usando várias threads
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println("Generated: " + generator.next());
            }).start();
        }
    }
}
