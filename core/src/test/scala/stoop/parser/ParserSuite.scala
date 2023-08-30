package stoop.parser

import munit.FunSuite
import munit.Location
import parsley.Result
import parsley.Success

/** Abstraction to make writing parser tests easier. */
class ParserSuite extends FunSuite {

  /** Assert that a result is success containing the given value. */
  def assertSuccess[A](result: Result[?, A], expected: A)(using
      loc: Location
  ) =
    result match {
      case Success(x) =>
        if x == expected then ()
        else
          fail(
            s"Parser succeeded with unexpected result. Expected $expected but got $x"
          )
      case other => fail(s"Parser failed with failure $other")
    }
}
