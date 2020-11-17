package threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Producer1 implements Runnable{
	
	String name;
	BlockingQueue<Integer> sharedQueue;
	int maxLimit;
	
	public Producer1(String name, BlockingQueue<Integer> queue, int limit) {
		this.name = name;
		sharedQueue = queue;
		maxLimit = limit;
	}
	
	@Override
	public void run() {
		for(int i=1;i<=maxLimit;i++) {
				
				System.out.println(name + " : Producing Item :" + i);
				try {
					sharedQueue.add(i);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}

class Consumer1 implements Runnable{
	
	String name;
	BlockingQueue<Integer> sharedQueue;
	
	public Consumer1(String name, BlockingQueue<Integer> queue) {
		this.name = name;
		sharedQueue = queue;
	}
	
	@Override
	public void run() {
		while(true) {
				
			try {
				System.out.println(name + " : Consuming Item :" + sharedQueue.poll());
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
public class ProducerConsumerUsingBlockingQueue {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BlockingQueue<Integer> sharedQueue = new BlockingQueue<Integer>(5);

		ExecutorService executor = Executors.newFixedThreadPool(5);
		executor.submit(new Producer1("Producer 1", sharedQueue, 20));
		executor.submit(new Consumer1("Consumer 1", sharedQueue));
		executor.submit(new Consumer1("Consumer 2", sharedQueue));
		executor.submit(new Consumer1("Consumer 3", sharedQueue));

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
