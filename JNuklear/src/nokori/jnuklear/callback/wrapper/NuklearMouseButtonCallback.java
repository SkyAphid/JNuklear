package nokori.jnuklear.callback.wrapper;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.*;

import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryStack.*;

import org.lwjgl.nuklear.NkContext;

import nokori.util.glfw.Window;
import nokori.util.glfw.callback.MouseCallback;

public class NuklearMouseButtonCallback implements MouseCallback {
	
	private NkContext nkContext;
	
	public NuklearMouseButtonCallback(NkContext nkContext) {
		this.nkContext = nkContext;
	}

	@Override
	public void mouseEvent(Window window, long timestamp, double mouseX, double mouseY, int button, boolean pressed, int mods) {
		try (MemoryStack stack = stackPush()) {
			
			int nkButton;
			switch (button) {
			case GLFW_MOUSE_BUTTON_RIGHT:
				nkButton = NK_BUTTON_RIGHT;
				break;
			case GLFW_MOUSE_BUTTON_MIDDLE:
				nkButton = NK_BUTTON_MIDDLE;
				break;
			default:
				nkButton = NK_BUTTON_LEFT;
			}
			
			nk_input_button(nkContext, nkButton, (int) mouseX, (int) mouseY, pressed);
		}
	}
}
