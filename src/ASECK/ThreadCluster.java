package ASECK;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;




import DS.SOThread;
import DS.Similarity;

 

import utility.SimilarityCal;
import utility.StringProcess;

public class ThreadCluster {

	
	public static Vector<Similarity> Cluster(Vector<SOThread> allRelatedThread) throws Exception
	{
		//查找idf
		Hashtable<String,Integer> scenarioidf=new Hashtable<String,Integer>();
		for(SOThread onethread:allRelatedThread)
		{
			String scenario=onethread.getScenario()+" "+onethread.getExplanation();
			Hashtable<String,Integer> hs=StringProcess.convertStringToVectorNoSplit(scenario);
			Set<String> keys=hs.keySet();
			for(String onekey:keys)
			{
				if(scenarioidf.keySet().contains(onekey))
				{
					int count=scenarioidf.get(onekey);
					count++;
					scenarioidf.remove(onekey);
					scenarioidf.put(onekey, count);
				}
				else
				{
					scenarioidf.put(onekey, 1);
				}
			}
			
			
		}
				
		Hashtable<String,Float> newscenarioidf=new Hashtable<String,Float>();
		Set<String> newkeys=scenarioidf.keySet();
		for(String onekey:newkeys)
		{
			float result=(float) Math.log(allRelatedThread.size()/scenarioidf.get(onekey));
			newscenarioidf.put(onekey, result);
		}
		
		Hashtable<String,Integer> codeidf=new Hashtable<String,Integer>();
		for(SOThread onethread:allRelatedThread)
		{
			String scenario=onethread.getCode();
			Hashtable<String,Integer> hs=StringProcess.convertStringToVectorNoSplit(scenario);
			Set<String> keys=hs.keySet();
			for(String onekey:keys)
			{
				if(codeidf.keySet().contains(onekey))
				{
					int count=codeidf.get(onekey);
					count++;
					codeidf.remove(onekey);
					codeidf.put(onekey, count);
				}
				else
				{
					codeidf.put(onekey, 1);
				}
			}
			
			
		}
				
		Hashtable<String,Float> newcodeidf=new Hashtable<String,Float>();
		newkeys=codeidf.keySet();
		for(String onekey:newkeys)
		{
			float result=(float) Math.log(allRelatedThread.size()/codeidf.get(onekey));
			newcodeidf.put(onekey, result);
		}
		
		
		
		Vector<Similarity> allsimi=new Vector<Similarity>();		
		for(int i=0;i<allRelatedThread.size();i++)
		{
			for(int j=i+1;j<allRelatedThread.size();j++)
			{
				Similarity wholeSimi=SOThreadSimi(i,j,allRelatedThread.get(i), allRelatedThread.get(j), newscenarioidf,newcodeidf);
				
				allsimi.add(wholeSimi);
			}
		}
		
		
		//查找归一化的最大值
		float maxScenario=-1;
		float maxCodeLexical=-1;
		float maxCodeSemantic=-1;
		
		for(Similarity onesimi:allsimi)
		{
			if(onesimi.getScenarioSimi()>maxScenario)
			{
				maxScenario=onesimi.getScenarioSimi();
			}
			
			if(onesimi.getCodeLexicalSimi()>maxCodeLexical)
			{
				maxCodeLexical=onesimi.getCodeLexicalSimi();
			}
			
			if(onesimi.getCodeSemanticSimi()>maxCodeSemantic)
			{
			    maxCodeSemantic=onesimi.getCodeSemanticSimi();	
			}
		}
		
		
		//聚类
		BufferedWriter bw=new BufferedWriter(new FileWriter("E:\\APIdoc\\cluster\\input.txt"));
    	for(Similarity onesimi:allsimi)
    	{
    		 
    		float norscenario=onesimi.getScenarioSimi()/maxScenario;
    		if(Float.isNaN(norscenario))
    		{
    			norscenario=0f;
    		}
    		
    		float norcodelex=onesimi.getCodeLexicalSimi()/maxCodeLexical;
    		if(Float.isNaN(norcodelex))
    		{
    			norcodelex=0f;
    		}
    		
     		float norcodesem=onesimi.getCodeSemanticSimi()/maxCodeSemantic;
     		if(Float.isNaN(norcodesem))
     		{
     			norcodesem=0f;
     		}
    		
    		float whole=norscenario+norcodelex+norcodesem;
    		
//    		System.out.println(norscenario+"  "+norcodelex+"  "+norcodesem);		 
			bw.write((onesimi.getId1()+1)+" "+(onesimi.getId2()+1)+" "+whole);
			bw.newLine();
    	}
    	bw.close();
    	
	 
	    File ff=new File("E:\\APIdoc\\cluster\\result.txt");
		if(ff.exists())
		{
			ff.delete();
			
		}
		
		Runtime rt = Runtime.getRuntime();
		Process p=rt.exec("cmd /c cd /d E:\\APIdoc\\cluster\\ & matlab -nosplash -nodesktop -nodisplay -r runf");
		p.waitFor();
	
		



		File f=new File("E:\\APIdoc\\cluster\\result.txt");
		while(true)
		{
			if(f.exists())
			{
				BufferedReader br=new BufferedReader(new FileReader("E:\\APIdoc\\cluster\\result.txt"));
				String s="";
				int index=0;
				while((s=br.readLine())!=null)
				{
					s=s.trim();
					BigDecimal db = new BigDecimal(s);
					String sss=db.toPlainString();
					if(sss.contains("."))
					{
						sss=sss.substring(0, sss.indexOf("."));
					}
					int k=Integer.parseInt(sss);
					
					k=k-1;
					
					SOThread newf=allRelatedThread.get(index);
					newf.setClusterid(k);
					
					allRelatedThread.remove(index);
					allRelatedThread.add(index, newf);
					
					
					index++;
				}
				br.close();
				break;
			}
			else
			{
				Thread.sleep(1000);
			}
		}
		
		 
		return allsimi;
	}
	
	
	public static Similarity SOThreadSimi(int id1,int id2,SOThread onethread,SOThread anotherthread,Hashtable<String,Float> scenarioidf,Hashtable<String,Float> codeidf) throws Exception
	{
		 
		String scenario1=onethread.getScenario()+" "+onethread.getExplanation();
		String code1=onethread.getCode();
		String scenario2=anotherthread.getScenario()+" "+onethread.getExplanation();
		String code2=anotherthread.getCode();
		
		float simi1=ScenarioSimi(scenario1,scenario2,scenarioidf);
		float simi2=CodeLexicalSimi(code1,code2,codeidf);
		float simi3=CodeSemanticSimi(code1,code2);
		
//		System.out.println(id1+" "+id2+"  "+simi1+"  "+simi2+" "+simi3);
		Similarity onesimi=new Similarity(id1,id2,simi1,simi2,simi3);
		return onesimi;
	}
	
	
	public static float ScenarioSimi(String s1,String s2,Hashtable<String,Float> scenarioidf) throws Exception
	{
	    float simi=0;
	    simi=SimilarityCal.calVSMSimi(s1, s2, scenarioidf);
	    return simi;
	}
	
	public static float CodeLexicalSimi(String code1,String code2,Hashtable<String,Float> codeidf) throws Exception
	{
		float simi=0;
		simi=SimilarityCal.calVSMSimi(code1, code2, codeidf);
		return simi;
	}
	
	public static float CodeSemanticSimi(String code1,String code2)
	{
		float simi=0;
		
		String Transs1=transform(code1);
		String Transs2=transform(code2);
		
		int LCS=LCScompute(Transs1,Transs2);
		int min=Math.min(Transs1.length(),Transs2.length());
	
		if(min!=0)
			simi=(float)LCS/min;
		return simi;		
	}
	public static String transform(String codes)
	{
		
		String code=codes;
		code=code.replace("\n", " ");
		code=code.replace("{", " ");
		code=code.replace("}", " ");
		code=code.replace("(", " ");
		code=code.replace(")", " ");
		code=code.replace("[", " ");
		code=code.replace("]", " ");
		code = code.replaceAll("\\<.*?>", ""); 
		
		Vector<String> keywords=new Vector<String>();
		keywords.add("boolean");
		keywords.add("byte");
		keywords.add("char");
		keywords.add("double");
		keywords.add("enum");
		keywords.add("float");
		keywords.add("int");
		keywords.add("long");
		keywords.add("short");
		keywords.add("String");
		keywords.add("boolean");
		
		Hashtable<String,String> maps=new Hashtable<String,String>();	
		if(code.contains(";"))
		{
			String[] state=code.split(";");
			for(String onestate:state)
			{
				if(onestate.contains("="))
				{
					onestate=onestate.substring(0, onestate.indexOf("="));
					onestate=onestate.replaceAll(" +", " ").trim();
					if(onestate.contains(" "))
					{
						String[] candimap=onestate.split(" ");
						String vari=candimap[candimap.length-1].trim();
						String type=candimap[candimap.length-2].trim();
						if(!vari.isEmpty()&&!type.isEmpty())
							maps.put(vari, type);
					}
				}
			}
		}
		else
		{
			if(code.contains("="))
			{
				code=code.substring(0, code.indexOf("="));
				code=code.replaceAll(" +", " ").trim();
				if(code.contains(" "))
				{
					String[] candimap=code.split(" ");
					String vari=candimap[candimap.length-1].trim();
					String type=candimap[candimap.length-2].trim();
					if(!vari.isEmpty()&&!type.isEmpty())
						maps.put(vari, type);
				}
			}
		}
		
//		System.out.println(maps.toString());
		
//		System.out.println(code);
		
		Set<String> keys=maps.keySet();
		for(String onekey:keys)
		{
			String type=maps.get(onekey);
			if(keywords.contains(type))
			{
				code=code.replace(" "+onekey+" ", "type");
				code=code.replace(" "+onekey+".", "type");
			}
			else
			{
				code=code.replace(" "+onekey+" ", "var");
				code=code.replace(" "+onekey+".", "var");
			}
		}
		
		code=code.replaceAll("[\\pP‘’“”]", "");
		code=code.replaceAll(" |	", "");		
		 
		return code;
	}
 
	public static int LCScompute(String string1, String string2)
    {
		
		
		
		char[] str1=string1.toCharArray();
		char[] str2=string2.toCharArray();
        int substringLength1 = str1.length;
        int substringLength2 = str2.length;
 
        int[][] opt = new int[substringLength1 + 1][substringLength2 + 1];
 
        for (int i = substringLength1 - 1; i >= 0; i--)
        {
            for (int j = substringLength2 - 1; j >= 0; j--)
            {
                if (str1[i] == str2[j])
                    opt[i][j] = opt[i + 1][j + 1] + 1;
                else
                    opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
            }
        }
        
        int i = 0, j = 0;
        while (i < substringLength1 && j < substringLength2)
        {
            if (str1[i] == str2[j])
            {
//                System.out.print(str1[i]);
                i++;
                j++;
            }
            else if (opt[i + 1][j] >= opt[i][j + 1])
                i++;
            else
                j++;
        }
//        System.out.println();
        return opt[0][0];
    }
 
	 
	 
}
