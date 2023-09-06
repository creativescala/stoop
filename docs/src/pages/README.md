# Stoop

Stoop is a series of parsers and interpreters that incrementally build a simple programming language with syntax (and semantics) that closely resemble Scala 3. Stoop is intended for educational purposes. The languages are simple, and the implementations are slow.

The languages follow the progression in [Shriram Krishnamurthi's][sk] book [Programming Languages: Application and Interpretation][plai] (PLAI):

1. `Arithmetic`, the first language, works with simple integer arithmetic, with the operators `+`, `-`, `/`, `*` and parenthesized expressions.

2. `Expression` adds boolean values and logical operators, inequalities, and equality. It adds boolean literals `true` and `false`, and operators `>`, `<`, `>=`, `<=`, `==`, `&&` and `||`.

3. `Conditional` adds conditional (`if`)  expressions. The syntax is the same as Scala 3: `if <expr> then <expr> else <expr>`. Unlike in Scala, the `else` branch is mandatory.

4. `Local` adds local definitions, using the syntax `let <name> = <expr> in <expr>`. There is no simple equivalent to this construct in Scala 3, so we use the same syntax as OCaml.

5. `Function` adds functions (lambda expressions) and function application. The syntax for functions is the same as Scala (`(<param>) => <expr>`) except functions can only have a single parameter and there are mandatory parentheses around the parameter. Function application is the usual `f(x)` syntax.

The rest of this document gives guidance for students who want to implement interpreters for these languages. It starts by describing the structure of the project and the code, then gives some advice on tackling the project, and finishes with some recommended extensions for those who want more challenge.


## Project Structure

The project structure is a bit more complicated than you may be used to. The sbt project (in `build.sbt`) defines several sub-projects:

- `core`, which contains the code you'll work with;
- `docs`, which contains this documentation; and
- the `root` project, which aggregates and depends on `core`.

From your point of view, the only relevant detail is that the code you'll work with is all within the `core` directory. So `core/src/main/scala` for code, and `core/src/test/scala` for tests.

You don't need to change into the `core` sub-project within sbt. Any commands you run on the default `root` project (such as `compile`, or `test`) will also run on the `core` sub-project.

You may want to use the `build` sbt command, which in addition to compiling and testing the code, will format it, add missing copyright headers, and more. It's not necessary to use this command, but you might be interested in how professional code is developed; this is the same command used in all the Creative Scala projects to perform basic checks on the code.


## Code Structure

The implementation is divided into parsers and interpreters. Each lives in their own package: the parsers in the `parse` package and the interpreters in the `eval` package. (An evaluator is another word for an interpreter.)


### Parsers

The parsers take `String` input, such as `"1 + 2"` and turn it into an algebraic data type that we can manipulate in code. Here's an example doing just that.

```scala mdoc
import stoop.parse.*

Arithmetic.parser.parse("1 + 2")
```

In the world of programming languages this representation is usually called an abstract syntax tree.

You'll notice that there is a parser for each language described above. The parsers become more complex as the languages increase in complexity, but you don't need to worry about how they work. There are two things you do need to know:

1. They all define an `Expr` algebraic data type to represent programs. The definition of `Expr` is a little bit more complicated than you may be used to, but the end result is just an algebraic data type.

2. They all contain an object called `parser` that has a method `parse` that takes a `String` input and produces an `Expr` (wrapped in a `Result`; parsing can fail).

See the `Parser` trait for a little bit more detail.

Here are some examples, showing the parsers for different languages.

```scala mdoc
Conditional.parser.parse("1 < 2")

Function.parser.parse("(x) => x + 2")
```

If the syntax is incorrect, the output offers some useful hints.

```scala mdoc
Arithmetic.parser.parse("1 ? 2")
```

If you want to explore extending the parsers, they are built using the [Parsley][parsley] Scala library.


### Interpreters

The intention is that you will implement the interpreters. They should all implement the `Interpreter` trait. Doing so will allow your implementations to work with the tests we've created.

The `Interpreter` trait only requires you implement one method: `eval`. It takes an input of type `Program` (which will the `Expr` type from whichever parser you are using), and will produce a result of type `Value`. What concrete type you should use for `Value`? It depends on the language you're implementing. For `Arithmetic` you can use `Int`. For the later languages until `Function`, we can use Scala 3's [union types][union-type] to return `Int | Boolean`. For `Function` we need add a representation of functions to the `Value` type.


## Challenges

Your mission, should you choose to accept it, is to implement interpreters for each parser. To guide you, you have the following:

1. the text above;
2. the test cases; and
3. [PLAI][plai].

We strongly recommend you read [PLAI][plai] as you work through the implementations. PLAI has many insights about the design space of programming languages. While learning how to implement simple interpreters is useful, it's learning about programming languages that is likely to be the most important knowledge you'll get from this exercise. If you fail to read PLAI you'll miss out on much of this.


## Where Next?

If you finish the five interpreters and want to take things further, here are some suggestions:

1. Read further in PLAI, and create new languages that extend `Function` with the feature you encounter. PLAI discusses objects, types, macros, and more.

2. PLAI lists several books you may read next, to go further in programming language theory.

3. You might want to focus on implementation techniques, for which there are two paths you could choose. The first path is to continue working with interpreters, in which case [Crafting Interpreters][crafting-interpreters] is a good starting point. The first part of that book covers the implementation technique we're used: a tree-walking interpreter. The second part looks at more efficient bytecode interpreters. For a different approach, see the [Graal Truffle][truffle] framework. The other path is to implement a compiler. Here [Jeremy Siek's][siek] book, *Essential of Compilation*, is a good introduction. PDFs are available from [his homepage][siek] or you can buy a printed copy.

[plai]: https://www.plai.org/
[sk]: https://cs.brown.edu/~sk/
[parsley]: https://j-mie6.github.io/parsley/latest/
[union-type]: https://docs.scala-lang.org/scala3/book/types-union.html
[crafting-interpreters]: https://craftinginterpreters.com/
[truffle]: https://www.graalvm.org/latest/graalvm-as-a-platform/language-implementation-framework/
[siek]: https://wphomes.soic.indiana.edu/jsiek/
