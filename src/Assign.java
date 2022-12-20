class Assign extends Instruction{
    String lhs;
    Value rhs;

    Assign(String s, Value x){
        lhs = s;
        rhs = x;
    }

    public String getVariableName() {
        return lhs;
    }

    public Value getValue() {
        return rhs;
    }

    @Override
    public String toString() {
        String var;

        if (rhs instanceof Variable) {
            var = ((Variable) rhs).getName();
        } else {
            var = ((Entier)rhs).getValue() + "";
        }
        return lhs + "=" + rhs;
    }
}
