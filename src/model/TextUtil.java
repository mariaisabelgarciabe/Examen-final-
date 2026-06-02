package model;


public class TextUtil {

    
    public static String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        
        texto = texto.trim();
        
        texto = texto.replaceAll("\\s+", " ");
        
        if (texto.isEmpty()) {
            return texto;
        }
        StringBuilder result = new StringBuilder();
        boolean nextTitle = true;
        for (char c : texto.toCharArray()) {
            if (Character.isWhitespace(c)) {
                result.append(c);
                nextTitle = true;
            } else {
                if (nextTitle) {
                    result.append(Character.toUpperCase(c));
                    nextTitle = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }
}