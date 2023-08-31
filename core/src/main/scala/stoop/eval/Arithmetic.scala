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
