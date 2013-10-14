package geomario.sections;

import java.util.Random;

import dk.itu.mario.level.MyLevel;
import dk.itu.mario.engine.sprites.Enemy;

import geomario.Section;

public class Pipes extends Section {
    public Pipes(MyLevel level) {
	super(level);
    }

    public void makeSection(int length) {
	Random r = this.level.getRandom();
	String density = this.level.DensityLevel();
	String crime = this.level.CrimeLevel();
	String lakes = this.level.LakeLevel();

	double pipeProbability = 0.0;
	if (density.equals("high")) {
	    pipeProbability = 0.6;
	}
	else if (density.equals("medium")) {
	    pipeProbability = 0.4;
	}
	else if (density.equals("low")) {
	    pipeProbability = 0.2;
	}

	boolean someHaveWings = false;
	double enemyProbability = 0.0;
	if (crime.equals("high")) {
	    enemyProbability = 0.7;
	    someHaveWings = true;
	}
	else if (crime.equals("medium")) {
	    enemyProbability = 0.5;
	}
	else if (crime.equals("low")) {
	    enemyProbability = 0.2;
	}

	double jumpProbability = 0.0;
	if (lakes.equals("high")) {
	    jumpProbability = 0.7;
	}
	else if (lakes.equals("medium")) {
	    jumpProbability = 0.5;
	}
	else if (lakes.equals("low")) {
	    jumpProbability = 0.3;
	}

	boolean lastWasAJumpSoDontMakeTheNextOneAlsoAJumpPlease = true;
	for (int x = 0; x < length - 3; x += r.nextInt(2) + 4) {
	    if (probabilitySelect(pipeProbability)) {
		lastWasAJumpSoDontMakeTheNextOneAlsoAJumpPlease = false;
		int k;
		for (k = 1; getGroundLevel(x - k) < 0; k++);

		int height = getGroundLevel(x - k) - r.nextInt(5);
		if (height <= 0) {
		    height = 1;
		}

		for (int i = 0; i < height; i++) {
		    setBlock(x, i, (byte) 0);
		    setBlock(x + 1, i, (byte) 0);
		}

		setBlock(x, height, this.level.TUBE_TOP_LEFT);
		setBlock(x + 1, height, this.level.TUBE_TOP_RIGHT);

		if (probabilitySelect(enemyProbability - 0.1) && (getGroundLevel(x) > 3)) {
		    setEnemy(x, getGroundLevel(x), 4, false);
		}

		for (int i = height + 1; i < this.level.getHeight(); i++) {
		    setBlock(x, i, this.level.TUBE_SIDE_LEFT);
		    setBlock(x + 1, i, this.level.TUBE_SIDE_RIGHT);
		}

		if (probabilitySelect(0.4)) {
		    int rocksWidth = 3;

		    if (getGroundLevel(x + 2) == getGroundLevel(x + 3) &&
			getGroundLevel(x + 2) == getGroundLevel(x + 4)) {
			setBlock(x + 2, getGroundLevel(x + 2) - 1, this.level.ROCK);
			setBlock(x + 3, getGroundLevel(x + 3) - 1, this.level.ROCK);
			setBlock(x + 3, getGroundLevel(x + 3) - 1, this.level.ROCK);
			setBlock(x + 4, getGroundLevel(x + 4) - 1, this.level.ROCK);

			if (probabilitySelect(0.2)) {
			    setBlock(x + 4, getGroundLevel(x + 4) - 1, this.level.ROCK);
			}
		    }
		}
	    }
	    else if (probabilitySelect(jumpProbability) && !lastWasAJumpSoDontMakeTheNextOneAlsoAJumpPlease) {
		lastWasAJumpSoDontMakeTheNextOneAlsoAJumpPlease = true;
		setJump(x, r.nextInt(6) + 1);
	    }
	    else if (probabilitySelect(enemyProbability)) {
		lastWasAJumpSoDontMakeTheNextOneAlsoAJumpPlease = false;
		setEnemy(x, getGroundLevel(x) - 1, r.nextInt(4), someHaveWings);
	    }

	}
    }
}
