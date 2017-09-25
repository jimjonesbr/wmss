package de.wwu.wmss.factory;

import java.sql.ResultSet;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import de.wwu.wmss.connectors.PostgreSQLConnector;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.RequestParameter;

public class FactoryWMSS {

	private static Logger logger = Logger.getLogger("Factory-WMSS");

	public static ArrayList<MusicScore> getScoreList(ArrayList<RequestParameter> parameters, DataSource dataSource){


		//TODO add where clause statements


		ArrayList<MusicScore> result = new ArrayList<MusicScore>();

		try {

			if (dataSource.getStorage().equals("postgresql")){

				String SQL = "SELECT grp.*,	scr.*, per.*, rol.*, doctype.*, movmed.*, mov.* " + 	
						"FROM wmss_scores scr " +
						"JOIN wmss_score_movements mov ON scr.score_id = mov.score_id " + 
						"JOIN wmss_movement_performance_medium movmed ON mov.movement_id = movmed.movement_id AND movmed.score_id = scr.score_id " + 
						"JOIN wmss_performance_medium med ON movmed.performance_medium_id = med.performance_medium_id " +
						"JOIN wmss_score_persons scrper ON scrper.score_id = scr.score_id  " +
						"JOIN wmss_persons per ON per.person_id = scrper.person_id  " +
						"JOIN wmss_roles rol ON rol.role_id = scrper.role_id " +
						"JOIN wmss_groups grp ON grp.group_id = scr.group_id " +
						"JOIN wmss_document doc ON doc.score_id = scr.score_id " +
						"JOIN wmss_document_type doctype ON doctype.document_type_id = doc.document_type_id;";	

				ResultSet rs = PostgreSQLConnector.executeQuery(SQL, dataSource);

				while (rs.next())
				{
					boolean added = false;

					for (int j = 0; j < result.size(); j++) {

						if(result.get(j).getIdentifier() == rs.getInt("score_id")){
							added = true;
						}

					}

					if(added == false){

						MusicScore rec = new MusicScore();
						rec.setIdentifier(rs.getInt("score_id"));
						rec.setTitle(rs.getString("score_name"));;
						rec.setTonalityMode(rs.getString("score_tonality_mode"));;
						rec.setTonalityTonic(rs.getString("score_tonality_note"));;
						rec.setGroupId(rs.getString("group_id"));
						rec.setGroupDescription(rs.getString("group_description"));

						result.add(rec);

					}

				}

				rs.close();

			}



		} catch (Exception e) {

			logger.error(e.getMessage());
		}


		if (dataSource.getStorage().equals("graphdb")){
			
			
			
		}



		return result;
	}

}
