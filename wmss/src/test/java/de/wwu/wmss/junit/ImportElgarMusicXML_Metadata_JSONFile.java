package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import org.junit.Test;
import de.wwu.wmss.settings.Util;

public class ImportElgarMusicXML_Metadata_JSONFile {
		
	@Test
	public void importMusicXML() {
			
		boolean result = true;				
		URL url = this.getClass().getResource("/musicxml");		
				
		try {
			
			File musicxml = new File(Paths.get(url.toURI()).toString()+"/elgar_cello_concerto_op.85.xml");
			File metadata = new File(Paths.get(url.toURI()).toString()+"/elgar_cello_concerto_op.85-metadata.json");
			result = Util.postMusicXMLMetadataFile(StartWMSS.server, StartWMSS.port, StartWMSS.source, musicxml, 10000, metadata, "musicxml");

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		assertEquals(true, result);
			
	}

}
