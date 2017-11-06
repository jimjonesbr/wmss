package de.wwu.wmss.settings;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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
}
