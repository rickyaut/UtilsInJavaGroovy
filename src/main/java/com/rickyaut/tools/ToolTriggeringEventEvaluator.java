package com.rickyaut.tools;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;


public class ToolTriggeringEventEvaluator implements TriggeringEventEvaluator {
	int count = 1;

	@Override
	public boolean isTriggeringEvent(LoggingEvent event) {
		return count++%100==0;
	}

}
