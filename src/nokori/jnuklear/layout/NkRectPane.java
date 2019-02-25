package nokori.jnuklear.layout;

import org.lwjgl.nuklear.*;
import org.lwjgl.system.*;

import nokori.jnuklear.NuklearContext;
import nokori.jnuklear.widgets.NkWidget;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.system.MemoryStack.*;

import java.rmi.server.UID;

public class NkRectPane extends NkPane {
	
	private String uid = new UID().toString();
	
	private boolean hovered = false;
	private boolean focused = false;
	
	public NkRectPane(NuklearContext context, int x, int y, int width, int height, PanelOption...panelOptions) {
		super(context, "", x, y, width, height, panelOptions);
	}
	
	public NkRectPane(NuklearContext context, String title, int x, int y, int width, int height, PanelOption...panelOptions) {
		super(context, title, x, y, width, height, panelOptions);
	}
	
	@Override
	public void layout() {
		NkContext ctx = context.get();
		
        try (MemoryStack stack = stackPush()) {
            NkRect rect = NkRect.mallocStack(stack);

            if (nk_begin_titled(ctx, uid, title, nk_rect(x, y, width, height, rect), panelOptions)) {
                for (int i = 0; i < widgets.size(); i++) {
                	NkWidget c = widgets.get(i);
                	c.layout(context, this);
                }
                
               hovered = nk_window_is_hovered(context.get());
               focused = nk_window_has_focus(context.get());
            }
            
            nk_end(ctx);
        }
	}

	@Override
	public void listen() {
		
	}

	public void setMinimized(boolean minimized) {
		nk_window_collapse(context.get(), uid, minimized ? NK_MINIMIZED : NK_MAXIMIZED);
	}
	
	public boolean isMinimized() {
		return nk_window_is_collapsed(context.get(), uid);
	}
	
	public boolean isFocused() {
		return focused;
	}
	
	public boolean isHovered() {
		return hovered;
	}
}
