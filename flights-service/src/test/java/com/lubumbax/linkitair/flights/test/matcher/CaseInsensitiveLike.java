package com.lubumbax.linkitair.flights.test.matcher;

import org.mockito.ArgumentMatcher;

import java.util.ArrayList;
import java.util.List;

public class CaseInsensitiveLike implements ArgumentMatcher<String> {
    List<String> strings;

    public CaseInsensitiveLike(String string) {
        strings = new ArrayList<>(1);
        strings.add(string);
    }

    public CaseInsensitiveLike(String ... strings) {
        this.strings = new ArrayList<>(strings.length);
        for (String s : strings) {
            this.strings.add(s);
        }
    }

    public boolean matches(String match) {
        for (String s : strings) {
            if (s.toLowerCase().indexOf(match.toLowerCase()) < 0) {
                return false;
            }
        }
        return true;
    }
}
