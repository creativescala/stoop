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

import munit.FunSuite
import munit.Location
import parsley.Result
import parsley.Success

/** Abstraction to make writing parser tests easier. */
class ParserSuite extends FunSuite {

  /** Assert that a result is success containing the given value. */
  def assertSuccess[A](result: Result[?, A], expected: A)(using
      loc: Location
  ) =
    result match {
      case Success(x) =>
        if x == expected then ()
        else
          fail(
            s"Parser succeeded with unexpected result. Expected $expected but got $x"
          )
      case other => fail(s"Parser failed with failure $other")
    }
}
