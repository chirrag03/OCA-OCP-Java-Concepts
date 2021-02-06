package threads;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Producer implements Runnable{
	
	String name;
	Queue<Integer> sharedQueue;
	int maxLimit;
	
	public Producer(String name, Queue<Integer> queue, int limit) {
		this.name = name;
		sharedQueue = queue;
		maxLimit = limit;
	}
	
	@Override
	public void run() {
		for(int i=1;i<=maxLimit;i++) {
			synchronized (sharedQueue) {
				while(sharedQueue.size() == maxLimit) {
					try {
						sharedQueue.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(name + " : Producing Item :" + i);
				sharedQueue.add(i);
				sharedQueue.notifyAll();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class Consumer implements Runnable{
	
	String name;
	Queue<Integer> sharedQueue;
	
	public Consumer(String name, Queue<Integer> queue) {
		this.name = name;
		sharedQueue = queue;
	}
	
	@Override
	public void run() {
		while(true) {
			synchronized (sharedQueue) {
				while(sharedQueue.size() == 0) {
					try {
						sharedQueue.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(name + " : Consuming Item :" + sharedQueue.poll());
				sharedQueue.notifyAll();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
public class ProducerConsumerProblem {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Queue<Integer> sharedQueue = new LinkedList<Integer>();

		ExecutorService executor = Executors.newFixedThreadPool(5);
		executor.submit(new Producer("Producer 1", sharedQueue, 20));
		executor.submit(new Consumer("Consumer 1", sharedQueue));
		executor.submit(new Consumer("Consumer 2", sharedQueue));
		executor.submit(new Consumer("Consumer 3", sharedQueue));

//		Thread p1 = new Thread(new Producer("Producer 1", sharedQueue, 10));
//		Thread c1 = new Thread(new Consumer("Consumer 1", sharedQueue));
//		Thread c2 = new Thread(new Consumer("Consumer 2", sharedQueue));
//		Thread c3 = new Thread(new Consumer("Consumer 3", sharedQueue));
//		
//		p1.start();
//		c1.start();
//		c2.start();
//		c3.start();
		
	}

}
