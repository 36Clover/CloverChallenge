package com.example.demo;

public class App {
    public static void main(String[] args) {
        ClovaSpeechClient cv = new ClovaSpeechClient();
        System.out.println(cv.pa_json());
    }
}
