package nokori.jnuklear.widgets;

import static org.lwjgl.nuklear.Nuklear.*;

import nokori.jnuklear.NuklearContext;
import nokori.jnuklear.callback.BooleanClickHandler;
import nokori.jnuklear.layout.NkPane;

public class NkLabeledRadioButton extends NkWidget {
	
	private String title;
	private boolean selected;
	
	protected BooleanClickHandler buttonClickHandler = null;

	public NkLabeledRadioButton(String title, boolean selected) {
		this.title = title;
		this.selected = selected;
	}
	
	@Override
	public void layout(NuklearContext context, NkPane parent) {
		super.layout(context, parent);
		
		boolean bSelected = selected;
		selected = nk_option_text(context.get(), title, selected);
		
        if (bSelected != selected) {
            buttonClickHandler.click(selected);
        }
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setOnButtonClick(BooleanClickHandler clickHandler) {
		this.buttonClickHandler = clickHandler;
	}
}
