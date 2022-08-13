package org.munydev.teavm.font;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface TextMetrics extends JSObject {
	@JSProperty
	int getWidth();
	@JSProperty
	int getActualBoundingBoxAscent();
	@JSProperty
	int getActualBoundingBoxDescent();
	@JSProperty
	int getActualBoundingBoxRight();
	@JSProperty
	int getActualBoundingBoxLeft();
	
}
