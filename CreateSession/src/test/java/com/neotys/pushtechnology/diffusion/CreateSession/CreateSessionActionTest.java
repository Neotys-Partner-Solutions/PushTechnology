package com.neotys.pushtechnology.diffusion.CreateSession;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CreateSessionActionTest {
	@Test
	public void shouldReturnType() {
		final CreateSessionAction action = new CreateSessionAction();
		assertEquals("CreateSession", action.getType());
	}

}
