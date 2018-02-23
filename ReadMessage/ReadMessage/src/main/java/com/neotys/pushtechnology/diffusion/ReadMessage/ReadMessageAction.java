package com.neotys.pushtechnology.diffusion.ReadMessage;

import java.net.URL;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

import com.google.common.base.Optional;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

public final class ReadMessageAction implements Action{
	private static final String BUNDLE_NAME = "com.neotys.pushtechnology.diffusion.ReadMessage.bundle";
	private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayName");
	private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");
	private static final ImageIcon LOGO_ICON;
	static final String TopicSelector ="TopicSelector";
	static final String Format="Format";
	static final String TimeOut="TimeOut";
	static {
		final URL iconURL = ReadMessageAction.class.getResource("pushtechnology.png");
		if (iconURL != null) {
			LOGO_ICON = new ImageIcon(iconURL);
		}
		else {
			LOGO_ICON = null;
		}
	}
	@Override
	public String getType() {
		return "ReadMessage";
	}

	@Override
	public List<ActionParameter> getDefaultActionParameters() {
		final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
		// TODO Add default parameters.
		parameters.add(new ActionParameter(TopicSelector,"Topic"));
		parameters.add(new ActionParameter(TimeOut,"5"));
		parameters.add(new ActionParameter(Format,"JSON"));
		return parameters;
	}

	@Override
	public Class<? extends ActionEngine> getEngineClass() {
		return ReadMessageActionEngine.class;
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

		description.append("ReadMessage from a specific topic .\n")
				.append("the Parameters of the custom action are  :\n")
				.append("- TopicSelector : TopicSelector to subscribe\n")
				.append("- Format : Format of the message of the topic :STRING,BINARY,JSON,DOUBLE, INT64\n")
				.append("- TimeOut : Timeout in s . the action will wait in maximum xs for new messages");
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
