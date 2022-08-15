package org.munydev.teavm.font;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSMethod;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;

public abstract class FontFace implements JSObject {
	@JSBody(script = "return new FontFace(name, src);", params = {"name", "src"})
	public static native FontFace create(String name, String src);
	
	@JSBody(script = "return new FontFace(name, src);", params = {"name", "src"})
	public static native FontFace create(String name, ArrayBuffer src);
	
	@JSMethod
	abstract JSPromise load();
}
