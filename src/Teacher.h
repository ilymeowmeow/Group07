#ifndef TEACHER_H
#define TEACHER_H

#include <string>

using namespace std;

class Teacher {
private:
    string teacherId;
    string teacherName;

public:
    // Constructor
    Teacher(string id, string name);

    // Use case: Select class & date
    void selectClassAndDate(string classId, string date);

    // Use case: Mark Attendance
    void markAttendance(string classId, string date);

    // Use case: Update Attendance
    void updateAttendance(string classId, string date);

    // Use case: View Attendance List
    void viewAttendanceList(string classId);

    // Use case: Search Student Attendance
    void searchStudentAttendance(string keyword);

    // Use case: View Absence Statistics
    void viewAbsenceStatistics(string classId);

    // Use case: Export Attendance Data
    void exportAttendanceCSV(string classId);
};

#endif
