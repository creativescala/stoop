/*
 * Copyright 2023 Creative Scala
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stoop

import munit.FunSuite
import munit.Location
import parsley.Result
import parsley.Success

class ArithmeticSuite extends FunSuite {
  import Arithmetic.*

  def assertSuccess[A](result: Result[?, Expr], expected: Expr)(using
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

  test("integer literals") {
    assertSuccess(expr.parse("1"), Integer(1))
    assertSuccess(expr.parse("10"), Integer(10))
    assertSuccess(expr.parse("99"), Integer(99))
    assertSuccess(expr.parse("999"), Integer(999))
    assertSuccess(expr.parse("12345"), Integer(12345))
    assertSuccess(expr.parse("-1"), Integer(-1))
    assertSuccess(expr.parse("-12345"), Integer(-12345))
  }
}
