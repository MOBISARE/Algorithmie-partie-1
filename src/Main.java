
public class Main {

    public static void main(String[] args){
		// String s = "x=1;y=x*2;z=x-y;x=y+z";
		Evaluateur evaluateur1 = new Evaluateur("prog1");
        Evaluateur evaluateur2 = new Evaluateur("opt_prog.txt");
        System.out.println("Question 1\n" + evaluateur1.evaluate());
        System.out.println("Question 3\n voir fichier créé ressources/opt_prog.txt");
        evaluateur1.abstractEvaluate();
        System.out.println("Question 4\n" + evaluateur1.simplifyEvaluate());
        System.out.println("Question 5\nmodifier le fichier ressources/opt_prog.txt " +
                "pour comparer si on obtient ou non le même résultat\n"
                + (evaluateur1.simplifyEvaluate() == evaluateur2.simplifyEvaluate()));

    }
}
