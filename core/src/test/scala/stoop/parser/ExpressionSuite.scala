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

package stoop.parser

import munit.FunSuite
import munit.Location
import parsley.Result
import parsley.Success

class ExpressionSuite extends FunSuite {
  import Expression.*

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

  test("boolean literals") {
    assertSuccess(expr.parse("true"), Bool(true))
    assertSuccess(expr.parse("false"), Bool(false))
  }

  test("inequalities") {
    assertSuccess(expr.parse("1 < 99"), Lt(Integer(1), Integer(99)))
    assertSuccess(expr.parse("10 > 32"), Gt(Integer(10), Integer(32)))
    assertSuccess(expr.parse("1 <= 99"), LtEq(Integer(1), Integer(99)))
    assertSuccess(expr.parse("10 >= 32"), GtEq(Integer(10), Integer(32)))
  }
}
