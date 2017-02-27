package ASECK;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import DS.SOThread;

public class Dumpfile {
	
	
	public static void dumpRelatedThread(Vector<SOThread> allRelatedThread,String path) throws Exception
	{
		if(allRelatedThread.size()>0)
		{
		
			File ff=new File(path);
		    if(!ff.exists())
		    {
		    	ff.mkdirs();
		    }
			
			for(SOThread onethread:allRelatedThread)
			{
				String Title_q=onethread.getQuestion_title();
				if(Title_q.contains("<"))
		      		Title_q=Title_q.replace("<", " ");
		      	if(Title_q.contains(">"))
		      		Title_q=Title_q.replace(">", " ");
		      	if(Title_q.contains("|"))
		      		Title_q=Title_q.replace("|", " ");
		      	if(Title_q.contains("/"))
		      		Title_q=Title_q.replace("/", " ");
		      	if(Title_q.contains("\\"))
		      		Title_q=Title_q.replace("\\", " ");
		      	if(Title_q.contains(":"))
		      		Title_q=Title_q.replace(":", " ");
		      	if(Title_q.contains("*"))
		      		Title_q=Title_q.replace("*", " ");
		      	if(Title_q.contains("?"))
		      		Title_q=Title_q.replace("?", " ");
		      	if(Title_q.contains("\""))
		      		Title_q=Title_q.replace("\"", " ");
		      	
				BufferedWriter bw=new BufferedWriter(new FileWriter(path+"\\"+Title_q+".txt"));
				bw.write(onethread.getQuestion_tag()+"\n----------\n"+onethread.getQuestion_score()+" "+onethread.getAnswer_score()+" "+onethread.getViewcount()+"\n----------\n"+onethread.getQuestion_title()+"\n----------\n"+onethread.getQuestion_body()+"\n----------\n"+onethread.getAnswer_body());
				bw.close();
			}
		}
	    
	}
	
	
	
	public static void dumpRecommendPair(Hashtable<SOThread,Integer> recommend,String path) throws Exception
	{
		if(recommend.size()>0)
		{
		    File f=new File(path);
			if(!f.exists())
			{
			    f.mkdirs();
			}
			Hashtable<SOThread,Integer> duprec=new Hashtable<SOThread,Integer>();
			duprec.putAll(recommend);
			
			Vector<SOThread> rankedrecommend=new Vector<SOThread>();
			while(recommend.size()>0)
			{
				int maxvalue=0;
				Vector<SOThread> up=new Vector<SOThread>();
				Set<SOThread> keyset=recommend.keySet();
				for(SOThread onekey:keyset)
				{
					int value=recommend.get(onekey);
					if(value>maxvalue)
					{
						maxvalue=value;
						up.add(onekey);
					}
				}
				rankedrecommend.add(up.get(up.size()-1));
				recommend.remove(up.get(up.size()-1));
				up.clear();
			}
			
			
			int i=0;
			for(SOThread onethread:rankedrecommend)
			{
		      	String codes=onethread.getCode();
		    	codes=codes.replace("&lt;", '<'+""); 
		    	codes=codes.replace("&gt;", '>'+"");
				BufferedWriter bw=new BufferedWriter(new FileWriter(path+"\\"+i+"_"+duprec.get(onethread)+".txt"));
				bw.write("----------usage scenario----------\n"+onethread.getScenario().trim()+"\n----------Code Sample----------\n"+codes.trim());
				bw.close();
				i++;
			}
		}
	}
	
	
	 

}
