class AssignOperator extends Instruction{
    String lhs;
    String op;
    Value t0;
    Value t1;

    AssignOperator(String s, String ope, Value x, Value y){
        lhs = s;
        op = ope;
        t0 = x;
        t1 = y;
    }

    public String getOperator() {
        return op;
    }

    public Value getOperand1() {
        return t0;
    }

    public Value getOperand2() {
        return t1;
    }

    public String getVariableName() {
        return lhs;
    }

    @Override
    public String toString() {
        String var1;
        String var2;

        if (t0 instanceof Variable) {
            var1 = ((Variable) t0).getName();
        } else {
            var1 = ((Entier)t0).getValue() + "";
        }

        if (t1 instanceof Variable) {
            var2 = ((Variable) t1).getName();
        } else {
            var2 =  ((Entier)t1).getValue() + "";
        }
        return lhs + "=" + var1 + op + var2;
    }
}
