package com.rawad.gamehelpers.client.renderengine;

import com.rawad.gamehelpers.game.Game;

import javafx.scene.shape.Rectangle;

// TODO: (Camera) Consider making this an Entity.
public class Camera {
	
	private static final double DEFAULT_MAX_SCALE = 5D;
	
	private static final double DEFAULT_MOVEMENT_SPEED = 5D;
	
	/** Area in which the camera is bound. */
	private Rectangle outerBounds;
	
	/** Position of the camera within {@code outerBounds}. */
	private Rectangle cameraBounds;
	
	private double scaleX;
	private double scaleY;
	
	private double maxScaleX;
	private double maxScaleY;
	
	private double movementSpeed;
	
	public Camera(Rectangle outerBounds) {
		
		this.outerBounds = outerBounds;
		
		cameraBounds = new Rectangle(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		
		scaleX = 1;
		scaleY = 1;
		
		maxScaleX = DEFAULT_MAX_SCALE;
		maxScaleY = DEFAULT_MAX_SCALE;
		
		movementSpeed = DEFAULT_MOVEMENT_SPEED;
		
	}
	
	public Camera() {
		this(new Rectangle(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT));
	}
	
	/**
	 * For manually moving the camera around.
	 * 
	 * @param up
	 * @param down
	 * @param right
	 * @param left
	 */
	public void update(boolean up, boolean down, boolean right, boolean left) {
		
		double dx = 0;
		double dy = 0;
		
		if(up) {
			dy = -movementSpeed;
		} else if(down) {
			dy = movementSpeed;
		}
		
		if(right) {
			dx = movementSpeed;
		} else if(left) {
			dx = -movementSpeed;
		}
		
		setX(getX() + dx);
		setY(getY() + dy);
		
	}
	
	public void setOuterBounds(Rectangle outerBounds) {
		this.outerBounds = outerBounds;
	}
	
	public Rectangle getCameraBounds() {
		return cameraBounds;
	}
	
	/**
	 * 
	 * @param delta Angle of rotation in degrees.
	 */
	public void increaseRotation(double delta) {
		setRotation(cameraBounds.getRotate() + delta);
	}
	
	public void setRotation(double theta) {
		cameraBounds.setRotate(theta);
	}
	
	public double getRotation() {
		return cameraBounds.getRotate();
	}
	
	public void setScale(double scaleX, double scaleY) {
		
		double minScaleX = cameraBounds.getWidth() / outerBounds.getWidth();
		
		if(scaleX < minScaleX) {
			scaleX = minScaleX;
		} else if(scaleX > maxScaleX) {
			scaleX = maxScaleX;
		}
		
		double minScaleY = cameraBounds.getHeight() / outerBounds.getHeight();
		
		if(scaleY < minScaleY) {
			scaleY = minScaleY;
		} else if(scaleY > maxScaleY) {
			scaleY = maxScaleY;
		}
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	public double getScaleX() {
		return scaleX;
	}
	
	public double getScaleY() {
		return scaleY;
	}
	
	public void setX(double x) {
		/**/
		double minX = outerBounds.getX();
		
		double maxWidth = outerBounds.getWidth();
		double camWidth = cameraBounds.getWidth() / scaleX;
		
		if(x < minX) {
			x = minX;
		} else if(x > maxWidth - camWidth) {
			x = maxWidth - camWidth;
		}/**/
		
		cameraBounds.setX(x);
		
	}
	
	public double getX() {
		return cameraBounds.getX();
	}
	
	public void setY(double y) {
		/**/
		double minY = outerBounds.getY();
		
		double maxHeight = outerBounds.getHeight();
		double camHeight = cameraBounds.getHeight() / scaleY;
		
		if(y < minY) {
			y = minY;
		} else if(y > maxHeight - camHeight) {
			y = maxHeight - camHeight;
		}/**/
		
		cameraBounds.setY(y);
		
	}
	
	public double getY() {
		return cameraBounds.getY();
	}
	
	public void setMovementSpeed(double movementSpeed) {
		this.movementSpeed = movementSpeed;
	}
	
}
