package utils;

import exceptions.ExceptionCode;
import exceptions.MyException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public final class DataFromUserService {

    private DataFromUserService() {}

    private static Scanner sc=new Scanner(System.in);

    public static String getStringSpaceAndUpperCase(){
        String text = sc.nextLine();
        if (!text.matches("[A-Z ]+")) {
            throw new MyException(ExceptionCode.VALIDATION, "VALUE IS NOT UPPERCASE OR SPACE: " + text);
        }
        return text;

    }
    public static int getInt() {

        String text = sc.nextLine();
        if (!text.matches("\\d+")) {
            throw new MyException(ExceptionCode.VALIDATION, "VALUE IS NOT DIGIT: " + text);
        }
        return Integer.parseInt(text);
    }

    public static int getInt(int scale) {

        String text = sc.nextLine();
        if (!text.matches("\\d+")) {
            throw new MyException(ExceptionCode.VALIDATION, "VALUE IS NOT DIGIT: " + text);
        }
        if (Integer.parseInt(text) < 1 || Integer.parseInt(text) > scale) {
            throw new MyException(ExceptionCode.VALIDATION, "RANGE OUT OF BOUND " + text);
        }

        return Integer.parseInt(text);
    }


    public static boolean getYesOrNo() {

        String text = sc.nextLine();
        if (!text.matches("TAK|NIE|tak|nie+")) {
            throw new MyException(ExceptionCode.VALIDATION, "NOT CORRECT AGREEMENT FORMAT " + text);
        }
        if (text.equalsIgnoreCase("TAK")) {
            return true;
        }
        if (text.equalsIgnoreCase("NIE")) {
            return false;
        } else {
            throw new MyException(ExceptionCode.VALIDATION, "INCORRECT VALUE " + text);
        }
    }

    public static BigDecimal getPrice() {

        String text = sc.nextLine();
        if (!text.matches("\\d.+|\\d|-\\d.+|-\\d")) {

            throw new MyException(ExceptionCode.VALIDATION, "VALUE IS NOT BIG DECIMAL: " + text);
        }
        if (new BigDecimal(text).compareTo(BigDecimal.ZERO) < 0) {
            throw new MyException(ExceptionCode.VALIDATION, "VALUE UNDER ZERO " + text);
        }

        return new BigDecimal(text);
    }
    public static String getEmail() {

        String text = sc.nextLine();
        if (!text.matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")) {
            throw new MyException(ExceptionCode.VALIDATION, "WRONG FORMAT OF E-MAIL " + text);
        }
        return text;
    }
    public static LocalDateTime getLocalDateTime() {

        String text = sc.nextLine();
        if (!text.matches("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]")) {
            throw new MyException(ExceptionCode.VALIDATION, "WRONG FORMAT OF LOCAL DATE TIME " + text);
        }
        return LocalDateTime.parse(
                text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static LocalDate getLocalDate() {
        String text = sc.nextLine();
        if (!text.matches("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])")) {
            throw new MyException(ExceptionCode.VALIDATION, "WRONG FORMAT OF LOCAL DATE FROM USER " + text);
        }
        return LocalDate.parse(
                text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static EPayment getEPayment() {
        String text = sc.nextLine();
        if (!text.matches("CASH|CARD|MONEY_TRANSFER")) {
            throw new MyException(ExceptionCode.VALIDATION, "WRONG FORMAT OF EPAYMENT FROM USER " + text);
        }
        return EPayment.valueOf(text);
    }
    public static LocalDate getLocalDateNotPast() {
        String text = sc.nextLine();
        if (!text.matches("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])")) {
            throw new MyException(ExceptionCode.VALIDATION, "WRONG FORMAT OF LOCAL DATE FROM USER " + text);
        }
        LocalDate localDateFromUser=LocalDate.parse(text);
        if(localDateFromUser.isBefore(LocalDate.now())){
            throw new MyException(ExceptionCode.CUSTOMER_ORDERS, "VALIDATION EXCEPTION - ORDER IN PAST");
        }
        return localDateFromUser;
    }
    public static Set<EnumGuaranteeComponents> getSetGuaranteeComponents(){
        String text = sc.nextLine();
        if(text.isEmpty()){
            return new HashSet<>();
        }
        if(!text.matches("[\\w,]*")){
            throw new MyException(ExceptionCode.VALIDATION,"WRONG FORMAT OF SET FROM USER "+text);
        }
        return Arrays.stream(text.split(","))
                .collect(Collectors.toSet())
                .stream()
                .map(EnumGuaranteeComponents::valueOf)
                .collect(Collectors.toSet());
    }
}
