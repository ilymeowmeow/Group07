#include <iostream>
#include <string>
#include <limits>
using namespace std;

#include "AuthService.h"
#include "adminmodule.h"
#include "Teacher.h"
#include "StudentView.h"

AttNode* insertAttendance(AttNode* root, const Attendance& a) {
    if (!root) {
        AttNode* node = new AttNode{ a, nullptr, nullptr };
        return node;
    }
    if (a.date < root->data.date) root->left = insertAttendance(root->left, a);
    else root->right = insertAttendance(root->right, a);
    return root;
}

CourseNode* insertCourse(CourseNode* root, const string& courseID) {
    if (!root) {
        Course c;
        c.courseID = courseID;
        c.attendanceRoot = nullptr;
        CourseNode* node = new CourseNode{ c, nullptr, nullptr };
        return node;
    }
    if (courseID < root->data.courseID) root->left = insertCourse(root->left, courseID);
    else if (courseID > root->data.courseID) root->right = insertCourse(root->right, courseID);
    return root;
}

CourseNode* buildDemoCourses() {
    CourseNode* courseRoot = nullptr;

    // Demo courses
    courseRoot = insertCourse(courseRoot, "CTDL");
    courseRoot = insertCourse(courseRoot, "DB");
    courseRoot = insertCourse(courseRoot, "OS");

    // Demo attendance for each course
    // OS
    if (CourseNode* os = findCourse(courseRoot, "OS")) {
        os->data.attendanceRoot = insertAttendance(os->data.attendanceRoot, { "2026-01-05", 'P' });
        os->data.attendanceRoot = insertAttendance(os->data.attendanceRoot, { "2026-01-12", 'L' });
        os->data.attendanceRoot = insertAttendance(os->data.attendanceRoot, { "2026-01-19", 'P' });
    }

    // DB
    if (CourseNode* db = findCourse(courseRoot, "DB")) {
        db->data.attendanceRoot = insertAttendance(db->data.attendanceRoot, { "2026-01-06", 'P' });
        db->data.attendanceRoot = insertAttendance(db->data.attendanceRoot, { "2026-01-13", 'A' });
        db->data.attendanceRoot = insertAttendance(db->data.attendanceRoot, { "2026-01-20", 'P' });
    }

    // CTDL
    if (CourseNode* ds = findCourse(courseRoot, "CTDL")) {
        ds->data.attendanceRoot = insertAttendance(ds->data.attendanceRoot, { "2026-01-07", 'P' });
        ds->data.attendanceRoot = insertAttendance(ds->data.attendanceRoot, { "2026-01-14", 'P' });
        ds->data.attendanceRoot = insertAttendance(ds->data.attendanceRoot, { "2026-01-21", 'L' });
    }

    return courseRoot;
}

int inputInt(const string& prompt, int lo, int hi) {
    int x;
    while (true) {
        cout << prompt;
        if (cin >> x && x >= lo && x <= hi) return x;
        cout << "Input khong hop le. Thu lai.\n";
        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
    }
}

void runTeacherFlow() {
    string id, name;
    cout << "Teacher ID: ";
    cin >> id;
    cout << "Teacher Name: ";
    cin.ignore(numeric_limits<streamsize>::max(), '\n');
    getline(cin, name);

    Teacher t(id, name);

    bool back = false;
    while (!back) {
        cout << "\n--- TEACHER MENU ---\n";
        cout << "1. Select class & date\n";
        cout << "2. Mark attendance\n";
        cout << "3. Update attendance\n";
        cout << "4. View attendance list\n";
        cout << "5. Search student attendance\n";
        cout << "6. View absence statistics\n";
        cout << "7. Export attendance CSV\n";
        cout << "8. Back\n";

        int ch = inputInt("Choose: ", 1, 8);

        string classId, date, keyword;
        switch (ch) {
            case 1:
                cout << "Class ID: "; cin >> classId;
                cout << "Date: "; cin >> date;
                t.selectClassAndDate(classId, date);
                break;
            case 2:
                cout << "Class ID: "; cin >> classId;
                cout << "Date: "; cin >> date;
                t.markAttendance(classId, date);
                break;
            case 3:
                cout << "Class ID: "; cin >> classId;
                cout << "Date: "; cin >> date;
                t.updateAttendance(classId, date);
                break;
            case 4:
                cout << "Class ID: "; cin >> classId;
                t.viewAttendanceList(classId);
                break;
            case 5:
                cout << "Keyword: "; cin >> keyword;
                t.searchStudentAttendance(keyword);
                break;
            case 6:
                cout << "Class ID: "; cin >> classId;
                t.viewAbsenceStatistics(classId);
                break;
            case 7:
                cout << "Class ID: "; cin >> classId;
                t.exportAttendanceCSV(classId);
                break;
            case 8:
                back = true;
                break;
        }
    }
}

void runStudentFlow(CourseNode* courseRoot) {
    bool back = false;
    while (!back) {
        cout << "\n--- STUDENT MENU ---\n";
        cout << "Courses (demo): OS / DB / CTDL\n";
        cout << "1. View attendance history\n";
        cout << "2. View attendance summary\n";
        cout << "3. Back\n";

        int ch = inputInt("Choose: ", 1, 3);
        if (ch == 3) { back = true; continue; }

        string courseID;
        cout << "Course ID: ";
        cin >> courseID;

        if (ch == 1) viewAttendanceHistory(courseRoot, courseID);
        else if (ch == 2) viewAttendanceSummary(courseRoot, courseID);
    }
}

int main() {
    cout << "===== STUDENT MANAGEMENT SYSTEM =====\n";

    AuthService auth;

    // Login (demo in AuthService.cpp: group07 / 123456)
    string username, password;
    cout << "Username: ";
    cin >> username;
    cout << "Password: ";
    cin >> password;

    if (!auth.login(username, password)) {
        cout << "Login failed!\n";
        return 0;
    }

    CourseNode* courseRoot = buildDemoCourses();

    bool running = true;
    while (running) {
        // Role selection is already implemented in AuthService
        auth.selectRole();
        Role role = auth.getRole();

        cout << "\n===== ROLE HUB =====\n";
        cout << "1. Go to selected role module\n";
        cout << "2. Select role again\n";
        cout << "3. Logout & Exit\n";

        int hub = inputInt("Choose: ", 1, 3);

        if (hub == 2) continue;
        if (hub == 3) {
            auth.logout();
            break;
        }

        // hub == 1: run module by role
        switch (role) {
            case Role::Admin: {
                AdminModule admin;
                if (admin.login()) admin.adminMenu();
                break;
            }
            case Role::Teacher: {
                runTeacherFlow();
                break;
            }
            case Role::Student: {
                runStudentFlow(courseRoot);
                break;
            }
            default:
                cout << "No role selected / invalid role.\n";
                break;
        }
    }

    cout << "Bye!\n";
    return 0;
}
