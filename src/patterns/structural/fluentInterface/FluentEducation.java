package patterns.structural.fluentInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class Student {
    List<String> degrees = new ArrayList<>();
    String name;

    public Student() {
    }

    public Student(List<String> degrees, String name) {
        this.degrees = degrees;
        this.name = name;
    }

    public Student studentName(String name) {
        this.name = name;
        return new Student(this.degrees, this.name);
    }

    public Student diplomaDegree(String major) {
        this.degrees.add("Diploma Degree of " + major);
        return new Student(this.degrees, this.name);
    }

    public Student bachelorDegree(String major) {
        this.degrees.add("Bachelor Degree of " + major);
        return new Student(this.degrees, this.name);
    }

    public Student masterDegree(String major) {
        this.degrees.add("Master Degree of " + major);
        return new Student(this.degrees, this.name);
    }

    public Student PHDDegree(String major) {
        this.degrees.add("PHD Degree of " + major);
        return new Student(this.degrees, this.name);
    }

    public static void studentHistory(Function<Student,Student> fun){
        Student student = new Student();
        student = fun.apply(student);
        System.out.println("Student whit name \"" + student.name + "\" has these degrees : \n" + student.degrees);
    }
}

public class FluentEducation {
    public static void main(String[] args) {
        Student.studentHistory(student -> {
            student.studentName("Sara")
                    .diplomaDegree("Mathematics")
                    .bachelorDegree("Electronic")
                    .masterDegree("Computer Science");
            return student;
        });
    }
}
