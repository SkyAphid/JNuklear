package nokori.jnuklear.layout;

import static org.lwjgl.nuklear.Nuklear.NK_DYNAMIC;
import static org.lwjgl.nuklear.Nuklear.NK_STATIC;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_begin;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_push;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_static;

import nokori.jnuklear.NuklearContext;

public interface Layout {
	
	/**
	 * Applies a Layout. Generally called right before a widget is rendered.
	 */
	public void apply();
	
	/**
	 * Corresponding function: <code>nk_layout_row_static()</code>
	 * <br><br>
	 * It provides each widget with same horizontal pixel width inside the row and does not grow if the owning window scales smaller or bigger.
	 * 
	 * @param height
	 * @param itemWidth
	 * @param columns
	 */
	public static void addStaticRow(NuklearContext context, float height, int itemWidth, int columns) {
		nk_layout_row_static(context.get(), height, itemWidth, columns);
	}
	
	/**
	 * Corresponding function: <code>nk_layout_row_dynamic()</code>
	 * <br><br>
	 * It provides each widgets with same horizontal space inside the row and dynamically grows if the owning window grows in width.
	 * 
	 * @param height
	 * @param columns
	 */
	public static void addDynamicRow(NuklearContext context, float height, int columns) {
		nk_layout_row_dynamic(context.get(), height, columns);
	}
	
	/**
	 * Corresponding function: <code>nk_layout_row_begin()</code>
	 * <br><br>
	 * Begins a new customizable static row.
	 * 
	 * @param rowHeight
	 * @param columns
	 */
	public static void beginStaticRow(NuklearContext context, float rowHeight, int columns) {
		nk_layout_row_begin(context.get(), NK_STATIC, rowHeight, columns);
	}
	
	/**
	 * Corresponding function: <code>nk_layout_row_begin()</code>
	 * <br><br>
	 * Begins a new customizable dynamic row.
	 * 
	 * @param rowHeight
	 * @param columns
	 */
	public static void beginDynamicRow(NuklearContext context, float rowHeight, int columns) {
		nk_layout_row_begin(context.get(), NK_DYNAMIC, rowHeight, columns);
	}
	
	/**
	 * Corresponding function: <code>nk_layout_row_push()</code>
	 * <br><br>
	 * Pushes the settings for a row.
	 * 
	 * @param width
	 */
	public static void pushRow(NuklearContext context, float width) {
		nk_layout_row_push(context.get(), width);
	}
}
