package geomario.sections;

import dk.itu.mario.level.MyLevel;

import geomario.Section;

public class Goal extends Section {

    public Goal(MyLevel level) {
	super(level);
    }

    public void makeSection(int length) {
	int floor = 14 - this.level.getRandom().nextInt(4);
	this.level.setExit(this.level.getCurrent() + 8, floor);

	// fills the end piece
	for (int x = this.level.getCurrent(); x < length; x++) {
	    for (int y = 0; y < 15; y++) {
		if (y >= floor) {
		    this.level.setBlock(x, y, this.level.GROUND);
		}
	    }
	}
    }
}
