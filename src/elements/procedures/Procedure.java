package elements.procedures;

import core.Evaluator;
import elements.Environment;
import elements.structures.EvalUnit;

import java.util.List;

public class Procedure {
    EvalUnit evalUnit;
    List<EvalUnit> args;

    public Procedure(EvalUnit evalUnit, EvalUnit args) {
        this.evalUnit = evalUnit;
        this.args = args.getEvalList().getList();
    }

    public EvalUnit call(List<EvalUnit> params,Environment environment){
        for(int i=0;i<params.size();i++){
            environment.add(args.get(i).getAtom().getValue(),params.get(i));
        }
        return Evaluator.eval(evalUnit,environment);
    }
}
