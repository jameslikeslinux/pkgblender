package com.thestaticvoid.blender.elf;

import com.sun.jna.Structure;

public class HdrInfo extends Structure {
	public int type, bits, arch, data, osabi;
}
