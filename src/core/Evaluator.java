package core;

import elements.*;
import elements.procedures.Procedure;
import elements.structures.Atom;
import elements.structures.AtomType;
import elements.structures.EvalList;
import elements.structures.EvalUnit;
import elements.utils.ArithmeticUtils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Evaluator {

    static EvalUnit parse(Deque<String> tokens) {

        if (tokens.size() == 0) return null;
        String token = tokens.removeFirst();
        if (token.equals("(")) {
            EvalList evalList = new EvalList();
            while (!tokens.getFirst().equals(")")) {
                evalList.append(parse(tokens));
            }
            tokens.removeFirst();
            return EvalUnit.evalList(evalList);
        } else if (token.equals(")")) {
            return null;
        } else {
            return EvalUnit.atom(Atom.getAtom(token));
        }
    }


    public static EvalUnit eval(EvalUnit parsed, Environment environment) {
        if (parsed.isAtomPresent()) {
            Atom atom = parsed.getAtom();
            if (atom.getAtomType() == AtomType.STRING)
                return environment.isExists(atom.getValue()) ? environment.get(atom.getValue()) : EvalUnit.atom(atom);
            else if (atom.getAtomType() == AtomType.NUMBER) return parsed;
        }
        List<EvalUnit> list = parsed.getEvalList().getList();
        EvalUnit evalUnit = list.get(0);
        if (evalUnit.isAtomPresent()) {
            Atom atom = evalUnit.getAtom();
            switch (atom.getValue()) {
                case "quote": {
                    return list.get(1);
                }
                case "atom?": {
                    return list.get(1).isAtomPresent()?EvalUnit.atom(Atom.getAtom("1")):EvalUnit.atom(Atom.getAtom("0"));
                }
                case "define": {
                    Atom var = list.get(1).getAtom();
                    EvalUnit exp = list.get(2);
                    EvalUnit evaluatedValForExp = eval(exp, environment);
                    environment.add(var.getValue(), evaluatedValForExp);
                    break;
                }
                case "if": {
                    EvalUnit toEval = list.get(1);
                    EvalUnit evalIfTrue = list.get(2);
                    EvalUnit evalIfFalse = list.get(3);
                    Atom result = eval(toEval, environment).getAtom();
                    if (result.getValue().equals("1")) return eval(evalIfTrue, environment);
                    else return eval(evalIfFalse, environment);
                }
                case "car": {
                    EvalUnit evalCar = eval(list.get(1), environment);
                    return evalCar.getEvalList().getList().get(0);
                }

                case "cdr": {
                    EvalUnit evalCdr = eval(list.get(1), environment);
                    List<EvalUnit> op = evalCdr.getEvalList().getList();
                    return EvalUnit.evalList(new EvalList(op.subList(1, op.size())));
                }

                case "cons": {
                    Atom opOne = eval(list.get(1), environment).getAtom();
                    EvalList opTwo = eval(list.get(2), environment).getEvalList();
                    opTwo.append(EvalUnit.atom(opOne));
                    return EvalUnit.evalList(opTwo);
                }

                case "eq": {
                    EvalUnit opOne = eval(list.get(1), environment);
                    EvalUnit opTwo = eval(list.get(2), environment);
                    if (opOne.equals(opTwo)) return EvalUnit.atom(Atom.getAtom("1"));
                    return EvalUnit.atom(Atom.getAtom("0"));
                }

                case "or": {
                    Atom opOne = eval(list.get(1), environment).getAtom();
                    Atom opTwo = eval(list.get(2), environment).getAtom();
                    if (opOne.getValue().equals("0") && opTwo.getValue().equals("0"))
                        return EvalUnit.atom(Atom.getAtom("0"));
                    return EvalUnit.atom(Atom.getAtom("1"));
                }

                case "+":
                case "-":
                case "*":
                case "%":
                case "/": {
                    Atom symbol = evalUnit.getAtom();
                    Atom opOne = eval(list.get(1), environment).getAtom();
                    Atom opTwo = eval(list.get(2), environment).getAtom();
                    return EvalUnit.atom(Atom.getAtom(String.valueOf(ArithmeticUtils.calculate(opOne, opTwo, symbol.getValue()))));
                }
                case "defun": {
                    String functionName = list.get(1).getAtom().getValue();
                    EvalUnit body = list.get(3);
                    environment.add(functionName, new Procedure(body, list.get(2)));
                    break;
                }
                default: {
                    String procedureName = list.get(0).getAtom().getValue();
                    Procedure method = environment.getMethod(procedureName);
                    List<EvalUnit> paramList = list.get(1).getEvalList().getList().stream().map(eu -> eval(eu, environment)).collect(Collectors.toList());
                    EvalUnit callbackResult = method.call(paramList, new Environment(environment));
                    return callbackResult;
                }
            }
        } else {
            EvalList evalList = new EvalList();
            for (EvalUnit e : list) {
                evalList.append(eval(e, environment));
            }
            return EvalUnit.evalList(evalList);
        }
        return parsed;
    }


    public static Environment evaluateProgramString(String program) throws IOException {
        program=importPackages(program);
        List<String> collect = Arrays.stream(program.replace("\n", "").replace("(", " ( ").replace(")", " ) ").split(" ")).filter(st -> !st.equals("")).collect(Collectors.toList());
        Deque<String> stringDeque = new LinkedList<>(collect);
        EvalUnit parse = parse(stringDeque);
        Environment environment = new Environment();
        eval(parse, environment);
        return environment;
    }

    private static String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    private static String importPackages(String program) throws IOException {
        String packageProgram=readFile("meta/binaryOperations.jlsp")
                + readFile("meta/factorial.jlsp")
                +readFile("meta/oddOrEven.jlsp");
        return "("+packageProgram+program+")";

    }
}
