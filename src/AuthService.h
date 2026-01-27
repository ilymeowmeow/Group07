#pragma once
#include <string>

enum class Role {
    Admin,
    Teacher,
    Student,
    None
};
class AuthService {
private:
    std::string demoUsername;
    std::string demoPassword;

    bool loggedIn;
    Role currentRole;
public:
    AuthService();
    bool login(const std::string& username, const std::string& password);
    void logout();
    bool isLoggedIn() const;
    Role getRole() const;
    void selectRole();
    bool canAccessAdminFeature() const;
    bool canAccessAttendanceFeature() const;
    static std::string roleToString(Role r);
};
