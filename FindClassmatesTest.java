import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


/*
 * This class includes test cases for the basic/normal functionality of the 
 * FriendFinder.findClassmates method, but does not check for any error handling.
 */

public class FindClassmatesTest {
	
	protected FriendFinder ff;
		
	protected ClassesDataSource defaultClassesDataSource = new ClassesDataSource() {

		@Override
		public List<String> getClasses(String studentName) {

			if (studentName.equals("A")) {
				return List.of("1", "2", "3");
			}
			else if (studentName.equals("B")) {
				return List.of("1", "2", "3");
			}
			else if (studentName.equals("C")) {
				return List.of("2", "4");
			}
			else return null;			
		
		}
		
	};
	
	protected StudentsDataSource defaultStudentsDataSource = new StudentsDataSource() {

		@Override
		public List<Student> getStudents(String className) {
			
			Student a = new Student("A", 101);
			Student b = new Student("B", 102);
			Student c = new Student("C", 103);

			if (className.equals("1")) {
				return List.of(a, b);
			}
			else if (className.equals("2")) {
				return List.of(a, b, c);
			}
			else if (className.equals("3")) {
				return List.of(a, b);
			}
			else if (className.equals("4")) {
				return List.of(c);
			}
			else return null;
		}
		
	};
	

	@Test
	public void testFindOneFriend() { 
		
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertEquals(1, response.size());
		assertTrue(response.contains("B"));

	}

	@Test
	public void testFindNoFriends() { 
		
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("C", 103));
		assertNotNull(response);
		assertTrue(response.isEmpty());

	}
	
	@Test
	public void testClassesDataSourceReturnsNullForInputStudent() { 
		
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("D", 104));
		assertNotNull(response);
		assertTrue(response.isEmpty());
	}

	@Test
	public void testBothDataSourceReturnsNull() {
		ClassesDataSource classesDataSource = new ClassesDataSource() {
			@Override
			public List<String> getClasses(String studentName) {
				return null;
			}
		};

		StudentsDataSource studentsDataSource = new StudentsDataSource() {
			@Override
			public List<Student> getStudents(String className) {
				return List.of();
			}
		};

		FriendFinder ff = new FriendFinder(classesDataSource, studentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.isEmpty());
	}

	@Test
	public void testStudentsDataSourceReturnsNull() {
		ClassesDataSource classesDataSource = new ClassesDataSource() {
			@Override
			public List<String> getClasses(String studentName) {
				return List.of("1", "2", "3"); // Providing some classes to prevent NullPointerException in FriendFinder.findClassmates
			}
		};

		StudentsDataSource studentsDataSource = new StudentsDataSource() {
			@Override
			public List<Student> getStudents(String className) {
				return null;
			}
		};

		FriendFinder ff = new FriendFinder(classesDataSource, studentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.isEmpty());
	}

	@Test
	public void testClassesDataSourceReturnsNull() {
		ClassesDataSource classesDataSource = new ClassesDataSource() {
			@Override
			public List<String> getClasses(String studentName) {
				if (studentName.equals("A")) {
					return null; // Simulating null return for student A
				}
				return List.of("1", "2", "3"); // Providing some classes to prevent NullPointerException in FriendFinder.findClassmates
			}
		};

		StudentsDataSource studentsDataSource = new StudentsDataSource() {
			@Override
			public List<Student> getStudents(String className) {
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				if (className.equals("1")) {
					return List.of(a, b); // Students A and B are taking class 1
				} else if (className.equals("2")) {
					return List.of(a); // Student A is taking class 2
				} else if (className.equals("3")) {
					return List.of(b); // Student B is taking class 3
				}
				return null;
			}
		};

		FriendFinder ff = new FriendFinder(classesDataSource, studentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.isEmpty());
	}

	@Test (expected = IllegalStateException.class)
	public void testClassesDataSourceIsNull() {
		StudentsDataSource mockStudentsDataSource = new StudentsDataSource() {
			@Override
			public List<Student> getStudents(String className) {
				// Return some students for testing purposes
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				return List.of(a, b);
			}
		};

		// Pass null for ClassesDataSource intentionally
		ff = new FriendFinder(null, mockStudentsDataSource);

		// Since ClassesDataSource is null, the method should return an empty set
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.isEmpty());
	}

	@Test(expected = IllegalStateException.class)
	public void testStudentsDataSourceIsNull() {
		// Mock ClassesDataSource
		ClassesDataSource classesDataSource = new ClassesDataSource() {
			@Override
			public List<String> getClasses(String studentName) {
				return List.of("1", "2", "3"); // Providing some classes to prevent NullPointerException in FriendFinder.findClassmates
			}
		};

		// Create FriendFinder with mock dependencies
		FriendFinder ff = new FriendFinder(classesDataSource, null);

		// Test the findClassmates method
		Set<String> response = ff.findClassmates(new Student("A", 101));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullStudent() {
		ClassesDataSource classesDataSource = new ClassesDataSource() {
			@Override
			public List<String> getClasses(String studentName) {
				return List.of("1", "2", "3"); // Providing some classes to prevent NullPointerException in FriendFinder.findClassmates
			}
		};

		StudentsDataSource studentsDataSource = new StudentsDataSource() {
			@Override
			public List<Student> getStudents(String className) {
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				if (className.equals("1")) {
					return List.of(a, b); // Students A and B are taking class 1
				} else if (className.equals("2")) {
					return List.of(a); // Student A is taking class 2
				} else if (className.equals("3")) {
					return List.of(b); // Student B is taking class 3
				}
				return null;
			}
		};

		FriendFinder ff = new FriendFinder(classesDataSource, studentsDataSource);
		ff.findClassmates(null); // Pass null as the student argument
	}

	@Test
	public void testNullStudentInClass1() {
		ClassesDataSource classesDataSource = new ClassesDataSource() {
			@Override
			public List<String> getClasses(String studentName) {
				return List.of("1", "2"); // Providing some classes to prevent NullPointerException in FriendFinder.findClassmates
			}
		};

		StudentsDataSource studentsDataSource = new StudentsDataSource() {
			@Override
			public List<Student> getStudents(String className) {
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				if (className.equals("1")) {
					List<Student> studentsInClass1 = new ArrayList<>();
					studentsInClass1.add(a);
					studentsInClass1.add(b);
					studentsInClass1.add(null);
					return studentsInClass1; // Students A and B are taking class 1
				} else if (className.equals("2")) {
					return List.of(a, b); // Student A is taking class 2
				}
				return null;
			}
		};

		FriendFinder ff = new FriendFinder(classesDataSource, studentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.contains("B"));
	}

	@Test
	public void testNullClassForOtherStudent() {
		ClassesDataSource classesDataSource = new ClassesDataSource() {
			@Override
			public List<String> getClasses(String studentName) {
				if (studentName.equals("C")) {
					return null;
				} else {
					return List.of("1", "2"); // Providing some classes to prevent NullPointerException in FriendFinder.findClassmates
				}
			}
		};

		StudentsDataSource studentsDataSource = new StudentsDataSource() {
			@Override
			public List<Student> getStudents(String className) {
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				if (className.equals("1")) {
					List<Student> studentsInClass1 = new ArrayList<>();
					studentsInClass1.add(a);
					studentsInClass1.add(b);
					return studentsInClass1; // Students A and B are taking class 1
				} else if (className.equals("2")) {
					List<Student> studentsInClass2 = new ArrayList<>();
					studentsInClass2.add(a);
					studentsInClass2.add(b);

					// Simulate a scenario where one student has a null class
					studentsInClass2.add(new Student("C", 103)); // Student C with a null class
					return studentsInClass2; // Student A, B, and C is taking class 2
				}
				return null;
			}
		};

		FriendFinder ff = new FriendFinder(classesDataSource, studentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.contains("B"));
	}
}
