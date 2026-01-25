#include <iostream>
#include <string>
using namespace std;

/* ===== Attendance BST ===== */

struct Attendance {
    string date;   // yyyy-mm-dd
    char status;   // P / A / L
};

struct AttNode {
    Attendance data;
    AttNode *left, *right;
};

AttNode* createAttNode(string date, char status) {
    AttNode* p = new AttNode;
    p->data.date = date;
    p->data.status = status;
    p->left = p->right = NULL;
    return p;
}

AttNode* insertAttendance(AttNode* root, string date, char status) {
    if (!root) return createAttNode(date, status);
    if (date < root->data.date)
        root->left = insertAttendance(root->left, date, status);
    else if (date > root->data.date)
        root->right = insertAttendance(root->right, date, status);
    return root;
}

void inorderAttendance(AttNode* root) {
    if (!root) return;
    inorderAttendance(root->left);
    cout << root->data.date << " - " << root->data.status << endl;
    inorderAttendance(root->right);
}

void countSummary(AttNode* root, int &p, int &a, int &l) {
    if (!root) return;
    countSummary(root->left, p, a, l);
    if (root->data.status == 'P') p++;
    else if (root->data.status == 'A') a++;
    else if (root->data.status == 'L') l++;
    countSummary(root->right, p, a, l);
}

/* ===== Course BST ===== */

struct Course {
    string courseID;   
    AttNode* attendanceRoot;
};

struct CourseNode {
    Course data;
    CourseNode *left, *right;
};

CourseNode* createCourseNode(string id) {
    CourseNode* p = new CourseNode;
    p->data.courseID = id;
    p->data.attendanceRoot = NULL;
    p->left = p->right = NULL;
    return p;
}

CourseNode* insertCourse(CourseNode* root, string id) {
    if (!root) return createCourseNode(id);
    if (id < root->data.courseID)
        root->left = insertCourse(root->left, id);
    else if (id > root->data.courseID)
        root->right = insertCourse(root->right, id);
    return root;
}

CourseNode* findCourse(CourseNode* root, string id) {
    if (!root) return NULL;
    if (id == root->data.courseID) return root;
    if (id < root->data.courseID) return findCourse(root->left, id);
    return findCourse(root->right, id);
}

/* ===== MAIN STUDENT VIEW ===== */

int main() {
    CourseNode* courseRoot = NULL;

    // Giả lập dữ liệu điểm danh của sinh viên
    courseRoot = insertCourse(courseRoot, "CTDL");
    courseRoot = insertCourse(courseRoot, "CNPM");

    CourseNode* c1 = findCourse(courseRoot, "CTDL");
    c1->data.attendanceRoot = insertAttendance(c1->data.attendanceRoot, "2026-01-10", 'P');
    c1->data.attendanceRoot = insertAttendance(c1->data.attendanceRoot, "2026-01-17", 'A');
    c1->data.attendanceRoot = insertAttendance(c1->data.attendanceRoot, "2026-01-24", 'P');

    CourseNode* c2 = findCourse(courseRoot, "CNPM");
    c2->data.attendanceRoot = insertAttendance(c2->data.attendanceRoot, "2026-01-11", 'P');
    c2->data.attendanceRoot = insertAttendance(c2->data.attendanceRoot, "2026-01-18", 'L');
int choice;
    string courseID;

    do {
        cout << "\n===== STUDENT MODULE =====\n";
        cout << "1. View Attendance History\n";
        cout << "2. View Attendance Summary\n";
        cout << "0. Exit\n";
        cout << "Choose: ";
        cin >> choice;

        if (choice == 1) {
            cout << "Enter Course ID: ";
            cin >> courseID;
            CourseNode* c = findCourse(courseRoot, courseID);
            if (!c) 
                cout << "Course not found!\n";
            else {
                cout << "\n--- Attendance History ---\n";
                inorderAttendance(c->data.attendanceRoot);
            }
        }

        else if (choice == 2) {
            cout << "Enter Course ID: ";
            cin >> courseID;
            CourseNode* c = findCourse(courseRoot, courseID);
            if (!c) 
                cout << "Course not found!\n";
            else {
                int p=0,a=0,l=0;
                countSummary(c->data.attendanceRoot, p,a,l);
                cout << "\n--- Attendance Summary ---\n";
                cout << "Present: " << p << endl;
                cout << "Absent : " << a << endl;
                cout << "Late   : " << l << endl;
            }
        }

    } while (choice != 0);

    return 0;
}
