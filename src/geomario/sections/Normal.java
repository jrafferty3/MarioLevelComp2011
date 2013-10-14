package geomario.sections;

import java.util.List;
import java.util.ArrayList;

import geomario.Section;
import geomario.Route;

import dk.itu.mario.level.MyLevel;

public class Normal extends Section {
    private List<Integer> elevations = new ArrayList<Integer>();

    public Normal(MyLevel level) {
	super(level);
	elevations = this.level.getRoute().getElevations();
        normalizeElevations();
    }

    public void makeSection(int length) {
	for (int x = 0; x < length; x++) {
	    for (int y = elevations.get(x); y < 15; y++) {
		if (getGroundLevel(x - 1) - 4 > y) {
		    // prevent areas that mario cannot jump over
		    y = getGroundLevel(x - 1) - 4;
		}
		setBlock(x, y, this.level.GROUND);
	    }
	}
    }

    private void normalizeElevations() {
	int max = 0;
	int min = Integer.MAX_VALUE;
	for (int x : elevations) {
	    if (x > max){
		max = x;
	    } else if(x < min) {
		min = x;
	    }
	}
	for (int i = 0; i < elevations.size() ; i++) {
	    int x = elevations.get(i);
	    elevations.set(i, 14+(x-min)*(4-14)/(max-min));
	}
    }                                                                                
}
