package com.thestaticvoid.blender.web;

import java.util.HashMap;
import java.util.Map;

import com.thestaticvoid.blender.domain.Architecture;

public class BuildHostForm {
	private String hostname;
	private Architecture architecture;
		
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Architecture getArchitecture() {
		return architecture;
	}

	public void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}

	public Map<String, String> getArchitectures() {
		Map<String, String> architectures = new HashMap<String, String>();
		
		for (Architecture architecture : Architecture.values())
			architectures.put(architecture.name(), architecture.toString());
		
		return architectures;
	}
}
