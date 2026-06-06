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
        
        texto = texto.toLowerCase();
        
        StringBuilder result = new StringBuilder();
        for (char c : texto.toCharArray()) {
            switch (c) {
                case 'á': case 'à': case 'ä': case 'â': case 'ã': case 'å':
                    result.append('a'); break;
                case 'é': case 'è': case 'ë': case 'ê':
                    result.append('e'); break;
                case 'í': case 'ì': case 'ï': case 'î':
                    result.append('i'); break;
                case 'ó': case 'ò': case 'ö': case 'ô': case 'õ':
                    result.append('o'); break;
                case 'ú': case 'ù': case 'ü': case 'û':
                    result.append('u'); break;
                case 'ñ':
                    result.append('n'); break;
                case 'ç':
                    result.append('c'); break;
                default:
                    result.append(c);
            }
        }
        
        return result.toString();
    }
}