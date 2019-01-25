package me.fril.regeneration.debugger.util;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class TextPaneLogger {
	private final JTextPane pane;
	
	public TextPaneLogger(JTextPane pane) {
		this.pane = pane;
	}
	
	public void println(String str, Color fg, Color bg) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, fg);
		aset = sc.addAttribute(aset, StyleConstants.Background, bg);
		
		try {
			pane.getDocument().insertString(pane.getDocument().getLength(), str + "\n", aset);
		} catch (BadLocationException e) {
			throw new RuntimeException("Logger crashed", e);
		}
	}
	
}
