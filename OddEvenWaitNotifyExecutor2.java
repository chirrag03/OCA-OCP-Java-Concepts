package threads;

class PrintEvenOddTask implements Runnable{

	NumType numType;
	int maxLimit;
	SharedPrinter printer;
	public PrintEvenOddTask(NumType numType, int maxLimit, SharedPrinter printer) {
		this.numType = numType;
		this.maxLimit = maxLimit;
		this.printer = printer;
		
	}

	@Override
	public void run() {
		if(NumType.ODD.equals(numType)) {
			printer.printOddNumbers(maxLimit);
		}else {
			printer.printEvenNumbers(maxLimit);
		}
	}
}

class SharedPrinter{
	
	public boolean printOdd=true;
	
	public void printOddNumbers(int maxLimit) {
		for(int i=1;i<=maxLimit;i++) {
			
			synchronized (this) {
				while(!printOdd) {
					waitForNotify();
				}
				
				System.out.println(2*i - 1);
				printOdd = false;
				notify();
				
				sleepFor5();
			}
		}
	}
	
	public void printEvenNumbers(int maxLimit) {
		for(int i=1;i<=maxLimit;i++) {
			
			synchronized (this) {
				while(printOdd) {
					waitForNotify();
				}
				
				System.out.println(2*i);
				printOdd = true;
				notify();
				
				sleepFor5();
			}
		}
	}
	
	private void waitForNotify() {
		try {
			wait();
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

public class OddEvenWaitNotifyExecutor2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SharedPrinter sharedPrinter = new SharedPrinter();
		Thread t1 = new Thread(new PrintEvenOddTask(NumType.ODD, 5, sharedPrinter));
		Thread t2 = new Thread(new PrintEvenOddTask(NumType.EVEN, 5, sharedPrinter));
		
		t1.start();
		t2.start();
	}

}
