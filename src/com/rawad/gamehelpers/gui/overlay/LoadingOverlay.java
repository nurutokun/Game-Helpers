package com.rawad.gamehelpers.gui.overlay;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import javax.swing.JProgressBar;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.rawad.gamehelpers.renderengine.BackgroundRender;
import com.rawad.gamehelpers.utils.Util;

public class LoadingOverlay extends Overlay {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -664976802953418060L;
	
	private String message;
	private JProgressBar progressBar;
	
	public LoadingOverlay() {
		super("Loading Overlay");
		
		message = "Loading...";
		
		initialize();
		
	}
	private void initialize() {
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("center:default:grow"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("center:pref:grow"),},
			new RowSpec[] {
				RowSpec.decode("pref:grow"),
				RowSpec.decode("pref:grow"),
				RowSpec.decode("pref:grow"),}));
		
		progressBar = new JProgressBar();
		add(progressBar, "2, 2, center, center");
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		BackgroundRender.instance().render((Graphics2D) g);
		
		FontMetrics fm = g.getFontMetrics();
		
		int stringWidth = fm.stringWidth(message);
		int lineHeight = fm.getHeight();
		
		g.drawString(message, getWidth()/2 - (stringWidth/2), progressBar.getY() - (lineHeight/2));
		
	}
	
	public void setProgress(double progress) {
		
		final double finalProgress = progress * 100d;
		
		DecimalFormat df = new DecimalFormat("###.##");
		
		message = "Loading... " + df.format(finalProgress) + "%";
		
		Util.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				progressBar.setValue((int) finalProgress);
			}
			
		});
		
	}
	
}
