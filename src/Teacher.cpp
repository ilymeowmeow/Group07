#include "Teacher.h"
#include <iostream>

using namespace std;

// Constructor
Teacher::Teacher(string id, string name) {
    teacherId = id;
    teacherName = name;
}

// Select class & date
void Teacher::selectClassAndDate(string classId, string date) {
    cout << "Teacher selects class: " << classId << endl;
    cout << "Attendance date: " << date << endl;
}

// Mark Attendance
void Teacher::markAttendance(string classId, string date) {
    cout << "Marking attendance for class " << classId
         << " on " << date << endl;
    cout << "Default status: Present (P / A / L)" << endl;
}

// Update Attendance
void Teacher::updateAttendance(string classId, string date) {
    cout << "Updating attendance for class "
         << classId << " on " << date << endl;
}

// View Attendance List
void Teacher::viewAttendanceList(string classId) {
    cout << "Viewing attendance list for class "
         << classId << endl;
}

// Search Student Attendance
void Teacher::searchStudentAttendance(string keyword) {
    cout << "Searching attendance for student: "
         << keyword << endl;
}

// View Absence Statistics
void Teacher::viewAbsenceStatistics(string classId) {
    cout << "Viewing absence statistics for class "
         << classId << endl;
}

// Export Attendance Data
void Teacher::exportAttendanceCSV(string classId) {
    cout << "Exporting attendance data of class "
         << classId << " to CSV file" << endl;
}
