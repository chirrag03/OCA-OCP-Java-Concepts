package threads;

class MyThread implements Runnable{
	@Override
	public void run() {
		System.out.println("Executing Runnable thread");
	}
}	

public class ThreadCreation {

	public static void main(String[] args) {
		//Pass an implementation of the Runnable interface to the constructor of Thread, then call start()
		
        //We can pass an object of the runnable implementation
		MyThread m1 = new MyThread(); //Or Runnable m1 = new MyThread();  
		Thread t1 = new Thread(m1);
		t1.start();
		

        //We can simplify this code by not creating a reference variable to MyThread 
		Thread t2 = new Thread(new MyThread());
		t2.start();
		
		
        //We can simplify this by not creating a reference variable to Thread
		(new Thread(new MyThread())).start();
		
		
        //We can pass the implementation of runnable using anonymous classes
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Executing thread using anonymous class");
			}
		});
		t3.start();
		
		
        //We can pass the implementation of runnable using lambda
		Runnable runnable = () -> { 
			System.out.println("Executing thread using lambda");
		};
		Thread t4 = new Thread(runnable);
		t4.start();
	}
}
