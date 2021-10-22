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

    @Override
    public boolean equals(Object obj) {
        EvalList another=(EvalList) obj;
        if(another.getList().size()!=list.size()) return false;
        for(int i=0;i<list.size();i++){
            if(!another.getList().get(i).equals(list.get(i))){
                return false;
            }
        }
        return true;
    }
}
