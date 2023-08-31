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

class ArithmeticSuite extends EvalSuite {
  test("basic expressions") {
    assertSuccess(Arithmetic.run(" 1 + 2"), 3)
    assertSuccess(Arithmetic.run("1 -  2"), -1)
    assertSuccess(Arithmetic.run("1 * 2"), 2)
    assertSuccess(Arithmetic.run("4 / 2"), 2)
  }

  test("compound expressions") {
    assertSuccess(Arithmetic.run("1 + 2 * 3"), 7)
    assertSuccess(Arithmetic.run("(1 + 2) * 3"), 9)
    assertSuccess(Arithmetic.run("3 - 2 + 4"), 5)
  }
}
