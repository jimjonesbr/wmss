package de.wwu.wmss.factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import de.wwu.wmss.connectors.PostgreSQLConnector;
import de.wwu.wmss.core.Collection;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.Format;
import de.wwu.wmss.core.Interval;
import de.wwu.wmss.core.MelodyLocation;
import de.wwu.wmss.core.MelodyLocationGroup;
import de.wwu.wmss.core.Movement;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.PerformanceMedium;
import de.wwu.wmss.core.PerformanceMediumType;
import de.wwu.wmss.core.Person;
import de.wwu.wmss.core.RequestParameter;
import de.wwu.wmss.core.Tonality;
import de.wwu.wmss.settings.SystemSettings;

public class FactoryWMSS {

	private static Logger logger = Logger.getLogger("Factory-WMSS");


	public static ArrayList<Collection> getCollections(DataSource dataSource){

		ArrayList<Collection> result = new ArrayList<Collection>(); 

		try {

			if (dataSource.getStorage().equals("postgresql")){

				String sql = "SELECT DISTINCT col.collection_id, col.collection_description \n " + 
							 "FROM wmss.wmss_collections col\n " + 
							 "JOIN wmss.wmss_scores scr ON scr.collection_id = col.collection_id ";

				ResultSet rs = PostgreSQLConnector.executeQuery(sql, dataSource);


				while (rs.next()){

					Collection rec = new Collection();
					rec.setId(rs.getInt("collection_id"));
					rec.setDescription(rs.getString("collection_description"));

					result.add(rec);
				}

				rs.close();
			}

		} catch (Exception e) {

			logger.error("Unexpected error ocurred at the PostgreSQL collection retrieval.");
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		
		return result;

	}

	public static ArrayList<Interval> getCreationInterval(DataSource dataSource){

		ArrayList<Interval> result = new ArrayList<Interval>(); 

		try {

			if (dataSource.getStorage().equals("postgresql")){

				String sql = "SELECT MIN(score_creation_date_min) AS min_date, \n"+
						     "       MAX(score_creation_date_max) AS max_date \n" + 
						     "FROM wmss.wmss_scores; ";

				ResultSet rs = PostgreSQLConnector.executeQuery(sql, dataSource);


				while (rs.next()){

					Interval rec = new Interval();
					rec.setFrom(rs.getDate("min_date"));
					rec.setTo(rs.getDate("max_date"));

					result.add(rec);
				}

				rs.close();
			}

		} catch (Exception e) {

			logger.error("Unexpected error ocurred at the PostgreSQL creation interval retrieval.");
			logger.error(e.getMessage());
			
			e.printStackTrace();
		}

		
		return result;

	}
	public static ArrayList<Format> getFormats(DataSource dataSource){

		ArrayList<Format> result = new ArrayList<Format>(); 

		try {

			if (dataSource.getStorage().equals("postgresql")){

				String sql = "SELECT DISTINCT typ.document_type_id, typ.document_type_description \n" + 
							 "FROM wmss.wmss_document_type typ\n" + 
							 "JOIN wmss.wmss_document doc ON doc.document_type_id = typ.document_type_id";

				ResultSet rs = PostgreSQLConnector.executeQuery(sql, dataSource);


				while (rs.next()){

					Format rec = new Format();
					rec.setFormatId(rs.getString("document_type_id"));
					rec.setFormatDescription(rs.getString("document_type_description"));

					result.add(rec);
				}

				rs.close();
			}

		} catch (Exception e) {

			logger.error("Unexpected error ocurred at the PostgreSQL formats retrieval.");
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		
		return result;

	}
	
	
	public static ArrayList<String> getRoles(DataSource dataSource){

		ArrayList<String> result = new ArrayList<String>(); 

		try {

			if (dataSource.getStorage().equals("postgresql")){

				String sql = "SELECT DISTINCT role_description \n" + 
							 "FROM wmss.wmss_roles rol \n" + 
							 "JOIN wmss.wmss_score_persons scrper ON scrper.role_id = rol.role_id \n" + 
							 "JOIN wmss.wmss_scores scr ON scrper.score_id = scr.score_id \n" + 
							 "JOIN wmss.wmss_persons per ON scrper.person_id = per.person_id ";

				ResultSet rs = PostgreSQLConnector.executeQuery(sql, dataSource);


				while (rs.next()){

					String rec = "";
					rec = rs.getString("role_description");
					
					result.add(rec);
				}

				rs.close();
			}

		} catch (Exception e) {

			logger.error("Unexpected error ocurred at the PostgreSQL roles retrieval.");
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		
		return result;

	}
	
	public static ArrayList<Tonality> getTonalities(DataSource dataSource){

		ArrayList<Tonality> result = new ArrayList<Tonality>(); 

		try {

			if (dataSource.getStorage().equals("postgresql")){

				String sql = "SELECT DISTINCT score_tonality_note, score_tonality_mode \n" + 
							 "FROM wmss.wmss_scores WHERE score_tonality_note IS NOT NULL AND \n" + 
							 "		       score_tonality_mode IS NOT NULL ";

				
				ResultSet rs = PostgreSQLConnector.executeQuery(sql, dataSource);

				

				while (rs.next()){

					Tonality rec = new Tonality();
					rec.setTonic(rs.getString("score_tonality_note"));
					rec.setMode(rs.getString("score_tonality_mode"));

					result.add(rec);
				}

				rs.close();
			}

		} catch (Exception e) {

			logger.error("Unexpected error ocurred at the PostgreSQL tonalities retrieval.");
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		
		return result;

	}
	
	public static ArrayList<String> getTempoMarkings(DataSource dataSource){

		ArrayList<String> result = new ArrayList<String>(); 

		try {

			if (dataSource.getStorage().equals("postgresql")){

				String sql = "SELECT DISTINCT movement_tempo \n" + 
						"FROM wmss.wmss_score_movements \n" + 
						"WHERE movement_tempo IS NOT NULL AND movement_tempo <> ''";

				ResultSet rs = PostgreSQLConnector.executeQuery(sql, dataSource);


				while (rs.next()){

					String tempo = "";
					tempo = rs.getString("movement_tempo");
					
					result.add(tempo);
				}

				rs.close();
			}

		} catch (Exception e) {

			logger.error("Unexpected error ocurred at the PostgreSQL tempo markings retrieval.");
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		
		return result;

	}
	
	public static ArrayList<PerformanceMediumType> getPerformanceMediumList(DataSource dataSource){
		
		ArrayList<PerformanceMedium> mediumList = new ArrayList<PerformanceMedium>();
		ArrayList<PerformanceMediumType> mediumTypeList = new ArrayList<PerformanceMediumType>();
		
		try {

			if (dataSource.getStorage().equals("postgresql")){

				String sql = "SELECT DISTINCT pmt.performance_medium_type_id, pmt.performance_medium_type_description, pm.performance_medium_id, pm.performance_medium_description \n" + 
							 "FROM wmss.wmss_performance_medium pm \n" + 
							 "JOIN wmss.wmss_performance_medium_type pmt ON pm.performance_medium_type_id = pmt.performance_medium_type_id \n" +
							 "JOIN wmss.wmss_movement_performance_medium mvpm ON pm.performance_medium_id = mvpm.performance_medium_id \n" + 
							 "ORDER BY pmt.performance_medium_type_id ";

				ResultSet rs = PostgreSQLConnector.executeQuery(sql, dataSource);


				
				while (rs.next()){


					PerformanceMediumType type = new PerformanceMediumType();
					PerformanceMedium medium = new PerformanceMedium();
					
					type.setMediumTypeId(rs.getString("performance_medium_type_id"));
					type.setMediumTypeDescription(rs.getString("performance_medium_type_description"));
					
					medium.setMediumId(rs.getString("performance_medium_id"));
					medium.setMediumDescription(rs.getString("performance_medium_description"));
					medium.setMediumTypeId(rs.getString("performance_medium_type_id"));
					
					mediumList.add(medium);
					
					boolean mediumTypeAdded = false;
					
					for (int i = 0; i < mediumTypeList.size(); i++) {
						
						if(mediumTypeList.get(i).getMediumTypeId().equals(type.getMediumTypeId())) {
							
							mediumTypeAdded = true;
							
						}
				
					}
				
					if(!mediumTypeAdded) {
						
						mediumTypeList.add(type);
						
					}
					
				}
				
				
				for (int i = 0; i < mediumTypeList.size(); i++) {
					
					for (int j = 0; j < mediumList.size(); j++) {
						
						if(mediumList.get(j).getMediumTypeId().equals(mediumTypeList.get(i).getMediumTypeId())) {
							
							mediumTypeList.get(i).getMediums().add(mediumList.get(j));
							
						}
												
					}
				}


				/**
				 * Removing MediumTypeId from medium records, so that they do not appear in the JSON document.
				 */				
				for (int i = 0; i < mediumTypeList.size(); i++) {
					
					for (int j = 0; j < mediumTypeList.get(i).getMediums().size(); j++) {
						
						mediumTypeList.get(i).getMediums().get(j).setMediumTypeId(null);
						
					}
				}
				
				
				
			}

		} catch (Exception e) {

			logger.error("Unexpected error ocurred at the PostgreSQL performance mediums retrieval.");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return mediumTypeList;
		
	}
	
	
	public static ArrayList<MusicScore> getScoreList(ArrayList<RequestParameter> parameters, DataSource dataSource){

		ArrayList<MusicScore> scoreList = new ArrayList<MusicScore>();
		ArrayList<Movement> movementList = new ArrayList<Movement>();
		ArrayList<PerformanceMedium> mediumList = new ArrayList<PerformanceMedium>();
		ArrayList<PerformanceMediumType> mediumTypeList = new ArrayList<PerformanceMediumType>();
		ArrayList<Person> personList = new ArrayList<Person>();
		ArrayList<Format> formatList = new ArrayList<Format>();
		ArrayList<MelodyLocation> melodyLocationList = new ArrayList<MelodyLocation>();
		
		ArrayList<String> filters = new ArrayList<String>();
		String melody = "";
		
		try {

			if (dataSource.getStorage().equals("postgresql")){

				String sqlHeader = "SELECT grp.*,	scr.*, per.*, rol.*, doctype.*, med.*, movmed.*,  medtype.*, mov.* \n";				
				String sqlJoins = "" +						 	
						"FROM wmss.wmss_scores scr \n" +
						"	JOIN wmss.wmss_score_movements mov ON scr.score_id = mov.score_id \n" + 
						"	JOIN wmss.wmss_movement_performance_medium movmed ON mov.movement_id = movmed.movement_id AND movmed.score_id = scr.score_id \n" + 
						"	JOIN wmss.wmss_performance_medium med ON movmed.performance_medium_id = med.performance_medium_id \n" +
						"	JOIN wmss.wmss_performance_medium_type medtype ON med.performance_medium_type_id = medtype.performance_medium_type_id \n" +
						"	JOIN wmss.wmss_score_persons scrper ON scrper.score_id = scr.score_id  \n" +
						"	JOIN wmss.wmss_persons per ON per.person_id = scrper.person_id  \n" +
						"	JOIN wmss.wmss_roles rol ON rol.role_id = scrper.role_id \n" +
						"	JOIN wmss.wmss_collections grp ON grp.collection_id = scr.collection_id \n" +
						"	JOIN wmss.wmss_document doc ON doc.score_id = scr.score_id \n" +
						"	JOIN wmss.wmss_document_type doctype ON doctype.document_type_id = doc.document_type_id \n ";
				String sqlFilter = "";

				

				for (int i = 0; i < parameters.size(); i++) {


					if(parameters.get(i).getRequest().equals("person")){

						filters.add(" LOWER(per.person_name) LIKE '%" + parameters.get(i).getValue().toLowerCase() +  "%' ");

					} else if(parameters.get(i).getRequest().equals("personrole")){

						filters.add(" LOWER(rol.role_description) = '" + parameters.get(i).getValue().toLowerCase() +  "' ");

					} else if(parameters.get(i).getRequest().equals("performancemedium")){

						filters.add(" LOWER(med.performance_medium_id) LIKE '" + parameters.get(i).getValue() +  "%' ");

					} else if(parameters.get(i).getRequest().equals("performancemediumtype")){

						filters.add(" medtype.performance_medium_type_id = '" + parameters.get(i).getValue() +  "' ");

					} else if(parameters.get(i).getRequest().equals("solo")){

						filters.add(" movmed.movement_performance_medium_solo = " + parameters.get(i).getValue() +  " ");

					} else if(parameters.get(i).getRequest().equals("tonalitytonic")){

						filters.add(" LOWER(scr.score_tonality_note) = '" + parameters.get(i).getValue() +  "' ");

					} else if(parameters.get(i).getRequest().equals("tonalitymode")){

						filters.add(" LOWER(scr.score_tonality_mode) = '" + parameters.get(i).getValue() +  "' ");

					} else if(parameters.get(i).getRequest().equals("tempo")){

						filters.add(" LOWER(mov.movement_tempo)  LIKE '%" + parameters.get(i).getValue() +  "%'");

					} else if(parameters.get(i).getRequest().equals("creationdatefrom")){

						filters.add(" scr.score_creation_date_min  >= '" + formatDate(parameters.get(i).getValue()) +  "' ");

					} else if(parameters.get(i).getRequest().equals("creationdateto")){

						filters.add(" scr.score_creation_date_max  <= '" + formatDate(parameters.get(i).getValue()) +  "' ");

					} else if(parameters.get(i).getRequest().equals("format")){

						filters.add(" LOWER(doc.document_type_id)  = '" + parameters.get(i).getValue() +  "' ");

					} else if(parameters.get(i).getRequest().equals("collection")){

						filters.add(" scr.collection_id IN (" + parameters.get(i).getValue() +  ") ");

					} else if(parameters.get(i).getRequest().equals("melody")){

						melody = parameters.get(i).getValue();

					}

				}
				
				

				if(!filters.isEmpty()){


					sqlFilter = " WHERE scr.score_id IN (SELECT scr.score_id \n " + sqlJoins + "\n WHERE \n";

					for (int j = 0; j < filters.size(); j++) {

						sqlFilter = sqlFilter + filters.get(j);

						if ((j+1) < filters.size()){

							sqlFilter = sqlFilter + " AND \n";
						}
					}

					sqlFilter = sqlFilter + " )";

				}
				
				
				
				
				
				
				/**
				 * Melody search without any other filter
				 */
								
				if(filters.isEmpty() && !melody.equals("")) {
					
					//melodyLocationList = findMelody(melody, "*", dataSource);					
					//System.out.println("split > "+melody.split("[|]")[0]);
					String[] melodyRequests = melody.split("[|]");
					ArrayList<MelodyLocation> tmpMelodyLocationList = new ArrayList<MelodyLocation>();	
					
					for (int j = 0; j < melodyRequests.length; j++) {
												
						
						String[] melodyExpressions = melodyRequests[j].split("/");
						
						/**
						 * If there is no multiple melody expressions
						 */
						if(melodyExpressions.length == 1) { 
						
							tmpMelodyLocationList = findMelody(melodyRequests[j], "*", dataSource);
							
							for (int k = 0; k < tmpMelodyLocationList.size(); k++) {

								melodyLocationList.add(tmpMelodyLocationList.get(k));

							}
							
						}
						
						/**
						 * multiple melody expressions
						 */						
						else {
																					
							boolean matchesExpressions = false;							
							String identifiers = "";
							
							for (int k = 0; k < melodyExpressions.length; k++) {
							
								if (k == 0) {
								
									melodyLocationList = findMelody(melodyExpressions[k], "*", dataSource);
									identifiers = getIdentifiersFromMelodyLocationList(melodyLocationList);
									
									if (!identifiers.equals("")) {
										matchesExpressions = true;
									}
								
								} else {
									
									/**
									 * If the previous iteration returned any result...
									 */
									if (matchesExpressions) {
										
										ArrayList<MelodyLocation> nextMelodyLocationList = findMelody(melodyExpressions[k], identifiers, dataSource);																			
										ArrayList<MelodyLocation> tmp = melodyLocationList;
										melodyLocationList = new ArrayList<MelodyLocation>();
										
										/**
										 * If the current iteration returns any melody location ...
										 */
										if(nextMelodyLocationList.size() != 0) {

											matchesExpressions = true;
											melodyLocationList.addAll(nextMelodyLocationList);
											identifiers = getIdentifiersFromMelodyLocationList(nextMelodyLocationList);
											
											for (int l = 0; l < nextMelodyLocationList.size(); l++) {
												
												for (int m = 0; m < tmp.size(); m++) {
													
													if(tmp.get(m).getScoreId().equals(nextMelodyLocationList.get(l).getScoreId())) {
														
														melodyLocationList.add(tmp.get(m));
													}
													
												}
												
											}
											


										} else {
											System.out.println("#################");
											matchesExpressions = false;
										}
										
										
									}
									
								}
								
								/**
								 * If one of the iterations returns an empty resultset, the whole request is ignored.
								 */
								if(!matchesExpressions) {
									System.out.println("#################");
									melodyLocationList = new ArrayList<MelodyLocation>();
								}
							}
												
							
						}
						
						
					}
					
									
					
					
					
					String idsRetrievedFromMelodySearch = "";
					
					for (int j = 0; j < melodyLocationList.size(); j++) {
						
						idsRetrievedFromMelodySearch = idsRetrievedFromMelodySearch + "'" + melodyLocationList.get(j).getScoreId() + "'";
						
						if((j+1)< melodyLocationList.size()) {
							
							idsRetrievedFromMelodySearch = idsRetrievedFromMelodySearch + ",";
							
						}
					}
					
					
					if (!idsRetrievedFromMelodySearch.equals("")) {
					
						sqlFilter = " WHERE scr.score_id IN ("+ idsRetrievedFromMelodySearch +")";
						
					} else {
						
						sqlFilter = " WHERE scr.score_id IN ('')";
					}
					
				}
				

				
				
				
				
				
				String sql = sqlHeader + sqlJoins + sqlFilter;

				ResultSet rs = PostgreSQLConnector.executeQuery(sql, dataSource);


				while (rs.next()){

					boolean scoreAdded = false;

					for (int j = 0; j < scoreList.size(); j++) {

						if(scoreList.get(j).getScoreId().equals(rs.getString("score_id"))){
							scoreAdded = true;
						}

					}

					MusicScore rec = new MusicScore();
					
					rec.setScoreId(rs.getString("score_id"));
					rec.setCreationDateFrom(rs.getDate("score_creation_date_min"));
					rec.setCreationDateTo(rs.getDate("score_creation_date_max"));
					rec.setTitle(rs.getString("score_name"));;
					rec.setTonalityMode(rs.getString("score_tonality_mode"));;
					rec.setTonalityTonic(rs.getString("score_tonality_note"));;
					rec.setCollectionId(rs.getString("collection_id"));
					rec.setCollectionDescription(rs.getString("collection_description"));
					rec.setThumbnail(rs.getString("score_thumbnail"));
					rec.setPrintResource(rs.getString("score_print_resource"));
					rec.setOnlineResource(rs.getString("score_online_resource"));
					
					Movement mov = new Movement();					
					mov.setMovementIdentifier(rs.getString("movement_id"));
					mov.setMovementName(rs.getString("score_movement_description"));
					mov.setTempo(rs.getString("movement_tempo"));
					mov.setScoreId(rs.getString("score_id"));

					movementList.add(mov);

					PerformanceMediumType pmt = new PerformanceMediumType();
					pmt.setMediumTypeId(rs.getString("performance_medium_type_id"));
					pmt.setMediumTypeDescription(rs.getString("performance_medium_type_description"));
					pmt.setScoreId(rs.getString("score_id"));
					pmt.setMovementId(rs.getString("movement_id"));

					mediumTypeList.add(pmt);

					PerformanceMedium med = new PerformanceMedium();
					med.setMediumId(rs.getString("performance_medium_id"));
					med.setMediumDescription(rs.getString("performance_medium_description"));
					med.setMediumTypeId(rs.getString("performance_medium_type_id"));
					med.setTypeDescription(rs.getString("performance_medium_type_description"));
					med.setMovementId(rs.getString("movement_id"));
					med.setScoreId(rs.getString("score_id"));
					med.setSolo(rs.getString("movement_performance_medium_solo"));
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
			logger.error(e.getMessage());
			e.printStackTrace();
		}


		/**
		 * Selecting and adding movements that belong to a music score
		 */

		for (int i = 0; i < scoreList.size(); i++) {

			for (int j = 0; j < movementList.size(); j++) {						

				if(scoreList.get(i).getScoreId().equals(movementList.get(j).getScoreId())){

					boolean movementAdded = false;

					for (int k = 0; k < scoreList.get(i).getMovements().size(); k++) {

						if(scoreList.get(i).getMovements().get(k).getScoreId().equals(movementList.get(j).getScoreId()) &&
								scoreList.get(i).getMovements().get(k).getMovementName().equals(movementList.get(j).getMovementName())){

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
		 * Adding performance medium types to the correspondent performance mediums played in the score.
		 */

		for (int i = 0; i < scoreList.size(); i++) {

			for (int j = 0; j < scoreList.get(i).getMovements().size(); j++) {

				for (int k = 0; k < mediumTypeList.size(); k++) {

					boolean mediumTypeAdded = false;


					if(scoreList.get(i).getScoreId().equals(mediumList.get(k).getScoreId())  &&
					   scoreList.get(i).getMovements().get(j).getMovementId().equals(mediumList.get(k).getMovementId())){

						for (int l = 0; l < scoreList.get(i).getMovements().get(j).getPerformanceMediumList().size(); l++) {

							if(scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getMediumTypeId().equals(mediumTypeList.get(k).getMediumTypeId()) &&
							   scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getMovementId().equals(mediumTypeList.get(k).getMovementId()) &&
							   scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getScoreId().equals(mediumTypeList.get(k).getScoreId())){

								mediumTypeAdded = true;

							}


						}


						if(!mediumTypeAdded) {

							scoreList.get(i).getMovements().get(j).getPerformanceMediumList().add(mediumTypeList.get(k));


						}					
					}
				}


			}

		}



		for (int i = 0; i < scoreList.size(); i++) {

			for (int j = 0; j < scoreList.get(i).getMovements().size(); j++) {

				for (int k = 0; k < mediumList.size(); k++) {

					for (int l = 0; l < scoreList.get(i).getMovements().get(j).getPerformanceMediumList().size(); l++) {

						boolean mediumAdded = false;

						if(scoreList.get(i).getScoreId().equals(mediumList.get(k).getScoreId())  &&
						   scoreList.get(i).getMovements().get(j).getMovementId().equals(mediumList.get(k).getMovementId()) &&
						   scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getMediumTypeId().equals(mediumList.get(k).getMediumTypeId())){


							for (int m = 0; m < scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getMediums().size(); m++) {

								if(scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getMediums().get(m).getMediumId().equals(mediumList.get(k).getMediumId()) &&
								   scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getMediums().get(m).getMovementId().equals(mediumList.get(k).getMovementId()) &&
								   scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getMediums().get(m).getScoreId().equals(mediumList.get(k).getScoreId()) && 
								   scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getMediums().get(m).getMediumTypeId().equals(mediumList.get(k).getMediumTypeId())) {

									mediumAdded = true;

								}

							}

							if(!mediumAdded) {

								scoreList.get(i).getMovements().get(j).getPerformanceMediumList().get(l).getMediums().add(mediumList.get(k));

							}
							
						}
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

				if(scoreList.get(i).getScoreId().equals(personList.get(j).getScoreId())){

					if(!personAdded){	

						scoreList.get(i).getPersons().add(personList.get(j));

					}

				}

			}


		}

		/**
		 * Selecting formats available for a given score
		 */

		for (int i = 0; i < scoreList.size(); i++) {

			for (int j = 0; j < formatList.size(); j++) {

				boolean formatAdded = false;

				for (int k = 0; k < scoreList.get(i).getFormats().size(); k++) {


					if(formatList.get(j).getScoreId().equals(scoreList.get(i).getFormats().get(k).getScoreId()) &&
							formatList.get(j).getFormatId().equals(scoreList.get(i).getFormats().get(k).getFormatId())){

						formatAdded = true;

					}							

				}

				if(scoreList.get(i).getScoreId().equals(formatList.get(j).getScoreId())){

					if(!formatAdded){

						scoreList.get(i).getFormats().add(formatList.get(j));

					}


				}

			}

		}



		
		
		

		if(!filters.isEmpty() && !melody.equals("")) {

			String identifiers = getIdentifiersFromScoreList(scoreList);

			ArrayList<MelodyLocation> tmpMelodyLocation = new ArrayList<MelodyLocation>();
			String[] melodyRequests = melody.split("[|]");

			for (int i = 0; i < melodyRequests.length; i++) {


				String[] melodyExpressions = melodyRequests[i].split("/");

				/**
				 * No multiple melody expressions
				 */
				if(melodyExpressions.length == 1) { 

					tmpMelodyLocation = findMelody(melodyRequests[i], identifiers, dataSource);

					for (int j = 0; j < tmpMelodyLocation.size(); j++) {

						melodyLocationList.add(tmpMelodyLocation.get(j));

					}

				} else {

					boolean matchesExpressions = false;							


					for (int k = 0; k < melodyExpressions.length; k++) {

						if (k == 0) {

							melodyLocationList = findMelody(melodyExpressions[k], identifiers, dataSource);
							identifiers = getIdentifiersFromMelodyLocationList(melodyLocationList);

							if (!identifiers.equals("")) {
								matchesExpressions = true;
							}

						} else {


							if (matchesExpressions) {

								ArrayList<MelodyLocation> nextMelodyLocationList = findMelody(melodyExpressions[k], identifiers, dataSource);																			
								ArrayList<MelodyLocation> tmp = melodyLocationList;
								melodyLocationList = new ArrayList<MelodyLocation>();

								/**
								 * If the current iteration returns any melody location ...
								 */
								if(nextMelodyLocationList.size() != 0) {

									matchesExpressions = true;
									melodyLocationList.addAll(nextMelodyLocationList);
									identifiers = getIdentifiersFromMelodyLocationList(nextMelodyLocationList);
									
									for (int l = 0; l < nextMelodyLocationList.size(); l++) {

										for (int m = 0; m < tmp.size(); m++) {

											if(tmp.get(m).getScoreId().equals(nextMelodyLocationList.get(l).getScoreId())) {

												melodyLocationList.add(tmp.get(m));
											}

										}

									}



								} else {
									
									matchesExpressions = false;
								}				

							} 

						}

						/**
						 * If one of the iterations returns an empty resultset, the whole request is ignored.
						 */
						if(!matchesExpressions) {

							melodyLocationList = new ArrayList<MelodyLocation>();
						}

					}

					



					//					ArrayList<MelodyLocation> melodyRequestResult = new ArrayList<MelodyLocation>();			
					//
					//					melodyRequestResult = findMelody(melodyRequests[i], identifiers, dataSource);
					//
					//					for (int k = 0; k < melodyRequestResult.size(); k++) {
					//
					//						tmpMelodyLocation.add(melodyRequestResult.get(k));
					//
					//					}


				}


			}



			tmpMelodyLocation = melodyLocationList;

			ArrayList<MusicScore> tmpScorelist = new ArrayList<MusicScore>();
			boolean found = false;

			for (int i = 0; i < scoreList.size(); i++) {

				for (int j = 0; j < tmpMelodyLocation.size(); j++) {

					if(tmpMelodyLocation.get(j).getScoreId().equals(scoreList.get(i).getScoreId())) {

						found = true;

					}

				}

				if(found) {

					tmpScorelist.add(scoreList.get(i));
					found = false;

				}

			}

			scoreList = tmpScorelist;
			melodyLocationList = tmpMelodyLocation;

		}
		
		
		
		
		
		
		
		
		
		
		
		if(!melody.equals("")) {

			for (int i = 0; i < melodyLocationList.size(); i++) {
				
				for (int j = 0; j < scoreList.size(); j++) {
					
					if(melodyLocationList.get(i).getScoreId().equals(scoreList.get(j).getScoreId())) {
						
						scoreList.get(j).getMelodyLocation().add(melodyLocationList.get(i));
						
					}
					
				}
				
			}
			
		}
		

		
		for (int i = 0; i < scoreList.size(); i++) {

			scoreList.get(i).setScoreId(dataSource.getId() + ":" + scoreList.get(i).getScoreId());

		}

		//TODO: create pretty print function for JSON documents (DEBUG / PRODUCTION Mode) 
		//TODO: create a different class for graph dbs

		if (dataSource.getStorage().equals("graphdb")){



		}



		return scoreList;
	}


	private static String getIdentifiersFromScoreList(ArrayList<MusicScore> scoreList) {
		
		String result = "";
		
		for (int i = 0; i < scoreList.size(); i++) {
			
			result = result + scoreList.get(i).getScoreId();
			
			if ((i+1) != scoreList.size()) {
				result = result + ",";
			}
			
		}	
		
		return result;
	}
	

	private static String getIdentifiersFromMelodyLocationList(ArrayList<MelodyLocation> melodyLocationList) {
		
		String result = "";
		
		for (int i = 0; i < melodyLocationList.size(); i++) {
			
			result = result + melodyLocationList.get(i).getScoreId();
			
			if ((i+1) != melodyLocationList.size()) {
				result = result + ",";
			}
			
		}	
		
		return result;
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

				String SQL = "SELECT score_document FROM wmss.wmss_document WHERE score_id = '" + scoreId + "' ";	

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
	
	private static ArrayList<MelodyLocation> findMelody(String melody, String identifiers, DataSource dataSource){
		
		ArrayList<MelodyLocation> result = new ArrayList<MelodyLocation>();
		try {
			
			
			ResultSet rs;
	
			rs = PostgreSQLConnector.executeQuery("SELECT DISTINCT * FROM wmss.wmss_find_melody('"+melody+"','"+identifiers+"')", dataSource);
		

			while (rs.next()){

				MelodyLocation rec = new MelodyLocation();
				
				rec.setScoreId(rs.getString("res_score"));
				rec.setMovementIdentifier(rs.getString("res_movement"));
				rec.setMovementName(rs.getString("res_movement_name"));
				rec.setStartingMeasure(rs.getString("res_measure"));
				rec.setStaff(rs.getString("res_staff"));
				rec.setVoice(rs.getString("res_voice"));
				rec.setInstrumentName(rs.getString("res_instrument_name"));
				rec.setMelody(melody);
				result.add(rec);

			}		
		

			rs.close();
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return result;
		
		
	}
	
	
	private static String formatDate (String date) {
		
		String result = "";

		try {
			
			
			SimpleDateFormat yearFormat  = new SimpleDateFormat("yyyy");
			SimpleDateFormat yearMonthFormat  = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat yearMonthDayFormat  = new SimpleDateFormat("yyyy-MM-dd");
			
			if(date.length()<=4) {
			
				result = yearMonthDayFormat.format(yearFormat.parse(date));
				
			} else if (date.length() > 4 && date.length() < 8) {
				
				result = yearMonthDayFormat.format(yearMonthFormat.parse(date));
				
			} else if (date.length() >= 8 && date.length() <= 10) {
								
				result = yearMonthDayFormat.format(yearMonthDayFormat.parse(date));
			}
			
		} catch (ParseException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		logger.info("Date converted from '"+date+"' to '"+result+"'");
		return result;
	}

}
