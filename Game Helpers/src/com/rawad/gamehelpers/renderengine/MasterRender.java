package com.rawad.gamehelpers.renderengine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.utils.Util;

public class MasterRender {
	
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(202, 212, 227);
	
	private Map<Class<? extends LayeredRender>, LayeredRender> renders;
	private ArrayList<LayeredRender> iterableRenders;
	
	private BufferedImage onscreen;
	private BufferedImage offscreenBuffer;
	
	private Graphics2D g;
	
	public MasterRender() {
		
		renders = new HashMap<Class<? extends LayeredRender>, LayeredRender>();
		iterableRenders = new ArrayList<LayeredRender>();
		
		onscreen = new BufferedImage(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		offscreenBuffer = new BufferedImage(onscreen.getWidth(), onscreen.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		g = offscreenBuffer.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	}
	
	public void clearBuffer() {
		
		offscreenBuffer.copyData(onscreen.getRaster());
		
		g.setColor(DEFAULT_BACKGROUND_COLOR);
		g.fillRect(0, 0, onscreen.getWidth(), onscreen.getHeight());
		
	}
	
	public void render() {
		
		for(LayeredRender render: iterableRenders) {
			
			render.render(g);
			
		}
		
	}
	
	public BufferedImage getBuffer() {
		
		return onscreen;
		
	}
	
	public Graphics2D getGraphics() {
		return g;
	}
	
	/**
	 * Binds the given {@code render} to the given {@code key} value.
	 * 
	 * @param key
	 * @param render
	 */
	public void registerRender(Class<? extends LayeredRender> key, LayeredRender render) {
		renders.put(key, render);
		iterableRenders.add(render);
	}
	
	/**
	 * Use default {@code class} variable of the given {@code render}.
	 * 
	 * @param render
	 */
	public void registerRender(LayeredRender render) {
		this.registerRender(render.getClass(), render);
	}
	
	public <T extends LayeredRender> T getRender(Class<T> key) {
		return Util.cast(renders.get(key));
	}
	
}
