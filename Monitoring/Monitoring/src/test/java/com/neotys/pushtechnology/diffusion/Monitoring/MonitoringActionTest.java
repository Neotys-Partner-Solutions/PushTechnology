package com.neotys.pushtechnology.diffusion.Monitoring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MonitoringActionTest {
	@Test
	public void shouldReturnType() {
		final MonitoringAction action = new MonitoringAction();
		assertEquals("Monitoring", action.getType());
	}

}
