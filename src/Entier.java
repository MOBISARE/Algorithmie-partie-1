class Entier extends Value {

    Integer x;
    Entier(Integer s){
        x = s;
    }

    @Override
    public String toString(){
        return "Entier " +  x.toString();
    }

    @Override
    public int getValue() {
        return x;
    }
}
