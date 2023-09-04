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

import stoop.parse.{Expression => Parser}

object Expression
    extends Interpreter[Parser.Expr, Int | Boolean](Parser.parser) {
  import Parser.*
  type Value = Int | Boolean

  /** Checks that a Value is an Int, raising an exception if not. */
  def isInt(v: Value): Int =
    v match {
      case i: Int => i
      case other =>
        throw new java.lang.ClassCastException(
          s"Expected an integer value but found the value ${other}"
        )
    }

  /** Checks that a Value is a Boolean, raising an exception if not. */
  def isBoolean(v: Value): Boolean =
    v match {
      case b: Boolean => b
      case other =>
        throw new java.lang.ClassCastException(
          s"Expected a boolean value but found the value ${other}"
        )
    }

  /** Binary operation that takes two integers and returns an integer */
  def intBinOp(l: Value, r: Value)(f: (Int, Int) => Int): Int =
    f(isInt(l), isInt(r))

  /** Binary operation that takes two booleans and returns a boolean */
  def boolBinOp(l: Value, r: Value)(f: (Boolean, Boolean) => Boolean): Boolean =
    f(isBoolean(l), isBoolean(r))

  /** Binary operation that takes two integers and returns a boolean */
  def intBoolBinOp(l: Value, r: Value)(f: (Int, Int) => Boolean): Boolean =
    f(isInt(l), isInt(r))

  def eval(expr: Expr): Value =
    expr match {
      case And(l, r)      => boolBinOp(eval(l), eval(r))(_ && _)
      case Or(l, r)       => boolBinOp(eval(l), eval(r))(_ || _)
      case Gt(l, r)       => intBoolBinOp(eval(l), eval(r))(_ > _)
      case Lt(l, r)       => intBoolBinOp(eval(l), eval(r))(_ < _)
      case GtEq(l, r)     => intBoolBinOp(eval(l), eval(r))(_ >= _)
      case LtEq(l, r)     => intBoolBinOp(eval(l), eval(r))(_ <= _)
      case Eq(l, r)       => eval(l) == eval(r)
      case Add(l, r)      => intBinOp(eval(l), eval(r))(_ + _)
      case Sub(l, r)      => intBinOp(eval(l), eval(r))(_ - _)
      case Mul(l, r)      => intBinOp(eval(l), eval(r))(_ * _)
      case Div(l, r)      => intBinOp(eval(l), eval(r))(_ / _)
      case Integer(value) => value
      case Bool(value)    => value
      case Parens(expr)   => eval(expr)
    }

}
