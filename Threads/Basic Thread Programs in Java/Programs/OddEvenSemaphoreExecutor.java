package threads;

import java.util.concurrent.Semaphore;

enum NumType{
	ODD(1),EVEN(0);
	int start;
	private NumType(int i) {
		start = i;
	}
	
	public int getStart() {
		return start;
	}
}
class PrintNumTask implements Runnable{

	NumType numType;
	int maxLimit;

	static Semaphore oddSem = new Semaphore(1); 	//Give permit to oddSem initially
	static Semaphore evenSem = new Semaphore(0);
	
	public PrintNumTask(NumType numType, int maxLimit) {
		this.numType = numType;
		this.maxLimit = maxLimit;
	}

	@Override
	public void run() {
		if(numType.equals(NumType.ODD)) {
			printOddNumbers();
		}else {
			printEvenNumbers();
		}
	}
	
	private void printOddNumbers() {
		for(int i=0;i<maxLimit;i++) {
			acquirePermitOnSem(oddSem);
			System.out.println(2*i + 1);
			releasePermitOnSem(evenSem);
		}
	}
	
	private void printEvenNumbers() {
		for(int i=1;i<maxLimit;i++) {
			acquirePermitOnSem(evenSem); 		//Acquire permit to enter critical section & decrement permit count
			System.out.println(2*i);
			releasePermitOnSem(oddSem);
		}
	}
	
	//Permission granted if sem value grater than 0 otherwise denied
	private void acquirePermitOnSem(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void releasePermitOnSem(Semaphore s) {
		s.release();
	}
}
public class OddEvenSemaphoreExecutor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Thread t1 = new Thread(new PrintNumTask(NumType.ODD, 5));
		Thread t2 = new Thread(new PrintNumTask(NumType.EVEN, 5));
		
		t1.start();
		t2.start();
	}

}
