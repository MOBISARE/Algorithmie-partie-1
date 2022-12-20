
public class Main {

    public static void main(String[] args){
		// String s = "x=1;y=x*2;z=x-y;x=y+z";
		Evaluateur evaluateur = new Evaluateur("prog1");
        System.out.println(evaluateur.evaluate());

        evaluateur.abstractEvaluate();
    }
}
