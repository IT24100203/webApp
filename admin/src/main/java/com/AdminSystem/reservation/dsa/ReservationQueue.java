package com.AdminSystem.reservation.dsa;

import com.AdminSystem.reservation.model.Reservation;

public class ReservationQueue {

    // Node class for linked list
    private static class Node {
        Reservation data;
        Node next;

        Node(Reservation data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node front;
    private Node rear;
    private int size;

    public ReservationQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    // Enqueue operation
    public void enqueue(Reservation reservation) {
        Node newNode = new Node(reservation);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    // Dequeue operation
    public Reservation dequeue() {
        if (front == null) {
            return null;
        }
        Reservation data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    // Size of the queue
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return front == null;
    }

    // Convert queue to array (for output/view only)
    public Reservation[] toArray() {
        Reservation[] array = new Reservation[size];
        Node current = front;
        int i = 0;
        while (current != null) {
            array[i++] = current.data;
            current = current.next;
        }
        return array;
    }
}
