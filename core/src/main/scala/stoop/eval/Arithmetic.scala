package stoop.eval

import parsley.Result
import stoop.parse.Arithmetic.*

object Arithmetic {

  def eval(expr: Expr): Int =
    expr match {
      case Add(l, r)      => eval(l) + eval(r)
      case Sub(l, r)      => eval(l) - eval(r)
      case Mul(l, r)      => eval(l) * eval(r)
      case Div(l, r)      => eval(l) / eval(r)
      case Integer(value) => value
      case Parens(expr)   => eval(expr)
    }

  def run(program: String): Result[String, Int] =
    parser.parse(program).map(eval)
}
