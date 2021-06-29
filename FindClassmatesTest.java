import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

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
            } else if (studentName.equals("B")) {
                return List.of("1", "2", "3");
            } else if (studentName.equals("C")) {
                return List.of("2", "4");
            } else {
                return null;
            }

        }

    };

    protected StudentsDataSource defaultStudentsDataSource = new StudentsDataSource() {

        @Override
        public List<Student> getStudents(String className) {

            Student a = new Student("A", "A");
            Student b = new Student("B", "B");
            Student c = new Student("C", "C");

            if (className.equals("1")) {
                return List.of(a, b);
            } else if (className.equals("2")) {
                return List.of(a, b, c);
            } else if (className.equals("3")) {
                return List.of(a, b);
            } else if (className.equals("4")) {
                return List.of(c);
            } else {
                return null;
            }
        }

    };

    @Test
    public void testFindOneFriend() {

        ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
        Set<String> response = ff.findClassmates(new Student("A", "A"));
        assertNotNull(response);
        assertEquals(1, response.size());
        assertTrue(response.contains("B"));

    }

    @Test
    public void testFindNoFriends() {

        ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
        Set<String> response = ff.findClassmates(new Student("C", "C"));
        assertNull(response);

    }
}
