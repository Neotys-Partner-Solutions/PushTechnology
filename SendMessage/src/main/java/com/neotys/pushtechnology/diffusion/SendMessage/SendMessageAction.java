package com.neotys.pushtechnology.diffusion.SendMessage;

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

public final class SendMessageAction implements Action{
	private static final String BUNDLE_NAME = "com.neotys.pushtechnology.diffusion.SendMessage.bundle";
	private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayName");
	private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");
	private static final ImageIcon LOGO_ICON;
	static final String Diffusion_URL="Diffusion_URL";
	static final String TopicSelector ="TopicSelector";
	static final String Format="Format";
	static final String Message="Message";
	static {
		final URL iconURL =SendMessageAction.class.getResource("pushtechnology.png");
		if (iconURL != null) {
			LOGO_ICON = new ImageIcon(iconURL);
		}
		else {
			LOGO_ICON = null;
		}
	}
	@Override
	public String getType() {
		return "SendMessage";
	}

	@Override
	public List<ActionParameter> getDefaultActionParameters() {
		final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
		parameters.add(new ActionParameter(Diffusion_URL,"ws://host:8080"));
		parameters.add(new ActionParameter(TopicSelector,"TopicSelector"));
		parameters.add(new ActionParameter(Format,"JSON"));
		parameters.add(new ActionParameter(Message,"message"));
		// TODO Add default parameters.
		return parameters;
	}

	@Override
	public Class<? extends ActionEngine> getEngineClass() {
		return SendMessageActionEngine.class;
	}

	@Override
	public Icon getIcon() {

		return LOGO_ICON;
	}

	@Override
	public boolean getDefaultIsHit(){
		return true;
	}

	@Override
	public String getDescription() {
		final StringBuilder description = new StringBuilder();
		// TODO Add description
		description.append("SendMessage action send a message to a specific topic .\n")
				.append("the Parameters of the custom action are  :\n")
				.append("- Diffusion_URL : Url of the connect to the diffusion server \n")
				.append("- TopicSelector : TopicSelector to publish a message\n")
				.append("- Format : Format of the message of the topic :STRING,BINARY,JSON,DOUBLE, INT64\n")
				.append("- Message : Content of the message");



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
