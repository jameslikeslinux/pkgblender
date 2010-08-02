package com.thestaticvoid.blender.web;

import java.io.IOException;
import java.io.PipedInputStream;

public class PipedInputStreamReader extends Thread {
	private PipedInputStream input;
	private int size;
	
	public PipedInputStreamReader(PipedInputStream input) {
		this.input = input;
		size = 0;
	}
	
	public void run() {
		byte[] buffer = new byte[1024];
		int read;
		
		try {
			while ((read = input.read(buffer)) > -1)
				size += read;
		} catch (IOException io) {
			size = -1;
			return;
		}
	}
	
	public int getSize() {
		return size;
	}
}
