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

class LocalSuite extends EvalSuite {
  test("basic expressions") {
    assertSuccess(Local.run(" 1 + 2"), 3)
    assertSuccess(Local.run("1 -  2"), -1)
    assertSuccess(Local.run("1 * 2"), 2)
    assertSuccess(Local.run("4 / 2"), 2)
  }

  test("compound expressions") {
    assertSuccess(Local.run("1 + 2 * 3"), 7)
    assertSuccess(Local.run("(1 + 2) * 3"), 9)
    assertSuccess(Local.run("3 - 2 + 4"), 5)
  }

  test("inequalities") {
    assertSuccess(Local.run("1 < 3"), true)
    assertSuccess(Local.run("1 < 1"), false)
    assertSuccess(Local.run("3 < 1"), false)
    assertSuccess(Local.run("3 > 1"), true)
    assertSuccess(Local.run("3 > 3"), false)
    assertSuccess(Local.run("1 > 3"), false)

    assertSuccess(Local.run("1 <= 3"), true)
    assertSuccess(Local.run("1 <= 1"), true)
    assertSuccess(Local.run("3 <= 1"), false)
    assertSuccess(Local.run("3 >= 1"), true)
    assertSuccess(Local.run("3 >= 3"), true)
    assertSuccess(Local.run("1 >= 3"), false)
  }

  test("equality") {
    assertSuccess(Local.run("1 == 1"), true)
    assertSuccess(Local.run("3 == 1"), false)

    assertSuccess(Local.run("true == true"), true)
    assertSuccess(Local.run("false == false"), true)
    assertSuccess(Local.run("true == false"), false)

    assertSuccess(Local.run("1 == false"), false)
    assertSuccess(Local.run("true == 3"), false)

    assertSuccess(Local.run("true == (1 == 1)"), true)
  }

  test("conditional") {
    assertSuccess(Local.run("if true then 1 else 2"), 1)
    assertSuccess(Local.run("if false then 1 else 2"), 2)
    assertSuccess(Local.run("if 1 < 2 then true else false"), true)
    assertSuccess(Local.run("if 2 < 1 then true else false"), false)
    assertSuccess(Local.run("if 1 < 2 && 3 < 4 then 10 else 20"), 10)
    assertSuccess(
      Local.run(
        "if 1 < 2 && 3 < 4 then if false then 30 else 10 else 20"
      ),
      10
    )
  }

  test("bindings") {
    assertSuccess(Local.run("let x = 1 in x + x"), 2)
    assertSuccess(Local.run("let x = 1 in let y = 41 in x + y"), 42)
    // Aliasing
    assertSuccess(Local.run("let x = 1 in let x = 42 in x + x"), 84)
    // Aliasing and scope
    assertSuccess(Local.run("let x = 1 in x + let x = 41 in x"), 42)
  }
}
