package org.munydev.teavm.font;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.GLUtil;

import static org.lwjgl.opengl.GL11.*;
import org.munydev.teavm.lwjgl.CurrentContext;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.canvas.ImageData;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.webgl.WebGLRenderingContext;
import org.teavm.webgl2.WebGL2RenderingContext;

public class FontRendererWebGL extends FontRenderer {
	JSArray<FontGlyph> fg =  JSArray.create();
	boolean initialized = false;
	FontCompletionCallback fcc;
	int cWidth, cHeight;
	int textureID;
	CanvasRenderingContext2D ctxGlob;
	public FontRendererWebGL(String name, String cssSize, String style, int cWidth, int cHeight, String cssColor) {
		HTMLCanvasElement hce = (HTMLCanvasElement) HTMLDocument.current().createElement("canvas");
		hce.setWidth(cWidth);
		hce.setHeight(cHeight);
		this.cWidth = cWidth;
		this.cHeight = cHeight;
		CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) hce.getContext("2d");
		ctx.setFont(style+" "+cssSize+" "+name);
		ctx.setTextAlign("left");
		this.ctxGlob = ctx;
//		ctx.setTextBaseline("top");
		float x = 0;
		float y = 0;
		float maxLineHeight = 0;
//		int curAdvance = 0;
		ctx.setFillStyle(cssColor);
		
		System.out.println(ctx.getFont());
		for (int i = 32; i < 128; i++) {
			TextMetrics tm = (TextMetrics) ctx.measureText(String.valueOf((char) i));
			float h = tm.getActualBoundingBoxDescent()+tm.getActualBoundingBoxAscent();
			if (x + tm.getWidth() > cWidth) {
				y += maxLineHeight ;
				x = 0;
				i-= 1;// Make it render again
				continue;
			}
			fg.set(i, FontRenderer.createFontGlyph(x, y, tm.getWidth(), h, tm.getActualBoundingBoxRight() +  tm.getActualBoundingBoxLeft(),tm.getActualBoundingBoxAscent(), tm.getActualBoundingBoxDescent()));
			ctx.fillText(String.valueOf((char) i), x, y + tm.getActualBoundingBoxAscent());
			GLUtil.log(tm);
			if (maxLineHeight <= h) {
				maxLineHeight = h;
			}
			
			x += tm.getWidth();
			
			
		}
		
		WebGL2RenderingContext wgl = (WebGL2RenderingContext) CurrentContext.getContext();
//		wgl.pixelStorei(WebGLRenderingContext.UNPACK_FLIP_Y_WEBGL, 1);
		int texID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		wgl.pixelStorei(WebGLRenderingContext.UNPACK_FLIP_Y_WEBGL, 0);
//		wgl.pixelStorei(WebGLRenderingContext.UNPACK_PREMULTIPLY_ALPHA_WEBGL, 1);
		wgl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		wgl.texImage2D(GL_TEXTURE_2D, 0, GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE, hce);
		wgl.generateMipmap(GL_TEXTURE_2D);
		textureID = texID;
		initialized = true;
		
		if (fcc != null) fcc.onComplete();
	}
	public int getNativeID() {
		return this.textureID;
	}
	
	@Override
	public void drawString(int x, int y, String text) {
		// TODO Auto-generated method stub
		if (text.length() <= 0 || text.length() > 255) {
			return;
		}
		GL11.glBindTexture(GL_TEXTURE_2D, textureID);
		TextMetrics tm = (TextMetrics)  ctxGlob.measureText(text);
		float baseline = tm.getActualBoundingBoxAscent();
		float xpos = (float) x;
		float ypos = (float) y;
		GL11.glBegin(GL_TRIANGLES);
		
		for (int c : text.toCharArray()) {
			if (fg.get(c) == JSObjects.undefined()) continue;
			float tx1 = (float) fg.get(c).getX() / (float) cWidth;
			float ty1 = (float) fg.get(c).getY() / (float) cHeight;
			float tx2 = (float) (fg.get(c).getX()+fg.get(c).getWidth()) / (float) cWidth;
			float ty2 = (float) (fg.get(c).getY() + fg.get(c).getHeight()) / (float) cHeight;
			float w = (float) fg.get(c).getWidth();
//			float w2 = (float) fg.get(c).getAdvance();
			float h = fg.get(c).getHeight();
//			System.out.println(String.format("%f, %f, %f, %f, %d, %d", tx1, ty1, tx2, ty2, w, h));
			float tempYPos = y+ baseline-fg.get(c).getAscent();
			GLUtil.log(fg.get(c));
			GLUtil.log(JSString.valueOf(String.valueOf(c)));
			GL11.glTexCoord2f(tx2, ty1); 
			GL11.glVertex2f(xpos + w, tempYPos); 
			
			GL11.glTexCoord2f(tx2, ty2); 
			GL11.glVertex2f(xpos + w, tempYPos+h); 
			
			GL11.glTexCoord2f(tx1, ty1);
			GL11.glVertex2f(xpos, tempYPos);
			
			GL11.glTexCoord2f(tx2, ty2); 
			GL11.glVertex2f(xpos + w, tempYPos+h); 
			
			GL11.glTexCoord2f(tx1, ty1);
			GL11.glVertex2f(xpos, tempYPos);
			
			GL11.glTexCoord2f(tx1, ty2);
			GL11.glVertex2f(xpos, tempYPos+h);
			xpos+=w;
			
		}
		GL11.glEnd();
		
	}
	public void debug() {
		GL11.glBindTexture(GL_TEXTURE_2D, textureID);
		GL11.glBegin(GL_TRIANGLE_STRIP);
		
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(400+cWidth, 0);
		
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(400+cWidth, 400+cHeight);
		
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(0, 0);
		
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(0, 400+cHeight);
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
