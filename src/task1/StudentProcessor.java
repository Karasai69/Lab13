package task1;

import java.util.*;
import java.util.stream.Collectors;

class Student {
    private String name;
    private int age;
    private int course;
    private double gpa;

    public Student(String name, int age, int course, double gpa) {
        this.name = name;
        this.age = age;
        this.course = course;
        this.gpa = gpa;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public int getCourse() { return course; }
    public double getGpa() { return gpa; }

    @Override
    public String toString() {
        return String.format("%s (Курс %d, GPA: %.2f)", name, course, gpa);
    }
}

public class StudentProcessor {

    private static List<Student> generateStudents() {
        List<Student> students = new ArrayList<>();
        String[] names = {"Алия", "Данияр", "Айжан", "Нурлан", "Гульмира",
                "Ермек", "Айгуль", "Сергей", "Аружан", "Самат",
                "Кайрат", "Асем", "Мадина", "Руслан", "Аслан",
                "Жанна", "Берик", "Сая", "Тимур", "Айна",
                "Алибек", "Динара", "Нурислам", "Гульдана", "Ерлан"};

        Random random = new Random();

        for (int i = 0; i < 25; i++) {
            String name = names[random.nextInt(names.length)];
            int age = 18 + random.nextInt(5); // 18-22 года
            int course = 1 + random.nextInt(4); // 1-4 курс
            double gpa = 2.5 + random.nextDouble() * 1.5; // GPA от 2.5 до 4.0
            students.add(new Student(name, age, course, gpa));
        }

        return students;
    }

    public static void main(String[] args) {
        List<Student> students = generateStudents();

        System.out.println("=== Обработка данных студентов ===\n");
        System.out.println("Всего студентов: " + students.size() + "\n");

        // 1. Найти всех студентов с GPA > 3.5
        System.out.println("1. Студенты с GPA > 3.5:");
        List<Student> topStudents = students.stream()
                .filter(s -> s.getGpa() > 3.5)
                .collect(Collectors.toList());
        topStudents.forEach(s -> System.out.println("- " + s));
        System.out.println("Всего: " + topStudents.size() + " студента\n");

        // 2. Отсортировать студентов по имени
        System.out.println("2. Студенты, отсортированные по имени:");
        List<Student> sortedByName = students.stream()
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
        sortedByName.forEach(s -> System.out.println("- " + s));
        System.out.println();

        // 3. Получить список имён студентов 3-го курса
        System.out.println("3. Имена студентов 3-го курса:");
        List<String> thirdCourseNames = students.stream()
                .filter(s -> s.getCourse() == 3)
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println(thirdCourseNames + "\n");

        // 4. Найти средний GPA всех студентов
        System.out.println("4. Средний GPA всех студентов:");
        double avgGpa = students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
        System.out.printf("Средний GPA: %.2f\n\n", avgGpa);

        // 5. Найти студента с максимальным GPA
        System.out.println("5. Студент с максимальным GPA:");
        students.stream()
                .max(Comparator.comparingDouble(Student::getGpa))
                .ifPresent(s -> System.out.println(s + "\n"));

        // 6. Сгруппировать студентов по курсам
        System.out.println("6. Студенты по курсам:");
        Map<Integer, List<Student>> byCourse = students.stream()
                .collect(Collectors.groupingBy(Student::getCourse));
        byCourse.forEach((course, studentList) ->
                System.out.println("Курс " + course + ": " + studentList.size() + " студентов"));
        System.out.println();

        // 7. Подсчитать количество студентов на каждом курсе
        System.out.println("7. Количество студентов на каждом курсе:");
        Map<Integer, Long> countByCourse = students.stream()
                .collect(Collectors.groupingBy(Student::getCourse, Collectors.counting()));
        System.out.println(countByCourse);
    }
}