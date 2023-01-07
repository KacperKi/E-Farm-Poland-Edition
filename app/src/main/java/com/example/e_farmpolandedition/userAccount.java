package com.example.e_farmpolandedition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class userAccount {
    private String login;
    private String name;
    private String surname;
    private String email;
    private String date;
    private String wojewodztwo;
    private String password;

    public userAccount() {
    }

    public userAccount(String login, String name, String surname, String email,
                       String date, String wojewodztwo, String password) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.date = date;
        this.wojewodztwo = wojewodztwo;
        this.password = password;
    }

    public String getWojewodztwo() {
        return wojewodztwo;
    }

    public void setWojewodztwo(String wojewodztwo) {
        this.wojewodztwo = wojewodztwo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean validationData(){

        String exName = "^[a-zA-Z\\s]+";
        String exLogin = "^[a-zA-Z0-9._-]{3,}$";
        String exEmail = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        String exDate = "^\\d{2}.\\d{2}.\\d{4}$";
        String exPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

        Pattern pName = Pattern.compile(exName, Pattern.CASE_INSENSITIVE);
        Pattern pSurName = Pattern.compile(exName, Pattern.CASE_INSENSITIVE);
        Pattern pLogin = Pattern.compile(exLogin, Pattern.CASE_INSENSITIVE);
        Pattern pEmail = Pattern.compile(exEmail, Pattern.CASE_INSENSITIVE);
        Pattern pDate = Pattern.compile(exDate, Pattern.CASE_INSENSITIVE);
        Pattern pPassword = Pattern.compile(exPassword,Pattern.CASE_INSENSITIVE);

        Matcher matcher = pName.matcher(this.getName());
        Matcher matcher1 = pSurName.matcher(this.getSurname());
        Matcher matcher2 = pLogin.matcher(this.getLogin());
        Matcher matcher3 = pEmail.matcher(this.getEmail());
        //Matcher matcher4 = pDate.matcher(this.getDate());
        Matcher matcher5 = pPassword.matcher(this.getPassword());

        if (    matcher.matches()
                && matcher1.matches()
                && matcher2.matches()
                && matcher3.matches()
               // && matcher4.matches()
                && matcher5.matches()
        ) return true;
        return false;
    }

}
