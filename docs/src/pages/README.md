# Stoop

Stoop is a series of parsers and interpreters that incrementally builds a simple Scala-like language.

The languages progress as follows:

1. `Arithmetic`, the first language, works with simple integer arithmetic.

2. `Expression` adds booleans and inequalities.

3. `Conditional` adds conditional (`if`)  expressions.

4. `Local` adds local definitions, using the syntax `let <name> = <expr> in <expr>`.
