package de.wwu.wmss.factory;

import java.sql.ResultSet;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import de.wwu.wmss.connectors.PostgreSQLConnector;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.Format;
import de.wwu.wmss.core.Movement;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.PerformanceMedium;
import de.wwu.wmss.core.Person;
import de.wwu.wmss.core.RequestParameter;
import de.wwu.wmss.settings.SystemSettings;

public class FactoryWMSS {

	private static Logger logger = Logger.getLogger("Factory-WMSS");

	public static ArrayList<MusicScore> getScoreList(ArrayList<RequestParameter> parameters, DataSource dataSource){

		ArrayList<MusicScore> scoreList = new ArrayList<MusicScore>();
		ArrayList<Movement> movementList = new ArrayList<Movement>();
		ArrayList<PerformanceMedium> mediumList = new ArrayList<PerformanceMedium>();
		ArrayList<Person> personList = new ArrayList<Person>();
		ArrayList<Format> formatList = new ArrayList<Format>();

		try {

			if (dataSource.getStorage().equals("postgresql")){

				String SQL = "SELECT grp.*,	scr.*, per.*, rol.*, doctype.*, med.*, movmed.*,  medtype.*, mov.* " + 	
						"FROM wmss_scores scr " +
						"JOIN wmss_score_movements mov ON scr.score_id = mov.score_id " + 
						"JOIN wmss_movement_performance_medium movmed ON mov.movement_id = movmed.movement_id AND movmed.score_id = scr.score_id " + 
						"JOIN wmss_performance_medium med ON movmed.performance_medium_id = med.performance_medium_id " +
						"JOIN wmss_performance_medium_type medtype ON med.performance_medium_type_id = medtype.performance_medium_type_id " +
						"JOIN wmss_score_persons scrper ON scrper.score_id = scr.score_id  " +
						"JOIN wmss_persons per ON per.person_id = scrper.person_id  " +
						"JOIN wmss_roles rol ON rol.role_id = scrper.role_id " +
						"JOIN wmss_groups grp ON grp.group_id = scr.group_id " +
						"JOIN wmss_document doc ON doc.score_id = scr.score_id " +
						"JOIN wmss_document_type doctype ON doctype.document_type_id = doc.document_type_id "
						+ "limit 1";	

				ResultSet rs = PostgreSQLConnector.executeQuery(SQL, dataSource);


				while (rs.next()){

					boolean scoreAdded = false;

					for (int j = 0; j < scoreList.size(); j++) {

						if(scoreList.get(j).getScoreIdentifier().equals(rs.getString("score_id"))){
							scoreAdded = true;
						}

					}

					MusicScore rec = new MusicScore();

					rec.setScoreIdentifier(rs.getString("score_id"));
					rec.setCreationDateFrom(rs.getInt("score_creation_date_min"));
					rec.setCreationDateTo(rs.getInt("score_creation_date_max"));
					rec.setTitle(rs.getString("score_name"));;
					rec.setTonalityMode(rs.getString("score_tonality_mode"));;
					rec.setTonalityTonic(rs.getString("score_tonality_note"));;
					rec.setGroupId(rs.getString("group_id"));
					rec.setGroupDescription(rs.getString("group_description"));

					Movement mov = new Movement();					
					mov.setMovementIdentifier(rs.getString("movement_id"));
					mov.setTitle(rs.getString("score_movement_description"));
					mov.setTempo(rs.getString("movement_tempo"));
					mov.setScoreId(rs.getString("score_id"));

					movementList.add(mov);

					PerformanceMedium med = new PerformanceMedium();
					med.setMediumClassification(rs.getString("performance_medium_id"));
					med.setMediumDescription(rs.getString("performance_medium_description"));
					med.setTypeDescription(rs.getString("performance_medium_type_description"));
					med.setMovementId(rs.getString("movement_id"));
					med.setScoreId(rs.getString("score_id"));
					med.setSolo(rs.getBoolean("movement_performance_medium_solo"));
					med.setMediumScoreDescription(rs.getString("movement_performance_medium_description"));

					mediumList.add(med);

					Person per = new Person();					
					per.setName(rs.getString("person_name"));
					per.setRole(rs.getString("role_description"));
					per.setScoreId(rs.getString("score_id"));

					personList.add(per);

					Format frm = new Format();
					frm.setFormatId(rs.getString("document_type_id"));
					frm.setFormatDescription(rs.getString("document_type_description"));
					frm.setScoreId(rs.getString("score_id"));

					formatList.add(frm);

					if(scoreAdded == false){

						scoreList.add(rec);

					} 

				}

				rs.close();


			}



		} catch (Exception e) {

			logger.error("Unexpected error ocurred at the PostgreSQL data crawler.");
			e.printStackTrace();
		}


		
		

		/**
		 * Selecting and adding movements that belong to a music score
		 */

		for (int i = 0; i < scoreList.size(); i++) {

			for (int j = 0; j < movementList.size(); j++) {						

				if(scoreList.get(i).getScoreIdentifier().equals(movementList.get(j).getScoreId())){

					boolean movementAdded = false;

					for (int k = 0; k < scoreList.get(i).getMovements().size(); k++) {

						if(scoreList.get(i).getMovements().get(k).getScoreId().equals(movementList.get(j).getScoreId()) &&
						   scoreList.get(i).getMovements().get(k).getTitle().equals(movementList.get(j).getTitle())){

							movementAdded = true;
						}

					}

					if(!movementAdded) {

						scoreList.get(i).getMovements().add(movementList.get(j));

					}

				}

			}


		}


		/**
		 * Selecting and adding performance medium that belong to a movement
		 */
		for (int i = 0; i < scoreList.size(); i++) {

			for (int j = 0; j < scoreList.get(i).getMovements().size(); j++) {

				for (int k = 0; k < mediumList.size(); k++) {

					boolean mediumAdded = false;

					if(scoreList.get(i).getScoreIdentifier().equals(mediumList.get(k).getScoreId())  &&
					   scoreList.get(i).getMovements().get(j).getMovementIdentifier().equals(mediumList.get(k).getMovementId())){

						for (int l = 0; l < scoreList.get(i).getMovements().get(j).getPerformanceMediumList().size(); l++) {

							if(scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getScoreId().equals(mediumList.get(k).getScoreId()) &&
							   scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getMovementId().equals(mediumList.get(k).getMovementId()) &&
							   scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getMediumScoreDescription().equals(mediumList.get(k).getMediumScoreDescription())){

								mediumAdded = true;

							} 

						}

						if(!mediumAdded) {scoreList.get(i).getMovements().get(j).getPerformanceMediumList().add(mediumList.get(k));}


					}
				}



			}

		}


		/**
		 * Selecting and adding persons that belong to a music score
		 */

		for (int i = 0; i < scoreList.size(); i++) {


			for (int j = 0; j < personList.size(); j++) {

				boolean personAdded = false;

				for (int k = 0; k < scoreList.get(i).getPersons().size(); k++) {

					if(scoreList.get(i).getPersons().get(k).getName().equals(personList.get(j).getName()) &&
							scoreList.get(i).getPersons().get(k).getScoreId().equals(personList.get(j).getScoreId())){

						personAdded = true;

					}

				}

				if(scoreList.get(i).getScoreIdentifier().equals(personList.get(j).getScoreId())){

					if(!personAdded){	

						scoreList.get(i).getPersons().add(personList.get(j));

					}

				}

			}


		}


		for (int i = 0; i < scoreList.size(); i++) {

			for (int j = 0; j < formatList.size(); j++) {

				boolean formatAdded = false;

				for (int k = 0; k < scoreList.get(i).getFormats().size(); k++) {


					if(formatList.get(j).getScoreId().equals(scoreList.get(i).getFormats().get(k).getScoreId()) &&
							formatList.get(j).getFormatId().equals(scoreList.get(i).getFormats().get(k).getFormatId())){

						formatAdded = true;

					}							

				}

				if(scoreList.get(i).getScoreIdentifier().equals(formatList.get(j).getScoreId())){

					if(!formatAdded){

						scoreList.get(i).getFormats().add(formatList.get(j));

					}


				}

			}

		}


		for (int i = 0; i < scoreList.size(); i++) {
			
			scoreList.get(i).setScoreIdentifier(dataSource.getId() + ":" + scoreList.get(i).getScoreIdentifier());
			
		}

		
		

		if (dataSource.getStorage().equals("graphdb")){



		}



		return scoreList;
	}
	
	
	public static String getScore(ArrayList<RequestParameter> parameters){
		
		String result = "";
		String scoreId = "";
		String format = "";
		
		String dataSourceId = "";
		DataSource dataSource = new DataSource();
		
		for (int i = 0; i < parameters.size(); i++) {
			
			
			
			if(parameters.get(i).getRequest().equals("identifier")){
				
				dataSourceId = parameters.get(i).getValue().split(":")[0];
				scoreId = parameters.get(i).getValue().split(":")[1];
				
			}
			
			if(parameters.get(i).getRequest().equals("format")){
				
				format = parameters.get(i).getValue();
				
			}
			
		}
		
		
		for (int i = 0; i < SystemSettings.sourceList.size(); i++) {
		
			if(SystemSettings.sourceList.get(i).getId().equals(dataSourceId)){
				
				dataSource = SystemSettings.sourceList.get(i); 
				
			}
			
		}
		
		
		try {

			if (dataSource.getStorage().equals("postgresql")){

				String SQL = "SELECT score_document FROM wmss_document WHERE score_id = '" + scoreId + "' ";	
				
				if(!format.equals("")){
				
					SQL = SQL + "AND document_type_id = '" + format + "'";
					
				}
				
				ResultSet rs = PostgreSQLConnector.executeQuery(SQL, dataSource);

				while (rs.next()){
					
					result = rs.getString("score_document");
					
				}
				
				rs.close();

			}



		} catch (Exception e) {

			logger.error("Unexpected error ocurred at the PostgreSQL connector (GetScore request).");
			e.printStackTrace();
		}
		
		return result;
		
	}

}
