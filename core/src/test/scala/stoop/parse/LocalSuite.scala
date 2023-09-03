package stoop.parse

class LocalSuite extends ParserSuite {
  import Local.*

  test("let expression") {
    assertSuccess(
      parser.parse("let x = 3 in x + 2"),
      Let("x", Integer(3), Add(Name("x"), Integer(2)))
    )
    assertSuccess(
      parser.parse("let x = 3 in let y = 2 in x * y"),
      Let("x", Integer(3), Let("y", Integer(2), Mul(Name("x"), Name("y"))))
    )
  }
}
