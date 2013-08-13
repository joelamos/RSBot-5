package com.joelchristophel.framework.methods;

import org.powerbot.script.wrappers.Component;

import com.joelchristophel.framework.MethodContext;
import com.joelchristophel.framework.MethodProvider;
import com.joelchristophel.framework.enums.NamedComponent;

public class Chat extends MethodProvider {

	public Chat(MethodContext ctx) {
		super(ctx);
	}

	private final Component[] chatTextComponents = { NamedComponent.NPC_CHAT_TEXT.get(false),
			NamedComponent.PLAYER_CHAT_TEXT.get(false), ctx.widgets.get(1186, 1), ctx.widgets.get(1189, 3) };

	/**
	 * Converses with an NPC as specified by instructions
	 * 
	 * @param instructions
	 *            - conversation instructions to be followed in order. The even indexes specify how many times to press
	 *            the space bar in a row. The odd indexes specify which numbered chat response to activate
	 */
	public void converse(int... instructions) {
		for (int index = 0; index < instructions.length; index++) {
			if (index % 2 == 0) { // even index
				pressContinue(instructions[index]);
			} else { // odd index
				respond(instructions[index]);
			}
		}
	}

	/**
	 * Presses the space bar once, activating the continue button
	 */
	public void pressContinue() {
		pressContinue(1);
	}

	/**
	 * Presses the space bar as many times as numberOfTimes specifies, activating the continue button each time
	 * 
	 * @param numberOfTimes
	 *            - the number of times to press the space bar
	 */
	public void pressContinue(int numberOfTimes) {
		String previousMessage = "";

		for (int i = 0; i < numberOfTimes; i++) {

			while (ctx.widgets.waitUntilOneOfTheComponentsIsVisible(chatTextComponents, "chat text").getText()
					.equals(previousMessage)) {
				sleep(200);
			}

			previousMessage = ctx.widgets.waitUntilOneOfTheComponentsIsVisible(chatTextComponents, "chat text")
					.getText();

			ctx.keyboard.send(" ");
		}
	}

	/**
	 * Presses a number key as specified by responseNumber, which activates a response button
	 * 
	 * @param responseNumber
	 *            - the number of the chat response to activate
	 */
	public void respond(int responseNumber) {

		switch (responseNumber) {
		case 1:
			NamedComponent.CHAT_RESPONSE_1.get(true);
			break;
		case 2:
			NamedComponent.CHAT_RESPONSE_2.get(true);
			break;
		case 3:
			NamedComponent.CHAT_RESPONSE_3.get(true);
			break;
		case 4:
			NamedComponent.CHAT_RESPONSE_4.get(true);
			break;

		default:
			throw new IllegalArgumentException();
		}

		ctx.keyboard.send(responseNumber + "");
	}
}