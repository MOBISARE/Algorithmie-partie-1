class Variable extends Value{
    String var;
    Variable(String s){
	    var = s;
    }

    public String getName() {
        return var;
    }

    @Override
    public String toString(){
	    return "Variable " + var;
    }   
}
