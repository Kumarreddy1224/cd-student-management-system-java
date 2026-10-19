import java.io.*;
import java.util.*;

abstract class Record {
    int id;
    String name;

    public abstract void display();
}

class Student extends Record {
    private String course;
    private int age;
    private double marks;

    public Student(int id, String name, String course, int age, double marks) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.age = age;
        this.marks = marks;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public int getAge() {
        return age;
    }

    public double getMarks() {
        return marks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    @Override
    public void display() {
        System.out.println(id + "," + name + "," + course + "," + age + "," + marks);
    }
}

interface Manageable {
    void addRecord(Student s);

    void viewRecords();

    void searchRecord(int id);

    void updateRecord(int id, String newName, String newCourse, int newAge, double newMarks);

    void deleteRecord(int id);
}

class StudentManagementSystem implements Manageable {
    private ArrayList<Student> students = new ArrayList<>();
    private final String fileName = "students.txt";

    public StudentManagementSystem() {
        loadData();
    }
    public void addRecord(Student s) {
        students.add(s);
        saveData();
        System.out.println("Student added successfully!");
    }
    public void viewRecords() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
        } else {
            for (Student s : students) {
                s.display();
            }
        }
    }
    public void searchRecord(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                s.display();
                return;
            }
        }
        System.out.println("Student not found!");
    }

    public void updateRecord(int id, String newName, String newCourse, int newAge, double newMarks) {
        for (Student s : students) {
            if (s.getId() == id) {
                s.setName(newName);
                s.setCourse(newCourse);
                s.setAge(newAge);
                s.setMarks(newMarks);
                saveData();
                System.out.println("Student updated successfully!");
                return;
            }
        }
        System.out.println("Student not found!");
    }
    public void deleteRecord(int id) {
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            Student s = iterator.next();
            if (s.getId() == id) {
                iterator.remove();
                saveData();
                System.out.println("Student deleted successfully!");
                return;
            }
        }
        System.out.println("Student not found!");
    }
    private void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Student s : students) {
                bw.write(s.getId() + "," + s.getName() + "," + s.getCourse() + "," + s.getAge() + "," + s.getMarks());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    students.add(new Student(Integer.parseInt(data[0]), data[1], data[2],
                            Integer.parseInt(data[3]), Double.parseDouble(data[4])));
                }
            }
        } catch (IOException e) {
            System.out.println("No existing data found.");
        } catch (NumberFormatException e) {
            System.out.println("Data format error in file. Please clear students.txt and re-run.");
        }
    }
}
class AutoSaveThread extends Thread {
    private StudentManagementSystem sms;

    public AutoSaveThread(StudentManagementSystem sms) {
        this.sms = sms;
    }
    public void run() {
        while (true) {
            try {
                Thread.sleep(30000); 
                System.out.println("Auto-saving data...");
                sms.viewRecords();
            } catch (InterruptedException e) {
                System.out.println("Auto-save interrupted.");
            }
        }
    }
}
public class Week5 {
    public static void main(String[] args) {
        StudentManagementSystem sms = new StudentManagementSystem();
        AutoSaveThread autoSave = new AutoSaveThread(sms);
        autoSave.start();
       Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Student Management System ---");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
              int choice = sc.nextInt();
            sc.nextLine();
         switch (choice) {
                case 1:
                    System.out.print("Enter Student ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Course: ");
                    String course = sc.nextLine();
                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();
                    System.out.print("Enter Marks: ");
                    double marks = sc.nextDouble();
                    sms.addRecord(new Student(id, name, course, age, marks));
                    break;
                case 2:
                    sms.viewRecords();
                    break;
                case 3:
                    System.out.print("Enter Student ID to search: ");
                    sms.searchRecord(sc.nextInt());
                    break;
                case 4:
                    System.out.print("Enter Student ID to update: ");
                    int uid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter New Name: ");
                    String newName = sc.nextLine();
                    System.out.print("Enter New Course: ");
                    String newCourse = sc.nextLine();
                    System.out.print("Enter New Age: ");
                    int newAge = sc.nextInt();
                    System.out.print("Enter New Marks: ");
                    double newMarks = sc.nextDouble();
                    sms.updateRecord(uid, newName, newCourse, newAge, newMarks);
                    break;
                case 5:
                    System.out.print("Enter Student ID to delete: ");
                    sms.deleteRecord(sc.nextInt());
                    break;
                case 6:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
