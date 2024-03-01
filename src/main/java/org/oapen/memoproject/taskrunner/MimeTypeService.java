package org.oapen.memoproject.taskrunner;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

@Component
public class MimeTypeService {
	
	private Tika tika = new Tika();
	
	public String getMimeTypeFromFileName(String fileName) {
		
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		
		switch(extension.toLowerCase()) {
			case "onix"		:
			case "marcxml"	: return "application/xml";
			case "mrc"		:	
			case "marc"		: return "application/marc";	
			case "ris"		: return "application/x-research-info-systems";
			case "kbart" 	: return "text/tab-separated-values";
			default			: return tika.detect(fileName); 
		}
	}
	
}
