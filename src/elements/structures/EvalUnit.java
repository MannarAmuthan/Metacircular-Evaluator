package elements.structures;

import elements.utils.Either;

public class EvalUnit extends Either<Atom, EvalList> {
    private EvalUnit(Atom atom,EvalList evalList) {
        super(atom,evalList);
    }

    public static EvalUnit atom(Atom atom){
        return new EvalUnit(atom,null);
    }

    public static EvalUnit evalList(EvalList evalList){
        return new EvalUnit(null,evalList);
    }

    public boolean isAtomPresent(){
        return super.isLPresent();
    }

    public boolean isEvalListPresent(){
        return super.isRPresent();
    }

    public Atom getAtom(){
        return super.getL();
    }

    public EvalList getEvalList(){
        return super.getR();
    }
}
