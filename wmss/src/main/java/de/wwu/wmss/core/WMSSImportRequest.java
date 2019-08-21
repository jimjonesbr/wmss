package de.wwu.wmss.core;

import java.io.File;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import de.wwu.wmss.exceptions.InvalidWMSSRequestException;
import de.wwu.wmss.settings.SystemSettings;

public class WMSSImportRequest {

	private String format = SystemSettings.getDefaultRDFFormat();
	private String commitSize = SystemSettings.getDefaultCommitSize();
	private String source = "";
	private Date startDate = new Date();
	private String hostname = "";
	private String urlDataset = "";
	private String metadata = "";
	private File scoreFile;
	
	private static Logger logger = Logger.getLogger("ImportRequestParser");
	
	public WMSSImportRequest(HttpServletRequest httpRequest) throws InvalidWMSSRequestException{

		File uploadDiretory = new File(SystemSettings.getImportdDirectory());

		if (!uploadDiretory.exists()) {
			uploadDiretory.mkdirs();			
		}
		
		try {

			ServletFileUpload sf = new ServletFileUpload(new DiskFileItemFactory());
			List<FileItem> multifiles = sf.parseRequest(httpRequest);

			for(FileItem item : multifiles) {

				File file = new File(uploadDiretory.getAbsolutePath()+"/"+item.getName());
				logger.debug("Uploaded: " + uploadDiretory.getAbsolutePath()+"/"+item.getName());
				item.write(file);
				
				if(item.getFieldName().equals("metadata")) {
										
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(file);
					doc.getDocumentElement().normalize();

					TransformerFactory tf = TransformerFactory.newInstance();
					Transformer transformer = tf.newTransformer();
					transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
					StringWriter writer = new StringWriter();
					transformer.transform(new DOMSource(doc), new StreamResult(writer));
					this.metadata = writer.getBuffer().toString();
					
					logger.debug("Metadata File Content:\n\n"+this.metadata);
					
				} 
				if(item.getFieldName().equals("file")) {			
					this.scoreFile = file;
					logger.debug("Uploaded: " + uploadDiretory.getAbsolutePath()+"/"+this.scoreFile.getName());
				}
			}

		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}					
		
		
		Enumeration<String> listParameters = httpRequest.getParameterNames();
		
		while(listParameters.hasMoreElements() ) {

			String parameter = (String) listParameters.nextElement();
			
			if (parameter.toLowerCase().equals("format")) {			
				
				if(httpRequest.getParameter(parameter).toLowerCase().equals("n-triples")) {
					this.format = "N-Triples";
				} else if(httpRequest.getParameter(parameter).toLowerCase().equals("json-ld")) {
					this.format = "JSON-LD";
				} else if(httpRequest.getParameter(parameter).toLowerCase().equals("rdf/xml")) {
					this.format = "RDF/XML";
				} else if(httpRequest.getParameter(parameter).toLowerCase().equals("turtle")) {
					this.format = "Turtle";
				} else if(httpRequest.getParameter(parameter).toLowerCase().equals("trig")) {
					this.format = "TriG";
				} else if(httpRequest.getParameter(parameter).toLowerCase().equals("musicxml")) {
					this.format = "musicxml";					
				} else {
					throw new InvalidWMSSRequestException(ErrorCodes.INVALID_RDFFORMAT_DESCRIPTION+" ["+this.format+"]", ErrorCodes.INVALID_RDFFORMAT_CODE , ErrorCodes.INVALID_RDFFORMAT_HINT);		
				}
								
			}
			
			if (parameter.toLowerCase().equals("commitsize")) {							
				this.commitSize = httpRequest.getParameter(parameter);				
			}

			if (parameter.toLowerCase().equals("source")) {							
				this.source = httpRequest.getParameter(parameter);				
			}
			
			if (parameter.toLowerCase().equals("url")) {							
				this.urlDataset = httpRequest.getParameter(parameter);				
			}	
			
			if (parameter.toLowerCase().equals("metadata")) {							
				this.metadata = httpRequest.getParameter(parameter);				
			}	
													
			this.hostname = httpRequest.getServerName();				
			
		}
		
	}
	
	public String getFormat() {
		return format;
	}

	public String getCommitSize() {
		return commitSize;
	}

	public String getSource() {
		return source;
	}

	public Date getStartDate() {
		return startDate;
	}

	public String getHostname() {
		return hostname;
	}

	public String getUrlDataset() {
		return urlDataset;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getMetadata() {
		return metadata;
	}

	public File getScoreFile() {
		return scoreFile;
	}

	
}
