package com.rawad.gamehelpers.utils.strings;

/**
 * First "C" = Custom.
 * 
 * @author Rawad
 *
 */
public class CCharacter {
	
	private int id;
	
	private double x;
	private double y;
	
	private double width;
	private double height;
	
	private double xoffset;
	private double yoffset;
	
	private double sizeX;
	private double sizeY;
	
	private double xadvance;
	
//	private int page;// Not sure about these two...
//	private int chnl;
	
	/**
	 * 
	 * @param id 
	 * 			- ASCII code for character.
	 */
	protected CCharacter(int id, double x, double y, double width, double height, double xoffset,
			double yoffset, double sizeX, double sizeY, double xadvance) {
		
		this.id = id;
		
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		
		this.xadvance = xadvance;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}
	
	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * @return the xoffset
	 */
	public double getXoffset() {
		return xoffset;
	}
	
	/**
	 * @return the yoffset
	 */
	public double getYoffset() {
		return yoffset;
	}
	
	/**
	 * @return the sizeX
	 */
	public double getSizeX() {
		return sizeX;
	}
	
	/**
	 * @return the sizeY
	 */
	public double getSizeY() {
		return sizeY;
	}
	
	/**
	 * @return the xadvance
	 */
	public double getXadvance() {
		return xadvance;
	}
	
}
