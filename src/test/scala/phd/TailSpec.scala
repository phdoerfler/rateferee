package phd

import org.specs2._
import Aux._
import org.scalacheck._

class TailSpec extends Specification with ScalaCheck { def is = s2"""
  A functionality like `tail` can also be defined for lists containing
  other lists, e.g. List[List[Int]].

  This `tail` throws an error in case the list is empty $ex1.
  If the list contains one list, which is also empty, it should also fail $ex2.
  In general the following property should hold: tail(nestedLists).flatten == nestedLists.flatten.tail $ex3.
  This holds true for simple cases $ex4.
  It also holds true for partially empty nested lists $ex5.
"""

  def ex1 = {
    tail(List()) must throwAn[UnsupportedOperationException]
  }

  def ex2 = {
    tail(List(List())) must throwAn[UnsupportedOperationException]
  }

  def ex3 = {
    prop {
      (nested: List[List[Int]]) => nested.flatten.nonEmpty ==> (tail(nested).flatten must_== nested.flatten.tail)
    }
  }

  def ex4 = {
    tail(List(List(1, 2, 3), List(4, 5, 6))) must_== List(List(2, 3), List(4, 5, 6))
  }

  def ex5 = {
    tail(List(List(), List(4, 5, 6))) must_== List(List(5, 6))
  }

}
