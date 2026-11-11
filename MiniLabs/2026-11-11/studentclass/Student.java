// 1. Create a Student class with private name, id, and gpa fields
public class Student {
    private String name;
    private int id;
    private float gpa;

    // Stretch: add a static counter that tracks how many Student objects have been created
    private static int numberStudents = 0;

    // 2. Write a constructor that sets all three
    public Student(String name, int id, float gpa) {
        this.name = name;
        this.id = id;
        // Pass GPA through the validation check
        setGpa(gpa);
        // If validation passes, we can safely increment this number :)
        numberStudents++;
    }

    // 3. Add getters, plus a setGpa() that rejects values outside 0.0-4.0
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public float getGpa() {
        return gpa;
    }

    public void setGpa(float newGpa) {
        if(newGpa < 0.0 || newGpa > 4.0) {
            throw new IllegalArgumentException("GPA out of acceptable range!");
        }
        this.gpa = newGpa;
    }

    // 4. Add getStudent() that returns the student's name, ID, and gpa in a comma separated string
    public String getStudent() {
        return String.format("%s,%d,%.1f", name, id, gpa);
    }

    public static int getTotalStudents() {
        return numberStudents;
    }

    // 5. In main, instantiate three students and print each one
    public static void main(String[] args) {
        Student student1 = new Student("Student One", 1, 4.0f);
        Student student2 = new Student("Student Two", 2, 3.2f);
        Student student3 = new Student("Student Three", 3, 2.4f);

        System.out.println("Students (name, id, gpa):");
        System.out.println(student1.getStudent().replace(",", ", "));
        System.out.println(student2.getStudent().replace(",", ", "));
        System.out.println(student3.getStudent().replace(",", ", "));
        
        System.out.println("A total of " + Student.getTotalStudents() + " students have been registered in this system!");
    }
}
