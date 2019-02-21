package nokori.jnuklear;

import org.lwjgl.nuklear.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL11C.glDeleteTextures;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL20C.glDeleteProgram;
import static org.lwjgl.opengl.GL20C.glDeleteShader;
import static org.lwjgl.opengl.GL20C.glDetachShader;
import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Objects;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;

import nokori.jnuklear.callback.wrapper.NuklearCharCallback;
import nokori.jnuklear.callback.wrapper.NuklearCursorPosCallback;
import nokori.jnuklear.callback.wrapper.NuklearKeyCallback;
import nokori.jnuklear.callback.wrapper.NuklearMouseButtonCallback;
import nokori.jnuklear.callback.wrapper.NuklearScrollCallback;
import nokori.jnuklear.layout.NkPane;
import nokori.util.glfw.Window;

public class NuklearContext {
	
	public static final int RECOMMENDED_MAX_VERTEX_BUFFER = 512 * 1024;
	public static final int RECOMMENDED_MAX_ELEMENT_BUFFER = 128 * 1024;
	
	/*
	 * Shader data
	 */
	
	private static final int BUFFER_INITIAL_SIZE = 4 * 1024;

	private static final NkAllocator ALLOCATOR;

	private static final NkDrawVertexLayoutElement.Buffer VERTEX_LAYOUT;

	static {
		ALLOCATOR = NkAllocator.create().alloc((handle, old, size) -> nmemAllocChecked(size))
				.mfree((handle, ptr) -> nmemFree(ptr));

		VERTEX_LAYOUT = NkDrawVertexLayoutElement.create(4).position(0).attribute(NK_VERTEX_POSITION)
				.format(NK_FORMAT_FLOAT).offset(0).position(1).attribute(NK_VERTEX_TEXCOORD).format(NK_FORMAT_FLOAT)
				.offset(8).position(2).attribute(NK_VERTEX_COLOR).format(NK_FORMAT_R8G8B8A8).offset(16).position(3)
				.attribute(NK_VERTEX_ATTRIBUTE_COUNT).format(NK_FORMAT_COUNT).offset(0).flip();
	}
	
	private NkBuffer cmds = NkBuffer.create();
	private NkDrawNullTexture null_texture = NkDrawNullTexture.create();

	private int vbo, vao, ebo;
	private int prog;
	private int vert_shdr;
	private int frag_shdr;
	private int uniform_tex;
	private int uniform_proj;
	
	/*
	 * GLFW Window 
	 */
	
	private Window window;

	/*
	 * Context data
	 */
	
	private NkContext nkContext = NkContext.create();
	
	private NuklearScrollCallback scrollCallback;
	private NuklearCharCallback charCallback;
	private NuklearKeyCallback keyCallback;
	private NuklearCursorPosCallback cursorPosCallback;
	private NuklearMouseButtonCallback mouseButtonCallback;

	private ArrayList<NkPane> nkPanes = new ArrayList<NkPane>();
	
	/**
	 * Initialize a NuklearContext. A GLFW window and a default font is required to successfully start the Nuklear wrapper. 
	 * The window is so that we have an OpenGL context and GLFW handle to work with, and the Font is so that we can set an active
	 * font for Nuklear to use. If a font isn't applied, the program will likely crash if the users doesn't set one themselves.
	 * 
	 * @param window
	 * @param font
	 */
	public NuklearContext(Window window, Font font) {
		this.window = window;
		setupWindow();
		font.apply(this);
	}
	
	/**
	 * @return this container's NkContext
	 */
	public NkContext get() {
		return nkContext;
	}
	
	/**
	 * @return the GLFW window tied to this Nuklear context
	 */
	public Window getWindow() {
		return window;
	}
	
	/**
	 * @return a list of all currently active NkPanels.
	 */
	public ArrayList<NkPane> getPanes(){
		return nkPanes;
	}
	
	/**
	 * Shorthand version of getPanes().add()
	 */
	public void addPane(NkPane panel) {
		nkPanes.add(panel);
	}
	
	/**
	 * Shorthand version of getPanes().remove()
	 */
	public boolean removePane(NkPane panel) {
		return nkPanes.remove(panel);
	}
	
	/**
	 * Render the Nuklear content with the default settings.
	 */
	public void render() {
		render(true, RECOMMENDED_MAX_VERTEX_BUFFER, RECOMMENDED_MAX_ELEMENT_BUFFER);
	}
	
	/**
	 * Render the Nuklear content with customized settings.
	 * 
	 * @param antiAliasingOn
	 * @param max_vertex_buffer
	 * @param max_element_buffer
	 */
	public void render(boolean antiAliasingOn, int max_vertex_buffer, int max_element_buffer) {
		try (MemoryStack stack = stackPush()) {
			// setup global state
			glEnable(GL_BLEND);
			glBlendEquation(GL_FUNC_ADD);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glDisable(GL_CULL_FACE);
			glDisable(GL_DEPTH_TEST);
			glEnable(GL_SCISSOR_TEST);
			glActiveTexture(GL_TEXTURE0);

			// setup program
			glUseProgram(prog);
			glUniform1i(uniform_tex, 0);
			glUniformMatrix4fv(uniform_proj, false, stack.floats(2.0f / window.getWidth(), 0.0f, 0.0f, 0.0f, 0.0f, -2.0f / window.getHeight(),
					0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f));
			
			glViewport(0, 0, window.getFramebufferWidth(), window.getFramebufferHeight());
		}

		/*
		 * 
		 *  convert from command queue into draw list and draw to screen
		 *  
		 */

		/*
		 *  allocate vertex and element buffer
		 */
		
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

		glBufferData(GL_ARRAY_BUFFER, max_vertex_buffer, GL_STREAM_DRAW);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, max_element_buffer, GL_STREAM_DRAW);

		/*
		 * load draw vertices & elements directly into vertex + element buffer
		 */
		
		ByteBuffer vertices = Objects
				.requireNonNull(glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, max_vertex_buffer, null));

		ByteBuffer elements = Objects
				.requireNonNull(glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY, max_element_buffer, null));

		try (MemoryStack stack = stackPush()) {
			int antiAliasing = antiAliasingOn ? Nuklear.NK_ANTI_ALIASING_ON : Nuklear.NK_ANTI_ALIASING_OFF;
			
			// fill convert configuration
			NkConvertConfig config = NkConvertConfig.callocStack(stack).vertex_layout(VERTEX_LAYOUT).vertex_size(20)
					.vertex_alignment(4).null_texture(null_texture).circle_segment_count(22).curve_segment_count(22)
					.arc_segment_count(22).global_alpha(1.0f).shape_AA(antiAliasing).line_AA(antiAliasing);

			// setup buffers to load vertices and elements
			NkBuffer vbuf = NkBuffer.mallocStack(stack);
			NkBuffer ebuf = NkBuffer.mallocStack(stack);

			nk_buffer_init_fixed(vbuf, vertices/* , max_vertex_buffer */);
			nk_buffer_init_fixed(ebuf, elements/* , max_element_buffer */);
			nk_convert(nkContext, cmds, vbuf, ebuf, config);
		}
		glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
		glUnmapBuffer(GL_ARRAY_BUFFER);

		/*
		 * iterate over and execute each draw command
		 */
		
		float fb_scale_x = (float) window.getFramebufferWidth() / (float) window.getWidth();
		float fb_scale_y = (float) window.getFramebufferHeight() / (float) window.getHeight();

		long offset = NULL;
		for (NkDrawCommand cmd = nk__draw_begin(nkContext, cmds); cmd != null; cmd = nk__draw_next(cmd, cmds, nkContext)) {
			if (cmd.elem_count() == 0) {
				continue;
			}

			glBindTexture(GL_TEXTURE_2D, cmd.texture().id());

			glScissor((int) (cmd.clip_rect().x() * fb_scale_x),
					(int) ((window.getHeight() - (int) (cmd.clip_rect().y() + cmd.clip_rect().h())) * fb_scale_y),
					(int) (cmd.clip_rect().w() * fb_scale_x), (int) (cmd.clip_rect().h() * fb_scale_y));

			glDrawElements(GL_TRIANGLES, cmd.elem_count(), GL_UNSIGNED_SHORT, offset);

			offset += cmd.elem_count() * 2;
		}

		nk_clear(nkContext);

		/*
		 *  default OpenGL state
		 */
		glUseProgram(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		glDisable(GL_BLEND);
		glDisable(GL_SCISSOR_TEST);
	}

	/**
	 * Call the layout() functions of all the layouts contained in this NuklearContext, which are responsible for updating and rendering
	 * their components.
	 */
	public void layout() {
		for (int i = 0; i < nkPanes.size(); i++) {
			nkPanes.get(i).layout();
		}
	}
	
	/**
	 * Polls NuklearContext's inputs and also calls glfwPollEvents().
	 */
	public void listen() {
		nk_input_begin(nkContext);
		
		glfwPollEvents();
		
		for (int i = 0; i < nkPanes.size(); i++) {
			nkPanes.get(i).listen();
		}

		nk_input_end(nkContext);
	}

	private void setupContext() {
        String NK_SHADER_VERSION = Platform.get() == Platform.MACOSX ? "#version 150\n" : "#version 300 es\n";
        
        String vertex_shader =
            NK_SHADER_VERSION +
            "uniform mat4 ProjMtx;\n" +
            "in vec2 Position;\n" +
            "in vec2 TexCoord;\n" +
            "in vec4 Color;\n" +
            "out vec2 Frag_UV;\n" +
            "out vec4 Frag_Color;\n" +
            "void main() {\n" +
            "   Frag_UV = TexCoord;\n" +
            "   Frag_Color = Color;\n" +
            "   gl_Position = ProjMtx * vec4(Position.xy, 0, 1);\n" +
            "}\n";
        
        String fragment_shader =
            NK_SHADER_VERSION +
            "precision mediump float;\n" +
            "uniform sampler2D Texture;\n" +
            "in vec2 Frag_UV;\n" +
            "in vec4 Frag_Color;\n" +
            "out vec4 Out_Color;\n" +
            "void main(){\n" +
            "   Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n" +
            "}\n";

		nk_buffer_init(cmds, ALLOCATOR, BUFFER_INITIAL_SIZE);
		
		prog = glCreateProgram();
		
		vert_shdr = glCreateShader(GL_VERTEX_SHADER);
		frag_shdr = glCreateShader(GL_FRAGMENT_SHADER);
		
		glShaderSource(vert_shdr, vertex_shader);
		glShaderSource(frag_shdr, fragment_shader);
		glCompileShader(vert_shdr);
		glCompileShader(frag_shdr);
		
		if (glGetShaderi(vert_shdr, GL_COMPILE_STATUS) != GL_TRUE) {
			throw new IllegalStateException();
		}
		
		if (glGetShaderi(frag_shdr, GL_COMPILE_STATUS) != GL_TRUE) {
			throw new IllegalStateException();
		}
		
		glAttachShader(prog, vert_shdr);
		glAttachShader(prog, frag_shdr);
		glLinkProgram(prog);
		
		if (glGetProgrami(prog, GL_LINK_STATUS) != GL_TRUE) {
			throw new IllegalStateException();
		}

		uniform_tex = glGetUniformLocation(prog, "Texture");
		uniform_proj = glGetUniformLocation(prog, "ProjMtx");
		int attrib_pos = glGetAttribLocation(prog, "Position");
		int attrib_uv = glGetAttribLocation(prog, "TexCoord");
		int attrib_col = glGetAttribLocation(prog, "Color");

		{
			// buffer setup
			vbo = glGenBuffers();
			ebo = glGenBuffers();
			vao = glGenVertexArrays();

			glBindVertexArray(vao);
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

			glEnableVertexAttribArray(attrib_pos);
			glEnableVertexAttribArray(attrib_uv);
			glEnableVertexAttribArray(attrib_col);

			glVertexAttribPointer(attrib_pos, 2, GL_FLOAT, false, 20, 0);
			glVertexAttribPointer(attrib_uv, 2, GL_FLOAT, false, 20, 8);
			glVertexAttribPointer(attrib_col, 4, GL_UNSIGNED_BYTE, true, 20, 16);
		}

		{
			// null texture setup
			int nullTexID = glGenTextures();

			null_texture.texture().id(nullTexID);
			null_texture.uv().set(0.5f, 0.5f);

			glBindTexture(GL_TEXTURE_2D, nullTexID);
			try (MemoryStack stack = stackPush()) {
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 1, 1, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV,
						stack.ints(0xFFFFFFFF));
			}
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		}

		glBindTexture(GL_TEXTURE_2D, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	private NkContext setupWindow() {
		scrollCallback = new NuklearScrollCallback(nkContext);
		charCallback = new NuklearCharCallback(nkContext);
		keyCallback = new NuklearKeyCallback(nkContext);
		cursorPosCallback = new NuklearCursorPosCallback(nkContext);
		mouseButtonCallback = new NuklearMouseButtonCallback(nkContext);
		
		window.addInputCallback(scrollCallback, charCallback, keyCallback, cursorPosCallback, mouseButtonCallback);

		nk_init(nkContext, ALLOCATOR, null);
		
		nkContext.clip(it -> it.copy((handle, text, len) -> {
			if (len == 0) {
				return;
			}

			try (MemoryStack stack = stackPush()) {
				ByteBuffer str = stack.malloc(len + 1);
				memCopy(text, memAddress(str), len);
				str.put(len, (byte) 0);

				glfwSetClipboardString(window.getHandle(), str);
			}
		}).paste((handle, edit) -> {
			long text = nglfwGetClipboardString(window.getHandle());
			
			if (text != NULL) {
				nnk_textedit_paste(edit, text, nnk_strlen(text));
			}
		}));

		setupContext();
		return nkContext;
	}
	
	private void disposeShaders() {
		glDetachShader(prog, vert_shdr);
		glDetachShader(prog, frag_shdr);
		glDeleteShader(vert_shdr);
		glDeleteShader(frag_shdr);
		glDeleteProgram(prog);
		glDeleteTextures(null_texture.texture().id());
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);
		nk_buffer_free(cmds);

		GL.setCapabilities(null);
	}
	
	public void dispose() {
		window.removeInputCallback(scrollCallback, charCallback, keyCallback, cursorPosCallback, mouseButtonCallback);
		
		Objects.requireNonNull(nkContext.clip().copy()).free();
		Objects.requireNonNull(nkContext.clip().paste()).free();
		nk_free(nkContext);
		disposeShaders();

		Objects.requireNonNull(ALLOCATOR.alloc()).free();
		Objects.requireNonNull(ALLOCATOR.mfree()).free();
	}
}
