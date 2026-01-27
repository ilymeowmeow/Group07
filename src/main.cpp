#include <iostream>
#include "AuthService.h"
#include "adminmodule.h"
#include "Teacher.h"
#include "StudentView.h"

using namespace std;

int main() {
    AuthService auth;
    AdminModule admin;
    Teacher teacher("T001", "Tran Thi Cam Tien");

    int choice;
cout << "1. Login\n";
cout << "0. Exit\n";
cout << "Choose: ";
cin >> choice;

if (choice == 1) {
    // login
} else {
    cout << "Exit program\n";
    return 0;
}
    string username, password;
    cout << "Username: ";
    cin >> username;
    cout << "Password: ";
    cin >> password;

    if (!auth.login(username, password)) {
        cout << "Login failed!\n";
        return 0;
    }

    cout << "Login success!\n";

    auth.selectRole();
    Role role = auth.getRole();

    if (role == Role::Admin) {
        cout << "\n[ADMIN MODE]\n";
        admin.adminMenu();
    }
    else if (role == Role::Teacher) {
        cout << "\n[TEACHER MODE]\n";

        teacher.selectClassAndDate("CSE101", "2026-01-01");
        teacher.viewAttendanceList("CSE1011");
    }
    else if (role == Role::Student) {
        cout << "\n[STUDENT MODE]\n";
        cout << "Student is viewing attendance...\n";
    }
    else {
        cout << "Invalid role!\n";
    }

    auth.logout();
    cout << "\nProgram ended.\n";
    return 0;
}
