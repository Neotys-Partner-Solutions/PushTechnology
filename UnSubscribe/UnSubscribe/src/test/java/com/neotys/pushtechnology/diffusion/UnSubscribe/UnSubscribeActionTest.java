package com.neotys.pushtechnology.diffusion.UnSubscribe;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnSubscribeActionTest {
	@Test
	public void shouldReturnType() {
		final UnSubscribeAction action = new UnSubscribeAction();
		assertEquals("UnSubscribe", action.getType());
	}

}
