package nokori.jnuklear.callback.wrapper;

import static org.lwjgl.nuklear.Nuklear.*;

import org.lwjgl.nuklear.NkContext;

import nokori.util.glfw.Window;
import nokori.util.glfw.callback.MouseMotionCallback;

public class NuklearCursorPosCallback implements MouseMotionCallback {
	
	private NkContext nkContext;
	
	public NuklearCursorPosCallback(NkContext nkContext) {
		this.nkContext = nkContext;
	}

	@Override
	public void mouseMotionEvent(Window window, long timestamp, double mouseX, double mouseY, double dx, double dy) {
		nk_input_motion(nkContext, (int) mouseX, (int) mouseY);
	}



}
