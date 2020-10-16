## Sync 3 Threads Using Semaphores

> **Constraints:**    
> Three different threads where one thread prints "a", second thread prints "b", third thread prints "c"     
> They should print in a sequential manner as abcabcabc....  


```java
package Sync3Threads;

import java.util.concurrent.Semaphore;

public class Main {

  public static void main(String[] args){
    Semaphore semaphoreA = new Semaphore(1);
    Semaphore semaphoreB = new Semaphore(0);
    Semaphore semaphoreC = new Semaphore(0);

    Task taskA = new Task('a', semaphoreA, semaphoreB);
    Task taskB = new Task('b', semaphoreB, semaphoreC);
    Task taskC = new Task('c', semaphoreC, semaphoreA);

    new Thread(taskB).start();
    new Thread(taskC).start();
    new Thread(taskA).start();
  }
}

class Task implements Runnable{

  private Character c;
  private Semaphore prev;
  private Semaphore next;

  public Task(Character c, Semaphore prev, Semaphore next){
    this.c = c;
    this.prev = prev;
    this.next = next;
  }

  @Override
  public void run() {
    for (int i=0;i<5;i++){
      try {
        prev.acquire();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(c);
      next.release();
    }
  }
}

```  


