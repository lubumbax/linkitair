package com.lubumbax.linkitair.flights.test.matcher;

import org.mockito.ArgumentMatcher;

public class CaseInsensitiveEquals implements ArgumentMatcher<String> {
    String s;

    public CaseInsensitiveEquals(String string) {
        this.s = string;
    }

    public boolean matches(String string) {
        return s.toLowerCase().equals(string.toLowerCase());
    }
}