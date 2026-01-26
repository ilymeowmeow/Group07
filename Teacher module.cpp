#include <iostream>
#include <string>
using namespace std;

/* ===== Node danh sách sinh viên ===== */

struct StudentNode {
    string studentID;
    string name;
    char status; // P / A / L
    StudentNode* next;
};

typedef StudentNode* StudentList;

/* ===== Khởi tạo danh sách ===== */
void initList(StudentList &l) {
    l = NULL;
}

/* ===== Tạo node sinh viên ===== */
StudentNode* createStudent(string id, string name) {
    StudentNode* p = new StudentNode;
    p->studentID = id;
    p->name = name;
    p->status = 'P'; // mặc định Present
    p->next = NULL;
    return p;
}

/* ===== Thêm sinh viên vào danh sách ===== */
void addStudent(StudentList &l, string id, string name) {
    StudentNode* p = createStudent(id, name);
    if (!l) l = p;
    else {
        StudentNode* temp = l;
        while (temp->next) temp = temp->next;
        temp->next = p;
    }
}

/* ===== Giáo viên điểm danh ===== */
void markAttendance(StudentList &l) {
    StudentNode* temp = l;
    cout << "\n--- Mark Attendance ---\n";
    while (temp) {
        cout << "SV: " << temp->studentID << " - " << temp->name;
        cout << " (P/A/L): ";
        cin >> temp->status;
        temp = temp->next;
    }
    cout << "Da luu diem danh!\n";
}

/* ===== Xem danh sách điểm danh ===== */
void viewAttendanceList(StudentList l) {
    cout << "\n--- Attendance List ---\n";
    cout << "ID\tName\tStatus\n";
    StudentNode* temp = l;
    while (temp) {
        cout << temp->studentID << "\t" << temp->name << "\t" << temp->status << endl;
        temp = temp->next;
    }
}

/* ===== Tìm sinh viên ===== */
void searchStudent(StudentList l, string id) {
    StudentNode* temp = l;
    while (temp) {
        if (temp->studentID == id) {
            cout << "Tim thay: " << temp->name 
                 << " - Status: " << temp->status << endl;
            return;
        }
        temp = temp->next;
    }
    cout << "Khong tim thay sinh vien!\n";
}

/* ===== Thống kê vắng ===== */
void absenceStatistics(StudentList l) {
    int absent = 0;
    StudentNode* temp = l;
    while (temp) {
        if (temp->status == 'A') absent++;
        temp = temp->next;
    }
    cout << "Tong so sinh vien vang: " << absent << endl;
}

/* ===== MAIN DEMO ===== */
int main() {
    StudentList lớp;
    initList(lớp);

    // Giả lập danh sách sinh viên của lớp
    addStudent(lớp, "SV01", "Nguyen Van A");
    addStudent(lớp, "SV02", "Tran Thi B");
    addStudent(lớp, "SV03", "Le Van C");

    int choice;
    string id;

    do {
        cout << "\n===== TEACHER MODULE =====\n";
        cout << "1. Mark Attendance\n";
        cout << "2. View Attendance List\n";
        cout << "3. Search Student\n";
        cout << "4. View Absence Statistics\n";
        cout << "0. Exit\n";
        cout << "Chon: ";
        cin >> choice;

        switch(choice) {
            case 1:
                markAttendance(lớp);
                break;
            case 2:
                viewAttendanceList(lớp);
                break;
            case 3:
                cout << "Nhap ma SV: ";
                cin >> id;
                searchStudent(lớp, id);
                break;
            case 4:
                absenceStatistics(lớp);
                break;
        }

    } while (choice != 0);

    return 0;
}
