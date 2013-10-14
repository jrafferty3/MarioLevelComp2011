package geomario.sections;

import java.util.Random;

import dk.itu.mario.level.MyLevel;
import dk.itu.mario.engine.sprites.Enemy;

import geomario.Section;

public class Coins extends Section {
    public Coins(MyLevel level) {
	super(level);
    }

    public void makeSection(int length) {
	Random r = this.level.getRandom();
	String income = this.level.IncomeLevel();
	String crime = this.level.CrimeLevel();
	String lakes = this.level.LakeLevel();

	double coinProbability = 0.0;
	if (income.equals("high")) {
	    coinProbability = 0.6;
	}
	else if (income.equals("medium")) {
	    coinProbability = 0.4;
	}
	else if (income.equals("low")) {
	    coinProbability = 0.2;
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
	    if (probabilitySelect(coinProbability)) {
		setBlock(x, getGroundLevel(x) - 4, this.level.BLOCK_COIN);

		int curr = 1;
		int max = 3;
		while (probabilitySelect(coinProbability - 0.2)) {
		    setBlock(x + curr, getGroundLevel(x) - 4, this.level.BLOCK_COIN);		    
		    curr++;
		    if (curr >= max) { break; }
		}

		x += curr;
	    }
	    else if (probabilitySelect(0.5)) {
		setBlock(x, getGroundLevel(x) - 4, this.level.BLOCK_EMPTY);
		setBlock(x + 1, getGroundLevel(x) - 4, this.level.BLOCK_EMPTY);
		setBlock(x + 2, getGroundLevel(x) - 4, this.level.BLOCK_EMPTY);

		if (probabilitySelect(0.5)) {
		    setBlock(x + 3, getGroundLevel(x) - 4, this.level.BLOCK_EMPTY);

		    x += 1;
		}
		x += 2;
	    }
	    else if (probabilitySelect(0.2)) {
		setBlock(x, getGroundLevel(x) - 4, this.level.BLOCK_POWERUP);
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
