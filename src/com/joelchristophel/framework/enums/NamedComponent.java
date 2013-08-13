package com.joelchristophel.framework.enums;

import org.powerbot.script.wrappers.Component;

import com.joelchristophel.framework.MethodContext;
import com.joelchristophel.framework.MethodProvider;

public enum NamedComponent {

	ACCEPT_QUEST_BUTTON(1500, 402),
	PLAYER_CHAT_CONTINUE_BUTTON(1191, 12),
	NPC_CHAT_CONTINUE_BUTTON(1184, 13),
	CHAT_RESPONSE_1(1188, 11),
	CHAT_RESPONSE_2(1188, 19),
	CHAT_RESPONSE_3(1188, 24), 
	CHAT_RESPONSE_4(1188, 29),
	HOME_TELEPORT_BUTTON(1465, 10),
	PLAYER_CHAT_TEXT(1191, 11),
	NPC_CHAT_TEXT(1184, 11),
	CLOSE_LODESTONE_NETWORK_BUTTON(1092, 69),
	LODESTONE_ACTIVE_MESSAGE(1092, 73),
	LODESTONE_NETWORK(1092, 0),
	MESSAGE_CONTINUE_BUTTON1(1189, 11),
	MESSAGE_CONTINUE_BUTTON2(1186, 2),
	HOVER_INTERACTION_LABEL(1477, 412, 0),
	QUEST_COMPLETE_CONTINUE_BUTTON(1244, 23);
	
	private int[] componentIndexes;
	MethodContext ctx = MethodProvider.ctx;

	NamedComponent(int... widgetIndexes) {
		this.componentIndexes = widgetIndexes;
	}

	public Component get(boolean wait) {
		if(wait) {
			if(componentIndexes.length == 2) {
				return ctx.widgets.get(componentIndexes[0], componentIndexes[1], true);
			} else if(componentIndexes.length == 3) {
				return ctx.widgets.get(componentIndexes[0], componentIndexes[1], componentIndexes[2], true);
			}
		} else {
			if(componentIndexes.length == 2) {
				return ctx.widgets.get(componentIndexes[0], componentIndexes[1], false);
			} else if(componentIndexes.length == 3) {
				return ctx.widgets.get(componentIndexes[0], componentIndexes[1], componentIndexes[2], false);
			}
		}
		
		return null;
	}
	
	public int[] getComponentIndexes() {
		return componentIndexes;
	}
}