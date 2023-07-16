/*
	Name: IPPOKRATIS PANTELIDIS 
	Student Number:p3210150
*/

class Student {
	
	private String Name;
	private String RN;
	private int Grade;
	
	Student(String Name, String RN, int Grade){
		this.Name = Name;
		this.RN = RN;
		this.Grade = Grade;
	}
	
	String getName(){
		return this.Name;
	}

	String getRN(){
		return this.RN;
	}

	int getGrade(){
		return this.Grade;
	}
	
}// Student
