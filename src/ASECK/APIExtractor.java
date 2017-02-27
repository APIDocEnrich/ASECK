package ASECK;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Vector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utility.StringProcess;

import DS.APIWholeName;

public class APIExtractor {

	
    public static Vector<APIWholeName> FindRealClass(String project) throws Exception
    {
    	if(project.equals("java"))
    	{
	    	Vector<APIWholeName> allrealclass=new Vector<APIWholeName>();
			File input = new File("E:/APIdoc/data/javadocs/api/overview-summary.html");
			Document doc = Jsoup.parse(input, "UTF-8", "");//解析HTML字符串返回一个Document实现
			Elements links =doc.getElementsByClass("colFirst");
			for (Element link : links) 
			{
				 
				  String linkHref = link.getElementsByTag("a").attr("href");
				  String packagename = link.text();
				  
				  if(!packagename.equals("Package"))
				  {
					  File f=new File("E:/APIdoc/data/javadocs/api/"+linkHref);
					  if(f.exists())
					  {
//						  allrealclass.add(new APIWholeName(packagename,""));
						  
						  Document everypackage=Jsoup.parse(f,"UTF-8","");
						  Elements tables=everypackage.getElementsByTag("table");
						  for(Element onetable:tables)
						  {
							  String types=onetable.getElementsByTag("caption").text();
							  if(types.startsWith("Class"))
							  {
								  Elements realclass=onetable.getElementsByClass("colFirst");
								  for (Element oneclass : realclass) 
								  {
									  String classname=oneclass.text();
									  if(!classname.equals("Class")&&!classname.trim().isEmpty())
									  {
	//									  System.out.println(packagename+"."+classname);
										  allrealclass.add(new APIWholeName(packagename,classname));
//										  System.err.println(classname+" sss");
									  }
								  }
								  
							  }
						  }
						  
					  }
				  }
			}
			
			 
			return allrealclass;
    	}
    	
    	
    	
    	
    	else 
    	{
	    	Vector<APIWholeName> allrealclass=new Vector<APIWholeName>();
	    	File input = new File("E:/APIdoc/data/androiddocs/reference/packages.html");
			Document doc = Jsoup.parse(input, "UTF-8", "");
			Elements links =doc.getElementsByClass("jd-linkcol");
			for (Element link : links) 
			{
				 
				  String linkHref = link.getElementsByTag("a").attr("href");
				  linkHref=linkHref.substring(3, linkHref.length());
				  String packagename = link.text();
				 
				  File f=new File("E:/APIdoc/data/androiddocs/"+linkHref);
				  if(f.exists()&&packagename.startsWith("android"))
				  {
	//				  System.out.println(linkHref);
					  
//					   allrealclass.add(new APIWholeName(packagename,""));
					  StringBuilder sb=new StringBuilder();
					  BufferedReader br=new BufferedReader(new FileReader(f));
					  String line="";
					  while((line=br.readLine())!=null)
					  {
						  sb.append(line+"  ");
					  }
					  br.close();
					  
					  String content=sb.toString();
					  if(content.contains("<li><h2>Classes</h2>"))
					  {
						  String sub=content.substring(content.indexOf("<li><h2>Classes</h2>"), content.length());
						  sub=sub.substring(0, sub.indexOf("</ul>"));
						  String[] subsub=sub.split("</a>");
						  for(String onesubsub:subsub)
						  {
							  onesubsub=onesubsub.substring(onesubsub.lastIndexOf(">")+1, onesubsub.length());
							  onesubsub=onesubsub.trim();
							  if(!onesubsub.isEmpty())
							  {
								  allrealclass.add(new APIWholeName(packagename,onesubsub));
//								  System.out.println(onesubsub);
							  }
						  }
					  }
					  
				  }
				   
			}
			
			
			
	    	return allrealclass;
    	}
    }

    public static Vector<APIWholeName> postProcessAPI(Vector<APIWholeName> allrealclass) throws Exception
    {	
    	 
    	
    	
    	Vector<APIWholeName> duprealclass=new Vector<APIWholeName>();  //rank
    	for(APIWholeName onename:allrealclass)
    	{
    		String classname=onename.getClassname();
    		 
    		if(StringProcess.tokenSplit(classname).size()>2)    //先装multi-word API
    		{
    			duprealclass.add(onename);
    			 
    		}
    		
    	}
    	for(APIWholeName onename:allrealclass)
    	{
    		String classname=onename.getClassname();
    		 
    		if(StringProcess.tokenSplit(classname).size()<=2)  //在装single-word API
    		{
    			duprealclass.add(onename);
    			 
    		}
    		
    	}
    	
    	
    	Vector<APIWholeName> result=new Vector<APIWholeName>();  //disambiguous
    	HashSet<String> contain=new HashSet<String>();
    	Vector<String> samename=new Vector<String>();
    	for(APIWholeName onename:duprealclass)
    	{
    		String classname=onename.getClassname();
    		if(contain.contains(classname))
    			samename.add(classname);
    		contain.add(classname);
    	}
    	
    	for(APIWholeName onename:duprealclass)
    	{
    		String classname=onename.getClassname();
    		if(samename.contains(classname))
    		{
    			String newclassname=onename.getPackagename()+"."+onename.getClassname();
    			APIWholeName newonename=new APIWholeName(onename.getPackagename(),newclassname);
    			result.add(newonename);
    		}
    		else
    		{
    			result.add(onename);
    		}
    	}
    	
    	
    	return result;
    }
    
   

}
