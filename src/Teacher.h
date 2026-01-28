#ifndef TEACHER_H
#define TEACHER_H

#include <string>
using std::string;

class Teacher {
private:
    string teacherId;
    string teacherName;

    // trạng thái lựa chọn hiện tại (để menu dùng lại)
    string selectedClassId;
    string selectedDate;      // format kiểu "2026-01-05"
    string selectedCourseId;  // nếu m có dùng theo course

public:
    Teacher(string id, string name);

    // 1) Select class & date (nên set selectedClassId/selectedDate)
    void selectClassAndDate(string classId, string date);

    // 2) Mark attendance (thường dùng selectedClassId/selectedDate)
    void markAttendance(string classId, string date);

    // 3) Update attendance
    void updateAttendance(string classId, string date);

    // 4) View attendance list
    void viewAttendanceList(string classId);

    // 5) Search student attendance
    // keyword = chuỗi để tìm (ví dụ: "SV001" hoặc "2026-01-05" hoặc "P")
    void searchStudentAttendance(string keyword);

    // 6) View absence statistics
    void viewAbsenceStatistics(string classId);

    // 7) Export attendance CSV
    void exportAttendanceCSV(string classId);

    // (optional) getter nếu m cần debug
    string getSelectedClassId() const { return selectedClassId; }
    string getSelectedDate() const { return selectedDate; }
    string getSelectedCourseId() const { return selectedCourseId; }
};

#endif
