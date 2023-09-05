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

class FunctionSuite extends EvalSuite {
  test("basic expressions") {
    assertSuccess(Function.run(" 1 + 2"), 3)
    assertSuccess(Function.run("1 -  2"), -1)
    assertSuccess(Function.run("1 * 2"), 2)
    assertSuccess(Function.run("4 / 2"), 2)
  }

  test("compound expressions") {
    assertSuccess(Function.run("1 + 2 * 3"), 7)
    assertSuccess(Function.run("(1 + 2) * 3"), 9)
    assertSuccess(Function.run("3 - 2 + 4"), 5)
  }

  test("inequalities") {
    assertSuccess(Function.run("1 < 3"), true)
    assertSuccess(Function.run("1 < 1"), false)
    assertSuccess(Function.run("3 < 1"), false)
    assertSuccess(Function.run("3 > 1"), true)
    assertSuccess(Function.run("3 > 3"), false)
    assertSuccess(Function.run("1 > 3"), false)

    assertSuccess(Function.run("1 <= 3"), true)
    assertSuccess(Function.run("1 <= 1"), true)
    assertSuccess(Function.run("3 <= 1"), false)
    assertSuccess(Function.run("3 >= 1"), true)
    assertSuccess(Function.run("3 >= 3"), true)
    assertSuccess(Function.run("1 >= 3"), false)
  }

  test("equality") {
    assertSuccess(Function.run("1 == 1"), true)
    assertSuccess(Function.run("3 == 1"), false)

    assertSuccess(Function.run("true == true"), true)
    assertSuccess(Function.run("false == false"), true)
    assertSuccess(Function.run("true == false"), false)

    assertSuccess(Function.run("1 == false"), false)
    assertSuccess(Function.run("true == 3"), false)

    assertSuccess(Function.run("true == (1 == 1)"), true)
  }

  test("conditional") {
    assertSuccess(Function.run("if true then 1 else 2"), 1)
    assertSuccess(Function.run("if false then 1 else 2"), 2)
    assertSuccess(Function.run("if 1 < 2 then true else false"), true)
    assertSuccess(Function.run("if 2 < 1 then true else false"), false)
    assertSuccess(Function.run("if 1 < 2 && 3 < 4 then 10 else 20"), 10)
    assertSuccess(
      Function.run(
        "if 1 < 2 && 3 < 4 then if false then 30 else 10 else 20"
      ),
      10
    )
  }

  test("bindings") {
    assertSuccess(Function.run("let x = 1 in x + x"), 2)
    assertSuccess(Function.run("let x = 1 in let y = 41 in x + y"), 42)
    // Aliasing
    assertSuccess(Function.run("let x = 1 in let x = 42 in x + x"), 84)
    // Aliasing and scope
    assertSuccess(Function.run("let x = 1 in x + let x = 41 in x"), 42)
  }

  test("apply") {
    assertSuccess(Function.run("((x) => x + 1)(2)"), 3)
    assertSuccess(Function.run("let f = (x) => x + 1 in f(3)"), 4)
    assertSuccess(Function.run("((x) => (y) => x + y)(2)(3)"), 5)
    // lexical scope
    assertSuccess(Function.run("let f = (x) => x + 1 in let x = 1 in f(3)"), 4)
  }
}
