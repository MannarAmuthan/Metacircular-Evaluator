package meta;
import java.util.Optional;

public class Either<L,R> {
    private final Optional<L> optionalL;
    private final Optional<R> optionalR;

    public Either(L l, R r) {
        this.optionalL = Optional.ofNullable(l);
        this.optionalR =Optional.ofNullable(r);
    }

    public boolean isLPresent(){
        return optionalL.isPresent();
    }

    public boolean isRPresent(){
        return optionalR.isPresent();
    }

    public L getL(){
        return optionalL.get();
    }

    public R getR() {
        return optionalR.get();
    }

}
