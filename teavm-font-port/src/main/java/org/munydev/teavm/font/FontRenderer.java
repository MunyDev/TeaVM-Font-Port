package org.munydev.teavm.font;

import org.teavm.jso.core.JSObjects;

public abstract class FontRenderer {
	
	public abstract void drawString(int x, int y, String text);
	public abstract void onInititalize(FontCompletionCallback fcc);
	public static FontGlyph createFontGlyph(int x, int y, int w, int h, int advance) {
		FontGlyph fg = JSObjects.create();
		fg.setAdvance(advance);
		fg.setHeight(h);
		fg.setWidth(w);
		fg.setX(x);
		fg.setY(y);
		return fg;
	}
}
