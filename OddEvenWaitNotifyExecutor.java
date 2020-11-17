package threads;

class PrintEvenOddTask1 implements Runnable{

	NumType numType;
	int maxLimit;

	static boolean printOdd = true;
	static Object lock = new Object();

	public PrintEvenOddTask1(NumType numType, int maxLimit) {
		this.numType = numType;
		this.maxLimit = maxLimit;
	}

	@Override
	public void run() {
		if(NumType.ODD.equals(numType)) {
			printOddNumbers();
		}else {
			printEvenNumbers();
		}
	}
	
	public void printOddNumbers() {
		for(int i=1;i<=maxLimit;i++) {
			
			synchronized (lock) {
				while(!printOdd) {
					waitForNotify();
				}
				
				System.out.println(2*i - 1);
				printOdd = false;
				lock.notify();
				
				sleepFor5();
			}
		}
	}
	
	public void printEvenNumbers() {
		for(int i=1;i<=maxLimit;i++) {
			
			synchronized (lock) {
				while(printOdd) {
					waitForNotify();
				}
				
				System.out.println(2*i);
				printOdd = true;
				lock.notify();
				
				sleepFor5();
			}
		}
	}
	
	private void waitForNotify() {
		try {
			lock.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void sleepFor5() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


public class OddEvenWaitNotifyExecutor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Thread t1 = new Thread(new PrintEvenOddTask1(NumType.ODD, 5));
		Thread t2 = new Thread(new PrintEvenOddTask1(NumType.EVEN, 5));
		
		t2.start();
		t1.start();
	}

}
