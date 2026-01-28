#ifndef TEACHER_H
#define TEACHER_H

#include <string>
using std::string;

class Teacher {
private:
    string teacherId;
    string teacherName;
    string selectedClassId;
    string selectedDate;      
    string selectedCourseId;  
public:
    Teacher(string id, string name);

    // Select class & date 
    void selectClassAndDate(string classId, string date);

    // Mark attendance 
    void markAttendance(string classId, string date);

    // Update attendance
    void updateAttendance(string classId, string date);

    // View attendance list
    void viewAttendanceList(string classId);

    // Search student attendance
    void searchStudentAttendance(string keyword);

    // View absence statistics
    void viewAbsenceStatistics(string classId);

    // Export attendance CSV
    void exportAttendanceCSV(string classId);

    string getSelectedClassId() const { return selectedClassId; }
    string getSelectedDate() const { return selectedDate; }
    string getSelectedCourseId() const { return selectedCourseId; }
};
#endif
