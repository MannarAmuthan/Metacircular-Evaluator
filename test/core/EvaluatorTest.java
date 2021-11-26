package core;


import elements.Environment;
import org.junit.jupiter.api.Test;
import java.io.IOException;


public class EvaluatorTest {

    @Test
    void testShouldDefineVariables() throws IOException {
        String program="(define a 10)";

        Environment environment = Evaluator.evaluateProgramString(program);

        assert environment.get("a").getAtom().getValue().equals("10");
    }

    @Test
    void testShouldDefineVariablesInMultiStatements() throws IOException {
        String program="((define a 10) (define a 20) (define a 100))";

        Environment environment = Evaluator.evaluateProgramString(program);

        assert environment.get("a").getAtom().getValue().equals("100");
    }

    @Test
    void testShouldConsiderLiteralAsStringIfNotExistsAsVariableInEnvironment() throws IOException {
        String programOne="((define a 10) (define b a))";
        String programTwo="((define a 10) (define b c))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("b").getAtom().getValue().equals("10");
        assert environmentTwo.get("b").getAtom().getValue().equals("c");
    }

    @Test
    void testShouldEvaluateConditionalStatements() throws IOException {
        String programOne="(if 1 (define x 10) (define x 20))";
        String programTwo="(if 0 (define x 10) (define x 20))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("x").getAtom().getValue().equals("10");
        assert environmentTwo.get("x").getAtom().getValue().equals("20");
    }

    @Test
    void shouldEvaluateWhetherElementIsAtom()  throws IOException {
        String programOne="(if (atom? 5) (define x 10) (define x 20))";
        String programTwo="(define x (atom? (2)))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("x").getAtom().getValue().equals("10");
        assert environmentTwo.get("x").getAtom().getValue().equals("0");
    }

    @Test
    void testShouldEvaluateCBooleanStatements() throws IOException {
        String programOne="(define x (eq 1 0))";
        String programTwo="(if (eq 0 0) (define x 10) (define x 20))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("x").getAtom().getValue().equals("0");
        assert environmentTwo.get("x").getAtom().getValue().equals("10");
    }


    @Test
    void shouldEquateTwoElementsCorrectly() throws IOException{
        String programOne="(define x (eq (quote(1 2 3)) (quote(1 2 3))))";
        String programTwo="(if (eq 0 0) (define x 10) (define x 20))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("x").getAtom().getValue().equals("1");
        assert environmentTwo.get("x").getAtom().getValue().equals("10");
    }

    @Test
    void shouldAddElementToList() throws IOException {
        String programOne="(define x (cons  3 (quote(1 2))  ) )";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);

        assert environmentOne.get("x").getEvalList().getList().get(0).getAtom().getValue().equals("1");
        assert environmentOne.get("x").getEvalList().getList().get(1).getAtom().getValue().equals("2");
        assert environmentOne.get("x").getEvalList().getList().get(2).getAtom().getValue().equals("3");
    }

    @Test
    void testShouldEvaluateBinaryExpressions() throws IOException {
        String programOne="(define x (or 1 1))";
        String programTwo="(if (or (eq 1 0) (eq 1 0)) (define x 10) (define x 20))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("x").getAtom().getValue().equals("1");
        assert environmentTwo.get("x").getAtom().getValue().equals("20");
    }

    @Test
    void testShouldEvaluateArithmeticExpressions() throws IOException {
        String programOne="(define x  (* (+ 1 1) (/ 100 10) )    )";
        String programTwo="(define x  (* (- 2 1) (% 1000000 9))   )";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("x").getAtom().getValue().equals("20");
        assert environmentTwo.get("x").getAtom().getValue().equals("1");
    }

    @Test
    void testShouldDoFunctionCalls() throws IOException {
        String programOne="((defun adder (a b) (+ a b)) (define isEleven (eq 11 (adder (5 6)))))";
        String programTwo="((defun fibonacci (N)\n" +
                "    (if (or (eq N 0) (eq N 1)) N ( + (fibonacci ((- N 1))) (fibonacci ((- N 2))) )   \n" +
                "            ))\n" +
                "(define x (fibonacci (20))))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("isEleven").getAtom().getValue().equals("1");
        assert environmentTwo.get("x").getAtom().getValue().equals("6765");
    }

    @Test
    void shouldEvaluateAndOperation() throws IOException {
        String programOne="(define x (and (1 (eq 1 1))))";
        String programTwo="(define x (and (1 0)))";
        String programThree="(define x (and (0 0)))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);
        Environment environmentThree = Evaluator.evaluateProgramString(programThree);

        assert environmentOne.get("x").getAtom().getValue().equals("1");
        assert environmentTwo.get("x").getAtom().getValue().equals("0");
        assert environmentThree.get("x").getAtom().getValue().equals("0");
    }

    @Test
    void shouldEvaluateNotOperation() throws IOException {
        String programOne="(define x (not (1)))";
        String programTwo="(define x (not (0)))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("x").getAtom().getValue().equals("0");
        assert environmentTwo.get("x").getAtom().getValue().equals("1");
    }

    @Test
    void shouldEvaluateXOROperation() throws IOException {
        String programOne="(define x (xor (1 (eq 1 1))))";
        String programTwo="(define x (xor (1 0)))";
        String programThree="(define x (xor (0 0)))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);
        Environment environmentThree = Evaluator.evaluateProgramString(programThree);

        assert environmentOne.get("x").getAtom().getValue().equals("0");
        assert environmentTwo.get("x").getAtom().getValue().equals("1");
        assert environmentThree.get("x").getAtom().getValue().equals("1");
    }

    @Test
    void shouldDoFactorial() throws IOException {
        String programOne="(define y (factorial (10)))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);

        assert environmentOne.get("y").getAtom().getValue().equals("3628800");
    }

    @Test
    void shouldFindIsEvenOrOdd() throws IOException {
        String programOne="(define y (isEven (8)))";
        String programTwo="(define y (isOdd (7)))";

        Environment environmentOne = Evaluator.evaluateProgramString(programOne);
        Environment environmentTwo = Evaluator.evaluateProgramString(programTwo);

        assert environmentOne.get("y").getAtom().getValue().equals("1");
        assert environmentTwo.get("y").getAtom().getValue().equals("1");
    }
}