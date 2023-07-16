/*
	Name: IPPOKRATIS PANTELIDIS
	Student Number:p3210150
*/


class Clock {
	
	//Data
	
	private int h;
	private int m;
	private int s;
	
	Clock(int h , int m , int s){
		this.h = h;
		this.m = m;
		this.s = s;
	}
	
	Clock(){
	
	}
	// Methods
	
	void setHour(int h) {
		this.h = h;
		
	}

	void setMin(int m) {
		this.m = m;
		
	}
	
	void setSec(int s) {
		this.s = s;
		
	}

	void tick() {
		if (this.s < 59){
			this.s = this.s + 1;
		}else {
			this.s = 0;
			if (this.m < 59){
				this.m = this.m + 1;
			}else{
				this.m = 0;
				this.h = this.h+1;
			  }
		}
		if (this.h == 24){
			this.h = 0;
		  }
	}
		

	public String toString(){
		String time;
		if (h<10){
			time = "0" + h + ":";
		}
		else{
			time = h + ":";
		}
		if (m<10){
			time = time + "0" + m + ":";
		}
		else{
			time = time + m + ":";
		}
		if (s<10){
			time = time + "0" + s;
		}
		else {
			time = time + s;
		}
		System.out.println(time);
		return time;
	} 
} 
	



