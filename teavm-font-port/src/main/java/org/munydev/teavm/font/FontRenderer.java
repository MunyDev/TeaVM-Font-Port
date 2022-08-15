package org.munydev.teavm.font;

import org.teavm.jso.core.JSObjects;

public abstract class FontRenderer {
	
	public abstract void drawString(int x, int y, String text);
	public abstract void onInititalize(FontCompletionCallback fcc);
	public static FontGlyph createFontGlyph(float x, float y, float f, float h, float g, float asc, float desc) {
		FontGlyph fg = JSObjects.create();
		fg.setAdvance(g);
		fg.setHeight(h);
		fg.setWidth(f);
		fg.setX(x);
		fg.setY(y);
		fg.setAscent(asc);
		fg.setDescent(desc);
		return fg;
	}
}
