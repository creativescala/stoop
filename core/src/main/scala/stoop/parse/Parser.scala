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

import parsley.Parsley
import parsley.Result

trait Parser[Program] {
  import Lexer.*

  def expr: Parsley[Program]

  lazy val parser: Parsley[Program] =
    lexer.fully(expr)

  def parse(input: String): Result[String, Program] =
    parser.parse(input)
}
