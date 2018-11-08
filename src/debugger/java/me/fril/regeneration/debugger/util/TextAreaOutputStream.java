package me.fril.regeneration.debugger.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class TextAreaOutputStream extends OutputStream {
	private JTextArea textArea;
	
	public TextAreaOutputStream(JTextArea textArea) {
		this.textArea = textArea;
	}
	
	@Override
	public void write(int b) throws IOException {
		textArea.append(String.valueOf((char) b));
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
}
