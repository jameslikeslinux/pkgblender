package com.thestaticvoid.blender.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.thestaticvoid.blender.domain.Architecture;
import com.thestaticvoid.blender.domain.Os;
import com.thestaticvoid.blender.service.AdministrationService;

public class BuildHostForm {	
	private String hostname;
	private Architecture architecture;
	private String os;
	private Map<String, String> oses;
		
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
	
	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}
	
	public void setOses(List<Os> oses) {
		this.oses = new HashMap<String, String>();
		
		for (Os os : oses)
			if (os.getStatus().equals(Os.Status.OK))
				this.oses.put(os.getSlug(), os.getName());
		
	}
	
	public Map<String, String> getOses() {
		return oses;
	}
}
