package DS;

import java.util.List;

public class Triplet {
	
	int id;
	String API;
	int clusterid;
	String scenario;
	String explanation;
	List<String> code;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAPI() {
		return API;
	}
	public void setAPI(String aPI) {
		API = aPI;
	}
	public int getClusterid() {
		return clusterid;
	}
	public void setClusterid(int clusterid) {
		this.clusterid = clusterid;
	}
	public String getScenario() {
		return scenario;
	}
	public void setScenario(String scenario) {
		this.scenario = scenario;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public List<String> getCode() {
		return code;
	}
	public void setCode(List<String> code) {
		this.code = code;
	}
	public Triplet(int id, String aPI, int clusterid, String scenario,
			String explanation, List<String> code) {
		super();
		this.id = id;
		API = aPI;
		this.clusterid = clusterid;
		this.scenario = scenario;
		this.explanation = explanation;
		this.code = code;
	}
	 
	 
	

}
