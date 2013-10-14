package geomario;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import dk.itu.mario.engine.sprites.Enemy;
import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.level.MyLevel;

public abstract class Section {
    protected MyLevel level;

    public Section(MyLevel level) {
	this.level = level;
    }

    public abstract void makeSection(int length);

    public boolean probabilitySelect(double probability) {
	return probability > this.level.getRandom().nextDouble();
    }

    public void setBlock(int x, int y, byte sprite) {
	this.level.setBlock(this.level.getCurrent() + x,  y, sprite);
    }
    
    public void setEnemy(int x, int y, int enemy, boolean mightHaveWings) {
	if (mightHaveWings) {
	    mightHaveWings = this.level.getRandom().nextBoolean();
	}

	this.level.setSpriteTemplate(this.level.getCurrent() + x,  y, new SpriteTemplate(enemy, mightHaveWings));
    }

    public void setJump(int x, int length) {
	if (getGroundLevel(x) > getGroundLevel(x + length) + 3) {
	    return;
	}

	for (int i = this.level.getCurrent() + x; i < this.level.getCurrent() + x + length; i++) {
	    for (int y = 0; y < this.level.getHeight(); y++) {
		setBlock(i, y, (byte) 0);
	    }
	}
    }

    public byte getBlock(int x, int y) {
	return this.level.getBlock(this.level.getCurrent() + x,  y);
    }

    public int getGroundLevel(int x) {
	for (int i = 0; i < this.level.getHeight(); i++) {
	    if (getBlock(x, i) == this.level.GROUND ||
		getBlock(x, i) == this.level.TUBE_TOP_LEFT ||
		getBlock(x, i) == this.level.TUBE_TOP_RIGHT ||
		getBlock(x, i) == this.level.ROCK) {

		return i;
	    }
	}

	/* no ground was found */
	return -1;
    }
}
