class Entier extends Value {

    Integer x;
    Entier(Integer s){
        x = s;
    }

    @Override
    public String toString(){
        return x.toString();
    }

    @Override
    public int getValue() {
        return x;
    }
}
