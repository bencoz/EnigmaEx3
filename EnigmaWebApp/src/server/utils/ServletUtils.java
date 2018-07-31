package server.utils;

import Enigma.EnigmaManager;
import GameManager.GameManager;
import Users.UserManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static server.constants.Constants.INT_PARAMETER_ERROR;

/*import Engine.chat.ChatManager;
import Engine.users.UserManager;*/

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
	private static final String GAME_MANAGER_ATTRIBUTE_NAME = "gameManager";
	private static final String ENIGMA_MANAGER_ATTRIBUTE_NAME = "enigmaManager";

	public static UserManager getUserManager(ServletContext servletContext) {
		if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
			servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	/*public static ChatManager getChatManager(ServletContext servletContext) {
		if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
			servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
		}
		return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
	}*/

	public static GameManager getGameManager(ServletContext servletContext) {
		if (servletContext.getAttribute(GAME_MANAGER_ATTRIBUTE_NAME) == null) {
			servletContext.setAttribute(GAME_MANAGER_ATTRIBUTE_NAME, new GameManager());
		}
		return (GameManager) servletContext.getAttribute(GAME_MANAGER_ATTRIBUTE_NAME);
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}

    public static EnigmaManager getEnigmaManager(ServletContext servletContext) {
		if (servletContext.getAttribute(ENIGMA_MANAGER_ATTRIBUTE_NAME) == null) {
			servletContext.setAttribute(ENIGMA_MANAGER_ATTRIBUTE_NAME, new EnigmaManager());
		}
		return (EnigmaManager) servletContext.getAttribute(ENIGMA_MANAGER_ATTRIBUTE_NAME);
	}

	public static String numToString(int num) {
		String id;
		switch (num) {
			case 1:
				id = "I";
				break;
			case 2:
				id = "II";
				break;
			case 3:
				id = "III";
				break;
			case 4:
				id = "IV";
				break;
			case 5:
				id = "V";
				break;
			default:
				id = "";
		}
		return id;
	}

	public static Integer StringToNum(String reflectorID) {
		Integer numID;
		switch (reflectorID) {
			case "I":
				numID = 1;
				break;
			case "II":
				numID = 2;
				break;
			case "III":
				numID = 3;
				break;
			case "IV":
				numID = 4;
				break;
			case "V":
				numID = 5;
				break;
			default:
				numID = 0;
		}
		return numID;
	}
}
