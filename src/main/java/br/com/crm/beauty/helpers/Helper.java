package br.com.crm.beauty.helpers;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class Helper {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    private Helper() {
    }

    public static String slugify(String input) {
        if (input == null) {
            return "";
        }

        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = slug.replaceAll("-{2,}", "-");
        return slug.toLowerCase(Locale.ENGLISH).replaceAll("^-|-$", "");
    }
}
