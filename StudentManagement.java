import java.util.*;
import java.util.stream.Collectors;

public class StudentManagement {
    private Repository<Student> studentRepository;
    private Repository<Course> courseRepository;
    private Map<String, Map<String, Double>> scoreBoard; // studentId -> (courseId -> score)

    public StudentManagement() {
        this.studentRepository = new Repository<>();
        this.courseRepository = new Repository<>();
        this.scoreBoard = new HashMap<>();
    }

    
    public Repository<Student> getStudentRepository() {
        return studentRepository;
    }

    public Repository<Course> getCourseRepository() {
        return courseRepository;
    }


    public void initSampleData() {
        System.out.println("\n=== Đang khởi tạo dữ liệu mẫu ===");
        
        // Tạo 5 khóa học
        Course[] courses = {
            new Course("CS101", "Lập trình Java", 3),
            new Course("CS102", "Cấu trúc dữ liệu", 4),
            new Course("CS103", "Cơ sở dữ liệu", 3),
            new Course("CS104", "Mạng máy tính", 3),
            new Course("CS105", "Hệ điều hành", 4)
        };
        
        for (Course course : courses) {
            courseRepository.add(course.getCourseId(), course);
        }
        System.out.println(" Đã tạo " + courses.length + " khóa học");

        // Tạo 5 sinh viên full-time
        for (int i = 1; i <= 5; i++) {
            Student student = new FullTimeStudent(
                "FT" + String.format("%03d", i),
                "Nguyễn Văn " + (char)('A' + i - 1),
                "ft" + i + "@example.com"
            );
            studentRepository.add(student.getId(), student);
        }
        System.out.println(" Đã tạo 5 sinh viên full-time");

        // Tạo 5 sinh viên part-time
        for (int i = 1; i <= 5; i++) {
            Student student = new PartTimeStudent(
                "PT" + String.format("%03d", i),
                "Trần Thị " + (char)('A' + i - 1),
                "pt" + i + "@example.com"
            );
            studentRepository.add(student.getId(), student);
        }
        System.out.println(" Đã tạo 5 sinh viên part-time");

        // Đăng ký ngẫu nhiên 2-3 khóa cho mỗi sinh viên
        Random random = new Random();
        for (Student student : studentRepository.findAll()) {
            int numCourses = 2 + random.nextInt(2); // 2 hoặc 3 khóa
            List<Course> availableCourses = new ArrayList<>(Arrays.asList(courses));
            
            for (int i = 0; i < numCourses; i++) {
                int index = random.nextInt(availableCourses.size());
                Course course = availableCourses.remove(index);
                student.enrollCourse(course);
            }
        }
        System.out.println(" Đã đăng ký khóa học cho tất cả sinh viên");
        System.out.println("=== Hoàn tất khởi tạo dữ liệu mẫu ===\n");
    }

    // Thêm sinh viên
    public void addStudent(Student student) throws DuplicateStudentException {
        if (studentRepository.exists(student.getId())) {
            throw new DuplicateStudentException("Sinh viên với ID " + student.getId() + " đã tồn tại!");
        }
        studentRepository.add(student.getId(), student);
        System.out.println(" Đã thêm sinh viên: " + student.getName());
    }

    // Thêm khóa học
    public void addCourse(Course course) throws DuplicateStudentException {
        if (courseRepository.exists(course.getCourseId())) {
            throw new DuplicateStudentException("Khóa học với ID " + course.getCourseId() + " đã tồn tại!");
        }
        courseRepository.add(course.getCourseId(), course);
        System.out.println(" Đã thêm khóa học: " + course.getCourseName());
    }

    // Đăng ký khóa học cho sinh viên
    public void enrollStudentToCourse(String studentId, String courseId) 
            throws StudentNotFoundException, CourseNotFoundException {
        
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (!studentOpt.isPresent()) {
            throw new StudentNotFoundException("Không tìm thấy sinh viên với ID: " + studentId);
        }

        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (!courseOpt.isPresent()) {
            throw new CourseNotFoundException("Không tìm thấy khóa học với ID: " + courseId);
        }

        Student student = studentOpt.get();
        Course course = courseOpt.get();
        
        if (student.getEnrolledCourses().contains(course)) {
            System.out.println("⚠ Sinh viên đã đăng ký khóa học này rồi!");
            return;
        }

        student.enrollCourse(course);
        System.out.println(" Đã đăng ký khóa " + course.getCourseName() + 
                         " cho sinh viên " + student.getName());
    }

    // Nhập điểm cho sinh viên
    public void inputScore(String studentId, String courseId, double score) 
            throws StudentNotFoundException, CourseNotFoundException, InvalidScoreException {
        
        if (score < 0 || score > 10) {
            throw new InvalidScoreException("Điểm phải trong khoảng 0-10. Điểm nhập: " + score);
        }

        if (!studentRepository.exists(studentId)) {
            throw new StudentNotFoundException("Không tìm thấy sinh viên với ID: " + studentId);
        }

        if (!courseRepository.exists(courseId)) {
            throw new CourseNotFoundException("Không tìm thấy khóa học với ID: " + courseId);
        }

        // Lưu điểm vào scoreBoard
        scoreBoard.putIfAbsent(studentId, new HashMap<>());
        scoreBoard.get(studentId).put(courseId, score);
        
        // Cập nhật GPA
        updateStudentGpa(studentId);
    }

    // Nhập điểm thread-safe
    public synchronized void safeInputScore(String studentId, String courseId, double score) 
            throws InvalidScoreException, StudentNotFoundException, CourseNotFoundException {
        inputScore(studentId, courseId, score);
    }

    // Cập nhật GPA của sinh viên
    private void updateStudentGpa(String studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            Map<String, Double> scores = scoreBoard.get(studentId);
            
            if (scores != null && !scores.isEmpty()) {
                Map<Course, Double> courseScores = new HashMap<>();
                for (Course course : student.getEnrolledCourses()) {
                    if (scores.containsKey(course.getCourseId())) {
                        courseScores.put(course, scores.get(course.getCourseId()));
                    }
                }
                double avgScore = student.calculateAverageScore(courseScores);
                student.setGpa(avgScore);
            }
        }
    }

    // In bảng điểm của sinh viên
    public void printStudentScoreBoard(String studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        
        if (!studentOpt.isPresent()) {
            System.out.println("✗ Không tìm thấy sinh viên với ID: " + studentId);
            return;
        }

        Student student = studentOpt.get();
        
        System.out.println("============BẢNG ĐIỂM SINH VIÊN ===========");
        
        System.out.println("  Mã SV    : " + student.getId());
        System.out.println("  Họ tên   : " + student.getName());
        System.out.println("  Email    : " + student.getEmail());
        System.out.println("  Loại     : " + student.getRole());
        
        System.out.println("  ĐIỂM CÁC MÔN:");
        

        Map<String, Double> scores = scoreBoard.get(studentId);
        boolean hasScores = false;

        for (Course course : student.getEnrolledCourses()) {
            String courseId = course.getCourseId();
            if (scores != null && scores.containsKey(courseId)) {
                System.out.printf("  %-10s %-30s : %.2f%n", 
                    courseId, course.getCourseName(), scores.get(courseId));
                hasScores = true;
            } else {
                System.out.printf("  %-10s %-30s : Chưa có điểm%n", 
                    courseId, course.getCourseName());
            }
        }

        if (!hasScores) {
            System.out.println("  (Chưa có điểm nào được nhập)");
        }

        
        System.out.printf("  ĐIỂM TRUNG BÌNH (GPA): %.2f%n", student.getGpa());
       
    }

    // Method Overloading - Tìm kiếm sinh viên theo ID
    public Optional<Student> searchStudent(String id) {
        return studentRepository.findById(id);
    }

    // Method Overloading - Tìm kiếm sinh viên theo tên và GPA tối thiểu
    public List<Student> searchStudent(String name, double minGpa) {
        return studentRepository.findAll().stream()
            .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
            .filter(s -> s.getGpa() >= minGpa)
            .collect(Collectors.toList());
    }

    // Tính học phí (đa hình)
    public double calculateTuition(Student student) {
        return student.calculateTuitionFee();
    }

    // Lọc sinh viên bằng Lambda
    public List<Student> filterStudents(StudentFilter filter) {
        return studentRepository.findAll().stream()
            .filter(filter::filter)
            .collect(Collectors.toList());
    }

    // Sắp xếp sinh viên theo GPA giảm dần
    public List<Student> sortStudentsByGpaDesc() {
        return studentRepository.findAll().stream()
            .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
            .collect(Collectors.toList());
    }

    // Sắp xếp sinh viên theo tên A-Z
    public List<Student> sortStudentsByNameAsc() {
        return studentRepository.findAll().stream()
            .sorted(Comparator.comparing(Student::getName))
            .collect(Collectors.toList());
    }

    // Hiển thị tất cả sinh viên
    public void displayAllStudents() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            System.out.println("Chưa có sinh viên nào trong hệ thống.");
            return;
        }

      
        System.out.println("║                           DANH SÁCH SINH VIÊN                              ║");
       
        System.out.printf("%-10s %-25s %-25s %-12s %s%n", 
            "Mã SV", "Họ tên", "Email", "Loại", "GPA");
       
        
        for (Student student : students) {
            System.out.printf("%-10s %-25s %-25s %-12s %.2f%n",
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getRole(),
                student.getGpa());
        }
    }

    // Hiển thị tất cả khóa học
    public void displayAllCourses() {
        List<Course> courses = courseRepository.findAll();
        if (courses.isEmpty()) {
            System.out.println("Chưa có khóa học nào trong hệ thống.");
            return;
        }

    
        System.out.println("║              DANH SÁCH KHÓA HỌC                      ║");
      
        System.out.printf("%-10s %-30s %s%n", "Mã khóa", "Tên khóa học", "Tín chỉ");
        
        
        for (Course course : courses) {
            System.out.printf("%-10s %-30s %d%n",
                course.getCourseId(),
                course.getCourseName(),
                course.getCredits());
        }
        
    }
}
