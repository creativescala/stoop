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

class ExpressionSuite extends ParserSuite {
  import Expression.*

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

  test("equality") {
    assertSuccess(expr.parse("1 == 10"), Eq(Integer(1), Integer(10)))
  }
}
