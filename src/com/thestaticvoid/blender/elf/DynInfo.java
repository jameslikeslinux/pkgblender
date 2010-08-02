package com.thestaticvoid.blender.elf;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class DynInfo extends Structure {
	public NativeLong runpath, def, dynstr;
	public Pointer  deps, vers;
	public byte[] hash = new byte[20];
	public Pointer elf;
}
