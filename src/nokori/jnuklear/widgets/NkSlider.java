package nokori.jnuklear.widgets;

import org.lwjgl.nuklear.Nuklear;

import nokori.jnuklear.NuklearContext;
import nokori.jnuklear.callback.SlideHandler;
import nokori.jnuklear.layout.NkPane;

public class NkSlider extends NkWidget {

	private float[] val = new float[1];
	private SlideHandler handler;
	
	private float minValue, maxValue, step;
	
	public NkSlider(float minValue, float maxValue, float step) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.step = step;
	}

	@Override
	public void layout(NuklearContext context, NkPane parent) {
		super.layout(context, parent);
		
		if (Nuklear.nk_slider_float(context.get(), minValue, val, maxValue, step) == 1) {
			handler.slide(val[0]);
		}
	}
	
	public void setOnSlide(SlideHandler handler) {
		this.handler = handler;
	}
}
