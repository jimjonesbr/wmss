package de.wwu.wmss.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.log4j.Logger;

import de.wwu.wmss.core.ErrorCodes;
import de.wwu.wmss.core.InvalidWMSSRequestException;
import de.wwu.wmss.core.WMSSImportRequest;
import de.wwu.wmss.factory.ServiceMessagingHandler;

public class ServletImport extends HttpServlet {

	/**
	 * @author Jim Jones
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("ServletImport");

	protected void doPost(HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException, IOException{

		ServletFileUpload sf = new ServletFileUpload(new DiskFileItemFactory());
								
		try {
			
			WMSSImportRequest importRequest = new WMSSImportRequest(httpRequest);

			logger.info("POST Request -> " + httpRequest.getQueryString());

			response.addHeader("Access-Control-Allow-Origin","*");
			response.addHeader("Access-Control-Allow-Methods","GET,POST");
			response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
			
			List<FileItem> multifiles = sf.parseRequest(httpRequest);
			
			for(FileItem item : multifiles) {
				
				item.write(new File("upload/"+item.getName()));	
				logger.info("Checking file integrity ["+item.getName()+"] ...");
				
				if(checkFile("upload/"+item.getName())) {
					
					logger.info("File Uploaded: " + item.getName() + " ["+FileUtils.byteCountToDisplaySize(item.getSize())+"]");
				
				} else {
					
					response.setContentType("text/javascript");
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport(ErrorCodes.INVALID_RDFFILE_CODE, ErrorCodes.INVALID_RDFFILE_DESCRIPTION +" ["+item.getName()+"]", ErrorCodes.INVALID_RDFFILE_HINT));

				}
			}


		} catch (InvalidWMSSRequestException e) {
			
			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport(e.getCode(), e.getMessage(), e.getHint()));
			e.printStackTrace();
			
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean checkFile(String filePath) {

		boolean result = true;

		try {
			
			Model model = ModelFactory.createDefaultModel() ;
			model.read(filePath) ;		
			
		} catch (Exception e) {
			//e.printStackTrace();
			result = false;
		}

		return result;
	}
}

