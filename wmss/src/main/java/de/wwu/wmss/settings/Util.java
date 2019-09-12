package de.wwu.wmss.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.ErrorCodes;
import de.wwu.wmss.core.Note;
import de.wwu.wmss.exceptions.InvalidClefException;
import de.wwu.wmss.exceptions.InvalidKeyException;
import de.wwu.wmss.exceptions.InvalidMelodyException;
import de.wwu.wmss.exceptions.InvalidTimeSignatureException;
import de.wwu.wmss.exceptions.InvalidWMSSRequestException;

public class Util {

	public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	public static String loadFileTail( File file, int lines) {
		java.io.RandomAccessFile fileHandler = null;
		try {
			fileHandler = 
					new java.io.RandomAccessFile( file, "r" );
			long fileLength = fileHandler.length() - 1;
			StringBuilder sb = new StringBuilder();
			int line = 0;

			for(long filePointer = fileLength; filePointer != -1; filePointer--){
				fileHandler.seek( filePointer );
				int readByte = fileHandler.readByte();

				if( readByte == 0xA ) {
					if (filePointer < fileLength) {
						line = line + 1;
					}
				} else if( readByte == 0xD ) {
					if (filePointer < fileLength-1) {
						line = line + 1;
					}
				}
				if (line >= lines) {
					break;
				}
				sb.append( ( char ) readByte );
			}

			String lastLine = sb.reverse().toString();
			return lastLine;
		} catch( java.io.FileNotFoundException e ) {
			e.printStackTrace();
			return null;
		} catch( java.io.IOException e ) {
			e.printStackTrace();
			return null;
		}
		finally {
			if (fileHandler != null )
				try {
					fileHandler.close();
				} catch (IOException e) {
				}
		}
	}

	public static String timeElapsed(Date startDate, Date endDate){

		long different = endDate.getTime() - startDate.getTime();
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;
		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;
		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;
		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;
		long elapsedSeconds = different / secondsInMilli;		
		long elapsedMilliseconds = different % secondsInMilli;
		String result = "";

		if (elapsedDays == 0 && elapsedHours != 0) {
			result = elapsedHours + " hours, " + elapsedMinutes + " minutes, " + elapsedSeconds + "." + elapsedMilliseconds+ " seconds";
		} else if (elapsedHours == 0 && elapsedMinutes != 0) {
			result = elapsedMinutes + " minutes, " + elapsedSeconds + "." + elapsedMilliseconds + " seconds";
		} else if (elapsedMinutes == 0 && elapsedSeconds != 0) {
			result = elapsedSeconds + "." + elapsedMilliseconds + " seconds";
		} else if (elapsedSeconds == 0) {			
			result = elapsedMilliseconds + " ms";
		}
		return result; 

	}

	public static DataSource getDataSource(String request) {

		DataSource dataSource = new DataSource();

		for (int i = 0; i < SystemSettings.sourceList.size(); i++) {
			if(SystemSettings.sourceList.get(i).getId().equals(request)){			
				dataSource = SystemSettings.sourceList.get(i); 
			}
		}

		return dataSource;
	}

	public static ArrayList<Note> createNoteSequence(String pea) throws InvalidWMSSRequestException, InvalidKeyException{

		@SuppressWarnings({ "unchecked", "rawtypes" })
		Set<String> notes = new HashSet();
		notes.add("C");notes.add("D");
		notes.add("E");notes.add("F");
		notes.add("G");notes.add("A");
		notes.add("B");notes.add("-");

		@SuppressWarnings({ "rawtypes", "unchecked" })
		Set<String> accidentals = new HashSet();
		accidentals.add("x");accidentals.add("b");
		accidentals.add("n");

		String duration = "";
		String accidental = "";
		String currentOctave = "4";		
		String currentDuration = "";
		String currentKey = "";
		String currentClef = "";
		String currentTimeSignature = "";
		int currentMeasure = 1;		
		boolean chord = false;
		boolean grace = false;
		boolean keySegment = false;
		boolean timeSegment = false;
		boolean clefSegment = false;
		int dots = 0;

		ArrayList<Note> sequence = new ArrayList<Note>();

		for (int i = 0; i < pea.length(); i++) {

			String element = Character.toString(pea.charAt(i));
			
			if(element.equals("$")) {
				keySegment = true;
				timeSegment = false;
				clefSegment = false;
			} else if(element.equals("@")) {
				timeSegment = true;
				keySegment = false;
				clefSegment = false;
			} else if(element.equals(" ")) {
				keySegment = false;
				clefSegment = false;
				timeSegment = false;
			} else if(element.equals("%")) {
				clefSegment = true;
				keySegment = false;
				timeSegment = false;
			}

			if(timeSegment) {

				currentTimeSignature = currentTimeSignature + element;

			} else if(keySegment) {

				currentKey = currentKey + element;

			} else if(clefSegment) {

				currentClef = currentClef + element;

			} else { 

				if(element.equals(",")) {
					currentOctave = currentOctave + element;
				} 

				if(element.equals("'")) {
					currentOctave = currentOctave + element;
				}
				
				if(element.equals(".")) {
					dots++;
				}				

				if(StringUtils.isNumeric(element)) {
					duration = element;
					currentDuration = element;
				}

				if(accidentals.contains(element)) {
					accidental = accidental + element;
				}

				if(element.equals("^")) {
					chord = true;
				}
				
				if(element.equals("g")||element.equals("q")) {
					grace = true;
				}
				
				if(element.equals("/")) {
					currentMeasure++;
				}
				

				if(notes.contains(element)) {

					if(currentOctave.contains("'")) {					
						currentOctave = String.valueOf(currentOctave.length()+2);
					} else if(currentOctave.contains(",")) {
						currentOctave = String.valueOf(5-currentOctave.length());
					} 

					if(duration.equals("")) {
						if(!currentDuration.equals("")) {
							duration = currentDuration;	
						} else {
							duration = "4";
						}					
					}

					if(element.equals("-")) {
						//currentOctave = "-";
					}

					Note note = new Note();
					note.setAccidental(accidental);
					note.setDuration(duration);
					note.setPitch(element);
					note.setOctave(currentOctave);
					note.setChord(chord);			
					note.setGrace(grace);
					note.setMeasure(currentMeasure);
					note.setDotted(dots);
					
					if(!currentClef.equals("")) {
						note.setClef(formatPEAclef(currentClef));
						currentClef = "";						
					}

					if(!currentTimeSignature.equals("")) {
						note.setTime(formatPEAtimeSignature(currentTimeSignature));
						currentTimeSignature = "";
					}

					if(!currentKey.equals("")) {
						note.setKey(formatPEAkey(currentKey));		
						currentKey = "";
					} else {
						note.setKey("");
					}

					if(!note.getAccidental().equals("x")&&!note.getAccidental().equals("xx")&&
							!note.getAccidental().equals("b")&&!note.getAccidental().equals("bb")&&
							!note.getAccidental().equals("")) {
						throw new InvalidMelodyException(ErrorCodes.INVALID_MELODY_LENGTH_DESCRIPTION +" ["+pea+"]",ErrorCodes.INVALID_MELODY_LENGTH_CODE,"Invalid accidental!");
					}

					sequence.add(note);
					duration = "";
					accidental = "";
					dots = 0;
					chord = false;
					grace = false;
				}

			}

		}

		
		for (int i = 0; i < sequence.size(); i++) {
			if(sequence.get(i).isChord()) {
				sequence.get(i-1).setChord(true);
			}
		}
		
		if(sequence.size()<3) {
			throw new InvalidMelodyException(ErrorCodes.INVALID_MELODY_LENGTH_DESCRIPTION +" ["+pea+"]",ErrorCodes.INVALID_MELODY_LENGTH_CODE,ErrorCodes.INVALID_MELODY_LENGTH_HINT);
		}

		return sequence;
	}

	public static String formatPEAtimeSignature(String time) throws InvalidTimeSignatureException {

		time = time.replace("@", "");

		if(!time.matches("^[0-9/c]+$")) {
			throw new InvalidTimeSignatureException(ErrorCodes.INVALID_TIMESIGNATURE_DESCRIPTION +" ["+time+"]",ErrorCodes.INVALID_TIMESIGNATURE_CODE,ErrorCodes.INVALID_TIMESIGNATURE_HINT);	
		}

		if(time.equals("c")) {
			time = "4/4";
		}	

		return time;
	}

	public static String formatPEAkey(String key) throws InvalidKeyException {
		
		if(key.length()>1) {
			key = key.replace("$", "");
		}
		
		ArrayList<String> keys = new ArrayList<>();
		keys.add("$");
		keys.add("xF");keys.add("xFC");keys.add("xFCG");keys.add("xFCGD");keys.add("xFCGDA");keys.add("xFCGDAE");keys.add("xFCGDAEB");
		keys.add("F");keys.add("FC");keys.add("FCG");keys.add("FCGD");keys.add("FCGDA");keys.add("FCGDAE");keys.add("FCGDAEB");
		keys.add("bB");keys.add("bBE");keys.add("bBEA");keys.add("bBEAD");keys.add("bBEADG");keys.add("bBEADGC");keys.add("bBEADGCF");

		if(!keys.contains(key)) {
			throw new InvalidKeyException(ErrorCodes.INVALID_KEY_DESCRIPTION +" ["+key+"]",ErrorCodes.INVALID_KEY_CODE,ErrorCodes.INVALID_KEY_HINT);
		}
		
		if(key.equals("xF")) {
			
		}
			



		return key;
	}

	public static String formatPEAclef(String clef) throws InvalidClefException {
		
		clef = clef.replace("%", "");
		
		ArrayList<String> validShapes = new ArrayList<>();
		validShapes.add("C");
		validShapes.add("G");
		validShapes.add("g");
		validShapes.add("F");
		
		ArrayList<String> validConnectors = new ArrayList<>();
		validConnectors.add("-");
		validConnectors.add("+");
		
		if(clef.length()!=3 ||
		   !validShapes.contains(String.valueOf(clef.charAt(0))) ||
		   !validConnectors.contains(String.valueOf(clef.charAt(1))) ||
		   !StringUtils.isNumeric(String.valueOf(clef.charAt(2))) ||
		   !String.valueOf(clef.charAt(2)).matches("[1-5]")) {
			
			throw new InvalidClefException(ErrorCodes.INVALID_CLEF_DESCRIPTION +" ["+clef+"]",ErrorCodes.INVALID_CLEF_CODE,ErrorCodes.INVALID_CLEF_HINT);
		}
		
		
		return clef;
	}
		
	public static String capitalizeFirstLetter(String string){

		if(string != null && string != "" ) {
			string = string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
		}

		return string;

	}
	
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
	
	public static boolean postMusicXMLMetadataFile(String server, int port, String source, File file, int commitSize, File metadata, String format) {

		boolean result = false;

		HttpClient httpClient = HttpClientBuilder.create().build();

		try { 

			
			HttpEntity requestEntity = MultipartEntityBuilder.create().addBinaryBody("file", file).addBinaryBody("metadata", metadata).build();
			HttpPost post = new HttpPost(server+":"+port+"/wmss/import?source="+source+"&format="+format+"&commitSize="+commitSize);
			post.setEntity(requestEntity);
			
			HttpResponse response=null;

			response = httpClient.execute(post);

			HttpEntity entity = response.getEntity(); 		
			JSONObject json = new JSONObject(IOUtils.toString(entity.getContent(),"utf-8"));
			
			String time = json.get("timeElapsed").toString();
			JSONArray fileArray = (JSONArray) json.get("files");
			JSONObject importedFile = (JSONObject) fileArray.get(0);
			
			String fileName = importedFile.get("file").toString();
			String fileSize = importedFile.get("size").toString();
			String records = importedFile.get("records").toString();
			
			System.out.println("\nFile   : "+fileName+ 
							   "\nSize   : "+fileSize+
							   "\nRecords: "+records+
							   "\nTime   : "+time  +"\n");
			
			if(!records.equals("0") && fileName.equals(file.getName())) {
				result = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return result;

	}

	public static String postListScoreRequest(String server, int port, String source, String jsonQuery) {
		
		String result = "";

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(server+":"+port+"/wmss/");
		
		try {
			StringEntity params = new StringEntity(jsonQuery.trim());
		    request.addHeader("content-type", "application/json");
		    request.setEntity(params);
		    
		    HttpResponse response = httpClient.execute(request);
		    
		    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		    StringBuilder builder = new StringBuilder();
		    for (String line = null; (line = reader.readLine()) != null;) {
		        builder.append(line).append("\n");
		        
		    }
		    
		    result = builder.toString();
		    System.out.println("ScoreListReport:\n " + result);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static boolean postMusicXMLMetadataString(String server, int port, String source, File file, int commitSize, String metadata, String format) {

		boolean result = false;

		HttpClient httpClient = HttpClientBuilder.create().build();

		try { 

			HttpEntity requestEntity = MultipartEntityBuilder.create().addBinaryBody("file", file).build();
			HttpPost post = new HttpPost(server+":"+port+"/wmss/import?source="+source+"&format="+format+"&commitSize="+commitSize+"&metadata="+URLEncoder.encode(metadata, "UTF-8"));
			post.setEntity(requestEntity);
			
			HttpResponse response=null;

			response = httpClient.execute(post);

			HttpEntity entity = response.getEntity(); 		
			JSONObject json = new JSONObject(IOUtils.toString(entity.getContent(),"utf-8"));
			
			String time = json.get("timeElapsed").toString();
			JSONArray fileArray = (JSONArray) json.get("files");
			JSONObject importedFile = (JSONObject) fileArray.get(0);
			
			String fileName = importedFile.get("file").toString();
			String fileSize = importedFile.get("size").toString();
			String records = importedFile.get("records").toString();
			
			System.out.println("\nFile   : "+fileName+ 
							   "\nSize   : "+fileSize+
							   "\nRecords: "+records+
							   "\nTime   : "+time  +"\n");
			
			if(!records.equals("0") && fileName.equals(file.getName())) {
				result = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return result;

	}

}
