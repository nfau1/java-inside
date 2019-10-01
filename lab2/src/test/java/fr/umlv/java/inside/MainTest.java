package fr.umlv.java.inside;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class MainTest {

	public static class Person {
		private final String firstName;
		private final String lastName;

		public Person(String firstName, String lastName) {
			this.firstName = Objects.requireNonNull(firstName);
			this.lastName = Objects.requireNonNull(lastName);
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public static String toJSON(Person person) {
			return "{\n" + "  \"firstName\": \"" + person.getFirstName() + "\"\n" + "  \"lastName\": \""
					+ person.getLastName() + "\"\n" + "}\n";
		}
	}
	public class Alien {
		private final String planet;
		private final int age;

		public Alien(String planet, int age) {
			if (age <= 0) {
				throw new IllegalArgumentException("Too young...");
			}
			this.planet = Objects.requireNonNull(planet);
			this.age = age;
		}

		public String getPlanet() {
			return planet;
		}

		public int getAge() {
			return age;
		}
	}

	@Test
	public void toJSonPerson() {
		var person = new Person("John", "Doe");
		
		assertEquals(
				"{\n" + 
				"	class:class fr.umlv.java.inside.MainTest$Person,\n" +
				"	firstName:John,\n" +
				"	lastName:Doe\n" +
				"}"
				,
				Main.toJSON(person)				
		);
	}
}
