/*
	Name: IPPOKRATIS PANTELIDIS
	Student Number:p3210150
*/

class AMPMClock extends Clock {
	
	boolean withAMPM = false;

	AMPMClock(){
		super(0,0,0);
	}

	AMPMClock(int h, int m, int s){
		super(h, m, s);
	}

	public void setAMPM(boolean yes) {
		this.withAMPM = yes;
	}

	public String toString() {
        String time = "";
        if (withAMPM) {
            int hour = h % 12;
            if (hour == 0) {
                hour = 12;
            }
            String ampm = (h < 12) ? "AM" : "PM";
            time = String.format("%02d:%02d:%02d %s", hour, m, s, ampm);
        } else {
            time = super.toString();
        }
		System.out.println(time);
        return time;
    }
}
