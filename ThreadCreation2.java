package threads;

class MyNewThread extends Thread{
	@Override
	public void run() {
		System.out.println("Executing thread");
	}
}

public class ThreadCreation2 {

	public static void main(String[] args) {
		
		//Create an object of the class extending Thread, then call start()
		MyNewThread t1 = new MyNewThread();
		t1.start();
		
		
        //We can simplify this by not creating a reference variable to MyNewThread
		(new MyNewThread()).start();
		
		
        //We can directly pass the implementation using anonymous classes
		Thread t2 = new Thread() {
			@Override
		    public void run() {
				System.out.println("Executing thread using anonymous class");
		    }
		};
		t2.start();
		
		
        //We can pass the implementation using lambda
		Thread t4 = new Thread(() -> System.out.println("Executing thread using lambda"));
		t4.start();
	}

}
