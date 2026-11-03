import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StudentRoster {
    // 1. Create the Student record and a roster of 8 students
    record Student(String name, double gpa, String major) {}
    static List<Student> roster = new ArrayList<>(List.of(
        new Student("Ana", 3.8, "CS"), new Student("Kai", 3.2, "Math"), new Student("Zoe", 3.9, "CS"),
        new Student("Raj", 3.2, "CS"), new Student("Lee", 2.7, "Art"), new Student("James", 4.0, "CS"),
        new Student("Aaron", 3.9, "CS"), new Student("Jean", 1.0, "Math")
    ));

    // Helpers to pass into various functions operating on the roster
    static void printStudent(Student student) {
        System.out.printf("%-5s in %-4s: %.1f%n", student.name, student.major, student.gpa);
    }
    static Student uppercaseName(Student student) {
        return new Student(student.name.toUpperCase(), student.gpa, student.major);
    }
    static List<Student> getStudentsByMajor(String major) {
        List<Student> students = new ArrayList<>();
        roster.forEach(student -> { if(student.major.equals(major)) { students.add(student); } });
        return students;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Student Roster!");
        
        // Empty line for formatting
        System.out.println();

        // 2. Print the roster sorted by name
        roster.sort(Comparator.comparing(Student::name));
        System.out.println("Sorted by name:");
        roster.forEach(StudentRoster::printStudent);
        
        // Empty line for formatting
        System.out.println();

        // 3. Print it sorted by major, then GPA high to low
        roster.sort(Comparator.comparing(Student::major).thenComparing(Student::gpa, Comparator.reverseOrder()));
        System.out.println("Sorted by major then by gpa descending:");
        roster.forEach(StudentRoster::printStudent);
        
        // Empty line for formatting
        System.out.println();

        // 4. Remove students under 2.0 GPA with removeIf()
        roster.removeIf(student -> student.gpa < 2.0);
        System.out.println("None under 2.0 GPA:");
        roster.forEach(StudentRoster::printStudent);
        
        // Empty line for formatting
        System.out.println();

        // 5. Print every name in uppercase with forEach()
        // Note: Because of step 4, we now no longer have students with a GPA under 2.0.
        roster.replaceAll(StudentRoster::uppercaseName);
        roster.forEach(StudentRoster::printStudent);
        
        // Empty line for formatting
        System.out.println();

        // Stretch: build a Map<String, List<Student>> of students by major using computeIfAbsent()
        Map<String, List<Student>> rosterByMajor = new TreeMap<>();
        roster.forEach(student -> rosterByMajor.computeIfAbsent(student.major, StudentRoster::getStudentsByMajor));

        // Print out the roster organized by major
        // Note: All students are still sorted by their GPA descending due to step 3.
        // Note: We now no longer have students with a GPA under 2.0 due to step 4.
        // Note: All students names are now also uppercased due to step 5.
        rosterByMajor.forEach((major, students) -> { 
            System.out.printf("Students majoring in %s:%n", major);
            students.forEach(StudentRoster::printStudent);
        
            // Empty line for formatting
            System.out.println();
        });
    }
}
