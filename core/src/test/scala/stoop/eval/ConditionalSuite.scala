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

class ConditionalSuite extends EvalSuite {
  test("basic expressions") {
    assertSuccess(Conditional.run(" 1 + 2"), 3)
    assertSuccess(Conditional.run("1 -  2"), -1)
    assertSuccess(Conditional.run("1 * 2"), 2)
    assertSuccess(Conditional.run("4 / 2"), 2)
  }

  test("compound expressions") {
    assertSuccess(Conditional.run("1 + 2 * 3"), 7)
    assertSuccess(Conditional.run("(1 + 2) * 3"), 9)
    assertSuccess(Conditional.run("3 - 2 + 4"), 5)
  }

  test("inequalities") {
    assertSuccess(Conditional.run("1 < 3"), true)
    assertSuccess(Conditional.run("1 < 1"), false)
    assertSuccess(Conditional.run("3 < 1"), false)
    assertSuccess(Conditional.run("3 > 1"), true)
    assertSuccess(Conditional.run("3 > 3"), false)
    assertSuccess(Conditional.run("1 > 3"), false)

    assertSuccess(Conditional.run("1 <= 3"), true)
    assertSuccess(Conditional.run("1 <= 1"), true)
    assertSuccess(Conditional.run("3 <= 1"), false)
    assertSuccess(Conditional.run("3 >= 1"), true)
    assertSuccess(Conditional.run("3 >= 3"), true)
    assertSuccess(Conditional.run("1 >= 3"), false)
  }

  test("equality") {
    assertSuccess(Conditional.run("1 == 1"), true)
    assertSuccess(Conditional.run("3 == 1"), false)

    assertSuccess(Conditional.run("true == true"), true)
    assertSuccess(Conditional.run("false == false"), true)
    assertSuccess(Conditional.run("true == false"), false)

    assertSuccess(Conditional.run("1 == false"), false)
    assertSuccess(Conditional.run("true == 3"), false)

    assertSuccess(Conditional.run("true == (1 == 1)"), true)
  }

  test("conditional") {
    assertSuccess(Conditional.run("if true then 1 else 2"), 1)
    assertSuccess(Conditional.run("if false then 1 else 2"), 2)
    assertSuccess(Conditional.run("if 1 < 2 then true else false"), true)
    assertSuccess(Conditional.run("if 2 < 1 then true else false"), false)
    assertSuccess(Conditional.run("if 1 < 2 && 3 < 4 then 10 else 20"), 10)
    assertSuccess(
      Conditional.run(
        "if 1 < 2 && 3 < 4 then if false then 30 else 10 else 20"
      ),
      10
    )
  }
}
