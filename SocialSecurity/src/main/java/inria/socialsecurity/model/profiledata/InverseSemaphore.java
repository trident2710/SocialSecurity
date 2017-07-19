/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.profiledata;

/**
 *
 * @author adychka
 */
class InverseSemaphore {
    private int value = 0;
    private final Object lock = new Object();

    public void beforeSubmit() {
        synchronized(lock) {
            value++;
        }
    }

    public void taskCompleted() {
        synchronized(lock) {
            value--;
            if (value == 0) lock.notifyAll();
        }
    }

    public void awaitCompletion() throws InterruptedException {
        synchronized(lock) {
            while (value > 0) lock.wait();
        }
    }
    
    public void awaitSubtaskCompletion() throws InterruptedException {
        synchronized(lock) {
            while (value > 1) lock.wait();
        }
    }

    public int getValue() {
        return value;
    }
}
