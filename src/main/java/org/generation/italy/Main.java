package org.generation.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	private static final String URL = "jdbc:mysql://localhost:3306/db_nations";
	private static final String USER = "root";
	private static final String PASSWORD = "root";
	
	public static void main(String[] args) {
		
		Scanner sc= new Scanner(System.in);
		System.out.println("Insert a nation:");
		String searchedStringOfNation=sc.nextLine();
		
		getListOfPossibleResults(searchedStringOfNation);
		
		System.out.println("Insert the Id from the list:");
		String idSearched=sc.nextLine();
		
		selectResultFromList(idSearched);
	}
	
	private static void getListOfPossibleResults(String searchedStringOfNation) {
		
		try(Connection con = DriverManager.getConnection(URL, USER, PASSWORD);) {
			
			final String sql = "SELECT * "
								+" FROM countries"
								+" JOIN regions"
								+" ON countries.region_id = regions.region_id "
								+" JOIN continents "
								+" ON regions.continent_id = continents.continent_id "
								+" WHERE countries.name LIKE ?"
								+" ORDER BY countries.name";
			
			try(PreparedStatement ps = con.prepareStatement(sql)){
				
				ps.setString(1, "%"+searchedStringOfNation+"%");

				try(ResultSet rs =	ps.executeQuery()){
					
					while (rs.next()) {
						final int id = rs.getInt(1);
						final String country = rs.getString(2);
						final String region = rs.getString(3);
						final String continent =rs.getString(4);
						
						System.out.println(
								id+ " - "
								+ country + " - "
								+ region + " - "
								+ continent + " - "
								);
						
					}
				}
			}
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		finally{
//			if(con != null) {
//				con.close();
//			}
		}

	}
	
	private static void selectResultFromList(String searchedId) {
		
		try(Connection con = DriverManager.getConnection(URL, USER, PASSWORD);) {
			
			final String sql = "SELECT countries.name , languages.`language`, country_stats.`year` , country_stats.population, country_stats.gdp   \n"
					+ "FROM countries\n"
					+ "JOIN country_languages\n"
					+ "ON countries.country_id =country_languages.country_id \n"
					+ "JOIN languages\n"
					+ "ON country_languages.language_id = languages.language_id\n"
					+ "JOIN country_stats\n"
					+ "ON countries.country_id = country_stats.country_id\n"
					+ "WHERE countries.country_id = ? && country_stats.`year`=2018";
			
			try(PreparedStatement ps = con.prepareStatement(sql)){
				
				ps.setString(1,searchedId);

				try(ResultSet rs =	ps.executeQuery()){
					

					
					while (rs.next()) {
						
						if(rs.isFirst())
							
						{
							String country = rs.getString(1);
							
							System.out.println(	"Details for country: "+ country);
							System.out.println(	"Languages: ");
							
						}

						final String language = rs.getString(2);

						if(!rs.isLast())
							System.out.print(language+", ");
						if(rs.isLast())
						{
							System.out.println(language+".");
							System.out.println("Most recent stats:");
							
							final String year =rs.getString(3);
							final String population =rs.getString(4);
							final String gdp =rs.getString(5);
							
							System.out.println("Year: "+year);
							System.out.println("Population: "+population);
							System.out.println("GDP: "+gdp);
									
							
						}
						
					}
				}
			}
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		finally{
//			if(con != null) {
//				con.close();
//			}
		}

	}

}
