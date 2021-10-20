package core;


import elements.Environment;
import org.junit.jupiter.api.Test;


public class EvaluatorTest {

    @Test
    void testShouldDefineVariables(){
        String program="(define a 10)";

        Environment environment = Evaluator.evaluateProgramString(program);

        assert environment.get("a").getAtom().getValue().equals("10");
    }

    @Test
    void testShouldDefineVariablesInMultiStatements(){
        String program="((define a 10) (define a 20) (define a 100))";

        Environment environment = Evaluator.evaluateProgramString(program);

        assert environment.get("a").getAtom().getValue().equals("100");
    }

    @Test
    void testShouldEvaluateConditionalStatements(){
        String programOne="(if 1 (define x 10) (define x 20))";
        String programTwo="(if 0 (define x 10) (define x 20))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("x").getAtom().getValue().equals("10");
        assert environmentTwo.get("x").getAtom().getValue().equals("20");
    }


    @Test
    void testShouldEvaluateCBooleanStatements(){
        String programOne="(define x (eq 1 0))";
        String programTwo="(if (eq 0 0) (define x 10) (define x 20))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("x").getAtom().getValue().equals("0");
        assert environmentTwo.get("x").getAtom().getValue().equals("10");
    }

    @Test
    void testShouldEvaluateBinaryExpressions() {
        String programOne="(define x (or 1 1))";
        String programTwo="(if (or (eq 1 0) (eq 1 0)) (define x 10) (define x 20))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("x").getAtom().getValue().equals("1");
        assert environmentTwo.get("x").getAtom().getValue().equals("20");
    }

    @Test
    void testShouldEvaluateArithmeticExpressions() {
        String programOne="(define x  (* (+ 1 1) (/ 100 10) )    )";
        String programTwo="(define x  (* (- 2 1) (% 1000000 9))   )";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("x").getAtom().getValue().equals("20");
        assert environmentTwo.get("x").getAtom().getValue().equals("1");
    }

    @Test
    void testShouldDoFunctionCalls() {
        String programOne="((defun adder (a b) (+ a b)) (define isEleven (eq 11 (adder (5 6)))))";
        String programTwo="((defun fibonacci (N)\n" +
                "    (if (or (eq N 0) (eq N 1)) N ( + (fibonacci ((- N 1))) (fibonacci ((- N 2))) )   \n" +
                "            ))\n" +
                "(define x (fibonacci (20)))\n" +
                "            )";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("isEleven").getAtom().getValue().equals("1");
        assert environmentTwo.get("x").getAtom().getValue().equals("6765");
    }
}