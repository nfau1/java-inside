package fr.umlv.java.inside;

public class StringSwitchExample {

	public static int stringSwitch(String str) {
		return switch(str) {
			case "foo"-> 0;
			case "bar" -> 1;
			case "bazz" -> 2;
			default -> -1;
		};
	}

}
