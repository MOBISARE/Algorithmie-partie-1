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
        return lhs + "=" + t0 + op + t1;
    }
}
