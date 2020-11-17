package threads;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class TaskToDo{
	
	public static void completePrintWork(int num) {
		System.out.println("Start printing from " + num);
		for(int i=0;i<=5;i++) {
			System.out.println(num + i);
			try{  
			    Thread.sleep(400);  
			}catch(Exception e){
				System.out.println(e);
			}  
		}
		System.out.println("End printing from " + num);
	}
}

public class TestThreadPoolExecutor {

    public static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
    
		// TODO Auto-generated constructor stub

    	public static void main(String[] args) {
		// TODO Auto-generated method stub
        executor.execute(() -> TaskToDo.completePrintWork(10));
        executor.execute(() -> TaskToDo.completePrintWork(20));
        executor.execute(() -> TaskToDo.completePrintWork(30));
        executor.execute(() -> TaskToDo.completePrintWork(40));
        executor.shutdown();
	}

}
