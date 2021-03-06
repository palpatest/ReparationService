package reparationservice.persistenceimpls.inmemory;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import reparationservice.entities.customer.Device;
import reparationservice.entities.customer.Reparation;
import reparationservice.entities.customer.ReparationDTO;

public final class DeviceImpl extends Device {
	private final long serialNumber;
	private final Map<DateTime, Reparation> reparations = new HashMap<DateTime, Reparation>();

	public DeviceImpl(long serialNumber) {
		this.serialNumber = serialNumber;
	}

	@Override
	public long getSerialNumber() {
		return serialNumber;
	}

	@Override
	public Reparation getReparation(DateTime creationDate) {
		return reparations.get(creationDate);
	}

	@Override
	public void addReparation(ReparationDTO reparationDTO) {
		reparations.put(reparationDTO.getCreationDate(),
				InMemoryConfigurator.getNewReparation(reparationDTO));
	}
}
