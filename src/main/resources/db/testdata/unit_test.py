import assignment_main
import unittest


class TestMethods(unittest.TestCase):

  def test_1(self):
    self.assertTrue(assignment_main.fooNoRepetido())

  def test_2(self):
    self.assertTrue(assignment_main.barNoRepetido())
