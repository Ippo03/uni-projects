/*
	Name:IPPOKRATIS PANTELIDIS 
	Student Number:p3210150
*/


import java.time.Clock;
import java.util.concurrent.TimeUnit;

public class clockApp {
	
	public static void main (String args[]) throws Exception{
		Clock c1 = new Clock(16 , 28 , 58);
		int count = 0;
		while (count < 180) {
			c1.toString();
			c1.tick();
			TimeUnit.SECONDS.sleep(1);
			count = count + 1;
		}
	}
}



