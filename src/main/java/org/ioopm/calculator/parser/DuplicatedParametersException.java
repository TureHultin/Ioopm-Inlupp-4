package org.ioopm.calculator.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DuplicatedParametersException extends SyntaxErrorException {
    public record DuplicatedParameter(
            int originalIndex,
            int index,
            String name
    ) {
    }

    String name;
    Collection<DuplicatedParameter> duplicatedParameters;

    public static Collection<DuplicatedParameter> calculateDuplicated(ArrayList<String> parameters) {
        HashMap<String, Integer> seenParameters = new HashMap<>();
        ArrayList<DuplicatedParameter> duplicated = new ArrayList<>();

        for (int i = 0; i < parameters.size(); i++) {
            String paramName = parameters.get(i);

            Integer original_index = seenParameters.get(paramName);
            if (original_index != null) {
                DuplicatedParameter duplicate = new DuplicatedParameter(original_index, i, paramName);
                duplicated.add(duplicate);
            } else {

                seenParameters.put(paramName, i);
            }
        }

        return duplicated;
    }

    static String makeMassage(String name, Collection<DuplicatedParameter> duplicatedParameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("Function ");
        sb.append(name);
        sb.append(" has duplicated ");

        if (duplicatedParameters.size() == 1) {
            sb.append("parameter");
        } else {
            sb.append("parameters");
        }

        sb.append(' ');
        for (DuplicatedParameter duplicatedParameter : duplicatedParameters) {
            sb.append("'").append(duplicatedParameter.name()).append("'");
            sb.append(" original on index ");
            sb.append(duplicatedParameter.originalIndex());
            sb.append(" ");
        }

        return sb.toString();
    }

    public DuplicatedParametersException(String name, Collection<DuplicatedParameter> duplicatedParameters) {
        super(DuplicatedParametersException.makeMassage(name, duplicatedParameters));
        this.name = name;
        this.duplicatedParameters = duplicatedParameters;
    }
}
