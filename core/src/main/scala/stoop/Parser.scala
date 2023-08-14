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

package stoop

import parsley.Parsley
import parsley.character
import parsley.token.Lexer
import parsley.token.descriptions.*
import parsley.token.predicate.Basic

object Parser {
  val desc = LexicalDesc.plain.copy(
    nameDesc = NameDesc.plain
      .copy(
        identifierStart = Basic(_.isLetter),
        identifierLetter = Basic(_.isLetterOrDigit)
      ),
    symbolDesc = SymbolDesc.plain.copy(
      hardKeywords = Set("val", "def")
    ),
    spaceDesc = SpaceDesc.plain.copy(
      space = Basic(parsley.character.isWhitespace(_))
    ),
    numericDesc = numeric.NumericDesc.plain.copy(
      leadingZerosAllowed = false,
      positiveSign = numeric.PlusSignPresence.Illegal,
      integerNumbersCanBeHexadecimal = false
      // exponents are already configured correctly by plain :)
    ),
    textDesc = text.TextDesc.plain.copy(
      escapeSequences = text.EscapeDesc.plain.copy(
        literals = Set('\\', '/', '\"'),
        singleMap = Map('b' -> 8, 'f' -> 12, 'n' -> 10, 'r' -> 13, 't' -> 9),
        hexadecimalEscape = text.NumericEscape.Supported(
          prefix = Some('u'),
          numDigits = text.NumberOfDigits.Exactly(4),
          maxValue = 0x10ffff
        )
      )
    )
  )

  val lexer = new Lexer(desc)

  val let = lexer.lexeme.symbol.apply("let")
  val equals = lexer.lexeme.symbol.apply('=')
  val id: Parsley[String] = lexer.lexeme.names.identifier
  val integer: Parsley[Int] = lexer.lexeme.numeric.integer.decimal32
  val string: Parsley[String] = lexer.lexeme.text.string.fullUtf16

  val openParen = lexer.lexeme.symbol.openParen
  val closeParen = lexer.lexeme.symbol.closingParen

  val binding = (let *> id).zip(equals *> integer)

  import parsley.expr.{precedence, SOps, InfixL, Atoms}

  sealed trait Expr
  final case class Add(l: Expr, r: Term) extends Expr
  final case class Sub(l: Expr, r: Term) extends Expr

  sealed trait Term extends Expr
  final case class Mul(l: Term, r: Atom) extends Term
  final case class Div(l: Term, r: Atom) extends Term

  sealed trait Atom extends Term
  final case class Integer(value: Int) extends Atom
  final case class Parens(expr: Expr) extends Atom

  lazy val expr: Parsley[Expr] =
    precedence(
      Atoms(
        integer.map(Integer.apply),
        openParen *> expr.map(Parens.apply) <* closeParen
      ) :+
        SOps(InfixL)(
          lexer.lexeme.symbol('*').as(Mul.apply),
          lexer.lexeme.symbol('/').as(Div.apply)
        ) :+
        SOps(InfixL)(
          lexer.lexeme.symbol('+').as(Add.apply),
          lexer.lexeme.symbol('-').as(Sub.apply)
        )
    )
  // lazy val value: Parsley[JValue] =
  //   str | num | list | obj | "true".as(JBool.True) | "false".as(
  //     JBool.False
  //   ) | "null".as(JNull)
  // val num: Parsley[JValue] = lexer.lexeme.numeric.signedCombined.decimal64Double
  //   .map(_.fold(JNum(_), JNum(_)))
  // val justStr: Parsley[String] = lexer.lexeme.text.string.fullUtf16
  // val str: Parsley[JValue] = justStr.map(JString(_))
  // val list: Parsley[JValue] = brackets {
  //   commaSep(value).map(JArray.fromSeq)
  // }
  // val kv: Parsley[(String, JValue)] = justStr.zip(":" ~> value)
  // val obj: Parsley[JValue] = braces {
  //   commaSep(kv).map(JObject.fromSeq)
  // }
}
