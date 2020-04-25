package fr.florianlallier.grafx.util;

import java.io.File;

public class Util {

	public static String parseLetter(String letter) {
		if (letter.length() > 1) {
			letter = letter.substring(0, 1);
		}

		return letter;
	}

	public static String parseNumber(String number) {
		if (number.length() > 3) {
			number = number.substring(0, 3);
		}

		return number;
	}
	
	public static boolean isLetter(String string) {
		if (string.equals("")) {
			return false;
		}
		return Character.isLetter(string.charAt(0));
	}
	
	public static boolean isNumber(String string) {
		if (string.equals("")) {
			return false;
		}
		for (int i = 0; i < string.length(); i++) {
			if (!Character.isDigit(string.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isXMLFile(File file) {
		String[] split = file.getName().split("\\.");
		if (split.length != 2) {
			return false;
		}
		return split[1].equals("xml");
	}
}