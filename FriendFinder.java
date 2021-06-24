
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class FriendFinder {
	
	protected ClassesDataSource classesDataSource;
	protected StudentsDataSource studentsDataSource;
	
	public FriendFinder(ClassesDataSource cds, StudentsDataSource sds) {
		classesDataSource = cds;
		studentsDataSource = sds;
	}
	
	/*
	 * This method takes a String representing the name of a student 
	 * and then returns a list containing the names of everyone else 
	 * who is taking the same classes as that student.
	 * The ordering of the elements in the list is non-deterministic. 
	 */
	public Set<String> findClassmates(Student theStudent) {
		
		String name = theStudent.getName();
		
		// find the classes that this student is taking
		List<String> myClasses = classesDataSource.getClasses(name);
		if (myClasses == null) {
			return null; // return null if the student isn't taking any classes
		}
		
		// use the classes to find the names of the students
		Set<String> classmates = new HashSet<String>();
		
		for (String myClass : myClasses) {
			// list all the students in the class
			List<Student> students = studentsDataSource.getStudents(myClass);
			
			for (Student student : students) {
				
				// find the other classes that they're taking
				List<String> theirClasses = classesDataSource.getClasses(student.getName());
							
				// see if all of the classes that they're taking are the same as the ones this student is taking
				boolean same = true;
				for (String c : myClasses) {
					
					if (theirClasses.contains(c) == false) {
						same = false;
						break;
					}
				}
				if (same) {
					if (student.getName().equals(name) == false && classmates.contains(student.getName()) == false) 
						classmates.add(student.getName());
				}
			}

		}
				
		if (classmates.isEmpty()) 
			return null;
		else return classmates;
	}
	

}
