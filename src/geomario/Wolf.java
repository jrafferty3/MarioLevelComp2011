package geomario;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

public class Wolf{
    private static String appid = "AKE8R5-JK4HH7LH9Q";
    private WAEngine engine;
	
    public Wolf(){
	engine = new WAEngine();
	engine.setAppID(appid);
        engine.addFormat("plaintext");
    }
	
    public int GetPop(String location){
	int population = 0;
	WAQuery query = engine.createQuery();
	query.setInput(location);
	try {
            WAQueryResult queryResult = engine.performQuery(query);
            if (queryResult.isError()) {
            } else if (!queryResult.isSuccess()) {
            } else {
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        if(pod.getTitle().equals("Populations")){
			    for (WASubpod subpod : pod.getSubpods()) {
				for (Object element : subpod.getContents()) {
				    if (element instanceof WAPlainText) {
					String s = ((WAPlainText) element).getText().substring(18);
					s = s.substring(0, s.indexOf(" "));
					population = Integer.parseInt(s);
				    }
				}
			    }
                        }
                    }
                }
            }
        } catch (WAException e) {
            e.printStackTrace();
        }
	return population;
    }
	
    public int GetIncome(String location){
	int income = 0;
	WAQuery query = engine.createQuery();
	query.setInput(location+" amount of per capita income");
	try {
            WAQueryResult queryResult = engine.performQuery(query);
            
            if (queryResult.isError()) {
            } else if (!queryResult.isSuccess()) {
            } else {
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        if(pod.getTitle().equals("Result")){
			    for (WASubpod subpod : pod.getSubpods()) {
				for (Object element : subpod.getContents()) {
				    if (element instanceof WAPlainText) {
					String s = (((WAPlainText) element).getText().substring(1,((WAPlainText) element).getText().indexOf(" ")));
					income = Integer.parseInt(s);
				    }
				}
			    }
                        }
                    }
                }
            }
        } catch (WAException e) {
            e.printStackTrace();
        }
	return income;
    }
	
    public float GetDensity(String location){
	float density = 0f;
	WAQuery query = engine.createQuery();
	query.setInput(location+" population density");
	try {
            WAQueryResult queryResult = engine.performQuery(query);
            
            if (queryResult.isError()) {
            } else if (!queryResult.isSuccess()) {
            } else {
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        if(pod.getTitle().equals("Result")){
			    for (WASubpod subpod : pod.getSubpods()) {
				for (Object element : subpod.getContents()) {
				    if (element instanceof WAPlainText) {
					String s = (((WAPlainText) element).getText().substring(0,((WAPlainText) element).getText().indexOf(" ")));
					density = Float.parseFloat(s);
				    }
				}
			    }
                        }
                    }
                }
            }
        } catch (WAException e) {
            e.printStackTrace();
        }
	return density;
    }
	
    public int GetLakes(String location){
	int lakes = 0;
	WAQuery query = engine.createQuery();
	query.setInput(location.substring(location.indexOf(",")+1)+" bodies of water");
	try {
            WAQueryResult queryResult = engine.performQuery(query);
            
            if (queryResult.isError()) {
            } else if (!queryResult.isSuccess()) {
            } else {
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        if(pod.getTitle().equals("Result")){
				int count = 0;
				boolean total = false;
			    for (WASubpod subpod : pod.getSubpods()) {
				count++;
				for (Object element : subpod.getContents()) {
				    if (element instanceof WAPlainText) {
					if(((WAPlainText) element).getText().contains("total")){
						String s = (((WAPlainText) element).getText().substring(((WAPlainText) element).getText().indexOf("total")+7,((WAPlainText) element).getText().indexOf(")")));
						lakes = Integer.parseInt(s);
					}else{
						total = true;
					}
				    }
				}
			    }
				if(total){
					lakes = count;
				}
                        }
                    }
                }
            }
        } catch (WAException e) {
            e.printStackTrace();
        }
	return lakes;
    }
	
    public float GetCrime(String location){
	float crime = 0;
	WAQuery query = engine.createQuery();
	query.setInput(location+" crime rate");
	try {
            WAQueryResult queryResult = engine.performQuery(query);
            
            if (queryResult.isError()) {
            } else if (!queryResult.isSuccess()) {
            } else {
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        if(pod.getTitle().equals("Comparisons")){
			    for (WASubpod subpod : pod.getSubpods()) {
				for (Object element : subpod.getContents()) {
				    if (element instanceof WAPlainText) {
					String s = ((WAPlainText) element).getText();
					if(s.indexOf("national") != -1)
						crime = Float.parseFloat(s.substring(0,s.indexOf(" ")));
				    }
				}
			    }
                        }
                    }
                }
            }
        } catch (WAException e) {
            e.printStackTrace();
        }
	return crime;
    }
	
    public String GetLocation(float lat, float lng){
	String city = "";
	String state = "";
	WAQuery query = engine.createQuery();
	if(lng<0){
	    lng = -lng;
	}
	query.setInput(lat+" N ,"+lng+" W city");
	try {
            WAQueryResult queryResult = engine.performQuery(query);
            
            if (queryResult.isError()) {
            } else if (!queryResult.isSuccess()) {
            } else {
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        if(pod.getTitle().contains("Nearest city")){
			    for (WASubpod subpod : pod.getSubpods()) {
				for (Object element : subpod.getContents()) {
				    if (element instanceof WAPlainText) {
						city = (((WAPlainText) element).getText().substring(0,((WAPlainText) element).getText().indexOf(",")));
						state = ((WAPlainText) element).getText().substring(((WAPlainText) element).getText().indexOf(",")+2);
						state = state.substring(0,state.indexOf(","));
					}
				}
			    }
                        }
                    }
                }
            }
        } catch (WAException e) {
            e.printStackTrace();
        }
	return city+","+state;
    }


}
