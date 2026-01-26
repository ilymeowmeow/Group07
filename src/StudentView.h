#ifndef STUDENT_VIEW_H
#define STUDENT_VIEW_H

#include <iostream>
#include <string>
using namespace std;

struct Attendance {
    string date;
    char status; // P, A, L
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

void inorderAttendance(AttNode* root);
void countSummary(AttNode* root, int& p, int& a, int& l);

CourseNode* findCourse(CourseNode* root, string id);

void viewAttendanceHistory(CourseNode* courseRoot, string courseID);
void viewAttendanceSummary(CourseNode* courseRoot, string courseID);

#endif
