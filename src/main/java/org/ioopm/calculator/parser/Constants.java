package org.ioopm.calculator.parser;

import java.util.HashMap;

public class Constants {
    public static final HashMap<String, Double> namedConstants = new HashMap<>();

    static {
        Constants.namedConstants.put("pi", Math.PI);
        Constants.namedConstants.put("e", Math.E);
        Constants.namedConstants.put("Answer", 42.0);
        Constants.namedConstants.put("L", 6.02214076e23);
        Constants.namedConstants.put("phi", 1.61803398875);
    }


}

