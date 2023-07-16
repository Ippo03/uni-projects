/*
	Name:IPPOKRATIS PANTELIDIS 
	Student Number:p3210150
*/

import java.util.concurrent.TimeUnit;

class AMPMClockApp {
	
	public static void main (String args[]) throws Exception{
		AMPMClock c1 = new AMPMClock(16 , 28 , 58);
		c1.setAMPM(true);
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
		
	




