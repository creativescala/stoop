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

import cats.syntax.all.*
import parsley.Parsley
import parsley.cats.instances.*
import parsley.errors.combinator.*
import parsley.expr.Atoms
import parsley.expr.InfixL
import parsley.expr.Postfix
import parsley.expr.SOps
import parsley.expr.precedence

object Function {
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
  final case class Mul(l: Pre1, r: Pre0) extends Pre1
  final case class Div(l: Pre1, r: Pre0) extends Pre1

  sealed trait Pre0 extends Pre1
  final case class Apply(fun: Pre1, arg: Expr) extends Atom

  sealed trait Atom extends Pre0
  final case class Integer(value: Int) extends Atom
  final case class Bool(value: scala.Boolean) extends Atom
  final case class Name(name: String) extends Atom
  final case class Parens(expr: Expr) extends Atom
  final case class If(cond: Expr, t: Expr, f: Expr) extends Atom
  final case class Let(name: String, value: Expr, body: Expr) extends Atom
  final case class Lambda(param: String, body: Expr) extends Atom

  object parser extends Parser[Expr] {
    import Lexer.*

    lazy val lambdaExpr: Parsley[Atom] =
      (misc.openParen *> name <* misc.closeParen, misc.arrow *> expr)
        .mapN(Lambda.apply)

    lazy val letExpr: Parsley[Atom] =
      (
        definition.letLexer *> name,
        definition.eqLexer *> expr,
        definition.inLexer *> expr
      ).mapN(Let.apply)

    lazy val conditionalExpr: Parsley[Atom] =
      (
        expression.ifLexer *> expr,
        expression.thenLexer *> expr,
        expression.elseLexer *> expr
      ).mapN(If.apply)

    lazy val parenExpr: Parsley[Atom] =
      lexer.lexeme.enclosing.parens(expr).map(Parens.apply)

    lazy val nameExpr: Parsley[Atom] =
      name.map(Name.apply)

    lazy val intExpr: Parsley[Atom] =
      literal.integer.map(Integer.apply).label("integer")

    lazy val boolExpr: Parsley[Atom] =
      literal.`true`
        .as(Bool(true))
        .orElse(literal.`false`.as(Bool(false)))
        .label("boolean")

    lazy val expr: Parsley[Expr] =
      precedence(
        Atoms(
          intExpr,
          boolExpr,
          letExpr,
          // There is ambiguity in parsing an open paren. It could be the start
          // of a lambda or a parenthesized expression. We try lambda first but
          // must backtrack if we fail, even if we consume input.
          Parsley.attempt(lambdaExpr),
          conditionalExpr,
          // There is ambiguity in parsing an expression like "x". It could be
          // interpreted as the start of an apply expression (if followed by
          // parens) or as a name. We try apply first, but we must backtrack if
          // the parsing fails even if it has consumed input.
          // Parsley.attempt(applyExpr),
          Parsley.attempt(parenExpr),
          nameExpr
        ) :+
          SOps(Postfix)(
            lexer.lexeme.enclosing
              .parens(expr)
              .map(arg => fun => Apply(fun, arg))
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
  }
}
