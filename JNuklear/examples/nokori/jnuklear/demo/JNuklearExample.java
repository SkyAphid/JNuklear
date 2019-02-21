package nokori.jnuklear.demo;

import nokori.jnuklear.NuklearContext;
import nokori.jnuklear.NuklearProgram;
import nokori.jnuklear.layout.Layout;
import nokori.jnuklear.layout.NkRectPane;
import nokori.jnuklear.widgets.NkButton;
import nokori.jnuklear.widgets.NkLabel;
import nokori.jnuklear.widgets.NkLabeledRadioButton;
import nokori.jnuklear.widgets.NkSlider;
import nokori.jnuklear.widgets.NkTextAlignment;
import nokori.util.glfw.GLFWException;
import nokori.util.glfw.Window;
import nokori.util.glfw.WindowManager;

import static nokori.jnuklear.layout.PanelOption.*;

import java.io.File;

import nokori.jnuklear.Font;

public class JNuklearExample extends NuklearProgram {
	
	private static final int WINDOW_WIDTH = 512;
	private static final int WINDOW_HEIGHT = 512;
	
	private static final int PANE_WIDTH = 400;
	private static final int PANE_HEIGHT = 400;
	
	private NkRectPane rectPane;

	public static void main(String[] args) {
		NuklearProgram.start(new JNuklearExample(), args);
	}
	
	@Override
	public void init(NuklearContext context, String[] args) {
		rectPane = new NkRectPane(context, "Test Pane", 
				WINDOW_WIDTH/2 - PANE_WIDTH/2, WINDOW_HEIGHT/2 - PANE_HEIGHT/2, PANE_WIDTH, PANE_HEIGHT,
				WINDOW_BORDER, WINDOW_MOVABLE, WINDOW_SCALABLE, WINDOW_MINIMIZABLE);
		
		context.addPane(rectPane);

		/*
		 * Button
		 */
		NkButton button = new NkButton("Button");
		
		button.setOnButtonClick(() -> {
			System.out.println("Button clicked");
		});
		
		button.setOnRightClick(() -> {
			System.out.println("Button right clicked");
		});
		
		button.setLayout(() -> {
			Layout.addDynamicRow(context, 50, 1);
		});
		
		rectPane.addWidget(button);
		
		/*
		 * Slider & Label
		 */
		
		//Label
		NkLabel label = new NkLabel("Label & Slider: ", NkTextAlignment.LEFT);
		label.setLayout(() -> {
			Layout.addDynamicRow(context, 50, 2);
		});
		label.setOnLeftClick(() -> {
			System.out.println("Label left click.");
		});
		
		//Slider - notice how I don't give it a layout. This is so that it ends up in the same row as the label.
		NkSlider slider = new NkSlider(0.0f, 1.0f, 0.1f);
		slider.setOnSlide(value -> {
			System.out.println("Slider value: " + value);
		});
		
		rectPane.addWidget(label, slider);
		
		/*
		 *  Labeled radio button
		 */
		
		NkLabeledRadioButton labeledRadio1 = new NkLabeledRadioButton("Radio 1", true);
		NkLabeledRadioButton labeledRadio2 = new NkLabeledRadioButton("Radio 2", false);
		
		labeledRadio1.setOnButtonClick(selected -> {
			labeledRadio2.setSelected(!labeledRadio1.isSelected());
		});
		
		labeledRadio2.setOnButtonClick(selected -> {
			labeledRadio1.setSelected(!labeledRadio2.isSelected());
		});
		
		rectPane.addWidget(labeledRadio1, labeledRadio2);
	}

	@Override
	public void run(NuklearContext context) {

	}

	@Override
	public Window createWindow(WindowManager windowManager) throws GLFWException {
		return windowManager.createWindow(50, 50, WINDOW_WIDTH, WINDOW_HEIGHT, true, true);
	}
	
	@Override
	public Font getInitialFont() {
		return new Font(new File("font/NotoSans-Regular.ttf"), 18);
	}
	
	@Override
	protected void dispose() {}
}
