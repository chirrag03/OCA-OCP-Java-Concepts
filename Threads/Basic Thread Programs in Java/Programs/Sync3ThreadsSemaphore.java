package threads;

import java.util.concurrent.Semaphore;

//There are three threads T1 prints a ... T2 prints b... and T3 prints c …. 
//How do you synchronize these three to print sequence abcabcabcabcabc....

class PrintLetterTask implements Runnable{
	
	String data;
	Semaphore prev;
	Semaphore next;
	
	int max = 5;
	
	public PrintLetterTask(String d, Semaphore p, Semaphore n) {
		// TODO Auto-generated constructor stub
		data = d;
		prev = p;
		next = n;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i=0;i<max;i++) {
			acquirePermitOnPrev();
			System.out.print(data);
			releasePermitForNext();
		}
	}
	
	private void acquirePermitOnPrev() {
		try {
			prev.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void releasePermitForNext() {
		next.release();
	}
}

public class Sync3ThreadsSemaphore {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Semaphore s1 = new Semaphore(1);
		Semaphore s2 = new Semaphore(1);
		Semaphore s3 = new Semaphore(1);
		
		try {
			s1.acquire();
			s2.acquire();
			s3.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Thread t1 = new Thread(new PrintLetterTask("a", s3, s1));
		Thread t2 = new Thread(new PrintLetterTask("b", s1, s2));
		Thread t3 = new Thread(new PrintLetterTask("c", s2, s3));
		
		t1.start();
		t2.start();
		t3.start();
		s3.release();
		
	}

}
