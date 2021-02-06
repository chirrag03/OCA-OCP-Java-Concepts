package threads;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue<T> {

	Queue<T> queue;
	int maxLimit;
	
	public BlockingQueue(int maxLimit) {
		super();
		this.queue = new LinkedList<>();
		this.maxLimit = maxLimit;
	}
	
	public void add(T data) throws InterruptedException {
		synchronized (this) {
			while(queue.size() >= maxLimit) {
				wait();
			}
			queue.add(data);
			notifyAll();
		}
	}
	
	public T poll() throws InterruptedException {
		synchronized (this) {
			while(queue.size() == 0) {
				wait();
			}
			T data = queue.poll();
			notifyAll();
			return data;
		}
	}
	
	public int size() {
		return queue.size();
	}
}
