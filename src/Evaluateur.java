import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Evaluateur {
    private final Vector<Instruction> instructions;

    public Evaluateur(String fileName) {
        instructions = Parser.parse(Objects.requireNonNull(FileToString(fileName)));
    }

    private static String FileToString(String filename){
        try{

            File file = new File(Objects.requireNonNull(Evaluateur.class.getResource(filename)).toURI());
            FileInputStream f = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            int res = f.read(data);
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
//                        while (operande2 > 0) {
//                            if (operande2 % 2 != 0) result = (result + operande1);
//                            operande2 >>= 1;
//                            operande1 = (2*operande1);
//                        }
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
            if (variables.get("x") instanceof Variable) {
                return variables.get(((Variable) variables.get("x")).getName()).getValue();
            }

            return variables.get("x").getValue();
        } else {
            return 0;
        }
    }

    public void abstractEvaluate() {
        Map<String, AbstractValue> variables = new HashMap<>();
        Vector<Instruction> newInstructions = new Vector<>();

        try {
            File myFile = new File("./ressources/opt_prog.txt");

            if (myFile.exists()) {
                myFile.delete();
                myFile.createNewFile();
            } else {
                myFile.createNewFile();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        for(Instruction i: instructions) {
            if (i instanceof Assign) {
                Assign a = (Assign) i;

                if (a.getValue() instanceof Entier) {
                    if (a.getValue().getValue() == 1) {
                        variables.put(a.getVariableName(),AbstractValue.UN);
                    } else if (a.getValue().getValue() == 0) {
                        variables.put(a.getVariableName(),AbstractValue.ZERO);
                    } else {
                        variables.put(a.getVariableName(), AbstractValue.UNDEFINED);
                        newInstructions.add(i);
                    }
                } else {
                    String varName = ((Variable)a.getValue()).getName();
                    variables.put(a.getVariableName(), variables.getOrDefault(varName, AbstractValue.UNDEFINED));
                    newInstructions.add(i);
                }
            } else {
                AssignOperator ao = (AssignOperator) i;

                AbstractValue operande1 = AbstractValue.UNDEFINED;
                AbstractValue operande2 = AbstractValue.UNDEFINED;

                if (ao.getOperand1() instanceof Variable) {
                    operande1 = variables.getOrDefault(((Variable) ao.getOperand1()).getName(), AbstractValue.UNDEFINED);
                } else {
                    if (ao.getOperand1().getValue() == 0) {
                        operande1 = AbstractValue.ZERO;
                    } else if (ao.getOperand1().getValue() == 1) {
                        operande1 = AbstractValue.UN;
                    }
                }

                if (ao.getOperand2() instanceof Variable) {
                    operande2 = variables.getOrDefault(((Variable) ao.getOperand2()).getName(), AbstractValue.UNDEFINED);
                } else {
                    if (ao.getOperand2().getValue() == 0) {
                        operande2 = AbstractValue.ZERO;
                    } else if (ao.getOperand2().getValue() == 1) {
                        operande2 = AbstractValue.UN;
                    }
                }

                AbstractValue result = AbstractValue.UNDEFINED;
                switch (ao.getOperator()) {
                    case "+":
                        if (operande1 == AbstractValue.ZERO && operande2 == AbstractValue.ZERO) {
                            result = AbstractValue.ZERO;
                        } else if ((operande1 == AbstractValue.ZERO && operande2 == AbstractValue.UN)
                                || (operande1 == AbstractValue.UN && operande2 == AbstractValue.ZERO)) {
                            result = AbstractValue.UN;
                        }
                        break;
                    case "-":
                        if ((operande1 == AbstractValue.ZERO && operande2 == AbstractValue.ZERO)
                                || (operande1 == AbstractValue.UN && operande2 == AbstractValue.UN)) {
                            result = AbstractValue.ZERO;
                        } else if (operande1 == AbstractValue.UN && operande2 == AbstractValue.ZERO) {
                            result = AbstractValue.UN;
                        }
                        break;
                    case "*":
                        if (operande1 == AbstractValue.ZERO || operande2 == AbstractValue.ZERO) {
                            result = AbstractValue.ZERO;
                        } else if (operande1 == AbstractValue.UN && operande2 == AbstractValue.UN) {
                            result = AbstractValue.UN;
                        }
                        break;
                }

                if (result == AbstractValue.UNDEFINED) {
                    newInstructions.add(i);
                }
                variables.put(ao.getVariableName(), result);
            }
        }

        try {

            FileWriter fileWriter = new FileWriter("./ressources/opt_prog.txt");
            if (variables.containsKey("x")) {
                if (variables.get("x") == AbstractValue.ZERO) {
                    fileWriter.write("x = 0\n");
                } else if (variables.get("x") == AbstractValue.UN) {
                    fileWriter.write("x = 1\n");
                } else {
                    for (Instruction i: newInstructions) {
                        fileWriter.write(i.toString() + "\n");
                    }
                }

            } else {
                fileWriter.write("x = 0\n");
            }
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int simplifyEvaluate() {
        int p = RandomPrime.randomPrime();
        HashMap<String, Value> variables = new HashMap<>();

        // Répéter jusqu'à ce que le compteur d'instruction dépasse la longueur du programme
        for (Instruction instruction : instructions) {

            final int result;

            if (instruction instanceof Assign) {
                // C'est une instruction d'affectation simple
                Assign assignment = (Assign) instruction;
                variables.put(assignment.getVariableName(), assignment.getValue());
            } else if (instruction instanceof AssignOperator) {
                // C'est une opération arithmétique
                AssignOperator arithmetic = (AssignOperator) instruction;
                final int operande1 = createOperande(arithmetic.getOperand1(), variables);
                final int operande2 = createOperande(arithmetic.getOperand2(), variables);

                switch (arithmetic.getOperator()) {
                    case "+":
                        result = operande1 + operande2;
                        break;
                    case "*":
                        int o1 = operande1;
                        int o2 = operande2;
                        int r = 0;
                        while (o2 != 0) {
                            if ((o2 & 1) == 1){
                                r = (r + o1) % p;
                            }
                            o2 >>= 1;
                            o1 = (2 * o1) % p;
                        }
                        result = r;
                        break;
                    case "-":
                        result = operande1 - operande2;
                        break;

                    default:
                        throw new IllegalStateException("Opérateur inexistant");
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


    private int createOperande(Value value, HashMap<String, Value> variables) {
        final int operande;
        if (value instanceof Variable) {
            Variable v = (Variable) value;
            if (variables.containsKey(v.getName())) {
                operande = variables.get(v.getName()).getValue();
            } else {
                throw new IllegalStateException("Variable non initialisée");
            }
        } else {
            operande = value.getValue();
        }
        return operande;
    }
}
