## Producer Consumer Using Blocking Queue

> **Constraints:**    
> Producer thread produce a new resource in every 1 second and put it in a blocking queue.    
> Consumer thread takes 1 seconds to process consumed resource from blocking queue.    
> Max capacity of taskQueue is 5000 i.e. maximum 5000 resources can exist inside ‘taskQueue’ at any given time.     
> Both threads run infinitely.  


```java
package ProducerConsumerBlockingQueue;

import java.util.LinkedList;
import java.util.Queue;

public class Main {

  public static void main(String[] args){
    BlockingQueue<Integer> queue = new BlockingQueue<>(5000);

    Producer producer = new Producer(queue);
    Consumer consumer = new Consumer(queue);

    new Thread(producer).start();
    new Thread(consumer).start();
  }
}


class Producer implements Runnable{

  private final BlockingQueue<Integer> taskQueue;

  public Producer(BlockingQueue<Integer> taskQueue){
    this.taskQueue = taskQueue;
  }

  @Override
  public void run() {

    int counter = 0;
    while (true){
      try {
        produce(counter++);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void produce(int itemNum) throws InterruptedException {
    Thread.sleep(100);
    System.out.println("Producing item " + itemNum);
    taskQueue.enqueue(itemNum);
  }
}

class Consumer implements Runnable{

  private final BlockingQueue<Integer> taskQueue;

  public Consumer(BlockingQueue<Integer> taskQueue){
    this.taskQueue = taskQueue;
  }

  @Override
  public void run() {

    while (true){
      try {
        consume();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }

  private void consume() throws InterruptedException {
    Thread.sleep(1000);
    System.out.println("Consuming item " + taskQueue.dequeue());
  }
}

class BlockingQueue<T> {

  private Queue<T> queue;
  private int capacity;

  public BlockingQueue(int capacity){
    this.queue = new LinkedList<>();
    this.capacity = capacity;
  }

  public synchronized void enqueue(T item) throws InterruptedException {
    while (this.queue.size() >= capacity){
      wait();
    }
    queue.add(item);
    if(this.queue.size() == 1) {
      notifyAll();
    }
  }

  public synchronized T dequeue() throws InterruptedException {
    while (this.queue.size() == 0){
      wait();
    }
    T data = queue.poll();
    if(this.queue.size() == this.capacity){
      notifyAll();
    }
    return data;
  }
}

/*
Notice how notifyAll() is only called from enqueue() and dequeue() if the queue size is equal to the size bounds (0 or limit). 
If the queue size is not equal to either bound when enqueue() or dequeue() is called, there can be no threads waiting to either enqueue or dequeue items.
*/


```  


