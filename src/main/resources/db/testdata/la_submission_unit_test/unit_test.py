import unittest

import assignment_main


class TestMethods(unittest.TestCase):

  def test_1(self):
    self.assertTrue(assignment_main.fooNoRepetido())

  def test_2(self):
    self.assertTrue(assignment_main.barNoRepetido())
