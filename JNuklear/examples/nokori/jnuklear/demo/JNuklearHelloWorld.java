package nokori.jnuklear.demo;

import nokori.jnuklear.NuklearContext;
import nokori.jnuklear.NuklearProgram;
import nokori.jnuklear.layout.Layout;
import nokori.jnuklear.layout.NkRectPane;
import nokori.jnuklear.widgets.NkLabel;
import nokori.jnuklear.widgets.NkTextAlignment;
import nokori.util.TinyFileDialog;
import nokori.util.glfw.GLFWException;
import nokori.util.glfw.Monitor;
import nokori.util.glfw.Window;
import nokori.util.glfw.WindowManager;

import java.io.File;

import nokori.jnuklear.Font;

public class JNuklearHelloWorld extends NuklearProgram {
	
	private static final int WINDOW_WIDTH = 256;
	private static final int WINDOW_HEIGHT = WINDOW_WIDTH;
	
	private static final int PANE_WIDTH = WINDOW_WIDTH/2;
	private static final int PANE_HEIGHT = PANE_WIDTH;
	
	private NkRectPane rectPane;

	public static void main(String[] args) {
		NuklearProgram.start(new JNuklearHelloWorld(), args);
	}
	
	@Override
	public void init(NuklearContext context, String[] args) {
		rectPane = new NkRectPane(context, WINDOW_WIDTH/2 - PANE_WIDTH/2, WINDOW_HEIGHT/2 - PANE_HEIGHT/2, PANE_WIDTH, PANE_HEIGHT);
		
		context.addPane(rectPane);

		//Label
		NkLabel label = new NkLabel("Hello world!", NkTextAlignment.CENTERED);
		
		label.setLayout(() -> {
			Layout.addDynamicRow(context, 100, 1);
		});
		
		label.setOnLeftClick(() -> {
			TinyFileDialog.showMessageDialog("Ta-daa!", "You clicked a label!", TinyFileDialog.DialogIcon.INFO);
		});
		
		rectPane.addWidget(label);
	}
	@Override
	public Window createWindow(WindowManager windowManager) throws GLFWException {
		Monitor primary = windowManager.getPrimaryMonitor();
		
		int centerX = primary.getDesktopVideoMode().getWidth()/2 - WINDOW_WIDTH/2;
		int centerY = primary.getDesktopVideoMode().getHeight()/2 - WINDOW_HEIGHT/2;
		
		return windowManager.createWindow(centerX, centerY, WINDOW_WIDTH, WINDOW_HEIGHT, true, true);
	}
	
	@Override
	public Font getInitialFont() {
		return new Font(new File("font/NotoSans-Regular.ttf"), 22);
	}
	
	@Override
	public void run(NuklearContext context) {}
	
	@Override
	protected void dispose() {}
}
