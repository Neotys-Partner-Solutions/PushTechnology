package com.neotys.pushtechnology.diffusion.CloseSession;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CloseSessionActionTest {
	@Test
	public void shouldReturnType() {
		final CloseSessionAction action = new CloseSessionAction();
		assertEquals("CloseSession", action.getType());
	}

}
