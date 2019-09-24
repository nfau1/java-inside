package fr.umlv.javainside.lab1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import fr.umlv.javainside.lab1.SwitchExample;

public class SwitchExampleTest {

	@Test
	public void dog() {
		assertEquals(1, SwitchExample.switchExample("dog"));
	}
	
	@Test
	public void cat() {
		assertEquals(2, SwitchExample.switchExample("cat"));
	}
	
	@Test
	public void other() {
		assertEquals(4, SwitchExample.switchExample("chien"));
	}

	@Test
	public void dog2() {
		assertEquals(1, SwitchExample.switchExample2("dog"));
	}
	
	@Test
	public void cat2() {
		assertEquals(2, SwitchExample.switchExample2("cat"));
	}
	
	@Test
	public void other2() {
		assertEquals(4, SwitchExample.switchExample2("chien"));
	}
	
}