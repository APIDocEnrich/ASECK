package DS;

public class Similarity {
	
	int id1;
	int id2;
	float scenarioSimi;
	float codeLexicalSimi;
	float codeSemanticSimi;
	public int getId1() {
		return id1;
	}
	public void setId1(int id1) {
		this.id1 = id1;
	}
	public int getId2() {
		return id2;
	}
	public void setId2(int id2) {
		this.id2 = id2;
	}
	public float getScenarioSimi() {
		return scenarioSimi;
	}
	public void setScenarioSimi(float scenarioSimi) {
		this.scenarioSimi = scenarioSimi;
	}
	public float getCodeLexicalSimi() {
		return codeLexicalSimi;
	}
	public void setCodeLexicalSimi(float codeLexicalSimi) {
		this.codeLexicalSimi = codeLexicalSimi;
	}
	public float getCodeSemanticSimi() {
		return codeSemanticSimi;
	}
	public void setCodeSemanticSimi(float codeSemanticSimi) {
		this.codeSemanticSimi = codeSemanticSimi;
	}
	public Similarity(int id1, int id2, float scenarioSimi,
			float codeLexicalSimi, float codeSemanticSimi) {
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.scenarioSimi = scenarioSimi;
		this.codeLexicalSimi = codeLexicalSimi;
		this.codeSemanticSimi = codeSemanticSimi;
	}
	
	

}
