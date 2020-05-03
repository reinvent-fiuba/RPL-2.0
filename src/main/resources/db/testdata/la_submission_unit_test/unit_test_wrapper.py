import json
import os
import unittest

import unit_test


class RplTestResult(unittest.TestResult):

  def __init__(self, stream=None, descriptions=None, verbosity=None):
    super().__init__(stream, descriptions, verbosity)
    self.passed = []

  def addSuccess(self, test):
    self.passed.append(test)
    # self.testResults.append(
    #   {"name": test._testMethodName, "success": True, "description": "passed"})
    super().addSuccess(test)

  # def addError(self, test, err):
  #     self.testResults.append(
  #         {"name": test._testMethodName, "success": False, "description": str(err)})
  #     super(RplTestResult, self).addError(test, err)
  #
  def addFailure(self, test, err):
    print({"name": test._testMethodName, "success": False,
           "description": f"failure: {str(err)}"})
    super().addFailure(test, err)

  #
  # def addSkip(self, test, reason):
  #     self.testResults.append({"name": test._testMethodName, "success": False,
  #                              "description": f"skipped: {str(reason)}"})
  #     super(RplTestResult, self).addSkip(test, reason)
  #
  # def addExpectedFailure(self, test, err):
  #     self.testResults.append(
  #         {"name": test._testMethodName, "success": False, "description": str(err)})
  #     super(RplTestResult, self).addExpectedFailure(test, err)
  #
  # def startTestRun(self):
  #     super(RplTestResult, self).startTestRun()

  def stopTestRun(self):
    tests = [
      {"name": test._testMethodName, "status": "PASSED", "messages": None} for
      test in self.passed]

    tests.extend(
      [{"name": test._testMethodName, "status": "FAILED", "messages": exception}
       for test, exception in
       self.failures])

    tests.extend(
      [{"name": test._testMethodName, "status": "ERROR", "messages": exception}
       for test, exception in
       self.errors])

    tests.extend(
      [{"name": test._testMethodName, "status": "SKIPPED", "messages": reason}
       for test, reason in
       self.skipped])

    result = {
      "passed": len(self.passed),
      "failed": len(self.failures),
      "errored": len(self.errors),
      'tests': tests
    }

    print(json.dumps(result, indent=4))

    super().stopTestRun()


if __name__ == '__main__':
  nullf = open(os.devnull, 'w')

  testToRun = unittest.TextTestRunner(stream=nullf, descriptions=True,
                                      verbosity=2, failfast=False, buffer=False,
                                      resultclass=RplTestResult)
  suite = unittest.TestLoader().loadTestsFromTestCase(unit_test.TestMethods)
  testToRun.run(suite)
