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
		return (EnigmaManager) servletContext.getAttribute(ENIGMA_MANAGER_ATTRIBUTE_NAME);    }
}
