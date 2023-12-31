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

class ArithmeticSuite extends ParserSuite {
  import Arithmetic.*

  test("integer literals") {
    assertSuccess(parser.parse("1"), Integer(1))
    assertSuccess(parser.parse("10"), Integer(10))
    assertSuccess(parser.parse("99"), Integer(99))
    assertSuccess(parser.parse("999"), Integer(999))
    assertSuccess(parser.parse("12345"), Integer(12345))
    assertSuccess(parser.parse("-1"), Integer(-1))
    assertSuccess(parser.parse("-12345"), Integer(-12345))
  }

  test("addition and subtraction") {
    assertSuccess(parser.parse("1 + 99"), Add(Integer(1), Integer(99)))
    assertSuccess(parser.parse("10 - 32"), Sub(Integer(10), Integer(32)))
    assertSuccess(
      parser.parse("99 - 45 + 8"),
      Add(Sub(Integer(99), Integer(45)), Integer(8))
    )
    assertSuccess(
      parser.parse("999 + 7 - 3"),
      Sub(Add(Integer(999), Integer(7)), Integer(3))
    )
  }

  test("operator precedence") {
    assertSuccess(
      parser.parse("1 + 9 * 7"),
      Add(Integer(1), Mul(Integer(9), Integer(7)))
    )
    assertSuccess(
      parser.parse("1 * 9 + 7"),
      Add(Mul(Integer(1), Integer(9)), Integer(7))
    )
  }
}
