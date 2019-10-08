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

		@JSONProperty("TON PRENOM")
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

		@JSONProperty
		public String getPlanet() {
			return planet;
		}

		@JSONProperty
		public int getAge() {
			return age;
		}
	}

	@Test
	public void toJSonPerson() {
		var person = new Person("John", "Doe");
		for(var i=0; i < 10000000; i++)
			assertEquals("{\n" + "	TON PRENOM:John,\n" + "	lastName:Doe\n" + "}", Main.toJSON(person));
	}
	

	@Test
	public void toJSonAlien() {
		var person = new Alien("John", 10);
		assertEquals("{\n" + "	age:10,\n" + "	planet:John\n" + "}", Main.toJSON(person));

	}
}
