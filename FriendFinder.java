
import java.util.List;
import java.util.Collections;
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
	 * and then returns a set containing the names of everyone else 
	 * who is taking the same classes as that student.
	 */
	public Set<String> findClassmates(Student theStudent) {
		if (classesDataSource == null) {
			throw new IllegalStateException("ClassesDataSource cannot be null");
		} else if (studentsDataSource == null) {
			throw new IllegalStateException("StudentsDataSource cannot be null");
		}

		if (theStudent == null) {
			throw new IllegalArgumentException("The student argument cannot be null.");
		}

		String name = theStudent.getName();

		// find the classes that this student is taking
		List<String> myClasses = classesDataSource.getClasses(name);
		if (myClasses == null) {
			return Collections.emptySet(); // return empty Set if the student isn't taking any classes
		}

		// use the classes to find the names of the students
		Set<String> classmates = new HashSet<>();

		for (String myClass : myClasses) {
			// list all the students in the class
			List<Student> students = studentsDataSource.getStudents(myClass);

			if (students == null) {
				continue; // Skip this class if there are no students (defensive action)
			}

			for (Student otherStudent : students) {
				if (otherStudent == null) {
					continue; // Skip this student if it's null (defensive action)
				}

				// find the other classes that they're taking
				List<String> theirClasses = classesDataSource.getClasses(otherStudent.getName());

				if (theirClasses == null) {
					continue; // Skip this student if their classes are null (defensive action)
				}

				// see if all of the classes that they're taking are the same as the ones this student is taking
				boolean allSame = true;
				for (String c : myClasses) {
					if (!theirClasses.contains(c)) {
						allSame = false;
						break;
					}
				}

				// if they're taking all the same classes, then add to the set of classmates
				if (allSame && !otherStudent.getName().equals(name)) {
					classmates.add(otherStudent.getName());
				}
			}
		}
		return classmates;
	}
}
