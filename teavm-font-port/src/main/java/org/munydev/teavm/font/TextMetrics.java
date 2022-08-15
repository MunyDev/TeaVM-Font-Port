package org.munydev.teavm.font;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface TextMetrics extends JSObject {
	@JSProperty
	float getWidth();
	@JSProperty
	float getActualBoundingBoxAscent();
	@JSProperty
	float getActualBoundingBoxDescent();
	@JSProperty
	float getActualBoundingBoxRight();
	@JSProperty
	float getActualBoundingBoxLeft();
	
}
