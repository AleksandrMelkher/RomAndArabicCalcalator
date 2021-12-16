package Calculate;

import java.util.*;

public class Calculator {
    public static void main(String[] args) {
        Main main = new Main();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println(main.calc(input));
    }
}

class Main {

    public String calc(String inputString) {

        String result;

        String[] inputArrayNotSign = signInExpression(inputString, "+-*/", false); // Массив с символами без знаков
        String[] inputArrayWithSign = signInExpression(inputString, "+-*/", true); // Массив с символами со знаками
        String[] inputArrayOnlySign = signInExpression(inputString, "iIvVxX1234567890", false); // Массив со знаками

        boolean normalOperandCount = chekOperandCount(inputArrayNotSign); // Проверка на количество операндов
        boolean extraSigns = checkSign(inputArrayOnlySign); // Проверка на лишние знаки
        boolean romCheck = checkRom(inputString); // Проверка на римские числа
        boolean arabCheck = checkArab(inputString); // Проверка на арабские числа

        if(normalOperandCount){
            if(extraSigns){
                if(romCheck|arabCheck){
                    if(romCheck) // Работа с римскими числами
                    {   String[] romToArabic = romToArabic(inputArrayWithSign);              //римские в арабские
                        ArrayList<String> arrayRomToArabic = removeSingleSigns(romToArabic); // |___окончание перевода
                        if(checkRange(arrayRomToArabic)){ // Проверяю на диапазон от 1 до 10
                            String arabicResult = math(arrayRomToArabic); // отправка на подсчет
                            result = arabicToRome(arabicResult); // Результат при подсчете арабских чисел и перевод в римские
                        } else result="Исключение";
                    }

                    else // Работа с арабскими числами
                    {
                        ArrayList<String> arrayArabic = removeSingleSigns(inputArrayWithSign);
                        if(checkRange(arrayArabic)){ // Проверяю на диапазон от 1 до 10
                            result = math(arrayArabic); //Результат при подсчете арабских чисел
                        }else result="Исключение";
                    }
                }else result="Исключение";
            }else result="Исключение";
        }else result="Исключение";

        return result;
    }


    /**
     *Метод преобразует строку в токены
     */
    private static String[] signInExpression(String expression,String delim, boolean signOn){
        StringTokenizer in = new StringTokenizer(expression, delim, signOn);
        String[]out = new String[in.countTokens()];
        for (int i = 0; i < expression.length(); i++) {
            if(in.hasMoreTokens()) {
                out[i] = in.nextToken();
            }else break;
        } return out;
    }

    /**
     *Проверка на римские числа
     */
    private static boolean checkRom(String expression){
        String[] s = signInExpression(expression, "+-*/IVXivx",false);
        return Arrays.toString(s).length() <= 2;
    }

    /**
     * Проверка на арабские числа
     */
    private static boolean checkArab(String expression){
        String[] s = signInExpression(expression, "+-*/1234567890",false);
        return Arrays.toString(s).length() <= 2;
    }

    /**
     * Проверка на лишние знаки
     */
    private static boolean checkSign(String[] signExpress){
        int normal = 0;
        if(signExpress.length>2) normal++;
        if(signExpress.length==2){
            if(!signExpress[0].equals("-")) normal++;
        }
        return normal <= 0;
    }

    /**
     * Проверка на количество операндов
     */
    private  static boolean chekOperandCount(String[] tokenNoSign){
        return tokenNoSign.length ==2;
    }

    /**
     * Проверка цифр в выражении на диапазон от 1 до 10
     */
    private static boolean checkRange(ArrayList<String> arrayWithSign){
        int rangeLeft = Integer.parseInt(arrayWithSign.get(0));
        int rangeRight = Integer.parseInt(arrayWithSign.get(2));
        int NegativeRangeRight;
        if(arrayWithSign.get(1).equals("-") & rangeRight < 0){
            NegativeRangeRight =-rangeRight;
        }else NegativeRangeRight = rangeRight;
        return rangeLeft <= 10 && rangeLeft > 0 && NegativeRangeRight <= 10 && NegativeRangeRight > 0;
    }

    /**
     * Перевод римских в арабские
     */
    private static String[] romToArabic (String[] LeftRight){
        String[] leftAndRight = new String[LeftRight.length];
        for (int i = 0; i < LeftRight.length; i++) {
            switch (LeftRight[i]) {
                case "I", "i" -> leftAndRight[i]="1";
                case "II", "ii", "iI", "Ii" -> leftAndRight[i]="2";
                case "III", "iii", "iII", "IiI", "IIi", "iIi", "iiI", "Iii" -> leftAndRight[i]="3";
                case "IV", "iv", "Iv", "iV" -> leftAndRight[i]="4";
                case "V", "v" -> leftAndRight[i]="5";
                case "VI", "vi", "vI", "Vi" -> leftAndRight[i]="6";
                case "VII", "vii", "vII", "ViI", "VIi", "vIi", "viI", "Vii" -> leftAndRight[i]="7";
                case "VIII", "viii", "Viii", "vIii", "viIi", "viiI", "ViiI", "VIii", "VIIi" -> leftAndRight[i]="8";
                case "IX", "ix", "Ix", "xI" -> leftAndRight[i]="9";
                case "X", "x" -> leftAndRight[i]="10";
                case "+" -> leftAndRight[i]="+";
                case "-" -> leftAndRight[i]="-";
                case "*" -> leftAndRight[i]="*";
                case "/" -> leftAndRight[i]="/";
                default -> leftAndRight[i]="11";
            }
        } return leftAndRight;
    }

    /**
     * Перевод арабских чисел в римские
     */
    private static String arabicToRome(String arabicNum){
        char[] arabicNumChar = arabicNum.toCharArray();
        int len = arabicNumChar.length;
        String romNum;
        if (arabicNumChar[0] == '-' || arabicNumChar[0] == '0'){
            romNum = "' '";
        } else if (len >= 3){
            romNum = "C";
        }else{
            String tensStr = "";
            String unitsStr;
            if (len == 1) {
                char units;
                units = arabicNumChar[0];
                unitsStr = Character.toString(units);   // перевод char в string
            } else {
                char tens;
                char units;
                tens = arabicNumChar[0];
                units = arabicNumChar[1];
                tensStr = Character.toString(tens);   // перевод char в string
                unitsStr = Character.toString(units); // перевод char в string
            }
            switch (tensStr) {                        // Кейс перевода десятков из арабского в римский
                case "1" -> tensStr = "X";
                case "2" -> tensStr = "XX";
                case "3" -> tensStr = "XXX";
                case "4" -> tensStr = "XL";
                case "5" -> tensStr = "L";
                case "6" -> tensStr = "LX";
                case "7" -> tensStr = "LXX";
                case "8" -> tensStr = "LXXX";
                case "9" -> tensStr = "XC";
            }
            switch (unitsStr) {                        // Кейс перевода единиц из арабского в римский
                case "1" -> unitsStr = "I";
                case "2" -> unitsStr = "II";
                case "3" -> unitsStr = "III";
                case "4" -> unitsStr = "IV";
                case "5" -> unitsStr = "V";
                case "6" -> unitsStr = "VI";
                case "7" -> unitsStr = "VII";
                case "8" -> unitsStr = "VIII";
                case "9" -> unitsStr = "IX";
                case "0" -> unitsStr = "";
            }
            romNum = (tensStr + unitsStr);
        } return romNum;
    }

    /**
     * Метод возвращает коллекцию без элементов, которые содержат одиночные знаки математического действия
     * для дальнейшей обработки
     */
    private static ArrayList<String> removeSingleSigns(String[]expression){
        for (int i = 0; i < expression.length; i++) {
            if (expression[i].equals("-")) {
                expression[i + 1] = expression[i] + expression[i + 1];
            } else expression[i] = expression[i];
        }
        ArrayList<String> list = new ArrayList<>(List.of(expression));

        if (list.get(0).equals("-")) list.remove(0);
        if (list.size() == 4) {
            String temporary;
            temporary = list.get(3);
            list.remove(3);
            list.remove(2);
            list.add(temporary);
        }
        return list;
    }

    /**
     * Метод производит математическое действие над операндами
     */
    private static String math(ArrayList<String> arrayArabic) {
        int left = Integer.parseInt(arrayArabic.get(0));
        int right = Integer.parseInt(arrayArabic.get(2));
        if (arrayArabic.get(1).equals("+")) {
            return Integer.toString(left + right);
        } else if (arrayArabic.get(1).equals("-") & right < 0) {
            return Integer.toString(left - (-right));
        } else if (arrayArabic.get(1).equals("-") & right > 0) {
            return Integer.toString(left - right);
        } else if (arrayArabic.get(1).equals("*")) {
            return Integer.toString(left * right);
        } else {
            return Integer.toString(left / right);
        }
    }
}