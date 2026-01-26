#ifndef TEACHER_MODULE_H
#define TEACHER_MODULE_H

#include <string>
#include <vector>

using namespace std;

/* =======================
   STRUCT / DATA MODEL
   ======================= */

struct TestCase {
    int testCaseID;
    string description;
    string expectedResult;
    string actualResult;
    string status; // Pass / Fail
};

struct Bug {
    int bugID;
    string description;
    string severity; // Low / Medium / High
    string status;   // Open / Fixed / Closed
};

/* =======================
   TEACHER MODULE API
   ======================= */

// Test case management
void addTestCase(vector<TestCase>& list,
                 int id,
                 string description,
                 string expectedResult);

void updateTestResult(vector<TestCase>& list,
                      int id,
                      string actualResult,
                      string status);

TestCase* findTestCase(vector<TestCase>& list, int id);

// Bug tracking
void addBug(vector<Bug>& bugs,
            int id,
            string description,
            string severity);

void updateBugStatus(vector<Bug>& bugs,
                     int id,
                     string status);

Bug* findBug(vector<Bug>& bugs, int id);

#endif
