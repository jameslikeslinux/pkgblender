package com.thestaticvoid.blender.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thestaticvoid.blender.service.RepoService;
import com.thestaticvoid.blender.service.Utils;

@Controller
public class RepoController {
	private RepoService repoService;
	
	@Autowired
	public RepoController(RepoService repoService) {
		this.repoService = repoService;
	}
	
	@RequestMapping(value = "/repo/{repo}/open/0/{fmri}", method = RequestMethod.GET)
	public ResponseEntity<String> open0(@PathVariable String repo, @PathVariable String fmri) {		
		int transactionId = repoService.openTransaction(repo, fmri);
		
		if (transactionId == -1)
			return new ResponseEntity<String>("Could not open transaction.", HttpStatus.BAD_REQUEST);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Transaction-ID", "" + transactionId);
		return new ResponseEntity<String>("", responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/repo/{repo}/close/0/{foo}")
	public void close0(@PathVariable String repo, @PathVariable String foo) {
		Logger.getLogger(getClass()).info("FOO: " + foo);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/repo/{repo}/add/0/{transactionId}/{action}", method = RequestMethod.POST)
	public ResponseEntity<String> add0(@PathVariable String repo, @PathVariable String transactionId, @PathVariable String action, HttpServletRequest request) throws IOException, NoSuchAlgorithmException, InterruptedException {
		Set<String> attrs = new HashSet<String>();
		
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			if (headerName.toLowerCase().startsWith("x-ipkg-setattr")) {
				Enumeration values = request.getHeaders(headerName);
				while (values.hasMoreElements()) {
					String attr = (String) values.nextElement();
					Logger.getLogger(getClass()).info("ATTR: " + attr);
					attrs.add(attr);
				}
			}
		}
		
		String contentLength = request.getHeader("Content-Length");
		if (contentLength != null && Integer.parseInt(contentLength) > 0) {
			InputStream input = request.getInputStream();
			byte[] buffer = new byte[1024];
			int read;
			
			MessageDigest md = MessageDigest.getInstance("SHA");
			MessageDigest gzipMd = MessageDigest.getInstance("SHA");
			
			File file = Utils.getTmpFile();
			FileOutputStream output = new FileOutputStream(file);
			DigestOutputStream digestOutput = new DigestOutputStream(output, md);
			
			PipedInputStream gzipPipedInput = new PipedInputStream();
			PipedOutputStream gzipPipedOutput = new PipedOutputStream(gzipPipedInput);
			DigestOutputStream gzipDigestOutput = new DigestOutputStream(gzipPipedOutput, gzipMd);
			GZIPOutputStream gzipOutput = new GZIPOutputStream(gzipDigestOutput);			

			PipedInputStreamReader pipedInputReader = new PipedInputStreamReader(gzipPipedInput);
			pipedInputReader.start();
			
			while ((read = input.read(buffer)) > -1) {
				digestOutput.write(buffer, 0, read);
				gzipOutput.write(buffer, 0, read);
			}

			digestOutput.close();
			gzipOutput.close();
			
			pipedInputReader.join();
			
			String hash = Utils.byteArrayToHexString(md.digest());
			String chash = Utils.byteArrayToHexString(gzipMd.digest());
			Logger.getLogger(getClass()).info("HASH: " + hash);
			Logger.getLogger(getClass()).info("CHASH: " + chash);
			Logger.getLogger(getClass()).info("CSIZE: " + pipedInputReader.getSize());
			
			File destFile = repoService.hashToFile(hash);
			if (!destFile.exists()) {
				destFile.getParentFile().mkdirs();
				file.renameTo(destFile);
			}
		}
		
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
}
