#include "AuthService.h"
#include <iostream>

AuthService::AuthService()
    : demoUsername("group07"),
      demoPassword("123456"),
      loggedIn(false),
      currentRole(Role::None) {}

bool AuthService::login(const std::string& username, const std::string& password) {
    if (username == demoUsername && password == demoPassword) {
        loggedIn = true;
        currentRole = Role::None; 
        return true;
    }
    return false;
}

void AuthService::logout() {
    loggedIn = false;
    currentRole = Role::None;
}

bool AuthService::isLoggedIn() const {
    return loggedIn;
}

Role AuthService::getRole() const {
    return currentRole;
}

void AuthService::selectRole() {
    if (!loggedIn) {
        std::cout << "Please login first.\n";
        return;
    }

    int choice;
    std::cout << "\nSelect your role:\n";
    std::cout << "1. Admin\n";
    std::cout << "2. Teacher\n";
    std::cout << "3. Student\n";
    std::cout << "Choice: ";
    std::cin >> choice;

    switch (choice) {
        case 1: currentRole = Role::Admin; break;
        case 2: currentRole = Role::Teacher; break;
        case 3: currentRole = Role::Student; break;
        default:
            currentRole = Role::None;
            std::cout << "Invalid role selected.\n";
            return;
    }

    std::cout << "Logged in as role: " << roleToString(currentRole) << "\n";
}

bool AuthService::canAccessAdminFeature() const {
    return loggedIn && currentRole == Role::Admin;
}

bool AuthService::canAccessAttendanceFeature() const {
    return loggedIn && (currentRole == Role::Admin || currentRole == Role::Teacher);
}

std::string AuthService::roleToString(Role r) {
    switch (r) {
        case Role::Admin: return "Admin";
        case Role::Teacher: return "Teacher";
        case Role::Student: return "Student";
        default: return "None";
    }
}
