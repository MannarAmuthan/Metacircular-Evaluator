package elements.utils;

import elements.structures.Atom;

public class ArithmeticUtils {
    public static Integer calculate(Atom a, Atom b, String symbol){
        Integer opOne=fromAtom(a);
        Integer opTwo=fromAtom(b);
        switch (symbol){
            case "+": return opOne+opTwo;
            case "-": return opOne-opTwo;
            case "*": return opOne*opTwo;
            case "/": return opOne/opTwo;
            case "%": return opOne%opTwo;
        }
        return 0;
    }

    static Integer fromAtom(Atom atom){
        return Integer.parseInt(atom.getValue());
    }
}
