package de.bxservice.model;

import java.sql.Timestamp;

public interface ITransportationResource {

	boolean isAvailable(Timestamp date);
	String getUnavailabilityReason(Timestamp date);
	String getName();
	String getDescription();
}
