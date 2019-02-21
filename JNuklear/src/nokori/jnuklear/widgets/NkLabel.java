package nokori.jnuklear.widgets;

import static org.lwjgl.nuklear.Nuklear.*;

import nokori.jnuklear.NuklearContext;
import nokori.jnuklear.layout.NkPane;

public class NkLabel extends NkWidget {
	
	private String title;
	
	private NkTextAlignment alignment;
	
	public NkLabel(String title, NkTextAlignment alignment) {
		this.title = title;
		this.alignment = alignment;
	}
	
	@Override
	public void layout(NuklearContext context, NkPane parent) {
		super.layout(context, parent);
		
        nk_label(context.get(), title, alignment.key);
	}

}
