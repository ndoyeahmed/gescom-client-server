/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthiam.gescom.config;

/**
 *
 * @author mbaye
 */
public abstract class Validator {

    public static boolean isEmail(String email) {
        boolean stricterFilter = true;
        String stricterFilterString = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        String laxString = ".+@.+\\.[A-Za-z]{2}[A-Za-z]*";
        String emailRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailRegex);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private static boolean isTel(String tel) {
        boolean stricterFilter = true;
        String stricterFilterString = "[0-9]{9}";
        String laxString = "";
        String telRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(telRegex);
        java.util.regex.Matcher m = p.matcher(tel);
        return m.matches();

    }

    public static boolean isNom(String nom) {
        boolean stricterFilter = true;
        String stricterFilterString = "[A-Za-zéèçàê]{2,50}";
        String laxString = "";
        String nomRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(nomRegex);
        java.util.regex.Matcher m = p.matcher(nom);
        return m.matches();
    }

    public static boolean isNomWhithWhiteEspace(String nom) {
        boolean stricterFilter = true;
        String stricterFilterString = "^[a-zA-Z éèçàê]{2,50}+";
        String laxString = "";
        String nomRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(nomRegex);
        java.util.regex.Matcher m = p.matcher(nom);
        return m.matches();
    }

    public static boolean isPassword(String pwd) {
        boolean stricterFilter = true;
        String stricterFilterString = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
        String laxString = "";
        String passwordRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(passwordRegex);
        java.util.regex.Matcher m = p.matcher(pwd);
        return m.matches();
    }

    public static boolean isAlphaNumeric(String alphanumerique) {
        boolean stricterFilter = true;
        String stricterFilterString = "^[a-zA-Z0-9 ]+";
        String laxString = "";
        String alphanumeriqueRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(alphanumeriqueRegex);
        java.util.regex.Matcher m = p.matcher(alphanumerique);
        return m.matches();
    }

    public static boolean isAlphaNumericAndHyphen(String alphanumerique) {
        boolean stricterFilter = true;
        String stricterFilterString = "^[a-zA-Z0-9- _:°,/éèçàê]+$";
        String laxString = "";
        String alphanumeriqueRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(alphanumeriqueRegex);
        java.util.regex.Matcher m = p.matcher(alphanumerique);
        return m.matches();
    }

    public static boolean isPositifNumber(String number) {
        boolean stricterFilter = true;
        String stricterFilterString = "^\\d+$";
        String laxString = "";
        String numberRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(numberRegex);
        java.util.regex.Matcher m = p.matcher(number);
        return m.matches();
    }

    public static boolean isDomainName(String number) {
        boolean stricterFilter = true;
        String stricterFilterString = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
        String laxString = "";
        String numberRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(numberRegex);
        java.util.regex.Matcher m = p.matcher(number);
        return m.matches();
    }

    public static boolean isIpAddress(String number) {
        boolean stricterFilter = true;
        String stricterFilterString = "([0-9]{1,3}\\.){3}[0-9]{1,3}";
        String laxString = "";
        String numberRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(numberRegex);
        java.util.regex.Matcher m = p.matcher(number);
        return m.matches();
    }

    public static boolean isSenegaleseCallNumber(String numero) {
        boolean aide = false;

        if(isTel(numero) && (numero.matches("77(.*)") || numero.matches("78(.*)") || numero.matches("76(.*)") || numero.matches("70(.*)")))
        {
            aide=true;
        }
        
        return aide;
         
    }

    public static boolean isSenegaleseFixeNumber(String numero) {
        boolean aide = false;

        if(isTel(numero) && numero.matches("33(.*)"))
        {
            aide =true;
        }

        return aide;

    }


}
