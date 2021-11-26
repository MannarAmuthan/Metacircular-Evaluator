## Implementation of Meta-circular evaluator:

Simple LISP like implementation of language, inspired by Metalinguistic abstraction from [SICP](https://en.wikipedia.org/wiki/Structure_and_Interpretation_of_Computer_Programs).

![Meta-circular evaluator](unnamed.png?raw=true)

##### Sample Program Executed:

```
((defun fibonacci (N)
         (if (or (eq N 0) (eq N 1)) N ( + (fibonacci ((- N 1))) (fibonacci ((- N 2))) )))

 (define x (fibonacci (20))))
```

```java
 // Above program is program string
 Environment environment = Evaluator.evaluateProgramString(program);
 assert environment.get("x").getAtom().getValue().equals("6765");
```
