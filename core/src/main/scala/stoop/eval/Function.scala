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

import stoop.parse.{Function => Parser}

final case class Function(
    param: String,
    env: Map[String, Int | Boolean | Function],
    body: Parser.Expr
)
object Function
    extends Interpreter[Parser.Expr, Int | Boolean | Function](Parser.parser) {
  import Parser.*
  type Value = Int | Boolean | Function
  type Environment = Map[String, Value]

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

  /** Checks that a Value is a Function, raising an exception if not. */
  def isFunction(v: Value): Function =
    v match {
      case f: Function => f
      case other =>
        throw new java.lang.ClassCastException(
          s"Expected a function value but found the value ${other}"
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

  def get(name: String, env: Environment): Value =
    env.get(name) match {
      case Some(value) => value
      case None =>
        throw new java.lang.IllegalStateException(
          s"No value was found in the environment for the name $name"
        )
    }

  def set(name: String, value: Value, env: Environment): Environment =
    env + (name -> value)

  def eval(expr: Expr): Value = {
    def loop(expr: Expr, env: Environment): Value =
      expr match {
        case And(l, r)      => boolBinOp(loop(l, env), loop(r, env))(_ && _)
        case Or(l, r)       => boolBinOp(loop(l, env), loop(r, env))(_ || _)
        case Gt(l, r)       => intBoolBinOp(loop(l, env), loop(r, env))(_ > _)
        case Lt(l, r)       => intBoolBinOp(loop(l, env), loop(r, env))(_ < _)
        case GtEq(l, r)     => intBoolBinOp(loop(l, env), loop(r, env))(_ >= _)
        case LtEq(l, r)     => intBoolBinOp(loop(l, env), loop(r, env))(_ <= _)
        case Eq(l, r)       => loop(l, env) == loop(r, env)
        case Add(l, r)      => intBinOp(loop(l, env), loop(r, env))(_ + _)
        case Sub(l, r)      => intBinOp(loop(l, env), loop(r, env))(_ - _)
        case Mul(l, r)      => intBinOp(loop(l, env), loop(r, env))(_ * _)
        case Div(l, r)      => intBinOp(loop(l, env), loop(r, env))(_ / _)
        case Integer(value) => value
        case Bool(value)    => value
        case Name(name)     => get(name, env)
        case Parens(expr)   => loop(expr, env)
        case If(cond, t, f) =>
          if isBoolean(loop(cond, env)) then loop(t, env) else loop(f, env)
        case Let(name, value, body) =>
          val newEnv = set(name, loop(value, env), env)
          loop(body, newEnv)
        case Lambda(param, body) => Function(param, env, body)
        case Apply(fun, arg) =>
          val f = isFunction(loop(fun, env))
          val x = loop(arg, env)
          val fEnv = set(f.param, x, f.env)
          loop(f.body, fEnv)
      }

    loop(expr, Map.empty)
  }

}
