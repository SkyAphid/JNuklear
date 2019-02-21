package nokori.jnuklear.widgets;

import static org.lwjgl.nuklear.Nuklear.*;

import nokori.jnuklear.NuklearContext;
import nokori.jnuklear.callback.ClickHandler;
import nokori.jnuklear.layout.NkPane;

public class NkButton extends NkWidget {
	
	protected String title;
	protected ClickHandler buttonClickHandler = null;

	public NkButton(String title) {
		this.title = title;
	}
	
	@Override
	public void layout(NuklearContext context, NkPane parent) {
		super.layout(context, parent);
		
        if (nk_button_label(context.get(), title)) {
            buttonClickHandler.click();
        }
	}

	public void setOnButtonClick(ClickHandler clickHandler) {
		this.buttonClickHandler = clickHandler;
	}
}
