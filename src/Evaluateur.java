import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Evaluateur {
    private Vector<Instruction> instructions;

    public Evaluateur(String fileName) {
        instructions = Parser.parse(Objects.requireNonNull(FileToString(fileName)));
    }

    private static String FileToString(String filename){
        try{

            File file = new File(Objects.requireNonNull(Evaluateur.class.getResource(filename)).toURI());
            FileInputStream f = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            f.read(data);
            f.close();

            return new String(data, StandardCharsets.UTF_8);
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int evaluate() {
        // Créez une Map qui mappe les noms de variables aux valeurs qu'elles ont été affectées
        Map<String, Value> variables = new HashMap<>();

        // Parcourez la liste d'instructions et exécutez chaque instruction
        for (Instruction instruction : instructions) {
            if (instruction instanceof Assign) {
                // C'est une instruction d'affectation simple
                Assign assignment = (Assign) instruction;
                variables.put(assignment.getVariableName(), assignment.getValue());
            } else if (instruction instanceof AssignOperator) {
                // C'est une opération arithmétique
                AssignOperator arithmetic = (AssignOperator) instruction;

                int operande1 = 0;
                if (arithmetic.getOperand1() instanceof Variable) {
                    Variable v = (Variable) arithmetic.getOperand1();
                    if (variables.containsKey(v.getName())) {
                        operande1 = variables.get(v.getName()).getValue();
                    }
                } else {
                    operande1 = arithmetic.getOperand1().getValue();
                }

                int operande2 = 0;
                if (arithmetic.getOperand2() instanceof Variable) {
                    Variable v = (Variable) arithmetic.getOperand2();
                    if (variables.containsKey(v.getName())) {
                        operande2 = variables.get(v.getName()).getValue();
                    }
                } else {
                    operande2 = arithmetic.getOperand2().getValue();
                }

                int result = 0;
                switch (arithmetic.getOperator()) {
                    case "+":
                        result = operande1 + operande2;
                        break;
                    case "*":
                        result = operande1 * operande2;
                        break;
                    case "-":
                        result = operande1 - operande2;
                        break;
                }
                variables.put(arithmetic.getVariableName(), new Entier(result));
            }
        }

        // Renvoyez la valeur de la variable x
        if (variables.containsKey("x")) {
            return variables.get("x").getValue();
        } else {
            return 0;
        }
    }
}
