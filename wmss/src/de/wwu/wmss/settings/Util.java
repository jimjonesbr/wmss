package de.wwu.wmss.settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.RequestParameter;

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

	public static DataSource getDataSource(ArrayList<RequestParameter> parameters) {
		
		String dataSourceId = "";
		DataSource dataSource = new DataSource();
		
		for (int i = 0; i < parameters.size(); i++) {
//			if(parameters.get(i).getRequest().equals("identifier")){			
//				dataSourceId = parameters.get(i).getValue().split(":")[0];				
//			}
			if(parameters.get(i).getRequest().equals("source")){			
				dataSourceId = parameters.get(i).getValue();				
			}
			
		}

		for (int i = 0; i < SystemSettings.sourceList.size(); i++) {
			if(SystemSettings.sourceList.get(i).getId().equals(dataSourceId)){			
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
	
    private static final char[] hexChar = {
            '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
        };
    
    public static String unicodeEscape(String s) {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < s.length(); i++) {
	    char c = s.charAt(i);
	    if ((c >> 7) > 0) {
		sb.append("\\u");
		sb.append(hexChar[(c >> 12) & 0xF]); 
		sb.append(hexChar[(c >> 8) & 0xF]);  
		sb.append(hexChar[(c >> 4) & 0xF]);  
		sb.append(hexChar[c & 0xF]); 
	    }
	    else {
		sb.append(c);
	    }
	}
	return sb.toString();
    }
}
