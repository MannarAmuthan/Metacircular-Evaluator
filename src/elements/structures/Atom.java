package elements.structures;


public class Atom{
    AtomType atomType;
    String value;

    private Atom(AtomType atomType, String value) {
        this.atomType = atomType;
        this.value = value;
    }

    static public Atom getAtom(String s){
        boolean isNumeric = s.chars().allMatch( Character::isDigit);
        if(isNumeric) return new Atom(AtomType.NUMBER,s);
        return new Atom(AtomType.STRING,s);
    }

    public AtomType getAtomType() {
        return atomType;
    }

    public String getValue() {
        return value;
    }

}
