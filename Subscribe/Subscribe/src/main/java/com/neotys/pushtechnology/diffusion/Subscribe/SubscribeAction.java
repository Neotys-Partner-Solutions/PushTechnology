package com.neotys.pushtechnology.diffusion.Subscribe;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

import com.google.common.base.Optional;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

public final class SubscribeAction implements Action{
	private static final String BUNDLE_NAME = "com.neotys.pushtechnology.diffusion.Subscribe.bundle";
	private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayName");
	private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");
	private static final ImageIcon LOGO_ICON;
	static final String Diffusion_URL="Diffusion_URL";
	static final String TopicSelector="TopicSelector";
	static final String Format="Format";

	static {
		final URL iconURL = SubscribeAction.class.getResource("pushtechnology.png");
		if (iconURL != null) {
			LOGO_ICON = new ImageIcon(iconURL);
		}
		else {
			LOGO_ICON = null;
		}
	}
	@Override
	public String getType() {
		return "Subscribe";
	}

	@Override
	public List<ActionParameter> getDefaultActionParameters() {
		final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
		// TODO Add default parameters.
		// TODO Add default parameters.
		parameters.add(new ActionParameter(Diffusion_URL,"ws://host:8080"));
		parameters.add(new ActionParameter(TopicSelector,"TopicSelector"));
		parameters.add(new ActionParameter(Format,"JSON"));

		return parameters;
	}

	@Override
	public Class<? extends ActionEngine> getEngineClass() {
		return SubscribeActionEngine.class;
	}

	@Override
	public Icon getIcon() {
		// TODO Add an icon
		return LOGO_ICON;
	}

	@Override
	public boolean getDefaultIsHit(){
		return false;
	}

	@Override
	public String getDescription() {
		final StringBuilder description = new StringBuilder();
		// TODO Add description
		description.append("Subscribe action subscribe to a specific topic .\n")
		.append("the Parameters of the custom action are  :\n")
		.append("- Diffusion_URL : Url of the connect to the diffusion server \n")
		.append("- TopicSelector: TopicSelector to subscribe\n")
		.append("- Format : Format of the message of the topic :STRING,BINARY,JSON,DOUBLE, INT64\n");

		return description.toString();
	}

	@Override
	public String getDisplayName() {
		return DISPLAY_NAME;
	}

	@Override
	public String getDisplayPath() {
		return DISPLAY_PATH;
	}

	@Override
	public Optional<String> getMinimumNeoLoadVersion() {
		return Optional.of("6.1");
	}

	@Override
	public Optional<String> getMaximumNeoLoadVersion() {
		return Optional.absent();
	}
}
