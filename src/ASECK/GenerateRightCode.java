package ASECK;

import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.SimilarityCal;
import utility.StringProcess;

import DS.SOThread;

public class GenerateRightCode { 
	
	
	public static String rightCode(SOThread onethread) throws Exception
	{
		Vector<String> buggycode=IsException(onethread);
		Vector<String> purecode=ExtractCode(onethread.getAnswer_body());
		if(buggycode.isEmpty())
		{
			StringBuilder sb=new StringBuilder();
			for(String onestate:purecode)
			{
				sb.append(onestate+"\n");
			}
			return sb.toString();
		}
		else
		{
			String result=Cmpbuggy_fix(onethread,buggycode,purecode);
			return result;
		}
	}

	
	
	public static Vector<String> IsException(SOThread onethread)
	{
		
		String s=onethread.getQuestion_body();
		Vector<String> result=new Vector<String>();	
		if(s.contains("<pre>"))
		{
			String[] candicode=s.split("<pre>");
			for(String onecandi:candicode)
			{
				if(onecandi.contains("</pre>"))
				{
					String purecode=onecandi.substring(0, onecandi.indexOf("</pre>"));
					purecode=purecode.replace("<code>", "");
					purecode=purecode.replace("</code>", "");
					result.add(purecode);
				}
			}
		}
		else if(s.contains("<pre "))
		{
			String[] candicode=s.split("<pre |<pre>");
			for(String onecandi:candicode)
			{
				if(onecandi.contains("</pre>"))
				{
					String purecode=onecandi.substring(0, onecandi.indexOf("</pre>"));
					purecode=purecode.replace("<code>", "");
					purecode=purecode.replace("</code>", "");
					result.add(purecode);
				}
			}
		}
		
		int isexcelabel1=0;
		Vector<String> needreturn=new Vector<String>();
		for(String codeinquestion:result)
		{
			String setcodeinquestion = codeinquestion.replace("\n","");
	        
			String regexp1="(Exception|Error:)(.)*?\\([A-Z][a-z0-9]*\\.java:[0-9]*\\)";        
			Pattern p1=Pattern.compile(regexp1,Pattern.CASE_INSENSITIVE);
			Matcher m1=p1.matcher(setcodeinquestion);
			boolean javamatches=m1.find();
			
			String regexp2="(INFO|DEBUG|WARN|ERROR|FATAL|VERBOSE)/(.)*?\\((\\s+){0,1}[0-9]*\\)";
			Pattern p2=Pattern.compile(regexp2,Pattern.CASE_INSENSITIVE);
			Matcher m2=p2.matcher(setcodeinquestion);
			boolean androidmatches=m2.find();
			
			if(javamatches==true||androidmatches==true)
			{
				
				isexcelabel1=1;       
			}
			else
			{
				needreturn.add(codeinquestion);
			}
			
			 
		}
		
		
		int isexcelabel2=0;
		String title=onethread.getQuestion_title().toLowerCase();
		if(title.contains("error")||title.contains("exception")||title.contains("fail")||title.contains("issue"))
		{
			isexcelabel2=1;
		}

		if(isexcelabel1==1||isexcelabel2==1)
		{
//			System.out.println(onethread.getId()+" "+isexcelabel1+"  "+isexcelabel2);
			return needreturn;
		}
		else
		{
			return new Vector<String>();
		}
		
		   
	}
	
	
	public static Vector<String> ExtractCode(String s)
	{
		Vector<String> allpure=new Vector<String>();
			
		if(s.contains("<pre>"))
		{
			String[] candicode=s.split("<pre>");
			for(String onecandi:candicode)
			{
				if(onecandi.contains("</pre>"))
				{
					String purecode=onecandi.substring(0, onecandi.indexOf("</pre>"));
					purecode=purecode.replace("<code>", "");
					purecode=purecode.replace("</code>", "");
//					result.append(purecode+"\n");
					allpure.add(purecode);
				}
			}
		}
		else if(s.contains("<pre "))
		{
			String[] candicode=s.split("<pre |<pre>");
			for(String onecandi:candicode)
			{
				if(onecandi.contains("</pre>"))
				{
					String purecode=onecandi.substring(0, onecandi.indexOf("</pre>"));
					purecode=purecode.replace("<code>", "");
					purecode=purecode.replace("</code>", "");
//					result.append(purecode+"\n");
					allpure.add(purecode);
				}
			}
		}
		
		Vector<String> result=new Vector<String>();	
		for(String onepure:allpure)
		{
			StringBuilder exceptimport=new StringBuilder();
			if(onepure.contains("\n"))
			{
				String[] allstate=onepure.split("\n");
				for(String onestate:allstate)
				{
					if(!onestate.trim().startsWith("import")&&!onestate.trim().startsWith("public class")&&!onestate.trim().startsWith("//")&&!onestate.trim().startsWith("at")&&!onestate.trim().startsWith("Exception"))
					{
						exceptimport.append(onestate+"\n");
					}
				}
			}
			else
			{
				if(!onepure.trim().startsWith("import")&&!onepure.trim().startsWith("public class")&&!onepure.trim().startsWith("//")&&!onepure.trim().startsWith("at")&&!onepure.trim().startsWith("Exception"))
				{
					exceptimport.append(onepure+"\n");
				}
			}
			
			
			String proonepure = onepure.replace("\n","");
			
			String regexp1="(Exception|Error:)(.)*?\\([A-Z][a-z0-9]*\\.java:[0-9]*\\)";        
			Pattern p1=Pattern.compile(regexp1,Pattern.CASE_INSENSITIVE);
			Matcher m1=p1.matcher(proonepure);
			boolean javamatches=m1.find();
			
			String regexp2="(V|D|W|I|E|F|INFO|DEBUG|WARN|ERROR|FATAL|VERBOSE)/(.)*?\\((\\s+){0,1}[0-9]*\\)";
			Pattern p2=Pattern.compile(regexp2,Pattern.CASE_INSENSITIVE);
			Matcher m2=p2.matcher(proonepure);
			boolean androidmatches=m2.find();
			
			if(javamatches==false&&androidmatches==false&&!exceptimport.toString().trim().isEmpty())
			{
				result.add(onepure);
			}
		}
		

		return result;
	}


    public static String Cmpbuggy_fix(SOThread onethread,Vector<String> buggycode,Vector<String> purecode) throws Exception
    {
	
    	String realpatch=FindRealPatch(onethread,purecode);
    	
    	if(!realpatch.trim().isEmpty())	
    	{
	    	Vector<String> allrightfix=new Vector<String>();
	    	
			String[] allfix=realpatch.split("\n");
			for(String onebuggy:buggycode)
			{
				Vector<Integer> matchindex=new Vector<Integer>();
				
				String[] allbuggy=onebuggy.split("\n");
				for(int i=0;i<allfix.length;i++)
				{
					for(int j=0;j<allbuggy.length;j++)
					{
						if(IsTureState(allfix[i])==1&&IsTureState(allbuggy[j])==1&&SimilarityCal.calEditSimi(allfix[i], allbuggy[j])>=0.8)
						{
//							System.err.println(allfix[i]+" <<<  "+allbuggy[j]);
							matchindex.add(j);
						}
					}
				}
				
				float per=(float)matchindex.size()/(float)allfix.length;
				   			
				if(matchindex.size()>0&&per>=0.5f)
				{
//					System.out.println(onethread.getId());
					int min=Integer.MAX_VALUE;
					int max=-1;
					for(int oneindex:matchindex)
					{
						if(oneindex>max)
							max=oneindex;
						if(oneindex<min)
							min=oneindex;
					}
//					System.err.println(min+"  "+max);
					for(int i=0;i<min;i++)
					{
						allrightfix.add(allbuggy[i]);
					}
					for(int i=0;i<allfix.length;i++)
					{
						if(!allrightfix.contains(allfix[i]))
							allrightfix.add(allfix[i]);
					}
					for(int i=max+1;i<allbuggy.length;i++)
					{
						allrightfix.add(allbuggy[i]);
					}
				}
				else
				{
					for(int i=0;i<allfix.length;i++)
					{
						if(!allrightfix.contains(allfix[i]))
							allrightfix.add(allfix[i]);
					}
				}
				
			}
	    	
	    	
	    	StringBuilder sb=new StringBuilder();
	    	for(String onefix:allrightfix)
	    	{
	    		sb.append(onefix+"\n");
	    	}
	    	 
	    	String code=sb.toString();
	    	code=code.replace("&lt;", "<");
	    	code=code.replace("&gt;", ">");

	    	return code;   
    	}
    	else
    	{
    		return "";
    	}
    }
    
    
    public static int IsTureState(String onestate) throws Exception
    {
    	Hashtable<String,Integer> hs=StringProcess.convertStringToVectorNoSplit(onestate);
    	if(hs.size()>=2)
    	{
    		return 1;
    	}
    	else
    	{
    		return 0;
    	}
    	
    }
    
    
    public static String FindRealPatch(SOThread onethread,Vector<String> purecode) throws Exception
    {
    	String answer=onethread.getAnswer_body();
    	if(answer.contains("change")&&answer.contains("to"))
    	{
    		String bottom=answer.substring(answer.indexOf("change"), answer.length());
    		if(bottom.contains("to"))
    			bottom=bottom.substring(bottom.indexOf("to"), bottom.length());
    		Vector<String> patches=ExtractCode(bottom);
    		StringBuilder sb=new StringBuilder();
    		for(String onepatch:patches)
    		{
    			sb.append(onepatch+"\n");
    		}
    		return sb.toString();
    	}
    	
    	else if(answer.contains("instead")&&answer.contains("of"))
    	{
    		String bottom=answer.substring(0, answer.indexOf("instead"));
    		Vector<String> patches=ExtractCode(bottom);
    		StringBuilder sb=new StringBuilder();
    		for(String onepatch:patches)
    		{
    			sb.append(onepatch+"\n");
    		}
    		return sb.toString();
    	}
    	
    	else if(answer.contains("rather than"))
    	{
    		String bottom=answer.substring(0, answer.indexOf("rather than"));
    		Vector<String> patches=ExtractCode(bottom);
    		StringBuilder sb=new StringBuilder();
    		for(String onepatch:patches)
    		{
    			sb.append(onepatch+"\n");
    		}
    		return sb.toString();
    	}
    	
    	else if(answer.contains("correct"))
    	{
        	String bottom=answer.substring(answer.indexOf("correct"), answer.length());
        	Vector<String> patches=ExtractCode(bottom);
        	StringBuilder sb=new StringBuilder();
    		for(String onepatch:patches)
    		{
    			sb.append(onepatch+"\n");
    		}
    		return sb.toString();
    	}
    		
    	else
    	{
    		if(purecode.size()>0)
    		{
	    		String onecode=purecode.get(purecode.size()-1);
	    		return onecode;
    		}
    		else
    		{
    			return "";
    		}
    	}
       	
    }
}
