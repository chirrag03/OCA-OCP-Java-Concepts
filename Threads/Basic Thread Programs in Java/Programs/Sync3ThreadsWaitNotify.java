package threads;

class LetterTask implements Runnable{

	String data;
	int maxLimit;
	
	static String[] order = {"a","b","c"};
	static int current = 0;
	static Object lock = new Object();
	
	public LetterTask(String data, int maxLimit) {
		super();
		this.data = data;
		this.maxLimit = maxLimit;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		for(int i=0;i<maxLimit;i++) {
			synchronized (lock) {
				while(!order[current].equals(data)) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.print(data);
				current = (current + 1) % 3;
				lock.notifyAll();
			}
		}
	}
	
}
public class Sync3ThreadsWaitNotify {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Thread t1 = new Thread(new LetterTask("a", 5));
		Thread t2 = new Thread(new LetterTask("b", 5));
		Thread t3 = new Thread(new LetterTask("c", 5));
		
		t1.start();
		t2.start();
		t3.start();
	}

}
