## Reader Writer

> **Constraints:**    
> Multiple readers can read if no one is writing, and no writers are waiting to lock for writing.    
> Only one writer can write and that too only if no threads are currently reading.  


```java
package ReaderWriter;

public class Main {

  public static void main(String[] args) {
    Book b = new Book("Jungle book", "Intro.");

    ReadWriteBook readWriteBook = new ReadWriteBook(b);
    for (int i=0;i<3;i++){
      new Thread(() -> {
        try {
          readWriteBook.read();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    }

    new Thread(() -> {
      try {
        readWriteBook.write();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();

    for (int i=0;i<3;i++){
      new Thread(() -> {
        try {
          readWriteBook.read();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    }

  }
}


class Book {

  String name;
  String text;

  public Book(String name, String text) {
    this.name = name;
    this.text = text;
  }
}

class ReadWriteBook {

  private ReadWriteLock readWriteLock;
  private Book b;

  public ReadWriteBook(Book b){
    this.b = b;
    this.readWriteLock = new ReadWriteLock();
  }

  public void read() throws InterruptedException {
    readWriteLock.lockRead();

    // multiple readers can enter this section
    // if not locked for writing, and not writers waiting
    // to lock for writing.
    System.out.println("Reading");
    System.out.println(b.text);
    Thread.sleep(10000);
    System.out.println("Finished Reading");
    readWriteLock.unlockRead();
  }

  public void write() throws InterruptedException {
    readWriteLock.lockWrite();

    // only one writer can enter this section,
    // and only if no threads are currently reading.
    System.out.println("Writing");
    b.text = b.text + " This is the next chapter.";
    Thread.sleep(10000);
    System.out.println("Finished Writing");

    readWriteLock.unlockWrite();
  }
}

class ReadWriteLock{

  private int readers       = 0;
  private int writers       = 0;
  private int writeRequests = 0;

  public synchronized void lockRead() throws InterruptedException{
    while(writers > 0 || writeRequests > 0){
      wait();
    }
    readers++;
  }

  public synchronized void unlockRead(){
    readers--;
    notifyAll();
  }

  public synchronized void lockWrite() throws InterruptedException{
    writeRequests++;

    while(readers > 0 || writers > 0){
      wait();
    }
    writeRequests--;
    writers++;
  }

  public synchronized void unlockWrite() throws InterruptedException{
    writers--;
    notifyAll();
  }
}

```  

**Links To Read:**  
http://tutorials.jenkov.com/java-util-concurrent/readwritelock.html  
http://tutorials.jenkov.com/java-concurrency/read-write-locks.html
