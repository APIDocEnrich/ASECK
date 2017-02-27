package ASECK;

import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import utility.Stemmer;
import utility.StringProcess;

import DS.SOThread;

public class ClusterRepresentation {
	  
    public static Hashtable<SOThread,Integer> SelectExemplarBasedonScore_Frequency(Vector<SOThread> allRelatedThread,String aimAPI) throws Exception
	{
		Hashtable<Integer,Vector<Integer>> ids=new Hashtable<Integer,Vector<Integer>>();
		for(int i=0;i<allRelatedThread.size();i++)
		{
			SOThread onethread=allRelatedThread.get(i);
			
			int clusterid=onethread.getClusterid();
			if(ids.keySet().contains(clusterid))
			{
				Vector<Integer> contain=ids.get(clusterid);
				contain.add(i);
				ids.remove(clusterid);
				ids.put(clusterid, contain);
			}
			else
			{
				Vector<Integer> contain=new Vector<Integer>();
				contain.add(i);
				ids.put(clusterid, contain);
			}
		}
				
		Hashtable<SOThread,Integer> recommend=new Hashtable<SOThread,Integer>();		
		Set<Integer> center=ids.keySet();
		for(int onecenter:center)
		{
			
			Vector<Integer> otherelement=ids.get(onecenter);
			Vector<metric> allmetrics=new Vector<metric>();
			int maxscore=-1;
			int maxfrequency=-1;
			 
			for(int threadid:otherelement)
			{
				SOThread thisthread=allRelatedThread.get(threadid);
				int userscore=thisthread.getAnswer_score();
				int fre=CheckingFrequency(thisthread.getCode(),aimAPI);
				if(userscore>maxscore)
				{
					maxscore=userscore;					 
				}
				if(fre>maxfrequency)
				{
					maxfrequency=fre;
				}
				metric onemetric=new metric(threadid,userscore,fre);
				allmetrics.add(onemetric);
			}
			
			int maxid=-1;
			float maxvalue=-1;
			for(metric onemetric:allmetrics)
			{
				float v1=0;
				float v2=0;
				if(maxscore!=0)
					v1=(float)onemetric.getUserscore()/maxscore;
				if(maxfrequency!=0)
				    v2=(float)onemetric.getFrequency()/maxfrequency;
				
				float thisvalue=v1+v2;
				if(thisvalue>maxvalue)
				{
					maxvalue=thisvalue;
					maxid=onemetric.getThreadid();
				}
			}
						
			recommend.put(allRelatedThread.get(maxid),otherelement.size());
			
		}
		

		return recommend;
		
	}
    
    public static int CheckingFrequency(String code,String API) throws Exception
    {
    	Hashtable<String,Integer> hs=StringProcess.convertStringToVectorNoSplit(code);
    	Stemmer s=new Stemmer();
	    String ok=s.porterstem(API);
	    if(hs.keySet().contains(ok))
	    {
	    	int directfre=hs.get(ok);
	    	int indirectfre=0;
	    	if(code.contains("\n"))
	    	{
	    		Vector<String> variable=new Vector<String>();
	    		String[] state=code.split("\n");
	    		for(String onestate:state)
	    		{
	    			onestate=onestate.toLowerCase();
	    			if(onestate.contains("new "+API)&&onestate.contains("="))
	    			{
	    				String front=onestate.substring(0, onestate.indexOf("=")).trim();
	    				if(front.contains(" "))
	    				{
	    					String var=front.substring(front.indexOf(" ")+1, front.length()).trim();
	    					if(!var.equals(API))
	    						variable.add(var);
	    				}
	    			}
	    			else if(onestate.contains("=")&&onestate.trim().startsWith(API))
	    			{
	    				String front=onestate.substring(0, onestate.indexOf("=")).trim();
	    				if(front.contains(" "))
	    				{
	    					String var=front.substring(front.indexOf(" ")+1, front.length()).trim();
	    					if(!var.equals(API))
	    						variable.add(var);
	    				}
	    			}
	    		}
	    		
	    		for(String onevar:variable)
	    		{
	    			Stemmer oneste=new Stemmer();
	    			String stemvar=oneste.porterstem(onevar);
	    			if(hs.keySet().contains(stemvar))
	    			{
	    				indirectfre+=hs.get(stemvar);
	    			}
	    		}
	    		
	    	}
	    	else
	    	{
	    		if(code.contains("new "+API)&&code.contains("="))
    			{
    				String front=code.substring(0, code.indexOf("=")).trim();
    				if(front.contains(" "))
    				{
    					front=front.substring(front.indexOf(" ")+1, front.length()).trim();
    					Stemmer oneste=new Stemmer();
    	    			String stemvar=oneste.porterstem(front);
	    				if(hs.keySet().contains(stemvar))
	    				{
	    					indirectfre+=hs.get(stemvar);
	    				}
    				}
    			}
	    		else if(code.contains("=")&&code.trim().startsWith(API))
	    		{
	    			String front=code.substring(0, code.indexOf("=")).trim();
    				if(front.contains(" "))
    				{
    					front=front.substring(front.indexOf(" ")+1, front.length()).trim();
    					Stemmer oneste=new Stemmer();
    	    			String stemvar=oneste.porterstem(front);
	    				if(hs.keySet().contains(stemvar))
	    				{
	    					indirectfre+=hs.get(stemvar);
	    				}
    				}
	    		}
	    		
	    	}
	    	return directfre+indirectfre;
	    }
	    else
	    {
	    	return 0;
	    }
    }
    
	
}



class metric
{
	int threadid;
	int userscore;
	int frequency;
	public int getThreadid() {
		return threadid;
	}
	public void setThreadid(int threadid) {
		this.threadid = threadid;
	}
	public int getUserscore() {
		return userscore;
	}
	public void setUserscore(int userscore) {
		this.userscore = userscore;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public metric(int threadid, int userscore, int frequency) {
		super();
		this.threadid = threadid;
		this.userscore = userscore;
		this.frequency = frequency;
	}
	
	
   
}
