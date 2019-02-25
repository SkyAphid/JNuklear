package nokori.jnuklear.callback.wrapper;

import static org.lwjgl.nuklear.Nuklear.*;

import org.lwjgl.nuklear.NkContext;

import nokori.util.glfw.Window;
import nokori.util.glfw.callback.CharCallback;

public class NuklearCharCallback implements CharCallback {
	
	private NkContext nkContext;
	
	public NuklearCharCallback(NkContext nkContext) {
		this.nkContext = nkContext;
	}

	@Override
	public void charEvent(Window window, long timestamp, int codepoint, String c, int mods) {
		nk_input_unicode(nkContext, codepoint);
	}
}
