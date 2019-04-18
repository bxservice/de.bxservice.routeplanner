package de.bxservice.process;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;

public class RoutePlanner_ProcessFactory implements IProcessFactory {

	@Override
	public ProcessCall newProcessInstance(String className) {
		if (RoutePlanner_CopyDelivery.class.getName().equals(className))
			return new RoutePlanner_CopyDelivery();
		if (KanbanBayenTourMap.class.getName().equals(className))
			return new KanbanBayenTourMap();
		return null;
	}

}