package dk.itu.mario.level;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import dk.itu.mario.MarioInterface.GamePlay;

import geomario.sections.*;
import geomario.Section;
import geomario.WolfDB;
import geomario.Route;
import geomario.Locations;
import geomario.util.Test;

public class MyLevel extends RandomLevel {
    private Random random;
    private ArrayList<Integer> elevations = new ArrayList<Integer>();
    private WolfDB wolframalpha;
    private Route route;
    private Locations locations;
    private int current;
    private float density, lake, income, crime;
    private float density1, density2;
    private int lake1, lake2;
    private int income1, income2;
    private float crime1, crime2;

    public MyLevel(int width, int height) {
	super(width, height);
    }

    public MyLevel(int width, int height, long seed, int difficulty,
		   int type, GamePlay playerMetrics) {
	this(width, height);

	//seed = 1;
	random = new Random(seed);

	wolframalpha = new WolfDB(); // use the cache version of Wolf
	locations = new Locations(playerMetrics, this);

	float[] mapLocations = locations.GetLocations();
	route = new Route(mapLocations[0], mapLocations[1],
			  mapLocations[2], mapLocations[3]);

	String loc1 = wolframalpha.GetLocation(mapLocations[0],mapLocations[1]);
	String loc2 = wolframalpha.GetLocation(mapLocations[2],mapLocations[3]);
	//calculate avg density
	density1 = wolframalpha.GetDensity(loc1);
	density2 = wolframalpha.GetDensity(loc2);
	density = (density1+density2)/2f;
	//calculate avg lakes
	lake1 = wolframalpha.GetLakes(loc1);
	lake2 = wolframalpha.GetLakes(loc2);
	lake = (lake1+lake2)/2f;
	//calculate avg income
	income1 = wolframalpha.GetIncome(loc1);
	income2 = wolframalpha.GetIncome(loc2);
	income = (income1+income2)/2f;
	//calculate avg crime
	crime1 = wolframalpha.GetCrime(loc1);
	crime2 = wolframalpha.GetCrime(loc2);
	crime = (crime1+crime2)/2f;
	wolframalpha.close();

	System.out.printf("Generating map using seed: %d\n", seed);
	System.out.printf("Population Density: %s\n", DensityLevel());
	System.out.printf("Lakes: %s\n", LakeLevel());
	System.out.printf("Income: %s\n", IncomeLevel());
	System.out.printf("Crime: %s\n", CrimeLevel());
	
	/* initialize pre-defined sections */

	Normal normal = new Normal(this);
	Goal goal = new Goal(this);
	Pipes pipes = new Pipes(this);
	Plain plain = new Plain(this);
	Coins coins = new Coins(this);

	/* generate a "plain" map of elevations until we get
	   to where we want to start our goal strip */
	current = 0;

	int goalBegin = this.width - 20;
	normal.makeSection(goalBegin);

	/* generate each of the consecutive sections, on the
	   same area as the plain normal map */

	int sectionWidth = 20;
	while (current < goalBegin) {
	    if (current == 0) {
		// safe at beginning of game
		normal.makeSection(sectionWidth);
		current += sectionWidth;

		continue;
	    }

	    int section = this.random.nextInt(2);

	    if (!Test.noHazards) {
		switch (section) {
		case 0: pipes.makeSection(sectionWidth);
		    break;
		case 1: coins.makeSection(sectionWidth);
		    break;
		}
	    }

	    current += sectionWidth;
	}

	/* generate the goal strip and actual goal, that goes to
	   the end of the map */
	goal.makeSection(this.width);

	/* helper function from original codebase, that converts ground
	   into the appropriate barrier for its location */

	fixWalls();
    }

    public int getCurrent() {
	return this.current;
    }

    public Random getRandom() {
	return this.random;
    }

    public WolfDB getWolframAlpha() {
	return this.wolframalpha;
    }

    public Route getRoute() {
	return this.route;
    }

    public void setExit(int xPosition, int yPosition) {
	this.xExit = xPosition;
	this.yExit = yPosition;
    }

    public void addEnemy(){
	super.ENEMIES++;
    }
	
    public void addCoinBlock(){
	super.BLOCKS_COINS++;
    }
	
    public void addCoin(){
	super.COINS++;
    }
	
    public String DensityLevel(){
	if(density < 3000){
	    return "low";
	}else if(density > 6000){
	    return "high";
	}else{
	    return "medium";
	}
    }
	
    public String LakeLevel(){
	if(lake < 5){
	    return "low";
	}else if(lake > 15){
	    return "high";
	}else{
	    return "medium";
	}
    }
	
    public String IncomeLevel(){
	if(income < 30000){
	    return "low";
	}else if(income > 35600){
	    return "high";
	}else{
	    return "medium";
	}
    }
	
    public String CrimeLevel(){
	if(crime < 1.5){
	    return "low";
	}else if(crime > 1.7){
	    return "high";
	}else{
	    return "medium";
	}
    }

    private void fixWalls()
    {
	boolean[][] blockMap = new boolean[width + 1][height + 1];

	for (int x = 0; x < width + 1; x++)
	    {
		for (int y = 0; y < height + 1; y++)
	            {
	                int blocks = 0;
	                for (int xx = x - 1; xx < x + 1; xx++)
			    {
				for (int yy = y - 1; yy < y + 1; yy++)
				    {
					if (getBlockCapped(xx, yy) == GROUND){
					    blocks++;
					}
				    }
			    }
	                blockMap[x][y] = blocks == 4;
	            }
	    }
	blockify(this, blockMap, width + 1, height + 1);
    }

    private void blockify(Level level, boolean[][] blocks,
			  int width, int height) {
	int to = 0;

	boolean[][] b = new boolean[2][2];

	for (int x = 0; x < width; x++)
	    {
		for (int y = 0; y < height; y++)
	            {
	                for (int xx = x; xx <= x + 1; xx++)
			    {
				for (int yy = y; yy <= y + 1; yy++)
				    {
					int _xx = xx;
					int _yy = yy;
					if (_xx < 0) _xx = 0;
					if (_yy < 0) _yy = 0;
					if (_xx > width - 1) _xx = width - 1;
					if (_yy > height - 1) _yy = height - 1;
					b[xx - x][yy - y] = blocks[_xx][_yy];
				    }
			    }

	                if (b[0][0] == b[1][0] && b[0][1] == b[1][1])
			    {
				if (b[0][0] == b[0][1])
				    {
					if (b[0][0])
					    {
						level.setBlock(x, y, (byte) (1 + 9 * 16 + to));
					    }
					else
					    {
						if (getBlock(x, y) == GROUND) {
						    level.setBlock(x, y, ROCK);
						}
					    }
				    }
				else
				    {
					if (b[0][0])
					    {
						//down grass top?
						level.setBlock(x, y, (byte) (1 + 10 * 16 + to));
					    }
					else
					    {
						//up grass top
						level.setBlock(x, y, (byte) (1 + 8 * 16 + to));
					    }
				    }
			    }
	                else if (b[0][0] == b[0][1] && b[1][0] == b[1][1])
			    {
				if (b[0][0])
				    {
					//right grass top
					level.setBlock(x, y, (byte) (2 + 9 * 16 + to));
				    }
				else
				    {
					//left grass top
					level.setBlock(x, y, (byte) (0 + 9 * 16 + to));
				    }
			    }
	                else if (b[0][0] == b[1][1] && b[0][1] == b[1][0])
			    {
				level.setBlock(x, y, (byte) (1 + 9 * 16 + to));
			    }
	                else if (b[0][0] == b[1][0])
			    {
				if (b[0][0])
				    {
					if (b[0][1])
					    {
						level.setBlock(x, y, (byte) (3 + 10 * 16 + to));
					    }
					else
					    {
						level.setBlock(x, y, (byte) (3 + 11 * 16 + to));
					    }
				    }
				else
				    {
					if (b[0][1])
					    {
						//right up grass top
						level.setBlock(x, y, (byte) (2 + 8 * 16 + to));
					    }
					else
					    {
						//left up grass top
						level.setBlock(x, y, (byte) (0 + 8 * 16 + to));
					    }
				    }
			    }
	                else if (b[0][1] == b[1][1])
			    {
				if (b[0][1])
				    {
					if (b[0][0])
					    {
						//left pocket grass
						level.setBlock(x, y, (byte) (3 + 9 * 16 + to));
					    }
					else
					    {
						//right pocket grass
						level.setBlock(x, y, (byte) (3 + 8 * 16 + to));
					    }
				    }
				else
				    {
					if (b[0][0])
					    {
						level.setBlock(x, y, (byte) (2 + 10 * 16 + to));
					    }
					else
					    {
						level.setBlock(x, y, (byte) (0 + 10 * 16 + to));
					    }
				    }
			    }
	                else
			    {
				level.setBlock(x, y, (byte) (0 + 1 * 16 + to));
			    }
	            }
	    }
    }
	    
}
