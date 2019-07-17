package de.wwu.wmss.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RiotException;
import org.apache.log4j.Logger;
import de.wwu.wmss.core.ErrorCodes;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.WMSSImportRecord;
import de.wwu.wmss.core.WMSSImportRequest;
import de.wwu.wmss.engine.DocumentBuilder;
import de.wwu.wmss.engine.Neo4jEngine;
import de.wwu.wmss.exceptions.InvalidWMSSRequestException;
import de.wwu.wmss.exceptions.ScoreExistsException;
import de.wwu.wmss.settings.SystemSettings;

public class ServletImport extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("ServletImport");

	protected void doPost(HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException, IOException{

		ServletFileUpload sf = new ServletFileUpload(new DiskFileItemFactory());
		File uploadDiretory = new File(SystemSettings.getImportdDirectory());

		if (!uploadDiretory.exists()) {
			uploadDiretory.mkdirs();			
		}
		
		try {

			WMSSImportRequest importRequest = new WMSSImportRequest(httpRequest);

			logger.info("POST Request -> " + httpRequest.getQueryString());

			response.addHeader("Access-Control-Allow-Origin","*");
			response.addHeader("Access-Control-Allow-Methods","GET,POST");
			response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");

			List<FileItem> multifiles = sf.parseRequest(httpRequest);			
			ArrayList<WMSSImportRecord> fileList = new ArrayList<WMSSImportRecord>();
			Neo4jEngine.prepareDatabase(importRequest);
			
			for(FileItem item : multifiles) {

				File file = new File(uploadDiretory.getAbsolutePath()+"/"+item.getName());
				logger.debug("Uploaded: " + uploadDiretory.getAbsolutePath()+"/"+item.getName() );
				item.write(file);	
				
				isFileValid(file,importRequest);
				
				logger.debug("Checking file integrity of "+item.getName()+" [" + FileUtils.byteCountToDisplaySize(item.getSize())  + "] ...");
		
				WMSSImportRecord record = new WMSSImportRecord();
				record.setFile(file.getName());
				record.setSize(FileUtils.byteCountToDisplaySize(item.getSize()));
				record.setRecords(Neo4jEngine.insertScore(file, importRequest));
				fileList.add(record);				
							
			}
			
			Neo4jEngine.formatGraph(importRequest);

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(DocumentBuilder.getImportReport(fileList, importRequest));

		} catch (InvalidWMSSRequestException e) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(DocumentBuilder.getServiceExceptionReport(e.getCode(), e.getMessage(), e.getHint()));
			e.printStackTrace();

		} catch (ScoreExistsException e) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(DocumentBuilder.getServiceExceptionReport(ErrorCodes.SCORE_EXISTS_CODE, ErrorCodes.SCORE_EXISTS_DESCRIPTION + ". Import aborted!", e.getMessage()));
			e.printStackTrace();
		
		} catch (RiotException e) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(DocumentBuilder.getServiceExceptionReport(ErrorCodes.INVALID_RDFFILE_CODE, ErrorCodes.INVALID_RDFFILE_DESCRIPTION + ": " +e.getMessage(), ErrorCodes.INVALID_RDFFILE_HINT));
			e.printStackTrace();
				
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
		
	private static void isFileValid(File file, WMSSImportRequest importRequest) throws Exception {

		logger.info(("Checking file: " + file.getAbsolutePath() + " .. "));
		
		Model model = ModelFactory.createDefaultModel() ;		
		model.read(file.getAbsolutePath()) ;
		
		String sparql = "PREFIX mo: <http://purl.org/ontology/mo/> \n"
				+ "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n"
				+ "SELECT ?uri ?title {?uri a mo:Score . ?uri dc:title ?title . }";
 
		QueryExecution qexec = QueryExecutionFactory.create(sparql, model); 
		ResultSet results = qexec.execSelect() ;
		
		for ( ; results.hasNext() ; )
		{
			String titleFile = "";
			String uriFile = "";

			QuerySolution soln = results.nextSolution() ;
			uriFile = soln.get("?uri").toString();
			titleFile = soln.get("?title").toString();

			ArrayList<MusicScore> scores = Neo4jEngine.scoreExists(uriFile, importRequest);
			
			for (int i = 0; i < scores.size(); i++) {

				logger.info("New Score: " + uriFile + " - " + scores.get(i).getTitle());

				if(uriFile.equals(scores.get(i).getScoreId())) {
					throw new ScoreExistsException("The provided RDF file '"+ file.getName() +", containing the music score [Identifier: '"+uriFile+"', Title: '"+ titleFile + "'], has a conflicting score in the database: [Identifier: '" + scores.get(i).getScoreId() + "', Title: '" + scores.get(i).getTitle()+"']. Either delete the existing score from the database or provide a different identifier for the new one.");
				}
			}

		}


		model.close();			

	}
}

