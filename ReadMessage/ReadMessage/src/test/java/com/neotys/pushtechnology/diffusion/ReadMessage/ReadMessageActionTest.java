package com.neotys.pushtechnology.diffusion.ReadMessage;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReadMessageActionTest {
	@Test
	public void shouldReturnType() {
		final ReadMessageAction action = new ReadMessageAction();
		assertEquals("ReadMessage", action.getType());
	}

}
