package nokori.jnuklear.callback.wrapper;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.*;

import org.lwjgl.nuklear.NkContext;

import nokori.util.glfw.Window;
import nokori.util.glfw.callback.KeyCallback;

public class NuklearKeyCallback implements KeyCallback {
	
	private NkContext nkContext;
	
	public NuklearKeyCallback(NkContext nkContext) {
		this.nkContext = nkContext;
	}

	@Override
	public void keyEvent(Window window, long timestamp, int key, int scanCode, boolean press, boolean repeat, int mods) {
		long handle = window.getHandle();
		
		switch (key) {
		case GLFW_KEY_DELETE:
			nk_input_key(nkContext, NK_KEY_DEL, press);
			break;
		case GLFW_KEY_ENTER:
			nk_input_key(nkContext, NK_KEY_ENTER, press);
			break;
		case GLFW_KEY_TAB:
			nk_input_key(nkContext, NK_KEY_TAB, press);
			break;
		case GLFW_KEY_BACKSPACE:
			nk_input_key(nkContext, NK_KEY_BACKSPACE, press);
			break;
		case GLFW_KEY_UP:
			nk_input_key(nkContext, NK_KEY_UP, press);
			break;
		case GLFW_KEY_DOWN:
			nk_input_key(nkContext, NK_KEY_DOWN, press);
			break;
		case GLFW_KEY_HOME:
			nk_input_key(nkContext, NK_KEY_TEXT_START, press);
			nk_input_key(nkContext, NK_KEY_SCROLL_START, press);
			break;
		case GLFW_KEY_END:
			nk_input_key(nkContext, NK_KEY_TEXT_END, press);
			nk_input_key(nkContext, NK_KEY_SCROLL_END, press);
			break;
		case GLFW_KEY_PAGE_DOWN:
			nk_input_key(nkContext, NK_KEY_SCROLL_DOWN, press);
			break;
		case GLFW_KEY_PAGE_UP:
			nk_input_key(nkContext, NK_KEY_SCROLL_UP, press);
			break;
		case GLFW_KEY_LEFT_SHIFT:
		case GLFW_KEY_RIGHT_SHIFT:
			nk_input_key(nkContext, NK_KEY_SHIFT, press);
			break;
		case GLFW_KEY_LEFT_CONTROL:
		case GLFW_KEY_RIGHT_CONTROL:
			if (press) {
				nk_input_key(nkContext, NK_KEY_COPY, glfwGetKey(handle, GLFW_KEY_C) == GLFW_PRESS);
				nk_input_key(nkContext, NK_KEY_PASTE, glfwGetKey(handle, GLFW_KEY_P) == GLFW_PRESS);
				nk_input_key(nkContext, NK_KEY_CUT, glfwGetKey(handle, GLFW_KEY_X) == GLFW_PRESS);
				nk_input_key(nkContext, NK_KEY_TEXT_UNDO, glfwGetKey(handle, GLFW_KEY_Z) == GLFW_PRESS);
				nk_input_key(nkContext, NK_KEY_TEXT_REDO, glfwGetKey(handle, GLFW_KEY_R) == GLFW_PRESS);
				nk_input_key(nkContext, NK_KEY_TEXT_WORD_LEFT, glfwGetKey(handle, GLFW_KEY_LEFT) == GLFW_PRESS);
				nk_input_key(nkContext, NK_KEY_TEXT_WORD_RIGHT, glfwGetKey(handle, GLFW_KEY_RIGHT) == GLFW_PRESS);
				nk_input_key(nkContext, NK_KEY_TEXT_LINE_START, glfwGetKey(handle, GLFW_KEY_B) == GLFW_PRESS);
				nk_input_key(nkContext, NK_KEY_TEXT_LINE_END, glfwGetKey(handle, GLFW_KEY_E) == GLFW_PRESS);
			} else {
				nk_input_key(nkContext, NK_KEY_LEFT, glfwGetKey(handle, GLFW_KEY_LEFT) == GLFW_PRESS);
				nk_input_key(nkContext, NK_KEY_RIGHT, glfwGetKey(handle, GLFW_KEY_RIGHT) == GLFW_PRESS);
				nk_input_key(nkContext, NK_KEY_COPY, false);
				nk_input_key(nkContext, NK_KEY_PASTE, false);
				nk_input_key(nkContext, NK_KEY_CUT, false);
				nk_input_key(nkContext, NK_KEY_SHIFT, false);
			}
			break;
		}
	}

}
