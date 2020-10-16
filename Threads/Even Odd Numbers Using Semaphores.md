## Even Odd Numbers Using Semaphores

> **Constraints:**    
> A thread prints odd numbers  
> Another thread prints even numbers  
> They should print in a sequential manner   


```java
package OddEvenNumbers;

import java.util.concurrent.Semaphore;

public class Main {

  public static void main(String[] args){

    Semaphore oddSemaphore = new Semaphore(1);
    Semaphore evenSemaphore = new Semaphore(0);

    PrintOddNum printOddNum = new PrintOddNum(10, oddSemaphore, evenSemaphore);
    PrintEvenNum printEvenNum = new PrintEvenNum(10, oddSemaphore, evenSemaphore);

    new Thread(printOddNum).start();
    new Thread(printEvenNum).start();
  }
}

class PrintOddNum implements Runnable {

  private int max;

  private Semaphore oddSemaphore;
  private Semaphore evenSemaphore;

  public PrintOddNum(int max, Semaphore oddSemaphore, Semaphore evenSemaphore){
    this.max = max;
    this.oddSemaphore = oddSemaphore;
    this.evenSemaphore = evenSemaphore;
  }

  @Override
  public void run(){
    for(int i=1;i<=max;i=i+2){
      try {
        oddSemaphore.acquire();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(i);
      evenSemaphore.release();
    }
  }
}

class PrintEvenNum implements Runnable {

  private int max;

  private Semaphore oddSemaphore;
  private Semaphore evenSemaphore;

  public PrintEvenNum(int max, Semaphore oddSemaphore, Semaphore evenSemaphore){
    this.max = max;
    this.oddSemaphore = oddSemaphore;
    this.evenSemaphore = evenSemaphore;
  }

  @Override
  public void run(){
    for(int i=2;i<=max;i=i+2){
      try {
        evenSemaphore.acquire();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(i);
      oddSemaphore.release();
    }
  }
}

```  


