package DS;

public class SOThread {
	
	
	String id;
	String question_title;
	String question_body;
	int question_score;
	String question_tag;
	String answer_body;
	int answer_score;
	int viewcount;
	String scenario;
	String code;
	String explanation;
	int clusterid;
	
	
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuestion_title() {
		return question_title;
	}
	public void setQuestion_title(String questionTitle) {
		question_title = questionTitle;
	}
	public String getQuestion_body() {
		return question_body;
	}
	public void setQuestion_body(String questionBody) {
		question_body = questionBody;
	}
	public int getQuestion_score() {
		return question_score;
	}
	public void setQuestion_score(int questionScore) {
		question_score = questionScore;
	}
	public String getQuestion_tag() {
		return question_tag;
	}
	public void setQuestion_tag(String questionTag) {
		question_tag = questionTag;
	}
	public String getAnswer_body() {
		return answer_body;
	}
	public void setAnswer_body(String answerBody) {
		answer_body = answerBody;
	}
	public int getAnswer_score() {
		return answer_score;
	}
	public void setAnswer_score(int answerScore) {
		answer_score = answerScore;
	}
	public int getViewcount() {
		return viewcount;
	}
	public void setViewcount(int viewcount) {
		this.viewcount = viewcount;
	}
	public SOThread(String id, String questionTitle, String questionBody,
			int questionScore, String questionTag, String answerBody,
			int answerScore, int viewcount, String scenario, String code,String explanation,int clusterid) {
		super();
		this.id = id;
		this.question_title = questionTitle;
		this.question_body = questionBody;
		this.question_score = questionScore;
		this.question_tag = questionTag;
		this.answer_body = answerBody;
		this.answer_score = answerScore;
		this.viewcount = viewcount;
		this.scenario = scenario;
		this.code = code;
		this.explanation=explanation;
		this.clusterid=clusterid;
	}
	 


}
