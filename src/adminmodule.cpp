#include "AdminModule.h"

// Constructor
AdminModule::AdminModule() {
    users.push_back({ "admin", "ADMIN" });
    users.push_back({ "student01", "STUDENT" });
    users.push_back({ "teacher01", "TEACHER" });
}

bool AdminModule::login() {
    string username, password;
    cout << "Username: ";
    cin >> username;
    cout << "Password: ";
    cin >> password;

    if (username == "admin" && password == "admin123") {
        cout << "Login successful!\n";
        return true;
    }
    cout << "Login failed!\n";
    return false;
}

bool AdminModule::isDuplicate(string username) {
    for (auto& u : users) {
        if (u.username == username)
            return true;
    }
    return false;
}

void AdminModule::viewUsers() {
    cout << "\n--- USER LIST ---\n";
    for (auto& u : users) {
        cout << "Username: " << u.username
            << " | Role: " << u.role << endl;
    }
}

void AdminModule::addUser() {
    User u;
    cout << "Enter username: ";
    cin >> u.username;

    if (isDuplicate(u.username)) {
        cout << "Username already exists!\n";
        return;
    }

    cout << "Enter role: ";
    cin >> u.role;
    users.push_back(u);
    cout << "User added!\n";
}

void AdminModule::editUser() {
    string name;
    cout << "Enter username to edit: ";
    cin >> name;

    for (auto& u : users) {
        if (u.username == name) {
            cout << "Enter new role: ";
            cin >> u.role;
            cout << "Updated!\n";
            return;
        }
    }
    cout << "User not found!\n";
}

void AdminModule::deleteUser() {
    string name;
    cout << "Enter username to delete: ";
    cin >> name;

    if (name == "admin") {
        cout << "Cannot delete admin!\n";
        return;
    }

for (size_t i = 0; i < users.size(); i++) {
    if (users[i].username == name) {
        users.erase(users.begin() + i);
        cout << "Deleted!\n";
        return;
    }
}
    cout << "User not found!\n";
}

void AdminModule::adminMenu() {
    int choice;
    do {
        cout << "\n--- ADMIN MENU ---\n";
        cout << "1. View Users\n";
        cout << "2. Add User\n";
        cout << "3. Edit User\n";
        cout << "4. Delete User\n";
        cout << "5. Logout\n";
        cout << "Choose: ";
        cin >> choice;

        switch (choice) {
        case 1: viewUsers(); break;
        case 2: addUser(); break;
        case 3: editUser(); break;
        case 4: deleteUser(); break;
        case 5: cout << "Logout!\n"; break;
        default: cout << "Invalid!\n";
        }
    } while (choice != 5);
}
