package org.munydev.teavm.font;

import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import org.munydev.teavm.lwjgl.CurrentContext;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.canvas.ImageData;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.webgl2.WebGL2RenderingContext;

public class FontRendererWebGL extends FontRenderer {
	JSArray<FontGlyph> fg =  JSArray.create();
	boolean initialized = false;
	FontCompletionCallback fcc;
	int textureID;
	public FontRendererWebGL(String name, String cssSize) {
		HTMLCanvasElement hce = (HTMLCanvasElement) HTMLDocument.current().createElement("canvas");
		hce.setWidth(512);
		hce.setHeight(512);
		CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) hce.getContext("2d");
		ctx.setFont(cssSize+" "+name);
		int x = 0;
		int y = 0;
		int maxLineHeight = 0;
//		int curAdvance = 0;
		for (int i = 32; i < 128; i++) {
			TextMetrics tm = (TextMetrics) ctx.measureText(String.valueOf(i));
			int h = tm.getActualBoundingBoxAscent()+tm.getActualBoundingBoxDescent();
			fg.push(FontRenderer.createFontGlyph(x, y, tm.getWidth(), h, tm.getActualBoundingBoxRight() +  tm.getActualBoundingBoxLeft()));
			if (maxLineHeight >= h) {
				maxLineHeight = h;
			}
			if (x + tm.getWidth() >= 512) {
				y+= maxLineHeight;
				x = 0;
				continue;
			}
			x += tm.getWidth();
			
			
		}
		ImageData id = ctx.getImageData(0, 0, 512, 512);
		WebGL2RenderingContext wgl = (WebGL2RenderingContext) CurrentContext.getContext();
		int texID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		wgl.texImage2D(GL11.GL_TEXTURE_2D, 0, GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE, id);
		textureID = texID;
		initialized = true;
		fcc.onComplete();
	}
	public int getNativeID() {
		return this.textureID;
	}
	
	@Override
	public void drawString(int x, int y, String text) {
		// TODO Auto-generated method stub
		GL11.glBindTexture(GL_TEXTURE_2D, textureID);
		GL11.glBegin(GL_TRIANGLE_STRIP);
		int xpos = x;
		int ypos = y;
		for (char c : text.toCharArray()) {
			int tx1 = fg.get(c).getX() / 512;
			int ty1 = fg.get(c).getY() / 512;
			int tx2 = (fg.get(c).getX()+fg.get(c).getWidth()) / 512;
			int ty2 = (fg.get(c).getY() + fg.get(c).getHeight()) / 512;
			int w = fg.get(c).getWidth();
			int h = fg.get(c).getHeight();
			GL11.glTexCoord2f(tx2, ty1); 
			GL11.glVertex2f(xpos + w, ypos); 
			
			GL11.glTexCoord2f(tx2, ty2); 
			GL11.glVertex2f(xpos + w, ypos+h); 
			
			GL11.glTexCoord2f(tx1, ty1);
			GL11.glVertex2f(xpos, ypos);
			
			GL11.glTexCoord2f(tx1, ty2);
			GL11.glVertex2f(xpos, ypos+h);
		
			
		}
		GL11.glEnd();
		
	}

	@Override
	public void onInititalize(FontCompletionCallback fcc) {
		// TODO Auto-generated method stub
		if (initialized) {
			fcc.onComplete();
		}else {
			this.fcc = fcc;
		}
	}
	
}
