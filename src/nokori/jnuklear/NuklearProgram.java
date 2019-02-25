package nokori.jnuklear;

import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.opengl.GL11C.glViewport;
import org.joml.Vector4f;

import nokori.util.JVMUtil;
import nokori.util.glfw.GLFWException;
import nokori.util.glfw.Window;
import nokori.util.glfw.WindowManager;

/**
 * A utility class that quickly assembles a JNuklear program. To implement this into your project, simply extend this class and call startProgram() in the main method.
 */
public abstract class NuklearProgram {

	protected WindowManager windowManager;
	protected Window window;
	
	protected NuklearContext context;
	
	protected Vector4f clearColor = new Vector4f(1f, 1f, 1f, 1f);
	
	public NuklearProgram() {
		try {
			windowManager = new WindowManager();
		} catch (GLFWException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the given JNuklear-based program.
	 * 
	 * @param program - a class that extends this one, meant to be the root of the program
	 * @param args - the args passed through the main method
	 */
	public static void start(NuklearProgram program, String[] args) {
		//Restarts the JVM if necessary on the first thread to ensure Mac compatibility
		if (JVMUtil.restartJVMOnFirstThread(true, args)) {
			return;
		}

		//Create Window and NuklearContext
		try {
			Window window = program.createWindow(program.windowManager);
			window.makeContextCurrent();
			
			program.window = window;
			program.context = new NuklearContext(window, program.getInitialFont());
			
			//Initialize the program
			program.init(program.context, args);
			
			//Run the program
			program.loop();
					
		} catch (GLFWException e) {
			e.printStackTrace();
			return;
		}
	}
	
	
	private void loop() {
		//Software loop
		while (!window.isCloseRequested()) {
			context.listen();
			
			glViewport(0, 0, window.getWidth(), window.getHeight());
			glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			context.layout();
			
			run(context);

			/*
			 * IMPORTANT: `nk_glfw_render` modifies some global OpenGL state with blending,
			 * scissor, face culling, depth test and viewport and defaults everything back
			 * into a default state. Make sure to either a.) save and restore or b.) reset
			 * your own state after rendering the UI.
			 */
			
			context.render();
			
			windowManager.update(false);
		}
		
		dispose();
		context.dispose();
		windowManager.dispose();
	}
	
	/**
	 * Called after the basic GLFW initialization is completed and before the program loop is started.
	 * 
	 * @param args - the args passed through the main method
	 * @param window - the newly created GLFW window
	 */
	public abstract void init(NuklearContext context, String[] args);

	/**
	 * Called from the program loop before rendering.
	 */
	public abstract void run(NuklearContext context);
	
	
	/**
	 * Creates the GLFW Window. Allows the user to customize it to their specific use-case.
	 * 
	 * @param windowManager
	 * @return
	 */
	public abstract Window createWindow(WindowManager windowManager) throws GLFWException;

	/**
	 * JNuklear requires at least one Font to be active at all times for it to work properly. Therefore, it expects a default font to be passed in
	 * at startup. This can be overridden later with subsequent Font.apply() calls.
	 * @return
	 */
	public abstract Font getInitialFont();
	
	/**
	 * This is called at the end of this program's life, right before WindowManager and NuklearContext are disposed.
	 */
	protected abstract void dispose();
}
