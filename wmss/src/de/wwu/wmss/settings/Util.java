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
import de.wwu.wmss.core.InvalidKeyException;
import de.wwu.wmss.core.InvalidMelodyException;
import de.wwu.wmss.core.InvalidWMSSRequestException;
import de.wwu.wmss.core.Note;

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
		

		boolean chord = false;
		boolean keySegment = false;
		ArrayList<Note> sequence = new ArrayList<Note>();

		for (int i = 0; i < pea.length(); i++) {

			String element = Character.toString(pea.charAt(i));

			if(element.equals("$")) {
				keySegment = true;
			} else if(element.equals(" ") || element.equals("@")) {
				keySegment = false;
			}


			if(keySegment) {

				currentKey = currentKey + element;
				
			} else { 

				if(element.equals(",")) {
					currentOctave = currentOctave + element;
				} 

				if(element.equals("'")) {
					currentOctave = currentOctave + element;
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

				if(notes.contains(element)) {

					if(currentOctave.contains("'")) {					
						currentOctave = String.valueOf(currentOctave.length()+2);
					} else if(currentOctave.contains(",")) {
						currentOctave = String.valueOf(5-currentOctave.length());
					} 
//					else if (currentOctave.equals("")) {
//						currentOctave = "4";
//					}

					if(duration.equals("")) {
						if(!currentDuration.equals("")) {
							duration = currentDuration;	
						} else {
							duration = "4";
						}					
					}

					if(element.equals("-")) {
						currentOctave = "-";
					}

					Note note = new Note();
					note.setAccidental(accidental);
					note.setDuration(duration);
					note.setPitch(element);
					note.setOctave(currentOctave);
					note.setChord(chord);
					
					if(!currentKey.equals("")) {
						note.setKey(formatPEAkey(currentKey));
					} else {
						note.setKey("");
					}

					//System.out.println("Pitch: " + note.getPitch() + "\n" + "Duration: " + note.getDuration() +"\n" + "Octave: " + note.getOctave() +"\n" + "Accidental: " + note.getAccidental() +"\n" + "Chord: " + note.isChord() + "\n");

					if(!note.getAccidental().equals("x")&&!note.getAccidental().equals("xx")&&
							!note.getAccidental().equals("b")&&!note.getAccidental().equals("bb")&&
							!note.getAccidental().equals("")) {
						throw new InvalidMelodyException(ErrorCodes.INVALID_MELODY_LENGTH_DESCRIPTION +" ["+pea+"]",ErrorCodes.INVALID_MELODY_LENGTH_CODE,"Invalid accidental!");
					}

					sequence.add(note);

					//currentOctave = "";
					duration = "";
					accidental = "";

					chord = false;
				}

			}

		}

		if(sequence.size()<3) {
			throw new InvalidMelodyException(ErrorCodes.INVALID_MELODY_LENGTH_DESCRIPTION +" ["+pea+"]",ErrorCodes.INVALID_MELODY_LENGTH_CODE,ErrorCodes.INVALID_MELODY_LENGTH_HINT);
		}

		return sequence;
	}

	public static String formatPEAkey(String key) throws InvalidKeyException {

		String result = "";

		ArrayList<String> keys = new ArrayList<>();
		keys.add("$");
		keys.add("$xF");keys.add("$xFC");keys.add("$xFCG");keys.add("$xFCGD");keys.add("$xFCGDA");keys.add("$xFCGDAE");keys.add("$xFCGDAEB");
		keys.add("$F");keys.add("$FC");keys.add("$FCG");keys.add("$FCGD");keys.add("$FCGDA");keys.add("$FCGDAE");keys.add("$FCGDAEB");
		keys.add("$bB");keys.add("$bBE");keys.add("$bBEA");keys.add("$bBEAD");keys.add("$bBEADG");keys.add("$bBEADGC");keys.add("$bBEADGCF");
		boolean match = false;

		for (int i = 0; i < keys.size(); i++) {
			if(keys.get(i).equals(key)) {
				match = true;
			};
		}

		if(key.length()>1 && !Character.toString(key.charAt(1)).equals("x") && !Character.toString(key.charAt(1)).equals("b") ) {
			result = "$x"+ key.substring(1, key.length());			
		} else {
			result = key;
		}

		if(!match) {
			throw new InvalidKeyException(ErrorCodes.INVALID_KEY_DESCRIPTION +" ["+key+"]",ErrorCodes.INVALID_KEY_CODE,ErrorCodes.INVALID_KEY_HINT);
		}

		return result;
	}

	public static String capitalizeFirstLetter(String string){

		if(string != null && string != "" ) {
			string = string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
		}

		return string;

	}
}
