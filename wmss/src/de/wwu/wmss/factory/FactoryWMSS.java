package de.wwu.wmss.factory;

import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.log4j.Logger;

import de.wwu.wmss.connectors.PostgreSQLConnector;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.SystemSettings;

public class FactoryWMSS {

	private static Logger logger = Logger.getLogger("Factory-WMSS");

	public static ArrayList<MusicScore> getScoreList(Enumeration<String> parameters){

		ArrayList<MusicScore> result = new ArrayList<MusicScore>();

		try {

			for (int i = 0; i < SystemSettings.sourceList.size(); i++) {

				if (SystemSettings.sourceList.get(i).getStorage().equals("postgresql")){

					PostgreSQLConnector.executeQuery("SELECT DISTINCT * FROM wmss_scores", SystemSettings.sourceList.get(i));

				}

			}

		} catch (Exception e) {

			logger.error(e.getMessage());
		}

		return result;
	}

}
