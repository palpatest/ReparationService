package reparationservice.entities.devicetype;

public abstract class DeviceType {
	public static final DeviceType NULL = new DeviceType() {
		@Override
		public String getDescription() {
			return "";
		}
	};

	public abstract String getDescription();
}
