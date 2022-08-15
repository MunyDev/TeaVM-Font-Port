package org.munydev.teavm.font;

import org.munydev.fs.FSFile;
import org.munydev.fs.FSFileInputStream;
import org.munydev.fs.access.CompletionCallback;
import org.teavm.jso.JSBody;

public class FontUtils {
	public static JSPromise uploadFontFace(String name, String src) {
		return FontFace.create(name, src).load();
	}
	@JSBody(script = "document.fonts.add(fontFace);", params= {"fontFace"})
	public static native void addFontFace(FontFace ff);
	
	@SuppressWarnings("resource")
	public static void uploadFontFace(String name, FSFile ff, CompletionCallback cc) {
		
		new FSFileInputStream(ff, (in)->{
			String src = "url("+in.getUrl()+")";
			FontFace ff2 = FontFace.create(name, src);
			ff2.load().then((obj)->{
				addFontFace(ff2);
				cc.onComplete();
				in.close();
			});
		});
	}
}
