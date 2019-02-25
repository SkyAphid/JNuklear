package nokori.jnuklear.layout;

import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_BACKGROUND;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_BORDER;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_CLOSABLE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_MINIMIZABLE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_MOVABLE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_INPUT;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_SCALABLE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_SCALE_LEFT;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_SCROLL_AUTO_HIDE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_TITLE;

public enum PanelOption {
	/**
	 * Draws a border around the window to visually separate window from the background
	 */
	WINDOW_BORDER(NK_WINDOW_BORDER),
	
	/**
	 * The movable flag indicates that a window can be moved by user input or by dragging the window header. 
	 * However, keep in mind that this may prevent you from being able to programmatically reposition a window every frame.
	 */
	WINDOW_MOVABLE(NK_WINDOW_MOVABLE),
	
	/**
	 * The scalable flag indicates that a window can be scaled by user input by dragging a scaler icon at the button of the window. 
	 * However, keep in mind that this may prevent you from being able to programmatically resize a window every frame.
	 */
	WINDOW_SCALABLE(NK_WINDOW_SCALABLE),
	
	/**
	 * Adds a closable icon into the header
	 */
	WINDOW_CLOSABLE(NK_WINDOW_CLOSABLE),
	
	/**
	 * Adds a minimize icon into the header
	 */
	WINDOW_MINIMIZABLE(NK_WINDOW_MINIMIZABLE),
	
	/**
	 * Removes the scrollbar from the window
	 */
	WINDOW_NO_SCROLLBAR(NK_WINDOW_NO_SCROLLBAR),
	
	/**
	 * Forces a header at the top at the window showing the title
	 */
	WINDOW_TITLE(NK_WINDOW_TITLE),
	
	/**
	 * Automatically hides the window scrollbar if no user interaction: also requires delta time in nk_context to be set each frame
	 */
	WINDOW_SCROLL_AUTO_HIDE(NK_WINDOW_SCROLL_AUTO_HIDE),
	
	/**
	 * Always keep window in the background
	 */
	WINDOW_BACKGROUND(NK_WINDOW_BACKGROUND),
	
	/**
	 * Puts window scaler in the left-bottom corner instead right-bottom
	 */
	WINDOW_SCALE_LEFT(NK_WINDOW_SCALE_LEFT),
	
	/**
	 * Prevents window of scaling, moving or getting focus
	 */
	WINDOW_NO_INPUT(NK_WINDOW_NO_INPUT);
	
	private int nkKey;
	
	private PanelOption(int nkKey) {
		this.nkKey = nkKey;
	}

	public int getNkKey() {
		return nkKey;
	}
	
	public static int compileOptions(PanelOption... options) {
		int bit = 0;
		
		for (int i = 0; i < options.length; i++) {
			bit |= options[i].getNkKey();
		}
		
		return bit;
	}
}