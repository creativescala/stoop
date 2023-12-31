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

import parsley.character
import parsley.token.descriptions.*
import parsley.token.predicate.Basic

object Lexer {
  val desc = LexicalDesc.plain.copy(
    nameDesc = NameDesc.plain
      .copy(
        identifierStart = Basic(_.isLetter),
        identifierLetter = Basic(_.isLetterOrDigit)
      ),
    symbolDesc = SymbolDesc.plain.copy(
      hardKeywords =
        Set("let", "in", "val", "def", "if", "then", "else", "true", "false")
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

  val lexer = new parsley.token.Lexer(desc)

  object definition {
    val defLexer = lexer.lexeme.symbol("def")
    val valLexer = lexer.lexeme.symbol("val")
    val letLexer = lexer.lexeme.symbol("let")
    val inLexer = lexer.lexeme.symbol("in")
    val eqLexer = lexer.lexeme.symbol("=")
  }

  object literal {
    val integer = lexer.lexeme.numeric.integer.decimal32
    val string = lexer.lexeme.text.string.fullUtf16
    val `true` = lexer.lexeme.symbol("true")
    val `false` = lexer.lexeme.symbol("false")
  }

  val name = lexer.lexeme.names.identifier

  object operator {
    val mul = lexer.lexeme.symbol('*')
    val div = lexer.lexeme.symbol('/')
    val add = lexer.lexeme.symbol('+')
    val sub = lexer.lexeme.symbol('-')

    val lt = lexer.lexeme.symbol('<')
    val gt = lexer.lexeme.symbol('>')

    val ltEq = lexer.lexeme.symbol("<=")
    val gtEq = lexer.lexeme.symbol(">=")

    val and = lexer.lexeme.symbol("&&")
    val or = lexer.lexeme.symbol("||")

    val eq = lexer.lexeme.symbol("==")
  }

  object expression {
    val ifLexer = lexer.lexeme.symbol("if")
    val thenLexer = lexer.lexeme.symbol("then")
    val elseLexer = lexer.lexeme.symbol("else")
  }

  object misc {
    val openParen = lexer.lexeme.symbol.openParen
    val closeParen = lexer.lexeme.symbol.closingParen
    val openBrace = lexer.lexeme.symbol.openBrace
    val closeBrace = lexer.lexeme.symbol.closingBrace
    val arrow = lexer.lexeme.symbol("=>")
  }
}
