package de.bxservice.model;

public interface ITransportationResource {

	boolean isAvailable();
	String getUnavailabilityReason();
	String getName();
	String getDescription();
}
