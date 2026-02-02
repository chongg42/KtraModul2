import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static StudentManagement management = new StudentManagement();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("============= HỆ THỐNG QUẢN LÝ SINH VIÊN ==============");
    
        
        boolean running = true;
        while (running) {
            displayMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                running = handleMenuChoice(choice);
            } catch (NumberFormatException e) {
                System.out.println(" Vui lòng nhập số hợp lệ!");
            } catch (Exception e) {
                System.out.println(" Lỗi: " + e.getMessage());
            }
        }
        
    
        scanner.close();
    }

    private static void displayMenu() {
        
        System.out.println(" ================== MENU ================== ");
        
        System.out.println("  1. Khởi tạo dữ liệu mẫu");
        System.out.println("  2. Thêm sinh viên ");
        System.out.println("  3. Thêm khóa học");
        System.out.println("  4. Đăng ký khóa học cho sinh viên");
        System.out.println("  5. Nhập điểm cho sinh viên");
        System.out.println("  6. Xem bảng điểm của 1 sinh viên ");
        System.out.println("  7. Tìm kiếm sinh viên");
        System.out.println("  8. Lọc & sắp xếp sinh viên");
        System.out.println("  9. Tính học phí sinh viên");
        System.out.println("  10. Nhập điểm tự động bằng đa luồng                          ");
        System.out.println("  11. Hiển thị tất cả sinh viên");
        System.out.println("  12. Hiển thị tất cả khóa học");
        System.out.println("  0. Thoát");
      
        System.out.print("Nhập lựa chọn của bạn: ");
    }

    private static boolean handleMenuChoice(int choice) throws Exception {
        switch (choice) {
            case 1:
                management.initSampleData();
                break;
            case 2:
                addStudent();
                break;
            case 3:
                addCourse();
                break;
            case 4:
                enrollStudentToCourse();
                break;
            case 5:
                inputScore();
                break;
            case 6:
                viewStudentScoreBoard();
                break;
            case 7:
                searchStudent();
                break;
            case 8:
                filterAndSortStudents();
                break;
            case 9:
                calculateTuition();
                break;
            case 10:
                autoInputScoresMultiThread();
                break;
            case 11:
                management.displayAllStudents();
                break;
            case 12:
                management.displayAllCourses();
                break;
            case 0:
                return false;
            default:
                System.out.println(" Lựa chọn không hợp lệ!");
        }
        return true;
    }

    private static void addStudent() {
        try {
            System.out.println("\n=== THÊM SINH VIÊN MỚI ===");
            System.out.print("Nhập mã sinh viên: ");
            String id = scanner.nextLine().trim();
            
            System.out.print("Nhập họ tên: ");
            String name = scanner.nextLine().trim();
            
            System.out.print("Nhập email: ");
            String email = scanner.nextLine().trim();
            
            System.out.print("Loại sinh viên (1-FullTime, 2-PartTime): ");
            int type = Integer.parseInt(scanner.nextLine());
            
            Student student;
            if (type == 1) {
                student = new FullTimeStudent(id, name, email);
            } else if (type == 2) {
                student = new PartTimeStudent(id, name, email);
            } else {
                System.out.println(" Loại sinh viên không hợp lệ!");
                return;
            }
            
            management.addStudent(student);
        } catch (DuplicateStudentException e) {
            System.out.println(" " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Lỗi nhập liệu: " + e.getMessage());
        }
    }

    private static void addCourse() {
        try {
            System.out.println("\n=== THÊM KHÓA HỌC MỚI ===");
            System.out.print("Nhập mã khóa học: ");
            String courseId = scanner.nextLine().trim();
            
            System.out.print("Nhập tên khóa học: ");
            String courseName = scanner.nextLine().trim();
            
            System.out.print("Nhập số tín chỉ: ");
            int credits = Integer.parseInt(scanner.nextLine());
            
            Course course = new Course(courseId, courseName, credits);
            management.addCourse(course);
        } catch (DuplicateStudentException e) {
            System.out.println(" " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Lỗi nhập liệu: " + e.getMessage());
        }
    }

    private static void enrollStudentToCourse() {
        try {
            System.out.println("\n=== ĐĂNG KÝ KHÓA HỌC CHO SINH VIÊN ===");
            System.out.print("Nhập mã sinh viên: ");
            String studentId = scanner.nextLine().trim();
            
            System.out.print("Nhập mã khóa học: ");
            String courseId = scanner.nextLine().trim();
            
            management.enrollStudentToCourse(studentId, courseId);
        } catch (StudentNotFoundException | CourseNotFoundException e) {
            System.out.println(" " + e.getMessage());
        }
    }

    private static void inputScore() {
        try {
            System.out.println("\n=== NHẬP ĐIỂM CHO SINH VIÊN ===");
            System.out.print("Nhập mã sinh viên: ");
            String studentId = scanner.nextLine().trim();
            
            System.out.print("Nhập mã khóa học: ");
            String courseId = scanner.nextLine().trim();
            
            System.out.print("Nhập điểm (0-10): ");
            double score = Double.parseDouble(scanner.nextLine());
            
            management.inputScore(studentId, courseId, score);
            System.out.println("✓ Đã nhập điểm thành công!");
        } catch (InvalidScoreException | StudentNotFoundException | CourseNotFoundException e) {
            System.out.println(" " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Lỗi nhập liệu: " + e.getMessage());
        }
    }

    private static void viewStudentScoreBoard() {
        System.out.println("\n=== XEM BẢNG ĐIỂM SINH VIÊN ===");
        System.out.print("Nhập mã sinh viên: ");
        String studentId = scanner.nextLine().trim();
        management.printStudentScoreBoard(studentId);
    }

    private static void searchStudent() {
        System.out.println("\n=== TÌM KIẾM SINH VIÊN ===");
        System.out.println("1. Tìm theo mã sinh viên");
        System.out.println("2. Tìm theo tên và GPA tối thiểu");
        System.out.print("Chọn phương thức tìm kiếm: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            
            if (choice == 1) {
                System.out.print("Nhập mã sinh viên: ");
                String id = scanner.nextLine().trim();
                
                Optional<Student> result = management.searchStudent(id);
                if (result.isPresent()) {
                    System.out.println("\n✓ Tìm thấy sinh viên:");
                    System.out.println(result.get());
                } else {
                    System.out.println(" Không tìm thấy sinh viên với ID: " + id);
                }
            } else if (choice == 2) {
                System.out.print("Nhập tên (hoặc một phần tên): ");
                String name = scanner.nextLine().trim();
                
                System.out.print("Nhập GPA tối thiểu: ");
                double minGpa = Double.parseDouble(scanner.nextLine());
                
                List<Student> results = management.searchStudent(name, minGpa);
                if (results.isEmpty()) {
                    System.out.println(" Không tìm thấy sinh viên nào!");
                } else {
                    System.out.println("\n✓ Tìm thấy " + results.size() + " sinh viên:");
                    results.forEach(System.out::println);
                }
            } else {
                System.out.println(" Lựa chọn không hợp lệ!");
            }
        } catch (Exception e) {
            System.out.println(" Lỗi: " + e.getMessage());
        }
    }

    private static void filterAndSortStudents() {
        System.out.println("\n=== LỌC & SẮP XẾP SINH VIÊN ===");
        System.out.println("1. Lọc sinh viên theo GPA tối thiểu (Lambda)");
        System.out.println("2. Lọc sinh viên Full-Time (Lambda)");
        System.out.println("3. Lọc sinh viên Part-Time (Lambda)");
        System.out.println("4. Sắp xếp theo GPA giảm dần");
        System.out.println("5. Sắp xếp theo tên A-Z");
        System.out.print("Chọn chức năng: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            List<Student> results = new ArrayList<>();
            
            switch (choice) {
                case 1:
                    System.out.print("Nhập GPA tối thiểu: ");
                    double minGpa = Double.parseDouble(scanner.nextLine());
                    results = management.filterStudents(s -> s.getGpa() >= minGpa);
                    System.out.println("\n✓ Sinh viên có GPA >= " + minGpa + ":");
                    break;
                case 2:
                    results = management.filterStudents(s -> s.getRole().equals("FULL_TIME"));
                    System.out.println("\n✓ Sinh viên Full-Time:");
                    break;
                case 3:
                    results = management.filterStudents(s -> s.getRole().equals("PART_TIME"));
                    System.out.println("\n✓ Sinh viên Part-Time:");
                    break;
                case 4:
                    results = management.sortStudentsByGpaDesc();
                    System.out.println("\n✓ Danh sách sinh viên (GPA giảm dần):");
                    break;
                case 5:
                    results = management.sortStudentsByNameAsc();
                    System.out.println("\n✓ Danh sách sinh viên (Tên A-Z):");
                    break;
                default:
                    System.out.println(" Lựa chọn không hợp lệ!");
                    return;
            }
            
            if (results.isEmpty()) {
                System.out.println("Không có kết quả nào!");
            } else {
                results.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println(" Lỗi: " + e.getMessage());
        }
    }

    private static void calculateTuition() {
        System.out.println("\n=== TÍNH HỌC PHÍ SINH VIÊN ===");
        System.out.print("Nhập mã sinh viên: ");
        String studentId = scanner.nextLine().trim();
        
        Optional<Student> studentOpt = management.searchStudent(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            double tuition = management.calculateTuition(student);
            
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("              THÔNG TIN HỌC PHÍ                       ");
            System.out.println("╠═══════════════════════════════════════════════════════╣");
            System.out.println("  Mã SV     : " + student.getId());
            System.out.println("  Họ tên    : " + student.getName());
            System.out.println("  Loại      : " + student.getRole());
            System.out.println("  Số khóa   : " + student.getEnrolledCourses().size());
            
            if (student instanceof FullTimeStudent) {
                int totalCredits = student.getEnrolledCourses().stream()
                    .mapToInt(Course::getCredits)
                    .sum();
                System.out.println("  Tổng TC   : " + totalCredits);
                System.out.println("  Đơn giá   : 300 VND/tín chỉ");
            } else {
                System.out.println("  Đơn giá   : 500 VND/khóa học");
            }
            
         
            System.out.printf("  HỌC PHÍ   : %.2f VND%n", tuition);
           
        } else {
            System.out.println(" Không tìm thấy sinh viên với ID: " + studentId);
        }
    }

    private static void autoInputScoresMultiThread() {
        System.out.println("\n=== NHẬP ĐIỂM TỰ ĐỘNG BẰNG ĐA LUỒNG ===");
        
        List<Student> allStudents = management.getStudentRepository().findAll();
        if (allStudents.isEmpty()) {
            System.out.println(" Chưa có sinh viên nào trong hệ thống!");
            return;
        }
        
        System.out.print("Nhập số luồng (thread) muốn chạy (1-5): ");
        try {
            int numThreads = Integer.parseInt(scanner.nextLine());
            if (numThreads < 1 || numThreads > 5) {
                System.out.println(" Số luồng phải từ 1-5!");
                return;
            }
            
            List<String> studentIds = allStudents.stream()
                .map(Student::getId)
                .collect(Collectors.toList());
            
            List<String> courseIds = management.getCourseRepository().findAll().stream()
                .map(Course::getCourseId)
                .collect(Collectors.toList());
            
            // Chia danh sách sinh viên cho các thread
            int studentsPerThread = (int) Math.ceil((double) studentIds.size() / numThreads);
            Thread[] threads = new Thread[numThreads];
            
            System.out.println("\nBắt đầu nhập điểm tự động với " + numThreads + " luồng...\n");
            
            long startTime = System.currentTimeMillis();
            
            for (int i = 0; i < numThreads; i++) {
                int start = i * studentsPerThread;
                int end = Math.min(start + studentsPerThread, studentIds.size());
                
                if (start < studentIds.size()) {
                    List<String> subList = studentIds.subList(start, end);
                    ScoreInputTask task = new ScoreInputTask(subList, courseIds, management);
                    threads[i] = new Thread(task, "Thread-" + (i + 1));
                    threads[i].start();
                }
            }
            
            // Đợi tất cả thread hoàn thành
            for (Thread thread : threads) {
                if (thread != null) {
                    thread.join();
                }
            }
            
            long endTime = System.currentTimeMillis();
            
            System.out.println("\n Hoàn thành nhập điểm tự động!");
            System.out.println("Thời gian thực hiện: " + (endTime - startTime) + " ms");
            System.out.println(" Đã nhập điểm cho " + studentIds.size() + " sinh viên");
            
        } catch (Exception e) {
            System.out.println(" Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
