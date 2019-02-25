package nokori.jnuklear.layout;

import java.util.ArrayList;
import java.util.Arrays;

import nokori.jnuklear.NuklearContext;
import nokori.jnuklear.widgets.NkWidget;

import static org.lwjgl.nuklear.Nuklear.*;

public abstract class NkPane {
	protected NuklearContext context;
	
	protected String title;
	protected int x, y, width, height;
	protected int panelOptions;
	
	protected ArrayList<NkWidget> widgets = new ArrayList<NkWidget>();
	
	public NkPane(NuklearContext context, String title, int x, int y, int width, int height, PanelOption...panelOptions) {
		this.context = context;
		this.title = title;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.panelOptions = PanelOption.compileOptions(panelOptions);
		
		//Force window title option if a title string is passed in
		if (!title.isEmpty() && Arrays.asList(panelOptions).contains(PanelOption.WINDOW_TITLE)) {
			this.panelOptions |= NK_WINDOW_TITLE;
		}
	}

	/**
	 * This function is used to update and render Nuklear components.
	 */
	public abstract void layout();

	/**
	 * This is called in-between NuklearContext's nk_input_begin and nk_input_end calls, and after glfwPollEvents(). 
	 */
	public abstract void listen();
	
	public ArrayList<NkWidget> getWidgets() {
		return widgets;
	}
	
	public void addWidget(NkWidget... widgets) {
		for (int i = 0; i < widgets.length; i++) {
			addWidget(widgets[i]);
		}
	}
	
	public void addWidget(NkWidget widget) {
		widgets.add(widget);
	}

	public void removeWidget(NkWidget... widgets) {
		for (int i = 0; i < widgets.length; i++) {
			removeWidget(widgets[i]);
		}
	}
	
	public void removeWidget(NkWidget widget) {
		widgets.remove(widget);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setPanelOptions(PanelOption...options) {
		this.panelOptions = PanelOption.compileOptions(options);
	}
}
