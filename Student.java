import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Student extends Person {
    protected double gpa;
    protected List<Course> enrolledCourses;

    public Student(String id, String name, String email) {
        super(id, name, email);
        this.gpa = 0.0;
        this.enrolledCourses = new ArrayList<>();
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }


    public void enrollCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
    }


    public void removeCourse(Course course) {
        enrolledCourses.remove(course);
    }

    public double calculateAverageScore(Map<Course, Double> scores) {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }
        
        double total = 0.0;
        int count = 0;
        
        for (Course course : enrolledCourses) {
            if (scores.containsKey(course)) {
                total += scores.get(course);
                count++;
            }
        }
        
        return count > 0 ? total / count : 0.0;
    }

   
    public abstract double calculateTuitionFee();

    @Override
    public String toString() {
        return super.toString() + ", Role: " + getRole() + ", GPA: " + String.format("%.2f", gpa) + 
               ", Courses: " + enrolledCourses.size();
    }
}
