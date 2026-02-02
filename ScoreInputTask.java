import java.util.List;
import java.util.Random;

public class ScoreInputTask implements Runnable {
    private List<String> studentIds;
    private List<String> courseIds;
    private StudentManagement management;

    public ScoreInputTask(List<String> studentIds, List<String> courseIds, StudentManagement management) {
        this.studentIds = studentIds;
        this.courseIds = courseIds;
        this.management = management;
    }

    @Override
    public void run() {
        Random random = new Random();
        
        for (String studentId : studentIds) {
            try {
               
                Student student = management.getStudentRepository().findById(studentId).orElse(null);
                
                if (student != null) {
                  
                    for (Course course : student.getEnrolledCourses()) {
                       
                        double score = Math.round(random.nextDouble() * 10 * 100.0) / 100.0;
                        
                        
                        management.safeInputScore(studentId, course.getCourseId(), score);
                        
                        System.out.println(Thread.currentThread().getName() + 
                                         " - Nhập điểm cho SV " + studentId + 
                                         ", Môn " + course.getCourseId() + 
                                         ": " + score);
                     
                        Thread.sleep(10);
                    }
                }
            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + " - Lỗi: " + e.getMessage());
            }
        }
    }
}
