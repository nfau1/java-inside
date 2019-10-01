package fr.umlv.java.inside;

import java.util.Objects;

public class Person {
	private final String firstName;
	private final String lastName;

	public Person(String firstName, String lastName) {
		this.firstName = Objects.requireNonNull(firstName);
		this.lastName = Objects.requireNonNull(lastName);
	}

	@JSONProperty
	public String getFirstName() {
		return firstName;
	}

	@JSONProperty
	public String getLastName() {
		return lastName;
	}

	public static String toJSON(Person person) {
		return "{\n" + "  \"firstName\": \"" + person.getFirstName() + "\"\n" + "  \"lastName\": \""
				+ person.getLastName() + "\"\n" + "}\n";
	}
}
