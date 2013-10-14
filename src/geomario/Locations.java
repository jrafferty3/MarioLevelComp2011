package geomario;

import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.level.MyLevel;

import java.util.ArrayList;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

public class Locations{
    private String[] locs = {"Atlanta,Ga","Cheyenne,Wy","Anchorage,Ak","New York,Ny","San Francisco,Ca","Detroit,Mi"};
    private float[][] coord = {{33.7489f,84.3881f},
			       {41.1400f,104.8197f},
			       {61.147046f,149.746399f},
			       {40.66f,73.94f},
			       {37.7750f,122.4183f},
			       {42.38f,83.1f}};
    private boolean[][] matrix = {{true,true,false,true},
				  {true,false,false,false},
				  {true,true,false,false},
				  {false,false,true,true},
				  {false,true,true,true},
				  {false,true,true,false}};
    private Map<String,float[]> map = new HashMap<String,float[]>();
    private GamePlay pm;
    private int death_by_enemies,total_jumps,collected_coins,available_coins,total_time,completion_time;
    private double aimless,death_by_falling;
    private boolean difficulty,jumps,coins,time;
    private Random random;
    private MyLevel level;
	private boolean newplayer;
	
    public Locations(GamePlay playerMetrics, MyLevel level){
	if(playerMetrics == null){
		newplayer = true;
		return;
	}
	pm = playerMetrics;
	death_by_enemies = pm.timesOfDeathByRedTurtle+pm.timesOfDeathByGoomba+pm.timesOfDeathByGreenTurtle+pm.timesOfDeathByArmoredTurtle+pm.timesOfDeathByJumpFlower+pm.timesOfDeathByCannonBall+pm.timesOfDeathByChompFlower;
	aimless = pm.aimlessJumps;
	total_jumps = pm.jumpsNumber;
	death_by_falling = pm.timesOfDeathByFallingIntoGap;
	collected_coins = pm.coinsCollected+pm.coinBlocksDestroyed;
	available_coins = pm.totalCoins+pm.totalCoinBlocks;
	total_time = pm.totalTime;
	completion_time = pm.completionTime;
	this.level = level;
	for(int i=0;i<locs.length;i++){
	    map.put(locs[i],coord[i]);
	}
    }

    public float[][] getAllCoords() {
	return this.coord;
    }

	
    public float[] GetLocations(){
	if(newplayer){
	float[] f = {33.77389f, -84.392053f, 33.773997f, -84.396914f};
	return f;
	}
	
	String[] ret = new String[2];
	float[] coords = new float[4];
	ArrayList<String> t = new ArrayList<String>();
	ArrayList<String> d = new ArrayList<String>();
	ArrayList<String> j = new ArrayList<String>();
	ArrayList<String> c = new ArrayList<String>();
	
	//calculating the different boolean variables
	if(death_by_enemies >= 2){
	    difficulty = false;
	}else{
	    difficulty = true;
	}
		
	if((int)aimless*2>=total_jumps && death_by_falling <=1){
	    jumps = true;
	}else{
	    jumps = false;
	}
		
	if((double)collected_coins/(double)available_coins >= 0.7){
	    coins = true;
	}else{
	    coins = false;
	}
		
	if(completion_time <= 75 && total_time <= 200 && death_by_falling+death_by_enemies < 3){
	    time = false;
	}else{
	    time = true;
	}
		
	//adding the cities that satisfy the boolean variables
	for(int i=0;i<locs.length;i++){
	    if(matrix[i][0] == time)
		t.add(locs[i]);
	}
		
	for(int i=0;i<locs.length;i++){
	    if(matrix[i][1] == difficulty)
		d.add(locs[i]);
	}
		
	for(int i=0;i<locs.length;i++){
	    if(matrix[i][2] == jumps)
		j.add(locs[i]);
	}
		
	for(int i=0;i<locs.length;i++){
	    if(matrix[i][3] == coins)
		c.add(locs[i]);
	}
		
	//count the number of times each city occurs
	int[] occ = new int[locs.length];
		
	for(int i=0;i<locs.length;i++){
	    if(t.contains(locs[i]))
		occ[i] = occ[i]+1;
	    if(d.contains(locs[i]))
		occ[i] = occ[i]+1;
	    if(j.contains(locs[i]))
		occ[i] = occ[i]+1;
	    if(c.contains(locs[i]))
		occ[i] = occ[i]+1;
	}
		
	//first city is the best fit
	int max = 0;
	int index = 0;
	for(int i=0;i<occ.length;i++){
	    if(occ[i]>max){
		max = occ[i];
		index = i;
	    }
	}

	ret[0] = locs[index];
	occ[index] = 0;
	max = 0;
		
	for(int i=0;i<occ.length;i++){
	    if(occ[i]>max){
		max = occ[i];
		index = i;
	    }
	}
		
	//second city is next best fit
	//choose random if multiple next best fits
	ArrayList<String> second = new ArrayList<String>();
	for(int i=0;i<occ.length;i++){
	    if(occ[i] == max)
		second.add(locs[i]);
	}
		
	random = level.getRandom();
	ret[1] = second.get(random.nextInt(second.size()));

	coords[0] = map.get(ret[0])[0];
	coords[1] = map.get(ret[0])[1];
	coords[2] = map.get(ret[1])[0];
	coords[3] = map.get(ret[1])[1];
	return coords;
		
    }
	
	
}
