package ASECK;


import java.util.HashSet;
import java.util.Hashtable;


import utility.StringProcess;


public class ThreadProcessor {

	
	public static String ExtractExplanation(String ss,String code) throws Exception
	{
		StringBuilder sb=new StringBuilder();
		if(ss.contains("<pre>"))
		{
			String front=ss.substring(0, ss.indexOf("<pre>"));
			if(!front.isEmpty())
			{
				if(front.contains("<p>"))
				{
					front=front.substring(front.lastIndexOf("<p>"), front.length());
					sb.append(front);
				}
				else
				{
				    sb.append(front);
				}
				
			}
			
		}
		
		String explanation=sb.toString();
		while(true)
	    {
		   if(explanation.contains("<")&&explanation.contains(">")&&explanation.indexOf("<")<explanation.indexOf(">"))
		   {
			   explanation=explanation.substring(0, explanation.indexOf("<"))+" "+explanation.substring(explanation.indexOf(">")+1, explanation.length());
		   }
		   else if(explanation.contains("<")&&!explanation.contains(">"))
	       {
			   explanation=explanation.substring(0, explanation.indexOf("<"));
		   }
		   else if(!explanation.contains("<")&&explanation.contains(">"))
		   {
			   explanation=explanation.substring(explanation.indexOf(">")+1, explanation.length());
		   }
		   else
			   break;
	   }
		Hashtable<String,Integer> hscode=StringProcess.convertStringToVectorNoSplit(code);
		Hashtable<String,Integer> hsexplan=StringProcess.convertStringToVectorNoSplit(explanation);
		
		int label=0;
		HashSet<String> allkeys=new HashSet<String>();
		allkeys.addAll(hscode.keySet());
		allkeys.addAll(hsexplan.keySet());
		for(String onekey:allkeys)
		{
			if(hscode.keySet().contains(onekey)&&hsexplan.keySet().contains(onekey))
			{
				label=1;
				break;
			}
		}
		
		if(label==1)
			return explanation;	
		else
			return "";
	}
	
	
	 
	
	 

}
