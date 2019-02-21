package nokori.jnuklear.callback.wrapper;

import static org.lwjgl.nuklear.Nuklear.*;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryStack.*;

import nokori.util.glfw.Window;
import nokori.util.glfw.callback.ScrollCallback;

public class NuklearScrollCallback implements ScrollCallback {
	
	private NkContext nkContext;
	
	public NuklearScrollCallback(NkContext nkContext) {
		this.nkContext = nkContext;
	}

	@Override
	public void scrollEvent(Window window, long timestamp, double mouseX, double mouseY, double xoffset, double yoffset) {
		try (MemoryStack stack = stackPush()) {
			NkVec2 scroll = NkVec2.mallocStack(stack).x((float) xoffset).y((float) yoffset);
			nk_input_scroll(nkContext, scroll);
		}
	}
}
