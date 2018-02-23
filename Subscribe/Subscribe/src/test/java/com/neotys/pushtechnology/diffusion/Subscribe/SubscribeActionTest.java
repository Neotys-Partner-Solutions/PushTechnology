package com.neotys.pushtechnology.diffusion.Subscribe;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SubscribeActionTest {
	@Test
	public void shouldReturnType() {
		final SubscribeAction action = new SubscribeAction();
		assertEquals("Subscribe", action.getType());
	}

}
