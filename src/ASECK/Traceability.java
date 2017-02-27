package ASECK;


import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.Stemmer;
import utility.StringProcess;
import DS.SOThread;

public class Traceability {
	

	public static int LinkPair2API(SOThread onethread,String aimAPI,String packagename,String project) throws Exception
	{
		
		String realpackagename=packagename.toLowerCase();
		if(realpackagename.contains("."))
			realpackagename=realpackagename.substring(realpackagename.lastIndexOf("."),realpackagename.length());
				
		Stemmer s=new Stemmer();
	    String ok=s.porterstem(aimAPI);
	
		int tracelabel1=0;
		
		//判断是否包含在代码中
		Hashtable<String,Integer> hscode=StringProcess.convertStringToVectorNoSplit(onethread.getCode());
		if(hscode.keySet().contains(ok))
		{
			tracelabel1=1;
		}
		
		
		String body=onethread.getQuestion_body();
		String title=onethread.getQuestion_title();
		
		String regexp1=".*(^|[a-z]+ |[\\.!?] |[\\(<])"+aimAPI+"([>\\)\\.,!?$]| [a-z]+).*";  //匹配单个单词
		Pattern p1=Pattern.compile(regexp1,Pattern.CASE_INSENSITIVE);
		Matcher m1=p1.matcher(body);
		boolean match1=m1.find();
		
		String regexp2="(?i).*\b"+realpackagename+"\\."+aimAPI+"\b.*";   //匹配全名
		Pattern p2=Pattern.compile(regexp2,Pattern.CASE_INSENSITIVE);
		Matcher m2=p2.matcher(body);
		boolean match2=m2.find();
		
		String regexp3=".*<code>.*\b"+aimAPI+"\b.*</code>.*";  //在code标签中
		Pattern p3=Pattern.compile(regexp3,Pattern.CASE_INSENSITIVE);
		Matcher m3_1=p3.matcher(body);
		boolean match3_1=m3_1.find();
		Matcher m3_2=p3.matcher(title);
		boolean match3_2=m3_2.find();
		
		String regexp4=".*<a.*href.*"+realpackagename+"/"+aimAPI+"\\.html.*>.*</a>.*";  //在链接中
		Pattern p4=Pattern.compile(regexp4,Pattern.CASE_INSENSITIVE);
		Matcher m4=p4.matcher(body);
		boolean match4=m4.find();
		
		String regexp5="(?i).*\b(a |an )"+aimAPI+"\b.*";   // an/a API
		Pattern p5=Pattern.compile(regexp5,Pattern.CASE_INSENSITIVE);
		Matcher m5=p5.matcher(title);
		boolean match5=m5.find();
		
		int tracelabel2=0;
		if(match1==true||match2==true||match3_1==true||match3_2==true||match4==true||match5==true)
		{
			tracelabel2=1;
		}
		
		int tracelabel3=0;
		if(project.equals("java")&&onethread.getQuestion_tag().toLowerCase().contains("java"))  //只搜索包含java或者android标签的问答对
		{
			tracelabel3=1;
		}
		else if(project.equals("android")&&onethread.getQuestion_tag().toLowerCase().contains("android"))
		{
			tracelabel3=1;
		}
		
		if(tracelabel1==1&&tracelabel2==1&&tracelabel3==1)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	 
	

}
