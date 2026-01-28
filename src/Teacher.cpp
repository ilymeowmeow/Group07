#include "Teacher.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <unordered_set>
#include <unordered_map>
#include <algorithm>
#include <iomanip>

using std::string;

static inline string trim(string s) {
    auto notSpace = [](unsigned char c){ return !std::isspace(c); };
    s.erase(s.begin(), std::find_if(s.begin(), s.end(), notSpace));
    s.erase(std::find_if(s.rbegin(), s.rend(), notSpace).base(), s.end());
    return s;
}

static std::vector<string> splitCSVLine(const string& line) {
    std::vector<string> out;
    std::stringstream ss(line);
    string cell;
    while (std::getline(ss, cell, ',')) out.push_back(trim(cell));
    return out;
}

struct AttendanceRow {
    string courseId, date, studentId, status;
};

static bool isHeaderLike(const std::vector<string>& cols, const std::vector<string>& expect) {
    if (cols.size() < expect.size()) return false;
    for (size_t i = 0; i < expect.size(); i++) {
        string a = cols[i], b = expect[i];
        std::transform(a.begin(), a.end(), a.begin(), ::tolower);
        std::transform(b.begin(), b.end(), b.begin(), ::tolower);
        if (a != b) return false;
    }
    return true;
}

static std::unordered_set<string> loadStudentsOfClass(const string& classId) {
    std::unordered_set<string> ids;
    std::ifstream f("data/students.csv");
    if (!f.is_open()) return ids;

    string line;
    bool first = true;
    while (std::getline(f, line)) {
        if (trim(line).empty()) continue;
        auto cols = splitCSVLine(line);
        if (cols.size() < 3) continue;

        if (first && isHeaderLike(cols, {"studentId","name","classId"})) { first = false; continue; }
        first = false;

        string studentId = cols[0];
        string cls = cols[2];
        if (cls == classId) ids.insert(studentId);
    }
    return ids;
}

static std::unordered_set<string> loadCoursesOfStudents(const std::unordered_set<string>& studentIds) {

    std::unordered_set<string> courseIds;
    std::ifstream f("data/enrollments.csv");
    if (!f.is_open()) return courseIds;

    string line;
    bool first = true;
    while (std::getline(f, line)) {
        if (trim(line).empty()) continue;
        auto cols = splitCSVLine(line);
        if (cols.size() < 2) continue;

        if (first && isHeaderLike(cols, {"studentId","courseId"})) { first = false; continue; }
        first = false;
        string sid = cols[0];
        string cid = cols[1];
        if (studentIds.count(sid)) courseIds.insert(cid);
    }
    return courseIds;
}

static std::vector<AttendanceRow> loadAttendanceAll() {
    std::vector<AttendanceRow> rows;
    std::ifstream f("data/attendance.csv");
    if (!f.is_open()) return rows;

    string line;
    bool first = true;
    while (std::getline(f, line)) {
        if (trim(line).empty()) continue;
        auto cols = splitCSVLine(line);
        if (cols.size() < 4) continue;

        if (first && isHeaderLike(cols, {"courseId","date","studentId","status"})) { first = false; continue; }
        first = false;

        AttendanceRow r{cols[0], cols[1], cols[2], cols[3]};
        rows.push_back(r);
    }
    return rows;
}

static void writeAttendanceAll(const std::vector<AttendanceRow>& rows) {
    std::ofstream out("data/attendance.csv", std::ios::trunc);
    out << "courseId,date,studentId,status\n";
    for (auto &r : rows) {
        out << r.courseId << "," << r.date << "," << r.studentId << "," << r.status << "\n";
    }
}

static bool validStatus(const string& s) {
    return s == "P" || s == "A" || s == "L";
}

Teacher::Teacher(std::string id, std::string name)
    : teacherId(std::move(id)), teacherName(std::move(name)),
      selectedClassId(""), selectedDate(""), selectedCourseId("") {}

void Teacher::selectClassAndDate(std::string classId, std::string date) {
    selectedClassId = std::move(classId);
    selectedDate = std::move(date);

    auto students = loadStudentsOfClass(selectedClassId);
    if (students.empty()) {
        std::cout << "No students found for class " << selectedClassId << " (check data/students.csv)\n";
        selectedCourseId.clear();
        return;
    }

    auto courses = loadCoursesOfStudents(students);
    if (courses.empty()) {
        std::cout << "No courses found for this class (check data/enrollments.csv)\n";
        selectedCourseId.clear();
        return;
    }

    std::cout << "Selected class: " << selectedClassId << ", date: " << selectedDate << "\n";
    std::cout << "Courses available for this class: ";
    for (auto &c : courses) std::cout << c << " ";
    std::cout << "\n";
    std::cout << "Enter courseId to work with (or leave empty to skip): ";
    std::getline(std::cin >> std::ws, selectedCourseId);
    selectedCourseId = trim(selectedCourseId);
}

void Teacher::viewAttendanceList(std::string classId) {
    auto studentIds = loadStudentsOfClass(classId);
    if (studentIds.empty()) {
        std::cout << "No students for class " << classId << "\n";
        return;
    }

    auto rows = loadAttendanceAll();

    std::cout << "\n=== Attendance list for class " << classId << " ===\n";
    std::cout << std::left
              << std::setw(10) << "Course"
              << std::setw(14) << "Date"
              << std::setw(10) << "Student"
              << std::setw(8)  << "Status" << "\n";

    bool any = false;
    for (auto &r : rows) {
        if (!studentIds.count(r.studentId)) continue;
        std::cout << std::left
                  << std::setw(10) << r.courseId
                  << std::setw(14) << r.date
                  << std::setw(10) << r.studentId
                  << std::setw(8)  << r.status << "\n";
        any = true;
    }
    if (!any) std::cout << "No attendance records for this class.\n";
}

void Teacher::markAttendance(std::string classId, std::string date) {
    auto studentIds = loadStudentsOfClass(classId);
    if (studentIds.empty()) {
        std::cout << "No students for class " << classId << "\n";
        return;
    }

    string courseId = selectedCourseId;
    if (courseId.empty()) {
        std::cout << "Enter courseId to mark attendance (e.g., OS/SE/CTDL): ";
        std::getline(std::cin >> std::ws, courseId);
        courseId = trim(courseId);
    }

    if (courseId.empty()) {
        std::cout << "courseId is required.\n";
        return;
    }

    auto all = loadAttendanceAll();

    std::cout << "\nMark attendance - class: " << classId << ", date: " << date
              << ", course: " << courseId << "\n";
    std::cout << "Status: P=Present, A=Absent, L=Late\n";

    for (const auto& sid : studentIds) {
        string st;
        std::cout << "Student " << sid << " status (P/A/L): ";
        std::getline(std::cin >> std::ws, st);
        st = trim(st);
        std::transform(st.begin(), st.end(), st.begin(), ::toupper);
        if (!validStatus(st)) st = "P";

        bool updated = false;
        for (auto &r : all) {
            if (r.courseId == courseId && r.date == date && r.studentId == sid) {
                r.status = st;
                updated = true;
                break;
            }
        }
        if (!updated) all.push_back({courseId, date, sid, st});
    }

    writeAttendanceAll(all);
    std::cout << "Marked attendance saved to data/attendance.csv\n";
}

void Teacher::updateAttendance(std::string classId, std::string date) {
    auto studentIds = loadStudentsOfClass(classId);
    if (studentIds.empty()) {
        std::cout << "No students for class " << classId << "\n";
        return;
    }

    string courseId = selectedCourseId;
    if (courseId.empty()) {
        std::cout << "Enter courseId to update: ";
        std::getline(std::cin >> std::ws, courseId);
        courseId = trim(courseId);
    }

    auto all = loadAttendanceAll();

    std::cout << "\nUpdate attendance - class: " << classId << ", date: " << date
              << ", course: " << courseId << "\n";
    std::cout << "Enter studentId to update (e.g., SV001): ";
    string sid;
    std::getline(std::cin >> std::ws, sid);
    sid = trim(sid);

    if (!studentIds.count(sid)) {
        std::cout << "Student " << sid << " is not in class " << classId << "\n";
        return;
    }

    std::cout << "New status (P/A/L): ";
    string st;
    std::getline(std::cin >> std::ws, st);
    st = trim(st);
    std::transform(st.begin(), st.end(), st.begin(), ::toupper);
    if (!validStatus(st)) {
        std::cout << "Invalid status, use P/A/L.\n";
        return;
    }

    bool ok = false;
    for (auto &r : all) {
        if (r.courseId == courseId && r.date == date && r.studentId == sid) {
            r.status = st;
            ok = true;
            break;
        }
    }
    if (!ok) {
        std::cout << "Record not found. Creating new record.\n";
        all.push_back({courseId, date, sid, st});
    }

    writeAttendanceAll(all);
    std::cout << "Update saved.\n";
}

void Teacher::searchStudentAttendance(std::string keyword) {
    keyword = trim(keyword);
    if (keyword.empty()) {
        std::cout << "Keyword empty.\n";
        return;
    }

    auto rows = loadAttendanceAll();

    std::cout << "\n=== Search results for: " << keyword << " ===\n";
    bool any = false;
    for (auto &r : rows) {
        if (r.courseId.find(keyword) != string::npos ||
            r.date.find(keyword) != string::npos ||
            r.studentId.find(keyword) != string::npos ||
            r.status.find(keyword) != string::npos) {
            std::cout << r.courseId << "," << r.date << "," << r.studentId << "," << r.status << "\n";
            any = true;
        }
    }
    if (!any) std::cout << "No match.\n";
}

void Teacher::viewAbsenceStatistics(std::string classId) {
    auto studentIds = loadStudentsOfClass(classId);
    if (studentIds.empty()) {
        std::cout << "No students for class " << classId << "\n";
        return;
    }

    auto rows = loadAttendanceAll();
    std::unordered_map<string, int> absent, late, present;

    for (auto &r : rows) {
        if (!studentIds.count(r.studentId)) continue;
        if (r.status == "A") absent[r.studentId]++;
        else if (r.status == "L") late[r.studentId]++;
        else present[r.studentId]++;
    }

    std::cout << "\n=== Absence statistics for class " << classId << " ===\n";
    std::cout << std::left
              << std::setw(10) << "Student"
              << std::setw(8)  << "P"
              << std::setw(8)  << "A"
              << std::setw(8)  << "L" << "\n";

    for (auto &sid : studentIds) {
        std::cout << std::left
                  << std::setw(10) << sid
                  << std::setw(8)  << present[sid]
                  << std::setw(8)  << absent[sid]
                  << std::setw(8)  << late[sid] << "\n";
    }
}

void Teacher::exportAttendanceCSV(std::string classId) {
    auto studentIds = loadStudentsOfClass(classId);
    if (studentIds.empty()) {
        std::cout << "No students for class " << classId << "\n";
        return;
    }

    auto rows = loadAttendanceAll();

    string outPath = "data/export_" + classId + "_attendance.csv";
    std::ofstream out(outPath, std::ios::trunc);
    out << "courseId,date,studentId,status\n";

    int count = 0;
    for (auto &r : rows) {
        if (!studentIds.count(r.studentId)) continue;
        out << r.courseId << "," << r.date << "," << r.studentId << "," << r.status << "\n";
        count++;
    }

    std::cout << "Exported " << count << " rows to " << outPath << "\n";
}
