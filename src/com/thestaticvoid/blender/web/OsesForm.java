package com.thestaticvoid.blender.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thestaticvoid.blender.domain.Os;

public class OsesForm {
	private List<String> oses;
	private Map<String, String> availableOses;
	
	public OsesForm() {
		oses = new ArrayList<String>();
		availableOses = new HashMap<String, String>();
	}

	public List<String> getOses() {
		return oses;
	}
	
	public void setOses(List<String> oses) {
		this.oses = oses;
	}

	public void addOs(String slug) {
		oses.add(slug);
	}

	public Map<String, String> getAvailableOses() {
		return availableOses;
	}
	
	public void setAvailableOses(List<Os> oses) {
		for (Os os : oses)
			availableOses.put(os.getSlug(), os.getName());
	}
}
