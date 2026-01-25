#include <iostream>
#include <string>
using namespace std;

/* ===== DATA STRUCTURES ===== */

struct Attendance {
    string date;
    char status; // 'P', 'A', 'L'
};

struct AttNode {
    Attendance data;
    AttNode* left;
    AttNode* right;
};

struct Course {
    string courseID;
    AttNode* attendanceRoot;
};

struct CourseNode {
    Course data;
    CourseNode* left;
    CourseNode* right;
};

/* ===== STUDENT VIEW FUNCTIONS ===== */

void inorderAttendance(AttNode* root) {
    if (!root) return;
    inorderAttendance(root->left);
    cout << root->data.date << " - " << root->data.status << endl;
    inorderAttendance(root->right);
}

void countSummary(AttNode* root, int& p, int& a, int& l) {
    if (!root) return;
    countSummary(root->left, p, a, l);

    if (root->data.status == 'P') p++;
    else if (root->data.status == 'A') a++;
    else if (root->data.status == 'L') l++;

    countSummary(root->right, p, a, l);
}

CourseNode* findCourse(CourseNode* root, string id) {
    if (!root) return NULL;
    if (id == root->data.courseID) return root;
    if (id < root->data.courseID)
        return findCourse(root->left, id);
    return findCourse(root->right, id);
}

/* ===== USE CASE: View Attendance List ===== */

void viewAttendanceHistory(CourseNode* courseRoot, string courseID) {
    CourseNode* c = findCourse(courseRoot, courseID);
    if (!c) {
        cout << "Course not found!\n";
        return;
    }

    cout << "\n--- Attendance History ---\n";
    inorderAttendance(c->data.attendanceRoot);
}

/* ===== USE CASE: View Attendance Summary ===== */

void viewAttendanceSummary(CourseNode* courseRoot, string courseID) {
    CourseNode* c = findCourse(courseRoot, courseID);
    if (!c) {
        cout << "Course not found!\n";
        return;
    }

    int p = 0, a = 0, l = 0;
    countSummary(c->data.attendanceRoot, p, a, l);

    cout << "\n--- Attendance Summary ---\n";
    cout << "Present: " << p << endl;
    cout << "Absent : " << a << endl;
    cout << "Late   : " << l << endl;
}

/* ===== MAIN (TEST / DEMO) ===== */

int main() {
    // DEMO đơn giản (chưa có dữ liệu nên sẽ báo not found)
    CourseNode* root = NULL;

    viewAttendanceHistory(root, "CTDL");
    viewAttendanceSummary(root, "CTDL");

    return 0;
}

