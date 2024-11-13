package com.yb.progconc.threads;

import java.util.concurrent.atomic.AtomicReference;

public class Exercise2<T> {

    private static class Node<T> {
        final T value;
        Node<T> next;

        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    private final AtomicReference<Node<T>> top = new AtomicReference<>(null);

    // Para adicionar elementos
    public void push(T value) {
        Node<T> newNode = new Node<>(value, null);
        Node<T> currentTop;

        do {
            currentTop = top.get();
            newNode.next = currentTop;
        } while (!top.compareAndSet(currentTop, newNode));
    }

    // Para remover elementos
    public T pop() {
        Node<T> currentTop;
        Node<T> newTop;

        do {
            currentTop = top.get();
            if (currentTop == null) {
                return null;
            }
            newTop = currentTop.next;
        } while (!top.compareAndSet(currentTop, newTop));

        return currentTop.value;
    }

    public boolean isEmpty() {
        return top.get() == null;
    }

    public static void main(String[] args) {
        Exercise2<Integer> stack = new Exercise2<>();

        // Multiplas threads para teste
        for (int i = 0; i < 10; i++) {
            final int value = i;
            new Thread(() -> {
                stack.push(value);
                System.out.println("Add: " + value);
            }).start();
        }

        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Integer value = stack.pop();
                System.out.println("Pop: " + value);
            }).start();
        }
    }
}

