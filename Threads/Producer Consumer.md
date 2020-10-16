## Producer Consumer

> **Constraints:**    
> Producer thread produce a new resource in every 1 second and put it in ‘taskQueue’.    
> Consumer thread takes 1 seconds to process consumed resource from ‘taskQueue’.    
> Max capacity of taskQueue is 5000 i.e. maximum 5000 resources can exist inside ‘taskQueue’ at any given time.     
> Both threads run infinitely.  


```java
package ProducerConsumer;

import java.util.LinkedList;
import java.util.Queue;

public class Main {

  public static void main(String[] args){
    Queue<Integer> queue = new LinkedList<>();

    Producer producer = new Producer(queue, 5000);
    Consumer consumer = new Consumer(queue);

    new Thread(producer).start();
    new Thread(consumer).start();
  }
}

class Producer implements Runnable{

  private final Queue<Integer> taskQueue;
  private final Integer maxCapacity;

  public Producer(Queue<Integer> taskQueue, int maxCapacity){
    this.taskQueue = taskQueue;
    this.maxCapacity = maxCapacity;
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
    synchronized (taskQueue){
      while (taskQueue.size() >= maxCapacity){
        taskQueue.wait();
      }

      Thread.sleep(100);
      System.out.println("Producing item " + itemNum);
      taskQueue.add(itemNum);
      taskQueue.notifyAll();
    }
  }
}

class Consumer implements Runnable{

  private final Queue<Integer> taskQueue;

  public Consumer(Queue<Integer> taskQueue){
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
    synchronized (taskQueue){
      while (taskQueue.size() == 0){
        taskQueue.wait();
      }

      Thread.sleep(100);
      System.out.println("Consuming item " + taskQueue.poll());
      taskQueue.notifyAll();
    }
  }
}
```  


