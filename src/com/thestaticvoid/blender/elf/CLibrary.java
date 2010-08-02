package com.thestaticvoid.blender.elf;

import com.sun.jna.Native;

public class CLibrary {
	static {
		Native.register("c");
	}
	
	public static native int open(String file, int mode);
	public static native int close(int fd);
}
