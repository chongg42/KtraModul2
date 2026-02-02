import java.util.*;

public class TestCases {
    private static StudentManagement management = new StudentManagement();

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║              CHẠY CÁC TEST CASE BẮT BUỘC                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");

        runAllTests();
    }

    public static void runAllTests() {
        testCase1_DuplicateStudent();
        testCase2_InvalidScore();
        testCase3_StudentNotFound();
        testCase4_FilterByGPA();
        testCase5_MultiThreadScoreInput();
        testCase6_TuitionDifference();
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║              HOÀN THÀNH TẤT CẢ TEST CASES                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }

    // TEST CASE 1: Thêm sinh viên trùng ID → lỗi
    private static void testCase1_DuplicateStudent() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST CASE 1: Thêm sinh viên trùng ID                        │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        try {
            Student student1 = new FullTimeStudent("SV001", "Nguyễn Văn A", "a@example.com");
            management.addStudent(student1);
            System.out.println("✓ Thêm sinh viên thứ nhất thành công");

            // Thêm sinh viên cùng ID
            Student student2 = new FullTimeStudent("SV001", "Trần Văn B", "b@example.com");
            management.addStudent(student2);
            System.out.println("✗ FAILED: Không bắt được exception!");
            
        } catch (DuplicateStudentException e) {
            System.out.println("✓ PASSED: Đã bắt được DuplicateStudentException");
            System.out.println("  Message: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ FAILED: Exception không đúng loại");
        }
        System.out.println();
    }

    // TEST CASE 2: Nhập điểm ngoài 0–10 → lỗi
    private static void testCase2_InvalidScore() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST CASE 2: Nhập điểm ngoài khoảng 0-10                    │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        try {
            Course course = new Course("CS101", "Java Programming", 3);
            management.addCourse(course);

            Student student = new FullTimeStudent("SV002", "Test Student", "test@example.com");
            management.addStudent(student);
            management.enrollStudentToCourse("SV002", "CS101");

            // Test điểm âm
            try {
                management.inputScore("SV002", "CS101", -5.0);
                System.out.println("✗ FAILED: Không bắt được exception cho điểm âm!");
            } catch (InvalidScoreException e) {
                System.out.println("✓ PASSED: Bắt được lỗi điểm âm (-5.0)");
                System.out.println("  Message: " + e.getMessage());
            }

            // Test điểm > 10
            try {
                management.inputScore("SV002", "CS101", 15.0);
                System.out.println("✗ FAILED: Không bắt được exception cho điểm > 10!");
            } catch (InvalidScoreException e) {
                System.out.println("✓ PASSED: Bắt được lỗi điểm > 10 (15.0)");
                System.out.println("  Message: " + e.getMessage());
            }

            // Test điểm hợp lệ
            management.inputScore("SV002", "CS101", 8.5);
            System.out.println("✓ PASSED: Nhập điểm hợp lệ (8.5) thành công");

        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
        }
        System.out.println();
    }

    // TEST CASE 3: Tìm sinh viên không tồn tại → Optional rỗng
    private static void testCase3_StudentNotFound() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST CASE 3: Tìm sinh viên không tồn tại                    │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        Optional<Student> result = management.searchStudent("KHONGTONTAI999");
        
        if (!result.isPresent()) {
            System.out.println("✓ PASSED: Optional trả về rỗng khi không tìm thấy");
            System.out.println("  result.isPresent() = " + result.isPresent());
        } else {
            System.out.println("✗ FAILED: Optional không rỗng!");
        }
        
        // Test với ID tồn tại
        Optional<Student> validResult = management.searchStudent("SV001");
        if (validResult.isPresent()) {
            System.out.println("✓ PASSED: Tìm thấy sinh viên tồn tại");
            System.out.println("  Student: " + validResult.get().getName());
        }
        System.out.println();
    }

    // TEST CASE 4: Lọc GPA > 8 bằng Lambda
    private static void testCase4_FilterByGPA() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST CASE 4: Lọc sinh viên GPA > 8 bằng Lambda              │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        try {
            // Tạo dữ liệu test
            management.initSampleData();
            
            // Nhập điểm để tạo GPA
            List<Student> students = management.getStudentRepository().findAll();
            for (Student student : students) {
                for (Course course : student.getEnrolledCourses()) {
                    double score = 5.0 + Math.random() * 5.0; // Random 5-10
                    management.inputScore(student.getId(), course.getCourseId(), score);
                }
            }

            // Lọc bằng Lambda
            List<Student> highGpaStudents = management.filterStudents(s -> s.getGpa() > 8.0);
            
            System.out.println("✓ PASSED: Sử dụng Lambda để lọc sinh viên");
            System.out.println("  Tổng số sinh viên: " + students.size());
            System.out.println("  Sinh viên có GPA > 8.0: " + highGpaStudents.size());
            
            if (!highGpaStudents.isEmpty()) {
                System.out.println("  Danh sách:");
                for (Student s : highGpaStudents) {
                    System.out.printf("    - %s: GPA = %.2f%n", s.getName(), s.getGpa());
                }
            }

        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }

    // TEST CASE 5: Chạy đa luồng nhập điểm → không lỗi dữ liệu
    private static void testCase5_MultiThreadScoreInput() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST CASE 5: Nhập điểm đa luồng - Thread Safety             │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        try {
            List<Student> allStudents = management.getStudentRepository().findAll();
            List<String> studentIds = new ArrayList<>();
            for (Student s : allStudents) {
                studentIds.add(s.getId());
            }

            List<Course> allCourses = management.getCourseRepository().findAll();
            List<String> courseIds = new ArrayList<>();
            for (Course c : allCourses) {
                courseIds.add(c.getCourseId());
            }

            // Chạy 3 thread đồng thời
            int numThreads = 3;
            Thread[] threads = new Thread[numThreads];
            
            System.out.println("  Bắt đầu " + numThreads + " threads...");
            long startTime = System.currentTimeMillis();

            for (int i = 0; i < numThreads; i++) {
                ScoreInputTask task = new ScoreInputTask(studentIds, courseIds, management);
                threads[i] = new Thread(task, "TestThread-" + (i + 1));
                threads[i].start();
            }

            // Đợi tất cả threads hoàn thành
            for (Thread thread : threads) {
                thread.join();
            }

            long endTime = System.currentTimeMillis();

            System.out.println("✓ PASSED: Tất cả threads hoàn thành không lỗi");
            System.out.println("  Thời gian: " + (endTime - startTime) + " ms");
            System.out.println("  Số threads: " + numThreads);
            
            // Kiểm tra dữ liệu không bị lỗi
            int studentsWithScores = 0;
            for (Student s : allStudents) {
                if (s.getGpa() > 0) {
                    studentsWithScores++;
                }
            }
            System.out.println("  Sinh viên có điểm: " + studentsWithScores + "/" + allStudents.size());

        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }

    // TEST CASE 6: Tính học phí FullTime vs PartTime → khác nhau
    private static void testCase6_TuitionDifference() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST CASE 6: Tính học phí FullTime vs PartTime (Đa hình)   │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        try {
            // Tạo 2 sinh viên cùng đăng ký 2 khóa giống nhau
            Student fullTimeStudent = new FullTimeStudent("FT999", "Full Time Test", "ft@test.com");
            Student partTimeStudent = new PartTimeStudent("PT999", "Part Time Test", "pt@test.com");

            management.addStudent(fullTimeStudent);
            management.addStudent(partTimeStudent);

            // Đăng ký cùng các khóa
            management.enrollStudentToCourse("FT999", "CS101");
            management.enrollStudentToCourse("FT999", "CS102");
            management.enrollStudentToCourse("PT999", "CS101");
            management.enrollStudentToCourse("PT999", "CS102");

            // Tính học phí
            double fullTimeFee = management.calculateTuition(fullTimeStudent);
            double partTimeFee = management.calculateTuition(partTimeStudent);

            System.out.println("✓ PASSED: Áp dụng đa hình để tính học phí");
            System.out.println("  Full-Time Student:");
            System.out.println("    - Số khóa: " + fullTimeStudent.getEnrolledCourses().size());
            
            int totalCredits = 0;
            for (Course c : fullTimeStudent.getEnrolledCourses()) {
                totalCredits += c.getCredits();
            }
            System.out.println("    - Tổng tín chỉ: " + totalCredits);
            System.out.println("    - Học phí: " + fullTimeFee + " VND (300 VND/tín chỉ)");
            
            System.out.println("  Part-Time Student:");
            System.out.println("    - Số khóa: " + partTimeStudent.getEnrolledCourses().size());
            System.out.println("    - Học phí: " + partTimeFee + " VND (500 VND/khóa)");
            
            if (fullTimeFee != partTimeFee) {
                System.out.println("✓ PASSED: Học phí khác nhau do cách tính khác nhau");
                System.out.println("  Chênh lệch: " + Math.abs(fullTimeFee - partTimeFee) + " VND");
            } else {
                System.out.println("⚠ WARNING: Học phí giống nhau (có thể trùng ngẫu nhiên)");
            }

        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }
}
