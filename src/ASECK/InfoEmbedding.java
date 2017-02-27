package ASECK;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

import DS.APIWholeName;



public class InfoEmbedding {

	
	public static void embeding(String project) throws Exception
	{
		//java是在这个的前面加上相应的代码样例： <div class="summary">
		//android是在这个的前面加上相应的代码样例：<div class="jd-descr"> 
		
		
		String label1="<div class=\"summary\">";
		String label2="<div class=\"jd-descr\">";
		
		
		Vector<APIWholeName> allAPI1=APIExtractor.FindRealClass(project);  
        Vector<APIWholeName> allAPI=APIExtractor.postProcessAPI(allAPI1);
        
        Vector<String> canRec=new Vector<String>();
        File root=new File("E:\\APIdoc\\result\\recommend\\"+project+"\\");
        File[] apilist=root.listFiles();
        for(File oneapi:apilist)
        {
        	canRec.add(oneapi.getName());
        }
        int number=0;
		for(APIWholeName onewholeAPI:allAPI)
		{
			String line1=onewholeAPI.getClassname().trim();
		
			String aimAPI=line1.trim().toLowerCase();
			String packagename=onewholeAPI.getPackagename().trim();
			if(packagename.contains("."))
				packagename=packagename.replace(".", "\\");
			if(canRec.contains(aimAPI))
			{
				
				Vector<Vector<String>> allscen=new Vector<Vector<String>>();
				File[] scenlist=new File("E:\\APIdoc\\result\\recommend\\"+project+"\\"+aimAPI).listFiles();
        		int max=scenlist.length;
        		if(scenlist.length>10)
        			max=10;
        		for(int i=0;i<max;i++)
        		{
        			String path="";
        			for(File onescen:scenlist)
        			{
        				if(onescen.getName().startsWith(i+"_"))
        				{
        					path=onescen.getName();
        					break;
        				}
        			}
        			Vector<String> content=new Vector<String>();
        			BufferedReader br=new BufferedReader(new FileReader("E:\\APIdoc\\result\\recommend\\"+project+"\\"+aimAPI+"\\"+path));
        			String line="";
        			while((line=br.readLine())!=null)
        			{
        				content.add(line);
        			}
        			br.close();
        			
        			 
        			
        			allscen.add(content);
        		}
				
	
				String path1="";
				if(project.equals("java"))
					path1="E:\\APIdoc\\enricheddoc\\javadocs\\api\\"+packagename+"\\"+line1+".html";
				else
					path1="E:\\APIdoc\\enricheddoc\\androiddocs\\reference\\"+packagename+"\\"+line1+".html";
				
				String replacedline=line1.replace(".", "\\");
				String path2="";
				if(project.equals("java"))
					path2="E:\\APIdoc\\enricheddoc\\javadocs\\api\\"+replacedline+".html";
				else
					path2="E:\\APIdoc\\enricheddoc\\androiddocs\\reference\\"+replacedline+".html";
				 
				
				
				File needcheck1=new File(path1);
				File needcheck2=new File(path2);
				if(needcheck1.exists())
				{
					System.err.println(project+" : "+line1+"   "+number);
					
					Vector<String> content=new Vector<String>();
					BufferedReader br=new BufferedReader(new FileReader(needcheck1));
	    			String line="";
	    			while((line=br.readLine())!=null)
	    			{
	    				content.add(line);
	    			}
	    			br.close();
	    			
	    			int insertloc=-1;
	    			int count=0;
	    			for(int i=0;i<content.size();i++)
	    			{
	    				String oneline=content.get(i).trim();
	    				if(oneline.equals(label1))
	    				{
	    					insertloc=i;
	    					break;
	    				}
	    				else if(oneline.equals(label2))
	    				{
	    				   	count++;
	    				   	if(count==2)
	    				   	{
	    				   		insertloc=i;
	    				   		break;
	    				   	}
	    				}
	    			}
	    			
	    			
	    			needcheck1.delete();
	    			File newfile=new File(path1);
	    			BufferedWriter bw=new BufferedWriter(new FileWriter(newfile));
	    			for(int i=0;i<insertloc;i++)
	    			{
	    				bw.write(content.get(i));
	    				bw.newLine();
	    			}
	    			
	    			bw.write("<h2>Commonly used usage scenarios with corresponding code samples:</h2>");
	    			bw.newLine();
	    			int in=1;
	    			for(Vector<String> onescen:allscen)
	    			{
	    				bw.write("*******************<b>usage scenario "+in+"</b>**********************");
	    			    bw.newLine();
	    			    bw.write("<br>");
	    			    bw.newLine();
	    				
	    				for(int j=0;j<onescen.size();j++)
	    				{
	    					String oneline=onescen.get(j);
    						bw.write(oneline+"<br>");
	    					bw.newLine();
	    				}
	    				bw.write("*******************<b>end of usage scenario "+in+"</b>*****************");
	    			    bw.newLine();
	    			    bw.write("<br><br>");
	    				bw.newLine();
	    				in++;
	    			}
	    			
	    			
	    			for(int i=insertloc;i<content.size();i++)
	    			{
	    				bw.write(content.get(i));
	    				bw.newLine();
	    			}
	    			bw.close();
	    			
				}
				else if(needcheck2.exists())
				{
					System.out.println(project+" : "+line1+"   "+number);
					Vector<String> content=new Vector<String>();
					BufferedReader br=new BufferedReader(new FileReader(needcheck2));
	    			String line="";
	    			while((line=br.readLine())!=null)
	    			{
	    				content.add(line);
	    			}
	    			br.close();
	    			
	    			int insertloc=-1;
	    			int count=0;
	    			for(int i=0;i<content.size();i++)
	    			{
	    				String oneline=content.get(i).trim();
	    				if(oneline.equals(label1))
	    				{
	    					insertloc=i;
	    					break;
	    				}
	    				else if(oneline.equals(label2))
	    				{
	    				   	count++;
	    				   	if(count==2)
	    				   	{
	    				   		insertloc=i;
	    				   		break;
	    				   	}
	    				}
	    			}
	    			
	    			
	    			needcheck2.delete();
	    			File newfile=new File(path2);
	    			BufferedWriter bw=new BufferedWriter(new FileWriter(newfile));
	    			for(int i=0;i<insertloc;i++)
	    			{
	    				bw.write(content.get(i));
	    				bw.newLine();
	    			}
	    			
	    			bw.write("<h2>Commonly used usage scenarios and corresponding code samples:</h2>");
	    			bw.newLine();
	    			int in=1;
	    			for(Vector<String> onescen:allscen)
	    			{
	    				bw.write("*******************<b>usage scenario "+in+"</b>**********************");
	    			    bw.newLine();
	    			    bw.write("<br>");
	    			    bw.newLine();
	    				
	    				for(int j=0;j<onescen.size();j++)
	    				{
	    					String oneline=onescen.get(j);
    						bw.write(oneline+"<br>");
	    					bw.newLine();
	    				}
	    				bw.write("*******************<b>end of usage scenario "+in+"</b>*****************");
	    			    bw.newLine();
	    			    bw.write("<br><br>");
	    				bw.newLine();
	    				in++;
	    			}
	    			
	    			
	    			for(int i=insertloc;i<content.size();i++)
	    			{
	    				bw.write(content.get(i));
	    				bw.newLine();
	    			}
	    			bw.close();
				}
				
				 
			    number++;	
			}
		}
	}
	
	
	
	
	 
}
