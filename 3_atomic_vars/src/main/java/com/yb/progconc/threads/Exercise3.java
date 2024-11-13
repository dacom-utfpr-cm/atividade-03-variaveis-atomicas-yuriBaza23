package com.yb.progconc.threads;

import java.util.concurrent.atomic.AtomicReference;

public class Exercise3<T> {

    private static class Node<T> {
        final T value;
        final AtomicReference<Node<T>> next;

        Node(T value) {
            this.value = value;
            this.next = new AtomicReference<>(null);
        }
    }

    private final AtomicReference<Node<T>> head;
    private final AtomicReference<Node<T>> tail;

    public Exercise3() {
        Node<T> dummy = new Node<>(null); // Nó sentinela
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
    }

    // Adicionar elemento
    public void enqueue(T value) {
        Node<T> newNode = new Node<>(value);
        Node<T> currentTail;

        while (true) {
            currentTail = tail.get();
            Node<T> nextNode = currentTail.next.get();

            if (nextNode == null) {
                if (currentTail.next.compareAndSet(null, newNode)) {
                    tail.compareAndSet(currentTail, newNode);
                    return;
                }
            } else {
                tail.compareAndSet(currentTail, nextNode);
            }
        }
    }

    // Remover elemento
    public T dequeue() {
        Node<T> currentHead;
        Node<T> currentTail;
        Node<T> nextNode;

        while (true) {
            currentHead = head.get();
            currentTail = tail.get();
            nextNode = currentHead.next.get();

            if (nextNode == null) {
                return null; // A fila está vazia
            }

            if (currentHead == currentTail) {
                tail.compareAndSet(currentTail, nextNode);
            } else {
                if (head.compareAndSet(currentHead, nextNode)) {
                    return nextNode.value;
                }
            }
        }
    }

    public boolean isEmpty() {
        return head.get().next.get() == null;
    }

    public static void main(String[] args) {
        Exercise3<Integer> queue = new Exercise3<>();

        for (int i = 0; i < 10; i++) {
            final int value = i;
            new Thread(() -> {
                queue.enqueue(value);
                System.out.println("Enqueued: " + value);
            }).start();
        }

        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Integer value = queue.dequeue();
                System.out.println("Dequeued: " + value);
            }).start();
        }
    }
}
