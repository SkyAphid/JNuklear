package nokori.jnuklear.demo.glfw;

import nokori.jnuklear.Font;
import nokori.jnuklear.NuklearContext;
import nokori.jnuklear.NuklearProgram;
import nokori.util.glfw.GLFWException;
import nokori.util.glfw.Window;
import nokori.util.glfw.WindowManager;

/**
 * This is the GLFW demo using the JNuklear wrapper.
 */
public class JNuklearGLFWDemo extends NuklearProgram {

	public static void main(String[] args) {
		start(new JNuklearGLFWDemo(), args);
	}

	private Font font;

	private final Demo demo = new Demo();
	private final Calculator calc = new Calculator();

	@Override
	public void init(NuklearContext context, String[] args) {
		clearColor.set(demo.background.r(), demo.background.g(), demo.background.b(), demo.background.a());
	}

	@Override
	public void run(NuklearContext context) {
		demo.layout(context.get(), 50, 50);
		calc.layout(context.get(), 300, 50);
	}

	@Override
	public Window createWindow(WindowManager windowManager) throws GLFWException {
		return windowManager.createWindow(50, 50, 512, 512, true, true);
	}
	
	@Override
	public Font getInitialFont() {
		font = new Font("font/NotoSans-Regular.ttf", 18);
		return font;
	}
	
	@Override
	protected void dispose() {
		font.dispose();
		calc.numberFilter.free();
	}

}
