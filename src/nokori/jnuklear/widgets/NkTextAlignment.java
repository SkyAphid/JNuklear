package nokori.jnuklear.widgets;

import static org.lwjgl.nuklear.Nuklear.NK_TEXT_CENTERED;
import static org.lwjgl.nuklear.Nuklear.NK_TEXT_LEFT;
import static org.lwjgl.nuklear.Nuklear.NK_TEXT_RIGHT;

public enum NkTextAlignment {
	LEFT(NK_TEXT_LEFT),
	RIGHT(NK_TEXT_RIGHT),
	CENTERED(NK_TEXT_CENTERED);
	
	int key;
	
	private NkTextAlignment(int key) {
		this.key = key;
	}
}