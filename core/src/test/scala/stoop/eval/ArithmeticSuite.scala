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
