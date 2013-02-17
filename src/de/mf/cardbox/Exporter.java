package de.mf.cardbox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Exporter
{
	private Connection con = null;

	public Exporter()
	{
	}

	public void export()
	{
		try
		{
			
			Statement stmt = getConnection().createStatement();
			ResultSet set = stmt.executeQuery("select * from karteikasten");
			int count=0;
			List<String> titles = new ArrayList<String>();
			Connection mySQLCon = getMySQLConnection();
			mySQLCon.setAutoCommit(true);
			while (set.next())
			{
				
				String letter = set.getString(1);
				String title = set.getString(2);
				String text = set.getString(3);
				String database = set.getString(4);
				
				if(titles.contains(title)) {
					continue;
				}
				if(database.equals("Thai"))
					continue;
				
				titles.add(title);
				count++;
				
				
				String sql = "insert into KARTEIKASTEN values ('" + letter + "', '" + title.trim() + "', '" + text + " ', '" + database + "')" ;
				mySQLCon.createStatement().execute( sql );
				System.out.println("Commited " + count);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private synchronized Connection getConnection()
	{

		if (con == null)
		{
			try
			{
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				con = DriverManager.getConnection("jdbc:odbc:elke");
				return con;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		} else
			return con;

	}
	
	private synchronized Connection getMySQLConnection() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		String ipAddress = "mysql01.manitu.net";
		String port = "3306";
		String parameter = "paderpoint1";
		String username = "mfaust";
		String password = "y58Xe9hh";
		String url = "jdbc:mysql://" + ipAddress + ":" + port + "/" + parameter;
		System.out.println(url);
		con = DriverManager.getConnection(url, username, password);
		return con;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		new Exporter().export();
	}

}
