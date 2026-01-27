#include "TeacherModule.h"

/* =========================
   TEST CASE FUNCTIONS
   ========================= */

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
