package de.wwu.wmss.web;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.InvalidContentTypeException;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.wwu.music2rdf.converter.MusicXML2RDF;
import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Person;
import de.wwu.music2rdf.core.ScoreResource;
import de.wwu.wmss.core.ErrorCodes;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.WMSSImportRecord;
import de.wwu.wmss.core.WMSSImportRequest;
import de.wwu.wmss.engine.DocumentBuilder;
import de.wwu.wmss.engine.Neo4jEngine;
import de.wwu.wmss.exceptions.DatabaseImportException;
import de.wwu.wmss.exceptions.InvalidWMSSRequestException;
import de.wwu.wmss.exceptions.ScoreExistsException;
import de.wwu.wmss.settings.SystemSettings;

public class ServletImport extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("ServletImport");

	protected void doPost(HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException, IOException{

		File uploadDiretory = new File(SystemSettings.getImportdDirectory());

		try {

			WMSSImportRequest importRequest = new WMSSImportRequest(httpRequest);		
			Neo4jEngine.prepareDatabase(importRequest);

			logger.info("POST Request -> " + httpRequest.getQueryString());

			response.addHeader("Access-Control-Allow-Origin","*");
			response.addHeader("Access-Control-Allow-Methods","GET,POST");
			response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
			ArrayList<WMSSImportRecord> fileList = new ArrayList<WMSSImportRecord>();

			if(httpRequest.getParameter("url")!=null) {

				isFileValid(new URL(httpRequest.getParameter("url")).toString(),importRequest);

				WMSSImportRecord record = new WMSSImportRecord();
				record.setFile(httpRequest.getParameter("url"));
				record.setRecords(Neo4jEngine.insertScoreURL(httpRequest.getParameter("url"), importRequest));
				fileList.add(record);

			} else {


				if(importRequest.getFormat().equals("musicxml")) {

					MusicXML2RDF converter = new MusicXML2RDF();
					converter = this.parseMetadata(importRequest.getMetadata());	
					converter.setInputFile(importRequest.getScoreFile());
					converter.setOutputFile(uploadDiretory.getAbsolutePath()+"/"+importRequest.getScoreFile().getName()+"-musicxml2rdf");
					converter.setOutputFormat("turtle");
					converter.parseMusicXML();			

					this.isFileValid(uploadDiretory.getAbsolutePath()+"/"+importRequest.getScoreFile().getName()+"-musicxml2rdf.ttl",importRequest);

					WMSSImportRecord record = new WMSSImportRecord();
					record.setFile(importRequest.getScoreFile().getName());
					record.setSize(FileUtils.byteCountToDisplaySize(importRequest.getScoreFile().length()));
					importRequest.setFormat("Turtle");
					record.setRecords(Neo4jEngine.insertScore(new File(importRequest.getScoreFile().getAbsolutePath()+"-musicxml2rdf.ttl"), importRequest));
					fileList.add(record);					

				} else {

					this.isFileValid(uploadDiretory.getAbsolutePath()+"/"+importRequest.getScoreFile().getName(),importRequest);

					logger.debug("Checking file integrity of "+importRequest.getScoreFile().getName()+" [" + FileUtils.byteCountToDisplaySize(importRequest.getScoreFile().length())  + "] ...");

					WMSSImportRecord record = new WMSSImportRecord();
					record.setFile(importRequest.getScoreFile().getName());
					record.setSize(FileUtils.byteCountToDisplaySize(importRequest.getScoreFile().length()));
					record.setRecords(Neo4jEngine.insertScore(importRequest.getScoreFile(), importRequest));
					fileList.add(record);				


				}

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
			response.getWriter().println(DocumentBuilder.getServiceExceptionReport(ErrorCodes.SCORE_EXISTS_CODE, ErrorCodes.SCORE_EXISTS_DESCRIPTION, e.getMessage()));
			e.printStackTrace();

		} catch (RiotException e) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(DocumentBuilder.getServiceExceptionReport(ErrorCodes.INVALID_RDFFILE_CODE, ErrorCodes.INVALID_RDFFILE_DESCRIPTION + ": " +e.getMessage(), ErrorCodes.INVALID_RDFFILE_HINT));
			e.printStackTrace();

		} catch (DatabaseImportException e) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(DocumentBuilder.getServiceExceptionReport(e.getCode(), e.getMessage(), e.getHint()));
			e.printStackTrace();
		} catch (InvalidContentTypeException e) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(DocumentBuilder.getServiceExceptionReport(ErrorCodes.INVALID_CONTENT_TYPE_CODE, ErrorCodes.INVALID_CONTENT_TYPE_DESCRIPTION+ ": " +e.getMessage(),ErrorCodes.INVALID_CONTENT_TYPE_HINT));
			e.printStackTrace();

		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace() ;
		} 

	}

	private MusicXML2RDF parseMetadata(String fXmlFile) {

		MusicXML2RDF music2rdf = new MusicXML2RDF();

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(fXmlFile));
			Document doc = builder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("score");

			for (int j = 0; j < nList.getLength(); j++) {

				Node nNode = nList.item(j);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element scoreElement = (Element) nNode;

					System.out.println("Score URI       : " + scoreElement.getElementsByTagName("scoreIdentifier").item(0).getTextContent());
					System.out.println("Title           : " + scoreElement.getElementsByTagName("title").item(0).getTextContent());
					System.out.println("Date Issued     : " + scoreElement.getElementsByTagName("issued").item(0).getTextContent());
					System.out.println("Thumbnail       : " + scoreElement.getElementsByTagName("thumbnail").item(0).getTextContent());

					music2rdf.setScoreURI(scoreElement.getElementsByTagName("scoreIdentifier").item(0).getTextContent());
					music2rdf.setDocumentTitle(scoreElement.getElementsByTagName("title").item(0).getTextContent());
					music2rdf.setDateIssued(scoreElement.getElementsByTagName("issued").item(0).getTextContent());
					music2rdf.setThumbnail(scoreElement.getElementsByTagName("thumbnail").item(0).getTextContent());

					Element personsElement = (Element) scoreElement.getElementsByTagName("persons").item(0);

					NodeList personList = personsElement.getElementsByTagName("person");

					for (int k = 0; k < personList.getLength(); k++) {

						Node personNode = personList.item(k);

						if (personNode.getNodeType() == Node.ELEMENT_NODE) {

							Element personElement = (Element) personNode;

							System.out.println("Person URI      : " + personElement.getElementsByTagName("personIdentifier").item(0).getTextContent());
							System.out.println("Person Name     : " + personElement.getElementsByTagName("personName").item(0).getTextContent());
							System.out.println("Person Role     : " + personElement.getElementsByTagName("personRole").item(0).getTextContent());

							String personURI = personElement.getElementsByTagName("personIdentifier").item(0).getTextContent();
							String personName = personElement.getElementsByTagName("personName").item(0).getTextContent();
							String personRole = personElement.getElementsByTagName("personRole").item(0).getTextContent();

							music2rdf.addPerson(new Person(personURI,personName,personRole));

						}
					}


					Element resourcesElement = (Element) scoreElement.getElementsByTagName("resources").item(0);

					NodeList resourceList = resourcesElement.getElementsByTagName("resource");

					for (int k = 0; k < resourceList.getLength(); k++) {

						Node resourceNode = resourceList.item(k);

						if (resourceNode.getNodeType() == Node.ELEMENT_NODE) {

							Element personElement = (Element) resourceNode;

							System.out.println("Resource URI    : " + personElement.getElementsByTagName("resourceURL").item(0).getTextContent());
							System.out.println("Resource Desc.  : " + personElement.getElementsByTagName("resourceDescription").item(0).getTextContent());
							System.out.println("Resource Type   : " + personElement.getElementsByTagName("resourceType").item(0).getTextContent());

							String resourceURI = personElement.getElementsByTagName("resourceURL").item(0).getTextContent();
							String resourceDescription = personElement.getElementsByTagName("resourceDescription").item(0).getTextContent();
							String resourceType = personElement.getElementsByTagName("resourceType").item(0).getTextContent();

							music2rdf.addResource(new ScoreResource(resourceURI, resourceDescription,resourceType));
						}
					}


					Element collectionsElement = (Element) scoreElement.getElementsByTagName("collections").item(0);

					NodeList collectionList = collectionsElement.getElementsByTagName("collection");

					for (int k = 0; k < collectionList.getLength(); k++) {

						Node collectionNode = collectionList.item(k);

						if (collectionNode.getNodeType() == Node.ELEMENT_NODE) {

							Element collectionElement = (Element) collectionNode;

							System.out.println("Collection URI  : " + collectionElement.getElementsByTagName("collectionURL").item(0).getTextContent());
							System.out.println("Collection Name : " + collectionElement.getElementsByTagName("collectionName").item(0).getTextContent());


							String collectionURL = collectionElement.getElementsByTagName("collectionURL").item(0).getTextContent();
							String collectionName = collectionElement.getElementsByTagName("collectionName").item(0).getTextContent();

							music2rdf.addCollection(new Collection(collectionURL,collectionName));
						}
					}

				}

			}


		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return music2rdf;
	}

	private void isFileValid(String file, WMSSImportRequest importRequest) throws Exception {

		Model model = ModelFactory.createDefaultModel() ;		

		if(file.toLowerCase().contains("http://") || file.toLowerCase().contains("ftp://") || file.toLowerCase().contains("https://")) {
			file = file.replace(" ", "%20");//Escaping spaces for URL
		}

		logger.info(("Checking RDF resource: " + file + " .. "));

		model.read(file);  

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
					throw new ScoreExistsException("The provided RDF file '"+ file +", containing the music score [Identifier: '"+uriFile+"', Title: '"+ titleFile + "'], has a conflicting score in the database: [Identifier: '" + scores.get(i).getScoreId() + "', Title: '" + scores.get(i).getTitle()+"']. Either delete the existing score from the database or provide a different identifier for the new one.");
				}
			}

		}


		model.close();			

	}
}

