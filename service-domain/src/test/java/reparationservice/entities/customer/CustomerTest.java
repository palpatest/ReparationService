package reparationservice.entities.customer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import reparationservice.entities.customer.Customer;
import reparationservice.entities.customer.Device;
import reparationservice.persistenceimpls.inmemory.InMemoryConfigurator;

@RunWith(HierarchicalContextRunner.class)
public class CustomerTest {
	private static final long NULL_CUSTOMER_ID = 0;
	private static final long ANY_SERIAL_NUMBER = -1;

	@Test
	public void testSpecialCase() {
		Customer nullCustomer = Customer.NULL;
		assertThat(nullCustomer).isNotNull();
		assertThat(nullCustomer.getId()).isEqualTo(NULL_CUSTOMER_ID);
		assertThat(nullCustomer.getDevice(ANY_SERIAL_NUMBER)).isEqualTo(
				Device.NULL);
		nullCustomer.addDevice(ANY_SERIAL_NUMBER);
		assertThat(nullCustomer.getDevice(ANY_SERIAL_NUMBER)).isEqualTo(
				Device.NULL);
	}

	public class Impl {
		private static final long DEVICE_SERIAL_NUMBER_1 = 300;
		private static final int DEVICE_SERIAL_NUMBER_2 = 2;
		private static final long CUSTOMER_1_ID = 1;
		private static final long CUSTOMER_2_ID = 2;

		private Customer customer1;

		@Before
		public void givenCustomer() {
			customer1 = createCustomer(CUSTOMER_1_ID);
		}

		@Test
		public void testCreation() {
			Customer customer2 = createCustomer(CUSTOMER_2_ID);

			assertThat(customer1).isNotNull();
			assertThat(customer2).isNotNull();
			assertThat(customer1.getId()).isEqualTo(CUSTOMER_1_ID);
			assertThat(customer2.getId()).isEqualTo(CUSTOMER_2_ID);
		}

		@Test
		public void returnNullDeviceWhenSerialNumberNotFound() {
			Device device = customer1.getDevice(DEVICE_SERIAL_NUMBER_1);
			assertThat(device).isNotNull();
			assertThat(device).isEqualTo(Device.NULL);
		}

		@Test
		public void addDevice() {
			customer1.addDevice(DEVICE_SERIAL_NUMBER_1);
			customer1.addDevice(DEVICE_SERIAL_NUMBER_2);

			assertDevice(customer1.getDevice(DEVICE_SERIAL_NUMBER_1),
					DEVICE_SERIAL_NUMBER_1);
			assertDevice(customer1.getDevice(DEVICE_SERIAL_NUMBER_2),
					DEVICE_SERIAL_NUMBER_2);
		}

		private void assertDevice(Device device, long deviceSerialNumber) {
			assertThat(device).isNotNull();
			assertThat(device).isNotEqualTo(Device.NULL);
			assertThat(device.getSerialNumber()).isEqualTo(deviceSerialNumber);
		}

		private Customer createCustomer(long customerId) {
			return InMemoryConfigurator.getNewCustomer(customerId);
		}
	}
}
