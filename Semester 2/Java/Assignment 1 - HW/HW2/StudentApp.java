/*
	Name: IPPOKRATIS PANTELIDIS
	Student Number:p3210150
*/

import java.util.Scanner;

class StudentApp {

	public static void main (String args[]) {
		
		StudentList Lesson = new StudentList();
		Scanner in = new Scanner(System.in);

		String in_name;
		String in_RN;
		int in_Grade;

		while(true) {
			System.out.println ("1. Insert Student");
			System.out.println ("2. Lookup Student");
			System.out.println ("3. Display List");
			System.out.println ("0. Exit");

			System.out.println();
			System.out.print("Enter your choice: ");
			int flag = in.nextInt();
			System.out.println();

			if (flag == 1){
				System.out.print("Enter student's name: ");
				in_name = in.next();
				System.out.print("Enter student's RN: ");
				in_RN = in.next();
				System.out.print("Enter student's grade: ");
				in_Grade = in.nextInt();
				System.out.println();
				Lesson.InsertStudent(new Student(in_name, in_RN, in_Grade));
			}else if(flag == 2){
				System.out.print("Enter student's RN: ");
				in_RN = in.next();
				System.out.println();
				Lesson.LookupStudent(in_RN);
			}else if(flag == 3){
				Lesson.DisplayList();
				System.out.println();
			}else{
				break;
			}
		}// for
		in.close();
	} //main
	
}//StudentApp
