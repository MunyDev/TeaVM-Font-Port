package org.munydev.teavm.font;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface FontGlyph extends JSObject {
	@JSProperty
	void setWidth(int w);
	@JSProperty
	void setHeight(int h);
	@JSProperty
	void setX(int xpos);
	@JSProperty
	void setY(int ypos);
	@JSProperty
	void setAdvance(int advance);
	
	@JSProperty
	int getWidth();
	@JSProperty
	int getHeight();
	@JSProperty
	int getX();
	@JSProperty
	int getY();
	@JSProperty
	int getAdvance();
}
