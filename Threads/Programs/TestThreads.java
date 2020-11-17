package threads;

import java.util.*;
import java.util.stream.Collectors;


class Task{
	
	public static synchronized void completePrintWork(int num) {
		for(int i=0;i<=5;i++) {
			System.out.println(num + i);
			try{  
			    Thread.sleep(400);  
			}catch(Exception e){
				System.out.println(e);
			}  
		}
	}
}

public class TestThreads {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		List<String> list = Arrays.asList("abc", "def");
//		HashMap<String, String> map = new HashMap<>();
//		list.stream().forEach(s -> map.put(s, s));
//		System.out.println(map.entrySet());
		
		new Thread(() -> Task.completePrintWork(5)).start();
		new Thread(() -> Task.completePrintWork(10)).start();
		

	}

}
