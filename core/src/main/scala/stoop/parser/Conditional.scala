package stoop.parser

import parsley.Parsley
import parsley.expr.Atoms
import parsley.expr.InfixL
import parsley.expr.SOps
import parsley.expr.precedence

import cats.syntax.all._
import parsley.cats.instances._

object Conditional {
  sealed trait Expr
  final case class And(l: Expr, r: Pre4) extends Expr
  final case class Or(l: Expr, r: Pre4) extends Expr

  sealed trait Pre4 extends Expr
  final case class Gt(l: Pre4, r: Pre3) extends Pre4
  final case class Lt(l: Pre4, r: Pre3) extends Pre4

  sealed trait Pre3 extends Pre4
  final case class GtEq(l: Pre3, r: Pre2) extends Pre3
  final case class LtEq(l: Pre3, r: Pre2) extends Pre3
  final case class Eq(l: Pre3, r: Pre2) extends Pre3

  sealed trait Pre2 extends Pre3
  final case class Add(l: Pre2, r: Pre1) extends Pre2
  final case class Sub(l: Pre2, r: Pre1) extends Pre2

  sealed trait Pre1 extends Pre2
  final case class Mul(l: Pre1, r: Atom) extends Pre1
  final case class Div(l: Pre1, r: Atom) extends Pre1

  sealed trait Atom extends Pre1
  final case class Integer(value: Int) extends Atom
  final case class Bool(value: scala.Boolean) extends Atom
  final case class Parens(expr: Expr) extends Atom
  final case class If(cond: Expr, t: Expr, f: Expr) extends Atom

  import Lexer.*

  lazy val conditionalExpr: Parsley[Atom] =
    (
      expression.ifLexer *> expr,
      expression.thenLexer *> expr,
      expression.elseLexer *> expr
    ).mapN((c, t, f) => If(c, t, f))

  lazy val parenExpr: Parsley[Atom] =
    misc.openParen *> expr.map(Parens.apply) <* misc.closeParen

  lazy val expr: Parsley[Expr] =
    precedence(
      Atoms(
        literal.integer.map(Integer.apply),
        literal.boolean.map {
          case "true"  => Bool(true)
          case "false" => Bool(false)
        },
        parenExpr,
        conditionalExpr
      ) :+
        SOps(InfixL)(
          operator.mul.as(Mul.apply),
          operator.div.as(Div.apply)
        ) :+
        SOps(InfixL)(
          operator.add.as(Add.apply),
          operator.sub.as(Sub.apply)
        ) :+
        SOps(InfixL)(
          operator.gtEq.as(GtEq.apply),
          operator.ltEq.as(LtEq.apply),
          operator.eq.as(Eq.apply)
        ) :+
        SOps(InfixL)(
          operator.gt.as(Gt.apply),
          operator.lt.as(Lt.apply)
        ) :+
        SOps(InfixL)(
          operator.and.as(And.apply),
          operator.or.as(Or.apply)
        )
    )

  lazy val parser: Parsley[Expr] =
    lexer.fully(expr)
}
