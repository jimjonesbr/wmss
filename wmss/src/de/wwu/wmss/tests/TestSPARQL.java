package de.wwu.wmss.tests;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class TestSPARQL {
	
		
	public static void main(String[] args) {

		String result = "";

		ArrayList<String> list = new ArrayList<>();
		
		list.add("Ahlen");
		list.add("Eppstein");
		list.add("Vacha");
		list.add("Siegen");
		list.add("Bielefeld");
		list.add("Emden");
		list.add("Gera");
		list.add("Neumünster");
		list.add("Buxtehude");
		list.add("Marktbreit");
		list.add("Ludwigshafen");
		list.add("Königsstein");
		list.add("Zeitz");
		list.add("Kempen");
		list.add("Bamberg");
		list.add("Weimar");
		list.add("Zwickau");
		list.add("Offenburg");
		list.add("Hildesheim");
		list.add("Krefeld");
		list.add("Tübingen");
		list.add("Bad Sooden-Allendorf");
		list.add("Flörsheim am Main");
		list.add("Aschersleben");
		list.add("Karlsruhe");
		list.add("Elbingerode");
		list.add("Zweibrücken");
		list.add("Alsleben (Saale)");
		list.add("Sondershausen");
		list.add("Gießen");
		list.add("Friedrichsstadt");
		list.add("Lich");
		list.add("Dessau");
		list.add("?wiebodzice");
		list.add("Borkum");
		list.add("Bingen am Rhein");
		list.add("Sylt");
		list.add("Mindelheim");
		list.add("Blankenburg");
		list.add("Eltville am Rhein");
		list.add("Friedrichshafen");
		list.add("Waldshut-Tiengen");
		list.add("Recklinghausen");
		list.add("Lindau (Bodensee)");
		list.add("Bad Soden");
		list.add("Detmold");
		list.add("Oberschönau");
		list.add("Ludwigsburg");
		list.add("Schulpforte");
		list.add("Darmstadt");
		list.add("Kempten");
		list.add("Ballenstedt");
		list.add("Pappenheim");
		list.add("Creuzburg");
		list.add("Brandenburg an der Havel");
		list.add("Münster");
		list.add("Stade");
		list.add("Wilhelmshaven");
		list.add("Oldenburg in Holstein");
		list.add("Halberstadt");
		list.add("Wolfenbüttel");
		list.add("Dresden");
		list.add("Mühlhausen");
		list.add("Niedernhausen");
		list.add("Ulm");
		list.add("Halle (Saale)");
		list.add("Mainz");
		list.add("Dausenau");
		list.add("Landsberg am Lech");
		list.add("Hann. Münden");
		list.add("Ilfeld");
		list.add("Badbergen");
		list.add("HalberstaHalberstadtdt");
		list.add("Bremen");
		list.add("Garmisch-Partenkirchen");
		list.add("Bad Lauterberg");
		list.add("Remagen");
		list.add("Bleckede");
		list.add("Regensburg");
		list.add("Legnica");
		list.add("Adelsheim");
		list.add("Memmingen");
		list.add("Wernigerode");
		list.add("Wuppertal");
		list.add("Witten");
		list.add("Erlangen");
		list.add("Castrop-Rauxel");
		list.add("Worpswede");
		list.add("Plauen");
		list.add("Bad Hersfeld");
		list.add("Fürth");
		list.add("Zittau");
		list.add("Waren (Müritz)");
		list.add("Schorndorf");
		list.add("Nördlingen");
		list.add("Roßleben");
		list.add("Braunlage");
		list.add("Dortmund");
		list.add("Schmitten");
		list.add("Ostróda");
		list.add("Minden");
		list.add("Schwalenberg");
		list.add("Toru?");
		list.add("Backnang");
		list.add("Bergisch Gladbach");
		list.add("Stavenhagen");
		list.add("Eschwege");
		list.add("Königslutter am Elm");
		list.add("Sowetsk (Kaliningrad)");
		list.add("Wildeshausen");
		list.add("Erbach");
		list.add("Salzgitter");
		list.add("Bad Staffelstein");
		list.add("Oldenburg");
		list.add("Hechingen");
		list.add("Bernkastel-Kues");
		list.add("Coburg");
		list.add("Maulbronn");
		list.add("Aachen");
		list.add("Oberwiesenthal");
		list.add("Lübeck");
		list.add("Suhl");
		list.add("Itzehoe");
		list.add("Herzberg am Harz");
		list.add("Bad Reichenhall");
		list.add("Paulinzella");
		list.add("Leer");
		list.add("Essen");
		list.add("Kevelaer");
		list.add("Limburg");
		list.add("Frankfurt am Main");
		list.add("Quedlingburg");
		list.add("Lahnau");
		list.add("Apolda");
		list.add("Stralsund");
		list.add("Wittenburg");
		list.add("Isselburg");
		list.add("Rastatt");
		list.add("Dillenburg");
		list.add("Szczecin");
		list.add("Papenburg");
		list.add("Finsterwalde");
		list.add("Brilon");
		list.add("Arnsberg");
		list.add("Torgau");
		list.add("Kiel");
		list.add("Gelnhausen");
		list.add("Schmalkalden");
		list.add("Steinfurt");
		list.add("Bocholt");
		list.add("Salzwedel");
		list.add("Gotha");
		list.add("Wunstorf");
		list.add("Gwardeisk");
		list.add("Ehringshausen");
		list.add("Mosbach");
		list.add("Amorbach");
		list.add("Neustadt an der Donau");
		list.add("Bensheim");
		list.add("Schwarzburg");
		list.add("Schwäbisch Gmünd");
		list.add("Namys?ów");
		list.add("Gernrode");
		list.add("Meiningen");
		list.add("Lahstedt");
		list.add("Bramsche");
		list.add("Würzburg");
		list.add("Wangen im Allgäu");
		list.add("Schwabach");
		list.add("Blankenburg (Harz)");
		list.add("Bad Camberg");
		list.add("Radebeul");
		list.add("Breisach am Rhein");
		list.add("Blomberg");
		list.add("Wilhemshaven");
		list.add("Annaberg-Buchholz");
		list.add("Lahnstein");
		list.add("München");
		list.add("Heilbronn");
		list.add("Helgoland");
		list.add("Wiesbaden");
		list.add("Rudolstadt");
		list.add("Gda?sk");
		list.add("Frankfrut am Main");
		list.add("Wildemann");
		list.add("Südharz");
		list.add("Halle");
		list.add("Worms");
		list.add("Erfurt");
		list.add("Bursfelde");
		list.add("Bodenwerder-Polle");
		list.add("Clausthal-Zellerfeld");
		list.add("Hohenstein");
		list.add("Verden");
		list.add("Michelstadt");
		list.add("Meißen");
		list.add("Oberstdorf");
		list.add("Flensburg");
		list.add("Melsungen");
		list.add("Waltershausen");
		list.add("Schneeberg");
		list.add("Kleve");
		list.add("Betzdorf-Gebhardshain");
		list.add("Schliersee");
		list.add("Hamburg");
		list.add("Steinach");
		list.add("Delmenhorst");
		list.add("Amöneburg");
		list.add("Ruhla");
		list.add("Lutherstadt Wittenberg");
		list.add("Bitterfeld-Wolfen");
		list.add("Eisenach");
		list.add("Bonn");
		list.add("Osterode am Harz");
		list.add("Schieder-Schwalenberg");
		list.add("Stuttgart");
		list.add("Wolframs-Eschenbach");
		list.add("Bad Pyrmont");
		list.add("Anklam");
		list.add("K?odzko");
		list.add("Merseburg");
		list.add("Bad Wildungen");
		list.add("Runkel");
		list.add("Walkenried");
		list.add("Wittenberg");
		list.add("Mansfeld");
		list.add("Göttingen");
		list.add("Mühlheim an der Ruhr");
		list.add("Hannover");
		list.add("Aschaffenburg");
		list.add("Dessau-Roßlau");
		list.add("Mölln");
		list.add("Aub");
		list.add("Ingolstadt");
		list.add("Demmin");
		list.add("Saarbrücken");
		list.add("Knittlingen");
		list.add("Passau");
		list.add("Lüneburg");
		list.add("Isenbüttel");
		list.add("Fulda");
		list.add("Wettin-Löbejün");
		list.add("Heidelberg");
		list.add("Nürnberg");
		list.add("Limburg an der Lahn");
		list.add("Soest");
		list.add("Wittenberge");
		list.add("Lohr am Main");
		list.add("Bautzen");
		list.add("Biedenkopf");
		list.add("Thale");
		list.add("Arnstadt");
		list.add("Emmerthal");
		list.add("Augsburg");
		list.add("Taunusstein");
		list.add("Ansbach");
		list.add("Bietigheim");
		list.add("Bad Orb");
		list.add("Köln");
		list.add("Altötting");
		list.add("Pößneck");
		list.add("Jena");
		list.add("Duderstadt");
		list.add("Nordhausen");
		list.add("Kulmbach");
		list.add("Kaliningrad");
		list.add("Büdingen");
		list.add("Berlin");
		list.add("Eisleben");
		list.add("Breitungen / Werra");
		list.add("Blieskastel");
		list.add("Langelsheim");
		list.add("Jever");
		list.add("Hof");
		list.add("Marktredwitz");
		list.add("Wolfsburg");
		list.add("Ueckermünde");
		list.add("Braunfels");
		list.add("Pasewalk");
		list.add("Obernhof");
		list.add("Stendal");
		list.add("Wroc?aw");
		list.add("Treffurt");
		list.add("Herstelle");
		list.add("Fritzlar");
		list.add("Bad Nauheim");
		list.add("Bodenwerder");
		list.add("Weinheim");
		list.add("Bad Gandersheim");
		list.add("Düsseldorf");
		list.add("Heidenrod");
		list.add("Schöppenstedt");
		list.add("Franfurt am Main");
		list.add("Bad Kissingen");
		list.add("Marl");
		list.add("Wasserburg am Inn");
		list.add("Amberg");
		list.add("Oberhof");
		list.add("Boppard");
		list.add("Landshut");
		list.add("Billerbeck");
		list.add("Altenburg");
		list.add("Alfeld (Leine)");
		list.add("Naumburg");
		list.add("Aalen");
		list.add("Wismar");
		list.add("Markgröningen");
		list.add("Celle");
		list.add("Nysa");
		list.add("Einbeck");
		list.add("Höxter");
		list.add("Waldsolms");
		list.add("Besigheim");
		list.add("Offenbach am Main");
		list.add("Frankfurt an der Oder");
		list.add("Quedlinburg");
		list.add("Wittlich");
		list.add("Helmstedt");
		list.add("Kassel");
		list.add("Kalkar");
		list.add("Chemnitz");
		list.add("Leipzig");
		list.add("Wertheim");
		list.add("Esslingen am Neckar");
		list.add("Rheinberg");
		list.add("Ilmenau");
		list.add("Lützen");
		list.add("Lemgo");
		list.add("Nauen");
		list.add("Schleswig");
		list.add("Cuxhaven");
		list.add("Olsztyn");
		list.add("Flecken Bovenden");
		list.add("Calau");
		list.add("Wetzlar");
		list.add("Linz am Rhein");
		list.add("Cloppenburg");
		list.add("Zerbst");
		list.add("Horneburg");
		list.add("Bochum");
		list.add("Neustadt an der Weinstraße");
		list.add("Bischofswerda");
		list.add("Husum");
		list.add("Schweinfurt");
		list.add("Güstrow");
		list.add("Baden-Baden");
		list.add("Görlitz");
		list.add("Bad Fallingbostel");
		list.add("Hameln");
		list.add("Bottrop");
		list.add("Bayreuth");
		list.add("Goslar");
		list.add("Kornwestheim");
		list.add("Bad Mergentheim");
		list.add("Bad Harzburg");
		list.add("Biberach an der Riß");
		list.add("Braunschweig");
		list.add("Trier");
		list.add("Pirmasens");
		list.add("Iserlohn");
		list.add("Freiburg im Breisgau");
		list.add("Neu-Ulm");
		list.add("Holzminden");
		list.add("Göllitz");
		list.add("Potsdam");
		list.add("Marburg");
		list.add("Artern");
		list.add("Konstanz");
		list.add("Polle");
		list.add("Osnabrück");
		list.add("Butzbach");
		list.add("Neustrelitz");
		list.add("Cottbus");
		list.add("Wedel");
		list.add("Rostock");
		list.add("Aßlar");
		list.add("Nievern");
		list.add("Frankenberg");
		list.add("Rüdesheim am Rhein");
		list.add("Wesel");
		list.add("Beverungen");
		list.add("Bad Reichenall");
		list.add("Kahla");
		list.add("Gütersloh");
		list.add("Hanau");
		list.add("Egeln");
		list.add("Schwäbisch Hall");
		list.add("Oldenburg (Oldenburg)");
		list.add("Hannoversch Münden");
		list.add("Bad Homburg");
		list.add("Ochsenfurt");
		list.add("Greifenstein");
		list.add("Saalfeld");
		list.add("Alsfeld");
		list.add("Pforzheim");
		list.add("Paderborn");
		list.add("Tangermünde");
		list.add("Magdeburg");
		list.add("Rothenburg ob der Tauber");
		list.add("Neuweiler");
		list.add("Greifswald");
		list.add("Remscheid");
		list.add("Mannheim");
		list.add("Schlangenbad");
		list.add("Koblenz");
		list.add("Volkach");

		
		for (int i = 0; i < list.size(); i++) {
		
			//System.out.println("> "+ list.get(i));
			
			String SPARQL = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
					"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" + 
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" + 
					"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" + 
					"PREFIX schema: <http://schema.org/>\n" + 
					"PREFIX cc: <http://creativecommons.org/ns#>\n" + 
					"PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n" + 
					"PREFIX prov: <http://www.w3.org/ns/prov#>\n" + 
					"PREFIX wikibase: <http://wikiba.se/ontology#>\n" + 
					"PREFIX wdata: <http://www.wikidata.org/wiki/Special:EntityData/>\n" + 
					"PREFIX wd: <http://www.wikidata.org/entity/>\n" + 
					"PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" + 
					"PREFIX wdtn: <http://www.wikidata.org/prop/direct-normalized/>\n" + 
					"SELECT DISTINCT ?city ?cityLabel\n" + 
					"WHERE\n" + 
					"{\n" + 
					"	BIND(wd:Q515 AS ?c)\n" + 
					"	?city wdt:P31/wdt:P279* ?c .  # find instances of subclasses of city\n" + 
					"	?city rdfs:label ?cityLabel .\n" + 
					"	FILTER(LANG(?cityLabel) = \"de\").  # only look for german city names\n" + 
					"	FILTER(STRSTARTS(?cityLabel, \""+list.get(i)+"\")) .  # find city with specific label\n" + 
					"}\n" + 
					"LIMIT 1";
			
			Query query = QueryFactory.create(SPARQL);
			
			//System.out.println(query);		
		
			QueryExecution qexec = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query);

			ResultSet results = qexec.execSelect();	
			
			String url = "";
			
			
			
			for ( ; results.hasNext() ; ) {
			      QuerySolution soln = results.nextSolution() ;
			      
			      url = soln.get("?city").toString();
			      
			    }
			
			
			result = result + list.get(i)+","+url+"\n";
			System.out.println(list.get(i)+","+url);
			qexec.close();

		}
		
		
		System.out.println("\n#########################################################\n");
		System.out.println(result);
		
	}

}
