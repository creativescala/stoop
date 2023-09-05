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

package stoop.parse

class FunctionSuite extends ParserSuite {
  import Function.*

  test("lambda expression") {
    assertSuccess(
      parser.parse("(x) => x + 2"),
      Lambda("x", Add(Name("x"), Integer(2)))
    )
    assertSuccess(
      parser.parse("(x) => (y) => x + y"),
      Lambda("x", Lambda("y", Add(Name("x"), Name("y"))))
    )
  }

  test("apply expression") {
    assertSuccess(
      parser.parse("f(1)"),
      Apply(Name("f"), Integer(1))
    )
    // Function returning a function
    assertSuccess(
      parser.parse("f(1)(2)"),
      Apply(Apply(Name("f"), Integer(1)), Integer(2))
    )
    assertSuccess(
      parser.parse("((x) => x + 1)(2)"),
      Apply(Parens(Lambda("x", Add(Name("x"), Integer(1)))), Integer(2))
    )
  }
}
