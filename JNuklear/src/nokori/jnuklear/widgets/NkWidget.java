package nokori.jnuklear.widgets;

import static org.lwjgl.nuklear.Nuklear.*;

import org.lwjgl.nuklear.Nuklear;

import nokori.jnuklear.NuklearContext;
import nokori.jnuklear.callback.ClickHandler;
import nokori.jnuklear.layout.Layout;
import nokori.jnuklear.layout.NkPane;

public abstract class NkWidget {
	protected Layout layout = null;
	
	private ClickHandler leftClickHandler, rightClickHandler, middleClickHandler = null;
	
	/**
	 * Nuklear Widget base functionality. While by default this is called in layout(), it has been separated from the actual layout() function so that base functionality 
	 * can be accessed from children widgets.
	 * 
	 * @param context
	 * @param parent
	 */
	protected void widgetBaseFunctionality(NuklearContext context, NkPane parent) {
		//Apply layout
		if (layout != null) {
			layout.apply();
		}
		
		//Hovering
		hovered = nk_widget_is_hovered(context.get());
		
		//General clicks
		if (Nuklear.nk_widget_is_mouse_clicked(context.get(), NK_BUTTON_LEFT) && leftClickHandler != null) {
			leftClickHandler.click();
		}
	
		if (Nuklear.nk_widget_is_mouse_clicked(context.get(), NK_BUTTON_MIDDLE) && middleClickHandler != null) {
			middleClickHandler.click();
		}
		
		if (Nuklear.nk_widget_is_mouse_clicked(context.get(), NK_BUTTON_RIGHT) && rightClickHandler != null) {
			rightClickHandler.click();
		}
	}
	
	/**
	 * This function is in charge of positioning and rendering Nuklear widgets.
	 * 
	 * @param context - the NuklearContext - necessary for rendering anything in Nuklear
	 * @param parent - the parent NkPane in case access to its data is required
	 */
	public void layout(NuklearContext context, NkPane parent) {
		widgetBaseFunctionality(context, parent);
	}
	
	/**
	 * Sets the layout for this widget and returns it. It has lambda functionality for ease of use.
	 * 
	 * @param layout
	 * @return
	 */
	public Layout setLayout(Layout layout) {
		this.layout = layout;
		return layout;
	}
	
	protected boolean hovered;
	
	public boolean isHovered() {
		return hovered;
	}
	
	public void setOnLeftClick(ClickHandler leftClickHandler) {
		this.leftClickHandler = leftClickHandler;
	}
	
	public void setOnRightClick(ClickHandler rightClickHandler) {
		this.rightClickHandler = rightClickHandler;
	}
	
	public void setOnMiddleClick(ClickHandler middleClickHandler) {
		this.middleClickHandler = middleClickHandler;
	}
}
