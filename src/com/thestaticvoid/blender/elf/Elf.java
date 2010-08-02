package com.thestaticvoid.blender.elf;

import com.sun.jna.Native;
import com.thestaticvoid.blender.service.Utils;

public class Elf {
	static {
		Native.register("/usr/lib/python2.6/vendor-packages/pkg/elf.so");
	}

	private static native int iself(int fd);
	private static native HdrInfo getheaderinfo(int fd);
	private static native DynInfo getdynamic(int fd);
	private static native void dyninfo_free(DynInfo dynInfo);
	
	public enum Architecture { 
		NONE("none"), SPARC("sparc"), I386("i386"), OTHER("other");
		
		private String elfarch;
		
		private Architecture(String elfarch) {
			this.elfarch = elfarch;
		}
		
		public static Architecture getByNum(int arch) {
			switch (arch) {
				case 0:
					return NONE;
				
				case 2:
				case 18:
				case 43:
					return SPARC;
				
				case 3:
				case 62:
					return I386;
				
				default:
					return OTHER;
			}
		}
		
		public String toString() {
			return elfarch;
		}
	}
	
	private boolean isElf;
	private int bits;
	private Architecture architecture;
	private String hash;
	
	public Elf(String file) {
		int fd = CLibrary.open(file, 0);
		
		isElf = iself(fd) == 1;
		if (isElf) {
			HdrInfo hdrInfo = getheaderinfo(fd);
			bits = hdrInfo.bits;
			architecture = Architecture.getByNum(hdrInfo.arch);
			
			DynInfo dynInfo = getdynamic(fd);
			hash = Utils.byteArrayToHexString(dynInfo.hash);
			dyninfo_free(dynInfo);
		}
		
		CLibrary.close(fd);
	}
	
	public boolean isElf() {
		return isElf;
	}
	
	public int getBits() {
		return bits;
	}
	
	public Architecture getArchitecture() {
		return architecture;
	}
	
	public String getHash() {
		return hash;
	}
}
