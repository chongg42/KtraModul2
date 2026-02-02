# STUDENT MANAGEMENT SYSTEM PRO - CONSOLE APPLICATION

## 📋 GIỚI THIỆU
Hệ thống quản lý sinh viên đầy đủ tính năng được xây dựng bằng Java Console, áp dụng đầy đủ các nguyên lý OOP và các kỹ thuật lập trình nâng cao.

## 🎯 CÁC TÍNH NĂNG CHÍNH

### 1. Quản lý Sinh viên
- Thêm sinh viên (Full-Time / Part-Time)
- Xem danh sách sinh viên
- Tìm kiếm sinh viên (theo ID hoặc tên + GPA)
- Lọc và sắp xếp sinh viên

### 2. Quản lý Khóa học
- Thêm khóa học mới
- Xem danh sách khóa học
- Đăng ký khóa học cho sinh viên

### 3. Quản lý Điểm số
- Nhập điểm thủ công
- Nhập điểm tự động bằng đa luồng
- Xem bảng điểm chi tiết
- Tính điểm trung bình (GPA)

### 4. Tính toán Học phí
- Tính học phí cho Full-Time Student (theo tín chỉ)
- Tính học phí cho Part-Time Student (theo số khóa)

## 🔧 CÁC KỸ THUẬT ÁP DỤNG

### ✅ 4 Tính chất OOP
1. **Encapsulation** - Đóng gói dữ liệu với private/protected fields và getter/setter
2. **Abstraction** - Abstract class Person, Student
3. **Inheritance** - Person → Student → FullTimeStudent/PartTimeStudent
4. **Polymorphism** - Method calculateTuitionFee() được override

### ✅ Method Overloading
```java
// Overloading trong StudentManagement
Optional<Student> searchStudent(String id)
List<Student> searchStudent(String name, double minGpa)
```

### ✅ Custom Exception
- `DuplicateStudentException` - Sinh viên trùng lặp
- `StudentNotFoundException` - Không tìm thấy sinh viên
- `CourseNotFoundException` - Không tìm thấy khóa học
- `InvalidScoreException` - Điểm không hợp lệ

### ✅ Collections Framework
- `List<Student>` - Danh sách sinh viên
- `Set` - Đảm bảo unique trong equals/hashCode
- `Map<String, Student>` - Repository storage
- `Map<String, Map<String, Double>>` - ScoreBoard

### ✅ Functional Interface & Lambda
```java
@FunctionalInterface
interface StudentFilter {
    boolean filter(Student s);
}

// Sử dụng Lambda
List<Student> highGPA = management.filterStudents(s -> s.getGpa() > 8.0);
```

### ✅ Optional
```java
Optional<Student> student = management.searchStudent("SV001");
student.ifPresent(s -> System.out.println(s.getName()));
```

### ✅ Generic
```java
public class Repository<T> {
    protected Map<String, T> storage;
    public Optional<T> findById(String id) { ... }
}
```

### ✅ Thread & Multi-threading
- Class `ScoreInputTask implements Runnable`
- Method `synchronized void safeInputScore(...)` đảm bảo thread-safety
- Tránh race condition khi nhập điểm đồng thời

## 📁 CẤU TRÚC FILE

```
Student-Management-System/
├── Person.java                    # Abstract base class
├── Student.java                   # Abstract student class
├── FullTimeStudent.java           # Full-time student implementation
├── PartTimeStudent.java           # Part-time student implementation
├── Course.java                    # Course entity
├── Repository.java                # Generic repository
├── StudentFilter.java             # Functional interface
├── CustomExceptions.java          # All custom exceptions
├── StudentManagement.java         # Main business logic
├── ScoreInputTask.java            # Multi-threading task
├── Main.java                      # Console application with menu
├── TestCases.java                 # Test all requirements
└── README.md                      # This file
```

## 🚀 HƯỚNG DẪN SỬ DỤNG

### Biên dịch
```bash
javac *.java
```

### Chạy chương trình chính
```bash
java Main
```

### Chạy test cases
```bash
java TestCases
```

## 📖 MENU CHƯƠNG TRÌNH

```
╔═══════════════════════════════════════════════════════════════╗
║           STUDENT MANAGEMENT SYSTEM - MENU                    ║
╠═══════════════════════════════════════════════════════════════╣
║  1. Khởi tạo dữ liệu mẫu                                      ║
║  2. Thêm sinh viên                                            ║
║  3. Thêm khóa học                                             ║
║  4. Đăng ký khóa học cho sinh viên                            ║
║  5. Nhập điểm cho sinh viên                                   ║
║  6. Xem bảng điểm của 1 sinh viên                             ║
║  7. Tìm kiếm sinh viên                                        ║
║  8. Lọc & sắp xếp sinh viên                                   ║
║  9. Tính học phí sinh viên                                    ║
║  10. Nhập điểm tự động bằng đa luồng                          ║
║  11. Hiển thị tất cả sinh viên                                ║
║  12. Hiển thị tất cả khóa học                                 ║
║  0. Thoát                                                     ║
╚═══════════════════════════════════════════════════════════════╝
```

## 🧪 TEST CASES BẮT BUỘC

### Test Case 1: Thêm sinh viên trùng ID
```
✓ Thêm sinh viên thứ nhất thành công
✓ PASSED: Đã bắt được DuplicateStudentException
```

### Test Case 2: Nhập điểm ngoài 0–10
```
✓ PASSED: Bắt được lỗi điểm âm (-5.0)
✓ PASSED: Bắt được lỗi điểm > 10 (15.0)
✓ PASSED: Nhập điểm hợp lệ (8.5) thành công
```

### Test Case 3: Tìm sinh viên không tồn tại
```
✓ PASSED: Optional trả về rỗng khi không tìm thấy
  result.isPresent() = false
```

### Test Case 4: Lọc GPA > 8 bằng Lambda
```
✓ PASSED: Sử dụng Lambda để lọc sinh viên
  Tổng số sinh viên: 10
  Sinh viên có GPA > 8.0: X
```

### Test Case 5: Chạy đa luồng nhập điểm
```
✓ PASSED: Tất cả threads hoàn thành không lỗi
  Thời gian: XXX ms
  Số threads: 3
```

### Test Case 6: Tính học phí FullTime vs PartTime
```
✓ PASSED: Áp dụng đa hình để tính học phí
  Full-Time: 2100.0 VND (300 VND/tín chỉ × 7 TC)
  Part-Time: 1000.0 VND (500 VND/khóa × 2 khóa)
  Chênh lệch: 1100.0 VND
```

## 🎓 MINH HỌA DỮ LIỆU MẪU

### Sinh viên
- 5 Full-Time Students: FT001 - FT005
- 5 Part-Time Students: PT001 - PT005

### Khóa học
- CS101: Lập trình Java (3 TC)
- CS102: Cấu trúc dữ liệu (4 TC)
- CS103: Cơ sở dữ liệu (3 TC)
- CS104: Mạng máy tính (3 TC)
- CS105: Hệ điều hành (4 TC)

## 💡 LƯU Ý QUAN TRỌNG

1. **Thread Safety**: Method `safeInputScore()` được synchronized để đảm bảo an toàn khi nhiều thread cùng nhập điểm

2. **Validation**: Tất cả input đều được validate trước khi xử lý
   - ID không được trùng
   - Điểm phải từ 0-10
   - Sinh viên/Khóa học phải tồn tại

3. **Exception Handling**: Mọi exception đều được bắt và hiển thị thông báo thân thiện

4. **GPA Calculation**: GPA tự động được cập nhật mỗi khi nhập điểm mới

5. **Polymorphism**: Học phí được tính khác nhau dựa trên loại sinh viên:
   - Full-Time: 300 VND × tổng số tín chỉ
   - Part-Time: 500 VND × số khóa học

## 🔍 VÍ DỤ SỬ DỤNG LAMBDA

```java
// Lọc sinh viên GPA >= 8
List<Student> topStudents = management.filterStudents(s -> s.getGpa() >= 8.0);

// Lọc sinh viên Full-Time
List<Student> fullTime = management.filterStudents(s -> s.getRole().equals("FULL_TIME"));

// Lọc theo điều kiện phức tạp
List<Student> custom = management.filterStudents(s -> 
    s.getGpa() >= 7.0 && s.getEnrolledCourses().size() >= 3
);
```

## 📊 KIẾN TRÚC HỆ THỐNG

```
┌─────────────────────────────────────────────────────┐
│                    Main (Menu)                      │
└─────────────────┬───────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────┐
│              StudentManagement                      │
│  - studentRepository: Repository<Student>           │
│  - courseRepository: Repository<Course>             │
│  - scoreBoard: Map<String, Map<String, Double>>     │
└───────┬─────────────────────────┬───────────────────┘
        │                         │
        ▼                         ▼
┌───────────────┐        ┌──────────────────┐
│  Repository<T>│        │ ScoreInputTask   │
│  (Generic)    │        │  (Runnable)      │
└───────┬───────┘        └──────────────────┘
        │
        ▼
┌──────────────────────────────────────────┐
│           Student Hierarchy              │
│                                          │
│  Person (Abstract)                       │
│    ↓                                     │
│  Student (Abstract)                      │
│    ↓                    ↓                │
│  FullTimeStudent    PartTimeStudent      │
└──────────────────────────────────────────┘
```

## ✨ ĐIỂM NỔI BẬT

- ✅ Áp dụng đầy đủ 4 tính chất OOP
- ✅ Code sạch, dễ đọc, có comment
- ✅ Exception handling hoàn chỉnh
- ✅ Thread-safe với synchronized
- ✅ Sử dụng Stream API và Lambda
- ✅ Menu console thân thiện
- ✅ Test cases đầy đủ
- ✅ Tài liệu chi tiết

## 👨‍💻 PHÁT TRIỂN BỞI

Dự án thực hành Java OOP - Student Management System
Version: 1.0
Date: 2024

---
**Happy Coding! 🚀**
"# KtraModul2" 
