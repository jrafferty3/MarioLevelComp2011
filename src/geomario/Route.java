package geomario;

import java.util.List;
import java.util.ArrayList;

import it.units.GoogleCommon.GeocodeException;
import it.units.GoogleCommon.Location;
import it.units.GoogleElevation.ElevationRequestor;
import it.units.GoogleElevation.ElevationResponse;
import it.units.GoogleElevation.Result;

public class Route {
    private List<Location> locations = new ArrayList<Location>();
    private List<Integer> elevations = new ArrayList<Integer>();

    public Route(float startLatitude, float startLongitude, float endLatitude, float endLongitude) {

        Location l;
        float lat, lng;

	// calculate intermediate locations

	for(int i = 0; i < 50; i++) {
            l = new Location();

            lat = startLatitude + (i/50.0f) * (endLatitude - startLatitude);
            lng = startLongitude + (i/50.0f) * (endLongitude - startLongitude);

            l.setLat(lat);
            l.setLng(lng);

            locations.add(l);
        }

	// calculate each elevation

	ArrayList<Integer> temp = new ArrayList<Integer>();
        ElevationRequestor requestor = new ElevationRequestor();
        ElevationResponse elevationResponse;
        try {
            elevationResponse = requestor.getElevations(locations);
            for (Result result : elevationResponse.getResults()){
                temp.add((int) result.getElevation());
            }
        } catch (GeocodeException ex) {

        }
		
	for(int i=0;i<temp.size();i++){
	    for(int j=0;j<6;j++)
		elevations.add(temp.get(i));
	}
    }

    public List<Location> getLocations() {
	return this.locations;
    }

    public List<Integer> getElevations() {
	return this.elevations;
    }

    public String toString() {
	return "" + this.locations;
    }
} 
