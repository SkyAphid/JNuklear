package nokori.jnuklear;

public class Color {
	public static final Color WHITE = new Color(255, 255, 255).immutable(true);
	public static final Color LIGHT_GRAY = new Color(211, 211, 211).immutable(true);
	public static final Color SILVER = new Color(192, 192, 192).immutable(true);
	public static final Color GRAY = new Color(128, 128, 128).immutable(true);
	public static final Color DIM_GRAY = new Color(169, 169, 169).immutable(true);
	public static final Color DARK_GRAY = new Color(64, 64, 64).immutable(true);
	public static final Color LIGHT_BLACK = new Color(10, 10, 10).immutable(true);
	public static final Color WHITE_SMOKE = new Color(245, 245, 245).immutable(true);
	public static final Color BLACK = new Color(0, 0, 0).immutable(true);
	public static final Color RED = new Color(255, 0, 0).immutable(true);
	public static final Color PINK = new Color(255, 175, 175).immutable(true);
	public static final Color ORANGE = new Color(255, 200, 0).immutable(true);
	public static final Color YELLOW = new Color(255, 255, 0).immutable(true);
	public static final Color LIGHT_YELLOW = new Color(255, 238, 158).immutable(true);
	public static final Color LIGHT_BLUE = new Color(158, 238, 255).immutable(true);
	public static final Color GREEN = new Color(0, 255, 0).immutable(true);
	public static final Color MAGENTA = new Color(255, 0, 255).immutable(true);
	public static final Color CYAN = new Color(0, 255, 255).immutable(true);
	public static final Color BLUE = new Color(0, 0, 255).immutable(true);
	public static final Color AQUA = new Color(3, 158, 211).immutable(true);
	public static final Color CORAL = new Color("#FF7F50").immutable(true);
	public static final Color VIOLET = new Color("#8A2BE2").immutable(true);
	public static final Color TRANSPARENT = new Color(255,255,255,0).immutable(true);

	private int rgba;
	private boolean immutable = false;
	
	public Color(int r, int g, int b) {
		this(r, g, b, 255);
	}

	public Color(int r, int g, int b, int a) {
		set(r, g, b, a);
	}

	public Color(int rgba) {
		set(rgba);
	}

	public Color(float r, float g, float b) {
		this((int) (r*255), (int) (g*255), (int) (b*255));
	}

	public Color(String hex) {
		this(Integer.valueOf(hex.substring(1, 3), 16), 
				Integer.valueOf(hex.substring(3, 5), 16),
				Integer.valueOf(hex.substring(5, 7), 16));
	}
	
	public Color(Color color) {
		set(color);
	}

	public Color immutable(boolean immutable) {
		this.immutable = immutable;
		return this;
	}

	public Color copy() {
		return new Color(this);
	}
	
	public Color set(Color color) {
		return set(color.getRGBA());
	}
	
	public Color set(float r, float g, float b, float a) {
		return set((int) (r*255), (int) (g*255), (int) (b*255), (int) (a*255));
	}
	
	public Color set(int r, int g, int b, int a) {
		//Set the new color value
		int rgb = ((a & 0xFF) << 24) |
				((r & 0xFF) << 16) |
				((g & 0xFF) << 8)  |
				((b & 0xFF) << 0);
		
		//Ensure that the color is valid
		testColorValueRange(r,g,b,a);
		
		return set(rgb);
	}
	
	/**
	 * Sets an opaque sRGBA color with the specified combined RGBA value
	 * consisting of the alpha component in bits 31-24, red component in bits 16-23, the green component
	 * in bits 8-15, and the blue component in bits 0-7.  The actual color
	 * used in rendering depends on finding the best match given the
	 * color space available for a particular output device. 
	 * 
	 * @return this Color object if the Color is not mutable, or a new Color object if this Color is set to be immutable.
	 */
	public Color set(int rgba) {
		if (immutable) {
			return new Color(rgba);
		} else {
			this.rgba = rgba;
			return this;
		}
	}
	
	public Color alpha(float a) {
		return set(getRed(), getGreen(), getBlue(), (int) (a*255));
	}
	
	public Color red(float r) {
		return set((int) (r*255), getGreen(), getBlue(), getAlpha());
	}
	
	public Color green(float g) {
		return set(getRed(), (int) (g * 255), getBlue(), getAlpha());
	}

	public Color blue(float b) {
		return set(getRed(), getGreen(), (int) (b * 255), getAlpha());
	}

	/**
	 * Returns the red component in the range 0-255 in the default sRGB
	 * space.
	 * @return the red component.
	 * @see #getRGB
	 */
	public int getRed() {
		return (getRGBA() >> 16) & 0xFF;
	}

	/**
	 * Returns the green component in the range 0-255 in the default sRGB
	 * space.
	 * @return the green component.
	 * @see #getRGB
	 */
	public int getGreen() {
		return (getRGBA() >> 8) & 0xFF;
	}

	/**
	 * Returns the blue component in the range 0-255 in the default sRGB
	 * space.
	 * @return the blue component.
	 * @see #getRGB
	 */
	public int getBlue() {
		return (getRGBA() >> 0) & 0xFF;
	}

	/**
	 * Returns the alpha component in the range 0-255.
	 * @return the alpha component.
	 * @see #getRGB
	 */
	public int getAlpha() {
		return (getRGBA() >> 24) & 0xff;
	}

	public int getRGBA() {
		return rgba;
	}

	public Color brighter(double factor) {
		int r = getRed();
		int g = getGreen();
		int b = getBlue();
		int alpha = getAlpha();

		/* From 2D group:
		 * 1. black.brighter() should return grey
		 * 2. applying brighter to blue will always return blue, brighter
		 * 3. non pure color (non zero rgb) will eventually return white
		 */
		int i = (int)(1.0/(1.0-factor));
		if ( r == 0 && g == 0 && b == 0) {
			return new Color(i, i, i, alpha);
		}
		if ( r > 0 && r < i ) r = i;
		if ( g > 0 && g < i ) g = i;
		if ( b > 0 && b < i ) b = i;

		return new Color(Math.min((int)(r/factor), 255),
				Math.min((int)(g/factor), 255),
				Math.min((int)(b/factor), 255),
				alpha);
	}

	private static final double FACTOR = 0.9;

	public Color brighter() {
		return brighter(FACTOR);
	}

	public Color darker() {
		return new Color(Math.max((int)(getRed()  *FACTOR), 0),
				Math.max((int)(getGreen()*FACTOR), 0),
				Math.max((int)(getBlue() *FACTOR), 0),
				getAlpha());
	}
	
	public String toString() {
		int r = getRed();
		int g = getGreen();
		int b = getBlue();
		return "Color:{" + r + "," + g + "," + b + "}";
	}

	public void getColorComponents(float[] array) {
		if ( array.length < 3 ) 
			return;

		array[0] = getRed()/255f;
		array[1] = getGreen()/255f;
		array[2] = getBlue()/255f;
	}
	
	/**
	 * Returns whether or not the RGB values of the given color match this one.
	 * 
	 * @param color
	 * @return
	 */
	public boolean rgbMatches(Color color) {
		return (getRed() == color.getRed() && getGreen() == color.getGreen() && getBlue() == color.getBlue());
	}
	
	/**
	 * Blends the to colors, gradually fading the "from" color to the "to" color based on the given normalized multiplier (a 0-1 value, where 1 is full transitioned).
	 * 
	 * @param from - starting color
	 * @param to - what color to transition to
	 * @param store - the Color object to store the blended colors into (to prevent making garbage)
	 * @param mult - how much of the transition is completed (0 = 100% the from color, 1 = 100% the to color)
	 * @return
	 */
	public static Color blend(Color from, Color to, Color store, double mult) {
		//May not be the best approach, but if it looks good, then whatever.
		int r1 = from.getRed();
		int g1 = from.getGreen();
		int b1 = from.getBlue();
		int a1 = from.getAlpha();
		
		int r2 = to.getRed();
		int g2 = to.getGreen();
		int b2 = to.getBlue();
		int a2 = to.getAlpha();
		
		store.set((int) mix(r1, r2, mult), (int) mix(g1, g2, mult), (int) mix(b1, b2, mult), (int) mix(a1, a2, mult));
		
		return store;
	}
	
	public static double mix(double x, double y, double a){
		return x + (y-x)*a;
	}

	private static void testColorValueRange(int r, int g, int b, int a) {
		boolean rangeError = false;
		String badComponentString = "";

		if ( a < 0 || a > 255) {
			rangeError = true;
			badComponentString = badComponentString + " Alpha";
		}
		if ( r < 0 || r > 255) {
			rangeError = true;
			badComponentString = badComponentString + " Red";
		}
		if ( g < 0 || g > 255) {
			rangeError = true;
			badComponentString = badComponentString + " Green";
		}
		if ( b < 0 || b > 255) {
			rangeError = true;
			badComponentString = badComponentString + " Blue";
		}
		if ( rangeError == true ) {
			throw new IllegalArgumentException("Color parameter outside of expected range: " + badComponentString);
		}
	}

}
