package ASECK;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;


import DS.APIWholeName;
import DS.SOThread;

public class Frame {
	
	public static void main(String args[]) throws Exception
	{
 		Framework("java");
 		Framework("android");		 
	}
	
 
    public static void Framework(String project) throws Exception
    {
    	BufferedWriter bw=new BufferedWriter(new FileWriter("E:\\APIdoc\\result\\"+project+"_statistic.csv",true));	 
		Vector<APIWholeName> allAPI1=APIExtractor.FindRealClass(project);
        Vector<APIWholeName> allAPI=APIExtractor.postProcessAPI(allAPI1);
        for(int ind=0;ind<allAPI.size();ind++)	 
		{
        	APIWholeName onewholeAPI=allAPI.get(ind);
			String line=onewholeAPI.getClassname().trim();
			String aimAPI=line.trim().toLowerCase();
			String packagename=onewholeAPI.getPackagename().trim();
			System.out.print(aimAPI+",");
			Vector<SOThread> allRelatedThread=new Vector<SOThread>();
			IndexReader reader= DirectoryReader.open(FSDirectory.open(Paths.get("F:\\so\\pairs\\")));
			IndexSearcher searcher= new IndexSearcher(reader);
						 
			String field="answer_body"; 
			Analyzer analyzer=new StandardAnalyzer();
			QueryParser parser = new QueryParser(field, analyzer);
		    Query query = parser.parse(aimAPI); 
		    TopDocs results = searcher.search(query,5000000);
		    
		    ScoreDoc[] hits = results.scoreDocs;
		    int numTotalHits = results.totalHits;
		  
		    int clusternum=0;
		    for(int i=0;i<numTotalHits;i++)
		    {
		    	Document doc = searcher.doc(hits[i].doc);
		    	
				String Title_q = doc.get("question_title");	
				String id=doc.get("id");
				String Tags_q=doc.get("question_tag");
				String Score_q=doc.get("question_score");
				String Score_a=doc.get("answer_score");
				String viewcount=doc.get("viewcount");
				String Body_q=doc.get("question_body");
				String Body_a=doc.get("answer_body");
				
				int qscore=Integer.parseInt(Score_q);
				int ascore=Integer.parseInt(Score_a);
				int viewtime=Integer.parseInt(viewcount);

				SOThread onethread=new SOThread(id,Title_q,Body_q,qscore,Tags_q,Body_a,ascore,viewtime,"","","",-1);
				
				String scenario=Title_q;                                              //拿title当作是使用场景
				String code=GenerateRightCode.rightCode(onethread);                   //从抽取出所有的代码样例，如果是debug/corrective类型，则生成代码补丁之后的正确代码
				String explanation=ThreadProcessor.ExtractExplanation(Body_a,code);   //根据谭琳的文章，抽取代码样例前面一段的文本当作是解释,并且强制必须有和代码重合的单词
				onethread.setScenario(scenario);
				onethread.setCode(code);
				onethread.setExplanation(explanation);
						 
				if(Traceability.LinkPair2API(onethread, aimAPI, packagename, project)==1)      //根据ICSE'15的论文找到和某个API相关的问答对，同时要求tag标签必须包含java或者android，在代码中包含此API
				{
		    		 allRelatedThread.add(onethread); 
				}
				
				 

		    }
		    reader.close();
		    
		    System.out.print(allRelatedThread.size()+",");     //记录个数
		    bw.write(aimAPI+","+allRelatedThread.size()+",");
		    
		    
		    Dumpfile.dumpRelatedThread(allRelatedThread, "E:\\APIdoc\\result\\seachresult\\"+project+"\\"+aimAPI+"\\");
		    
		    
		    Hashtable<SOThread,Integer> recommend=new Hashtable<SOThread,Integer>();
		    if(allRelatedThread.size()>1)
		    {
		   	    ThreadCluster.Cluster(allRelatedThread);
		   	    HashSet<Integer> clusterids=new HashSet<Integer>();
		   	    for(SOThread onethread:allRelatedThread)
		   	    {
		   	    	clusterids.add(onethread.getClusterid());
		   	    }
		   	    clusternum=clusterids.size();
			    recommend.putAll(ClusterRepresentation.SelectExemplarBasedonScore_Frequency(allRelatedThread,aimAPI));    
	        }
		    else if(allRelatedThread.size()==1)
		    {
		    
		    	recommend.put(allRelatedThread.get(0),1);
		    	clusternum=1;
		    }
		    
		    int recommendsize=recommend.size();
		    if(recommend.size()>10)
		    	recommendsize=10;
		    System.out.println(clusternum+","+recommendsize);
		    bw.write(clusternum+","+recommendsize);

			Dumpfile.dumpRecommendPair(recommend, "E:\\APIdoc\\result\\recommend\\"+project+"\\"+aimAPI+"\\");
  
		    bw.newLine(); 
		    bw.flush();
		
		   
	   }
	   bw.close();
	   
	   InfoEmbedding.embeding(project);
    }
}
