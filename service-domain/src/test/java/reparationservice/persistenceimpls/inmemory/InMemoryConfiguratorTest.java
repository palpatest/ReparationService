package reparationservice.persistenceimpls.inmemory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import reparationservice.persistenceimpls.inmemory.InMemoryConfigurator;

public class InMemoryConfiguratorTest {
	@Test(expected = AssertionError.class)
	public void cannotInstantiateClassEvenWithReflection() throws Exception {
		Constructor<InMemoryConfigurator> privateConstructor = InMemoryConfigurator.class
				.getDeclaredConstructor();
		privateConstructor.setAccessible(true);
		try {
			privateConstructor.newInstance();
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof AssertionError)
				throw (AssertionError) e.getCause();
			else
				throw e;
		}
	}
}
