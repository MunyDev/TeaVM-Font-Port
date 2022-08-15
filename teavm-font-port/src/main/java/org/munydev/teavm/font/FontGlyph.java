package org.munydev.teavm.font;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface FontGlyph extends JSObject {
	@JSProperty
	void setWidth(float w);
	@JSProperty
	void setHeight(float h);
	@JSProperty
	void setX(float xpos);
	@JSProperty
	void setY(float ypos);
	@JSProperty
	void setAdvance(float advance);
	@JSProperty
	void setAscent(float ascent);
	@JSProperty
	void setDescent(float descent);
	
	@JSProperty
	float getAscent();
	@JSProperty
	float getDescent();
	
	@JSProperty
	float getWidth();
	@JSProperty
	float getHeight();
	@JSProperty
	float getX();
	@JSProperty
	float getY();
	@JSProperty
	float getAdvance();
}
