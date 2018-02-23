package com.neotys.pushtechnology.diffusion.SendMessage;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SendMessageActionTest {
	@Test
	public void shouldReturnType() {
		final SendMessageAction action = new SendMessageAction();
		assertEquals("SendMessage", action.getType());
	}

}
