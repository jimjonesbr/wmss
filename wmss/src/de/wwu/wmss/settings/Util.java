package de.wwu.wmss.settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.ErrorCodes;
import de.wwu.wmss.core.InvalidMelodyException;
import de.wwu.wmss.core.InvalidWMSSRequestException;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.Note;
import de.wwu.wmss.core.RequestParameter;
import de.wwu.wmss.core.WMSSRequest;

public class Util {
	
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
	
	public static DataSource getDataSource(WMSSRequest request) {
		
		DataSource dataSource = new DataSource();

		for (int i = 0; i < SystemSettings.sourceList.size(); i++) {
			if(SystemSettings.sourceList.get(i).getId().equals(request.getSource())){			
				dataSource = SystemSettings.sourceList.get(i); 
			}
		}
		
		return dataSource;
	}
	
	public static MusicScore getScoreRequestData(ArrayList<RequestParameter> parameters) {
		
		MusicScore result = new MusicScore();
		
		for (int i = 0; i < parameters.size(); i++) {
			
			if(parameters.get(i).getRequest().equals("identifier")){
				
				//System.out.println(">>>> " + parameters.get(i).getValue().indexOf(":"));
				int index = parameters.get(i).getValue().indexOf(":");
				
				result.setSource(parameters.get(i).getValue().substring(0, index));
				result.setScoreId(parameters.get(i).getValue().substring(index+1, parameters.get(i).getValue().length()));
				
//				result.setScoreId(parameters.get(i).getValue().split(":")[1]);
//				result.setSource(parameters.get(i).getValue().split(":")[0]);

			}

		}
		return result;
	}
	
	public static ArrayList<Note> createNoteSequence(String pea) throws InvalidWMSSRequestException{
		
		Set<String> notes = new HashSet();
		notes.add("C");notes.add("D");
		notes.add("E");notes.add("F");
		notes.add("G");notes.add("A");
		notes.add("B");notes.add("-");
		
		Set<String> accidentals = new HashSet();
		accidentals.add("x");accidentals.add("b");
		accidentals.add("n");
		
		String duration = "";
		String accidental = "";
		String octave = "";
		String lastDuration = "";
		boolean chord = false;
		
		ArrayList<Note> sequence = new ArrayList<Note>();
				
		for (int i = 0; i < pea.length(); i++) {

			String element = Character.toString(pea.charAt(i));
			
			if(element.equals(",")) {
				octave = octave + element;
			}
			
			if(element.equals("'")) {
				octave = octave + element;
			}
			
			if(StringUtils.isNumeric(element)) {
				duration = element;
				lastDuration = element;
			}
		
			if(accidentals.contains(element)) {
				accidental = accidental + element;
			}
			
			if(element.equals("^")) {
				chord = true;
			}
			
			if(notes.contains(element)) {

				if(octave.contains("'")) {					
					octave = String.valueOf(octave.length()+3);
				} else if(octave.contains(",")) {
					octave = String.valueOf(4-octave.length());
				} else if (octave.equals("")) {
					octave = "4";
				}

				if(duration.equals("")) {
					if(!lastDuration.equals("")) {
						duration = lastDuration;	
					} else {
						duration = "4";
					}					
				}
				
				if(element.equals("-")) {
					octave = "-";
				}
								
				Note note = new Note();
				note.setAccidental(accidental);
				note.setDuration(duration);
				note.setPitch(element);
				note.setOctave(octave);
				note.setChord(chord);
				
				//System.out.println("Pitch: " + note.getPitch() + "\n" + "Duration: " + note.getDuration() +"\n" + "Octave: " + note.getOctave() +"\n" + "Accidental: " + note.getAccidental() +"\n" + "Chord: " + note.isChord() + "\n");
				
				if(!note.getAccidental().equals("x")&&!note.getAccidental().equals("xx")&&
				   !note.getAccidental().equals("b")&&!note.getAccidental().equals("bb")&&
				   !note.getAccidental().equals("")) {
					throw new InvalidMelodyException("[PEA] Invalid accidental: " + note.getAccidental());
				}
				
				sequence.add(note);

				octave = "";
				duration = "";
				accidental = "";
				
				chord = false;
			}
			
		}
				
		if(sequence.size()<3) {
			throw new InvalidMelodyException(ErrorCodes.INVALID_MELODY_LENGTH_DESCRIPTION +" ["+pea+"]",ErrorCodes.INVALID_MELODY_LENGTH_CODE,ErrorCodes.INVALID_MELODY_LENGTH_HINT);
		}
		
		return sequence;
	}

}
