package com.rickyaut.tools.cucumber;

public class Hello {
    private final String greeting;

    public Hello(String greeting) {
        this.greeting = greeting;
    }

    public String sayHi() {
        return greeting + " World";
    }
}
