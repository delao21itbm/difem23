package com.gem.sistema.util;

public class UtilParseString {
	public static String[] parseString(String cadena,String separador) {
		 String[] arrayString= cadena.split(separador);
		 return arrayString;
	}

}
