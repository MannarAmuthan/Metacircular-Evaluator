package elements;

import elements.procedures.Procedure;
import elements.structures.Atom;
import elements.structures.EvalUnit;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    Map<String, EvalUnit> variables;
    Map<String, Procedure> methods;

    public Environment() {
        this.variables = new HashMap<>();
        this.methods = new HashMap<>();
    }

    public Environment(Environment environment) {
        this.variables = new HashMap<>();
        this.methods = environment.methods;
    }

    public EvalUnit get(String variableName){
        return variables.get(variableName);
    }

    public boolean isExists(String variableName){
        return variables.containsKey(variableName);
    }

    public void add(String variableName,EvalUnit variableValue){
        variables.put(variableName,variableValue);
    }

    public void add(String variableName,Procedure procedure){
        methods.put(variableName,procedure);
    }

    public Procedure getMethod(String methodName){
        return methods.get(methodName);
    }
}
