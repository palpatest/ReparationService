package reparationservice.entities.customer;

import org.joda.time.DateTime;

public abstract class Device {
	public static final Device NULL = new Device() {
		@Override
		public String toString() {
			return "Null Device";
		}

		@Override
		public long getSerialNumber() {
			return -1;
		}

		@Override
		public Reparation getReparation(DateTime creationDate) {
			return Reparation.NULL;
		}

		@Override
		public void addReparation(ReparationDTO reparationDTO) {
		}
	};

	public abstract long getSerialNumber();

	public abstract Reparation getReparation(DateTime creationDate);

	public abstract void addReparation(ReparationDTO reparationDTO);
}
