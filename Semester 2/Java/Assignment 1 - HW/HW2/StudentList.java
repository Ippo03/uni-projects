/*
	Name: IPPOKRATIS PANTELIDIS
	Student Number:p3210150
*/

class StudentList {
	
	private Student [] myList = new Student[50];
	
	private int length = 0;
	
		
	void InsertStudent(Student newStudent){
		myList[length] = newStudent;
        	length = length + 1;
		
	}//InsertStudent
	
	
	void LookupStudent(String RN) {

		if (length == 0){
			System.out.println("\nList is empty, no student to lookup. \n");
			return;
		}

		for(int i = 0; i < length; i++){ 
			if (RN.equals(myList[i].getRN())){
				System.out.println(myList[i].getName()+"'s grade is " + myList[i].getGrade()+".\n");
				return;
			}	//LookupStudent
    		}
		System.out.println("Student not found.\n");
	}


	void DisplayList() {
		for(int i = 0; i < length; i++){
			System.out.println("Student " + (i + 1));
			System.out.println("Name: " + myList[i].getName());
            		System.out.println("RN: " + myList[i].getRN());
            		System.out.println("Grade: " + myList[i].getGrade());
			System.out.println();
		}//DisplayList
    }//StudentList	
}