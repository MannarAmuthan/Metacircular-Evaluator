package elements.structures;

import java.util.LinkedList;
import java.util.List;

public class EvalList{

    List<EvalUnit> list;

    public EvalList() {
        list=new LinkedList<>();
    }

    public EvalList(List<EvalUnit> list) {
        this.list = list;
    }

    public void append(EvalUnit evalUnit){
        list.add(evalUnit);
    }

    public List<EvalUnit> getList() {
        return list;
    }
}
