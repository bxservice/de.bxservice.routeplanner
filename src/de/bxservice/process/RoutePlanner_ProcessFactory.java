package de.bxservice.process;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;

public class RoutePlanner_ProcessFactory implements IProcessFactory {

	@Override
	public ProcessCall newProcessInstance(String className) {
		ProcessCall process = null;
		if ("de.bxservice.process.RoutePlanner_CopyDelivery".equals(className)) {
			try {
				process = RoutePlanner_CopyDelivery.class.newInstance();
			} catch (Exception e) {}
		}
		return process;
	}

}
