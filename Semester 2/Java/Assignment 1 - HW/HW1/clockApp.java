/*
	Name:IPPOKRATIS PANTELIDIS 
	Student Number:p3210150
*/

import java.util.concurrent.TimeUnit;

public class clockApp {
	
	public static void main (String args[]) throws Exception{
		Clock c1 = new Clock(16 , 28 , 58);
		int count = 0;
		c1.toString();
		while (count < 180) {
			TimeUnit.SECONDS.sleep(1);
			c1.tick();
			c1.toString();
			count = count + 1;
		}
	}
}



