# Stoop

Stoop is a series of parsers and interpreters that incrementally build a simple programming language with syntax (and semantics) as close as possible to Scala 3. The languages follow the progression in [Shriram Krishnamurthi's][sk] book [Programming Languages: Application and Interpretation][plai]:

1. `Arithmetic`, the first language, works with simple integer arithmetic.

2. `Expression` adds boolean values and logical operators, inequalities, and equality.

3. `Conditional` adds conditional (`if`)  expressions. The syntax is `if <expr> then <expr> else <expr>`. The `else` branch is mandatory.

4. `Local` adds local definitions, using the syntax `let <name> = <expr> in <expr>`. (There is no simple equivalent to this construct in Scala 3, so we use the same syntax as OCaml.)

5. `Function` adds function definitions (lambda expressions) and function application. The syntax for function definitions is the same as Scala (`(<param>) => <expr>`) except functions can only have a single parameter. Function application is the usual `f(x)` syntax.

## Code Structure

The implementation is divided into parsers and interpreters. The parsers are in the `parse` package, and the interpreters in the `eval` package. Each package defines an interface (`Parsers` and `Interpreter`, respectively) that implementations should implement. There are also test cases.

The intention is that parsers will be supplied to students, and they will implement the interpreters. 

[plai]: https://www.plai.org/
[sk]: https://cs.brown.edu/~sk/
