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
import stoop.parse.Parser
import scala.io.StdIn

/** An `Interpreter` accepts programs represented as an abstract syntax tree of
  * type `Program` and evaluates them to produce values of type `Value`.
  */
trait Interpreter[Program, Value](parser: Parser[Program]) {

  /** The core method of an interpreter, produces values of type `Value` given a
    * program.of type `Program`.
    */
  def eval(program: Program): Value

  /** Parses a program from a `String`, and evaluates that program. */
  def run(program: String): Result[String, Value] =
    parser.parse(program).map(eval)

  /** Run a read-eval-print loop, which reads input from standard input,
    * evaluates it, and prints the result.
    */
  def repl: Unit = {
    print("> ")
    println(run(StdIn.readLine()))
    repl
  }
}
