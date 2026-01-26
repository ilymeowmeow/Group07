#pragma once
#pragma once
#include <iostream>
#include <vector>
#include <string>

using namespace std;

struct User {
    string username;
    string role;
};

class AdminModule {
private:
    vector<User> users;
    bool isDuplicate(string username);

public:
    AdminModule();
    bool login();
    void adminMenu();
    void viewUsers();
    void addUser();
    void editUser();
    void deleteUser();
};
