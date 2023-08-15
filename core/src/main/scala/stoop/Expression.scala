package stoop

import parsley.Parsley
import parsley.expr.Atoms
import parsley.expr.InfixL
import parsley.expr.SOps
import parsley.expr.precedence

object Expression {
  sealed trait Expr
  final case class And(l: Expr, r: Term) extends Expr
  final case class Or(l: Expr, r: Term) extends Expr
  final case class Gt(l: Expr, r: Term) extends Expr
  final case class GtEq(l: Expr, r: Term) extends Expr
  final case class Lt(l: Expr, r: Term) extends Expr
  final case class LtEq(l: Expr, r: Term) extends Expr
  final case class Add(l: Expr, r: Term) extends Expr
  final case class Sub(l: Expr, r: Term) extends Expr

  sealed trait Term extends Expr
  final case class Mul(l: Term, r: Atom) extends Term
  final case class Div(l: Term, r: Atom) extends Term

  sealed trait Atom extends Term
  final case class Integer(value: Int) extends Atom
  final case class Bool(value: scala.Boolean) extends Atom
  final case class Parens(expr: Expr) extends Atom

  import Lexer.*
  lazy val expr: Parsley[Expr] =
    precedence(
      Atoms(
        literal.integer.map(Integer.apply),
        literal.boolean.map {
          case "true"  => Bool(true)
          case "false" => Bool(false)
        },
        misc.openParen *> expr.map(Parens.apply) <* misc.closeParen
      ) :+
        SOps(InfixL)(
          operator.mul.as(Mul.apply),
          operator.div.as(Div.apply)
        ) :+
        SOps(InfixL)(
          operator.add.as(Add.apply),
          operator.sub.as(Sub.apply),
          operator.gt.as(Gt.apply),
          operator.gtEq.as(Gt.apply),
          operator.lt.as(Lt.apply),
          operator.ltEq.as(LtEq.apply),
          operator.and.as(And.apply),
          operator.or.as(Or.apply)
        )
    )
}
