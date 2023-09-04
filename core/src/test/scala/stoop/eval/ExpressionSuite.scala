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

package stoop.eval

class ExpressionSuite extends EvalSuite {
  test("basic expressions") {
    assertSuccess(Expression.run(" 1 + 2"), 3)
    assertSuccess(Expression.run("1 -  2"), -1)
    assertSuccess(Expression.run("1 * 2"), 2)
    assertSuccess(Expression.run("4 / 2"), 2)
  }

  test("compound expressions") {
    assertSuccess(Expression.run("1 + 2 * 3"), 7)
    assertSuccess(Expression.run("(1 + 2) * 3"), 9)
    assertSuccess(Expression.run("3 - 2 + 4"), 5)
  }

  test("inequalities") {
    assertSuccess(Expression.run("1 < 3"), true)
    assertSuccess(Expression.run("1 < 1"), false)
    assertSuccess(Expression.run("3 < 1"), false)
    assertSuccess(Expression.run("3 > 1"), true)
    assertSuccess(Expression.run("3 > 3"), false)
    assertSuccess(Expression.run("1 > 3"), false)

    assertSuccess(Expression.run("1 <= 3"), true)
    assertSuccess(Expression.run("1 <= 1"), true)
    assertSuccess(Expression.run("3 <= 1"), false)
    assertSuccess(Expression.run("3 >= 1"), true)
    assertSuccess(Expression.run("3 >= 3"), true)
    assertSuccess(Expression.run("1 >= 3"), false)
  }

  test("equality") {
    assertSuccess(Expression.run("1 == 1"), true)
    assertSuccess(Expression.run("3 == 1"), false)

    assertSuccess(Expression.run("true == true"), true)
    assertSuccess(Expression.run("false == false"), true)
    assertSuccess(Expression.run("true == false"), false)

    assertSuccess(Expression.run("1 == false"), false)
    assertSuccess(Expression.run("true == 3"), false)

    assertSuccess(Expression.run("true == (1 == 1)"), true)
  }
}
