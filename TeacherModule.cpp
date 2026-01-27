#include "TeacherModule.h"

/* =======================
   TEST CASE FUNCTIONS
   ======================= */

void addTestCase(vector<TestCase>& list,
                 int id,
                 string description,
                 string expectedResult) {
    TestCase tc;
    tc.testCaseID = id;
    tc.description = description;
    tc.expectedResult = expectedResult;
    tc.actualResult = "";
    tc.status = "Not Run";
    list.push_back(tc);
}

void updateTestResult(vector<TestCase>& list,
                      int id,
                      string actualResult,
                      string status) {
    for (auto& tc : list) {
        if (tc.testCaseID == id) {
            tc.actualResult = actualResult;
            tc.status = status;
            return;
        }
    }
}

TestCase* findTestCase(vector<TestCase>& list, int id) {
    for (auto& tc : list) {
        if (tc.testCaseID == id) {
            return &tc;
        }
    }
    return nullptr;
}

/* =======================
   BUG TRACKING FUNCTIONS
   ======================= */

void addBug(vector<Bug>& bugs,
            int id,
            string description,
            string severity) {
    Bug bug;
    bug.bugID = id;
    bug.description = description;
    bug.severity = severity;
    bug.status = "Open";
    bugs.push_back(bug);
}

void updateBugStatus(vector<Bug>& bugs,
                     int id,
                     string status) {
    for (auto& bug : bugs) {
        if (bug.bugID == id) {
            bug.status = status;
            return;
        }
    }
}

Bug* findBug(vector<Bug>& bugs, int id) {
    for (auto& bug : bugs) {
        if (bug.bugID == id) {
            return &bug;
        }
    }
    return nullptr;
}
