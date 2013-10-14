package geomario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WolfDB extends Wolf {
    private Connection connection;
    private PreparedStatement statement;

    public WolfDB() {
	super();

	try {
	    Class.forName("org.sqlite.JDBC");
	}
	catch (ClassNotFoundException e) {
	    System.err.println(e);
	}

	connection = null;

	try {
	    connection = DriverManager.getConnection("jdbc:sqlite:cache.db");
	    statement = connection.prepareStatement("create table if not exists `places` (id integer primary key autoincrement, city string, latitude real, longitude real, income integer, popdensity integer, lakes integer, crime real)");

	    statement.setQueryTimeout(30);
	    statement.executeUpdate();
	}
	catch(SQLException e) {
	    System.err.println(e.getMessage());
	}
    }

    public void close() {
	try {
	    if (connection != null) {
		connection.close();
	    }
	}
	catch(SQLException e) {
	    System.err.println(e);
	}
    }

    public boolean isInCache(String location) {
	int cacheCount = 0;

	try {
	    statement = connection.prepareStatement("select count(*) from `places` where city = ?");
	    statement.setString(1, location);
	    ResultSet results = statement.executeQuery();

	    while (results.next()) {
		cacheCount = results.getInt(1);
	    }
	} catch (SQLException e) {
	    System.out.println(e);
	}

	return cacheCount > 0;
    }

    public int GetIncome(String location) {
	if (isInCache(location)) {
	    try {
		statement = connection.prepareStatement("select `income` from `places` where city = ?");
		statement.setString(1, location);
		ResultSet results = statement.executeQuery();

		if (results.next()) {
		    return results.getInt("income");
		}
	    } catch (SQLException e) {
		System.out.println(e);
	    }
	}

	return super.GetIncome(location);
    }
	
    public float GetDensity(String location) {
	if (isInCache(location)) {
	    try {
		statement = connection.prepareStatement("select `popdensity` from `places` where city = ?");
		statement.setString(1, location);
		ResultSet results = statement.executeQuery();

		if (results.next()) {
		    return results.getFloat("popdensity");
		}
	    } catch (SQLException e) {
		System.out.println(e);
	    }
	}

	return super.GetDensity(location);
    }
	
    public int GetLakes(String location) {
	if (isInCache(location)) {
	    try {
		statement = connection.prepareStatement("select `lakes` from `places` where city = ?");
		statement.setString(1, location);
		ResultSet results = statement.executeQuery();

		if (results.next()) {
		    return results.getInt("lakes");
		}
	    } catch (SQLException e) {
		System.out.println(e);
	    }
	}

	return super.GetLakes(location);
    }
	
    public float GetCrime(String location) {
	if (isInCache(location)) {
	    try {
		statement = connection.prepareStatement("select `crime` from `places` where city = ?");
		statement.setString(1, location);
		ResultSet results = statement.executeQuery();

		if (results.next()) {
		    return results.getFloat("crime");
		}
	    } catch (SQLException e) {
		System.out.println(e);
	    }
	}
	
	return super.GetCrime(location);
    }
	
    public String GetLocation(float lat, float lng) {
	String location = null;
	try {
	    statement = connection.prepareStatement("select city from `places` where latitude = ? and longitude = ?");
	    statement.setFloat(1, lat);
	    statement.setFloat(2, lng);
	    ResultSet results = statement.executeQuery();

	    while (results.next()) {
		location = results.getString("city");
	    }
	} catch (SQLException e) {
	    System.out.println(e);
	}

	boolean needToUpdate = false;

	if (location == null) {
	    System.out.printf("Fetching city for: '%s, %s'\n", lat, lng);
	    location = super.GetLocation(lat, lng);
	    needToUpdate = true;
	}
	else {
	    System.out.printf("Got cached city for: '%s, %s'\n", lat, lng);
	}

	if (isInCache(location)) {
	    if (needToUpdate) {
		try {
		    statement = connection.prepareStatement("select * from `places` where city = ? limit 1");
		    statement.setString(1, location);
		    ResultSet results = statement.executeQuery();

		    if (results.next()) {
			int income = results.getInt("income");
			float popdensity = results.getFloat("popdensity");
			int lakes = results.getInt("lakes");
			float crime = results.getFloat("crime");

			statement = connection.prepareStatement("insert into `places` (city, latitude, longitude, income, popdensity, lakes, crime) values(?,?,?,?,?,?,?)");
			statement.setString(1, location);
			statement.setFloat(2, lat);
			statement.setFloat(3, lng);
			statement.setInt(4, income);
			statement.setFloat(5, popdensity);
			statement.setInt(6, lakes);
			statement.setFloat(7, crime);

			statement.executeUpdate();
		    }
		} catch(SQLException e) {
		    System.out.println(e);
		}
	    }

	    System.out.printf("Got cached city information for: '%s'\n", location);

	    return location;
	}
	System.out.printf("Fetching city information for: '%s'\n", location);

        int income = super.GetIncome(location);
	float popdensity = super.GetDensity(location);
	int lakes = super.GetLakes(location);
	float crime = super.GetCrime(location);

	try {
	    statement = connection.prepareStatement("insert into `places` (city, latitude, longitude, income, popdensity, lakes, crime) values(?,?,?,?,?,?,?)");
	    statement.setString(1, location);
	    statement.setFloat(2, lat);
	    statement.setFloat(3, lng);
	    statement.setInt(4, income);
	    statement.setFloat(5, popdensity);
	    statement.setInt(6, lakes);
	    statement.setFloat(7, crime);

	    statement.executeUpdate();
	} catch (SQLException e) {
	    System.out.println(e);
	}

	return location;
    }
}
