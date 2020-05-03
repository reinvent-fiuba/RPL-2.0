import unittest

import assignment_main


class TestMethods(unittest.TestCase):

  def test_1(self):
    self.assertEqual(assignment_main.fooNoRepetido(), 1)

  def test_2(self):
    self.assertEqual(assignment_main.barNoRepetido(), 2)
