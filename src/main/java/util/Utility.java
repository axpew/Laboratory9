package util;

import java.text.CollationKey;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Calendar;
import java.util.Stack;

public class Utility {

    private static final Random random;
  //  private static CircularLinkedList employeeList;
  //  private static CircularDoublyLinkedList jobPositionList;
  //  private static CircularDoublyLinkedList staffingList;

    //Para laboratorio 7
  //  private static LinkedQueue queue;
  //  private static LinkedStack stack;

    //constructor estático, inicializador estático
    static {
        // semilla para el random
        long seed = System.currentTimeMillis();
        random = new Random(seed);

        //Para laboratorio 7
     //   queue = new LinkedQueue();
     //   stack = new LinkedStack();

     //   employeeList = new CircularLinkedList();
     //   jobPositionList = new CircularDoublyLinkedList();
     //   staffingList = new CircularDoublyLinkedList();
    }

/*
    //---- PARA LABORATORIO 7 ----
    public static LinkedQueue getQueue() {
        return queue;
    }

    public static void setQueue(LinkedQueue queue) {
        Utility.queue = queue;
    }

    public static LinkedStack getStack() {
        return stack;
    }

    public static void setStack(LinkedStack stack) {
        Utility.stack = stack;
    }
*/
    /*
    public static CircularLinkedList getEmployeeList() {
        return employeeList;
    }

    public static void setEmployeeList(CircularLinkedList employeeList) {
        Utility.employeeList = employeeList;
    }

    public static CircularDoublyLinkedList getJobPositionList() {
        return jobPositionList;
    }

    public static void setJobPositionList(CircularDoublyLinkedList jobPositionList) {
        Utility.jobPositionList = jobPositionList;
    }

    public static CircularDoublyLinkedList getStaffingList() {
        return staffingList;
    }

    public static void setStaffingList(CircularDoublyLinkedList staffingList) {
        Utility.staffingList = staffingList;
    }
*/
    // ------------------------------------------------------------- Métodos:
    public static int random(int bound){
        // return(int) Math.floor(Math.random()*bound); //Forma 1
        return random.nextInt(bound);
    }


    public static void fill(int[] a) {
        for (int i = 0; i < a.length; i++){
            a[i] = random(99);
        }
    }

    public static String format(long n) {
        return new DecimalFormat("###,###,###.##").format(n); //Establecer un formato para n
    }

    public static String format(double n) {
        return new DecimalFormat("###,###,###.##").format(n); //Establecer un formato para n
    }

    public static String $format(double n) {
        return new DecimalFormat("$###,###,###.##").format(n); //Establecer un formato para n
    }

    public static int min(int x, int y) {
        return x<y ? x : y;
    }

    public static int max(int x, int y) {
        return x>y ? x : y;
    }

    public static String show(int[] a) {
        String result ="";
        for (int item : a){
            if (item == 0) break; //si es cero es porque no hay más elementos
            result+=item + " ";
        }//End for
        return result;
    }

    public static int compare(Object a, Object b) {
        switch (instanceOf(a,b)){
            case "Integer":
                Integer int1 = (Integer)a;
                Integer int2 = (Integer)b;
                return int1 < int2 ? -1 :int1 > int2 ? 1 : 0; // ? --> Si es cierto haga... /// : --> Pero si no es cierto entonces haga... Puntos son como el else, ? es como el if

            case "String":
                String str1 = (String)a;
                String str2 = (String)b;
                return str1.compareTo(str2) < 0 ? -1 : str1.compareTo(str2) > 0 ? 1 : 0;

            case "Character":
                Character ch1 = (Character) a; Character ch2 = (Character) b;
                return ch1.compareTo(ch2) < 0 ? -1 : ch1.compareTo(ch2) > 0 ? 1 : 0;

          /*  case "Employee":
                Employee emp1 = (Employee) a; Employee emp2 = (Employee) b;
                return emp1.getId() < emp2.getId() ? -1 :  emp1.getId() > emp2.getId() ? 1 : 0;

            case "JobPosition":
                JobPosition job1 = (JobPosition) a;
                JobPosition job2 = (JobPosition) b;
                return job1.getId() < job2.getId() ? -1 : job1.getId() > job2.getId() ? 1 : 0;
                */

        }//End switch

        return 2; //Cuando es un caso Unknown
    }

    public static String instanceOf(Object a, Object b) {
        if (a instanceof Integer && b instanceof Integer) return "Integer";
        if (a instanceof String && b instanceof String) return "String";
        if (a instanceof Character && b instanceof Character) return "Character";
       // if (a instanceof Employee && b instanceof Employee) return "Employee";
       // if (a instanceof JobPosition && b instanceof JobPosition) return "JobPosition";

        return "Unknown";
    }

    public static String dateFormat(Date value) {
        return new SimpleDateFormat("dd/MM/yyyy").format(value);
    }//End dateFormat

    public static Date parseDate(String dateStr) { //Convierte de String a Date
        SimpleDateFormat fechaFormateada = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return fechaFormateada.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getAge(Date date) {
        Calendar calendarBD = Calendar.getInstance();
        calendarBD.setTime(date);

        Calendar calendarToday = Calendar.getInstance();

        int age = calendarToday.get(Calendar.YEAR) - calendarBD.get(Calendar.YEAR); //Se resta el año actual al año de nacimiento y da la edad

        //Si el cumpleaños todavía no ocurre
        if (calendarToday.get(Calendar.DAY_OF_YEAR) < calendarBD.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }


    private static int getPriorityOperators(char operator) {

        switch (operator) {
            case '+': case '-': return 1; //Prioridad más baja
            case '*': case '/': return 2;
            case '^': return 3; //Prioridad más alta
        } return -1;

    }

/*
    public static boolean isBalanced(String expression){

        LinkedStack linkedStack = new LinkedStack();

        try {
            for (char character : expression.toCharArray()) {
                if (character == '(' || character == '[' || character == '{') {
                    linkedStack.push(character);
                } else if (character == ')' || character == ']' || character == '}') {
                    if (linkedStack.isEmpty()) {
                        return false;
                    }
                    char top = (char) linkedStack.pop();
                    if (!matches(top, character)) {
                        return false;
                    }
                }
            }
        } catch (StackException e) {
            throw new RuntimeException(e);
        }

        return linkedStack.isEmpty();
    }
*/
    private static boolean matches(char open, char close) {
        return (open == '(' && close == ')') ||
                (open == '[' && close == ']') ||
                (open == '{' && close == '}');
    }

    //METODO QUE RESUELVE LA OPERACION DE LA EXPRESION INFIJA -----------------------------------------------------
/*
    public static int evaluateInfix(String infixExpression) {
        Stack<Integer> operands = new Stack<>();  // Pila para operandos
        Stack<Character> operators = new Stack<>();  // Pila para operadores

        for (int i = 0; i < infixExpression.length(); i++) {
            char c = infixExpression.charAt(i);

            if (c == ' ') continue; //Para ignorar los espacios en blanco

            //Si es dígito se agrega a la pila de operando
            if (Character.isDigit(c)) {
                operands.push(c - '0');
            }
            else if (c == '(') {
                operators.push(c);
            }
            else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    char operator = operators.pop();
                    int operand2 = operands.pop();
                    int operand1 = operands.pop();
                    int result = calculate(operator, operand1, operand2);
                    operands.push(result);
                }
                operators.pop();
            }
            //Si es un operador resuelve las operaciones
            else if (isOperator(c)) {
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    char operator = operators.pop();
                    int operand2 = operands.pop();
                    int operand1 = operands.pop();
                    int result = calculate(operator, operand1, operand2);
                    operands.push(result);
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) {
            char operator = operators.pop();
            int operand2 = operands.pop();
            int operand1 = operands.pop();
            int result = calculate(operator, operand1, operand2);
            operands.push(result);
        }

        //El resultado final estará en la cima de la pila de operandos
        return operands.pop();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static boolean hasPrecedence(char operator1, char operator2) {
        if (operator2 == '(' || operator2 == ')') {
            return false;
        }
        if ((operator1 == '*' || operator1 == '/') && (operator2 == '+' || operator2 == '-')) {
            return false;
        }
        return true;
    }
*/
    private static int calculate(char operator, int operand1, int operand2) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    throw new ArithmeticException("División por cero");
                }
                return operand1 / operand2;
            case '^':
                return (int)Math.pow(operand1, operand2);
            default:
                throw new IllegalArgumentException("Operador inválido");
        }
    }

// ---------------------------------------------------------------------------------------------------------------------------------------------------------

    //-------------------- Métodos para Laboratory7 --------------------
    public static String getPlace() {
        String places[] = {"San José", "Ciudad Quesada", "Paraíso",
                "Turrialba", "Limón", "Liberia", "Puntarenas", "San Ramón", "Puerto Viejo", "Volcán Irazú", "Pérez Zeledón",
                "Palmares", "Orotina", "El coco", "Ciudad Neilly", "Sixaola", "Guápiles","Siquirres"
                , "El Guarco", "Cartago", "Santa Bárbara", "Jacó", "Manuel Antonio", "Quepos", "Santa Cruz",
                "Nicoya"};

        return places[random(places.length-1)];
    }

    public static String getWeather() {
        String weathers[] = {"Rainy", "Thunderstorm", "Sunny", "Cloudy", "Foggy"};
        return  weathers[random(weathers.length-1)];
    }

    public static String getPersonName() {
        String names [] = {"Juan", "Alejandro", "Natalia", "Fabiana", "Victoria", "Ana", "Valeria", "Nicole", "Esteban", "Rodrigo",
                            "Victor", "Ricardo", "Bryan", "Pedro", "David", "José", "María", "Caleb", "Eduardo", "Jason", "Alex"};
        return names[random(names.length-1)];
    }

    public static String getMood() {
        String moods[] = {"Happiness", "Sadness", "Anger", "Sickness", "Cheerful",
                "Reflective", "Gloomy", "Romantic", "Calm", "Hopeful", "Fearful", "Tense", "Lonely"};

        return moods[random(moods.length-1)];
    }

    public static int getAttention() {
        return random.nextInt(100); // Genera un número entre 0 y 99;
    }



    //------------------------------------------------------------------------------

    //-------------------- Métodos para Laboratory8 --------------------
    
    public static int maxArray(int[] a) {
        int max = a[0]; //first element
        for (int i = 1; i < a.length; i++) {
            if(a[i]>max){
                max=a[i];
            }
        }
        return max;
    }//end maxArray
    
    public static int[] getIntegerArray(int n){
        int arrayResult[] = new int[n];

        for (int i = 0; i < n; i++)
            arrayResult[i] = random(9999);

        return arrayResult;
    }//end getIntegerArray

    /*
    public static void sortElementaryArrays(String algorithm, int[] array) {

        switch (algorithm) {
            case "bubbleSort":
                Elementary.bubbleSort(array);
                break;

            case "improvedBubbleSort":
                Elementary.improvedBubbleSort(array);
                break;

            case "selectionSort":
                Elementary.selectionSort(array);
                break;

            case "countingSort":
                Elementary.countingSort(array);
                break;

        }//End switch

    }//end sortElementaryArrays()
*/

    public static int[] copyArray(int[] a, int lenght){
        int[] result = new int[lenght];

        for (int i = 0; i < lenght; i++)
            result[i] = a[i];

        return result;

    }//end copyArray

    //Para recibir low y high
    public static int random(int low, int high) {
        return low + random.nextInt(high - low + 1);
    }

    public static boolean isDigit(String s) {
        return s != null && s.matches("\\d+");
    }

    //------------------------------------------------------------------------------

    //-------------------- Métodos para Laboratory9 --------------------











}//END CLASS

