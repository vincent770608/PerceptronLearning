package jch.ml.pla;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PLAlgorithm 
{

	public static void main(String [] args)
	{
		PLAlgorithm pla = new PLAlgorithm();
		pla.TestInput();
	}
	public void PLAtraining(List<Double[]> inputs,List<Double> weights,List<Integer> outputs)
	{
		PLAlgorithm pla = new PLAlgorithm();
		int err = 0;
		do
		{
			err = 0;
			for(int i=0; i<inputs.size();i++)
			{
				int calopt = pla.CalOutput(inputs.get(i), weights);
				if(calopt!=outputs.get(i))
				{
					err = (outputs.get(i)-calopt)/2;
//					System.out.println("err "+ err);
					List<Double> newweights = new ArrayList<Double>(); 
					for(int j=0; j<weights.size();j++)
					{
//						System.out.println(weights.get(j));
//						System.out.println(inputs.get(i)[j]);
//						System.out.println(weights.get(j)+err*inputs.get(i)[j]);
						newweights.add(weights.get(j)+err*inputs.get(i)[j]);
					}
					weights.clear();
					weights.addAll(newweights);
					newweights.clear();
				}
			}
		}while(err!=0);

		try 
		{
			File wfile = new File("dataset/weight");
			FileWriter wfilewr = new FileWriter(wfile);
			BufferedWriter wfilebr = new BufferedWriter(wfilewr);
			
			for(int k=0; k<weights.size();k++)
			{

				wfilebr.write(""+weights.get(k));
				wfilebr.write("\r\n");

//				System.out.println(k+" : "+weights.get(k));
			}
			
			wfilebr.close();
			wfilewr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Integer CalOutput(Double[] xT,List<Double> weights)
	{
		Double wTx = 0.0;
		for(int i=0; i<xT.length; i++)
		{
			wTx += xT[i]*weights.get(i);
		}

		return wTx>=0 ? 1:-1;
	}
	
	
	public void TestInput()
	{
		PLAlgorithm pla = new PLAlgorithm();

		List<Double[]> inputs = new ArrayList<Double[]>();
		List<Double> weights = new ArrayList<Double>();
		List<Integer> outputs = new ArrayList<Integer>();
		
		File trainfile = new File("dataset/train");
//		File testfile = new File("dataset/test");
		for(Map.Entry<List<Integer>, List<Double[]>> getentry : pla.getData(trainfile).entrySet())
		{
			inputs = getentry.getValue();
			outputs = getentry.getKey();
		}
		
		weights = pla.InitWeight(inputs.get(0).length);
		
		pla.PLAtraining(inputs,weights,outputs);
	}
	
	public void TestInput(List<Double[]> inputs, List<Integer> outputs)
	{
		PLAlgorithm pla = new PLAlgorithm();
		
		List<Double> weights = pla.InitWeight(inputs.get(0).length);
		pla.PLAtraining(inputs,weights,outputs);
	}
	
	public List<Double> InitWeight(int len)
	{
		List<Double> initw = new ArrayList<Double>();
		Random r = new Random();
        
        //initialize weights
        for(int i=0;i<len;i++)
        {
        	initw.add(r.nextDouble());
        	System.out.println("init w "+initw.get(i));
        }
		return initw;
	}
	
	public Map<List<Integer>,List<Double[]>> getData(File file)
	{
		List<Double[]> dataset = new ArrayList<Double[]>();
		List<Integer> labels = new ArrayList<Integer>();
		Map<List<Integer>,List<Double[]>> labeldata = new HashMap<List<Integer>,List<Double[]>>();
		try
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) 
			{
				String[] datas = line.split(" ");
				Double[] vectors = new Double[datas.length];
				vectors[0] = 1.0;
				labels.add(Integer.parseInt(datas[0]));
//				vectors[0] = Double.parseDouble(datas[0]);
//				dataset.add(Double.parseDouble(datas[0]));
				
			    for(int i=0;i<vectors.length-1;i++)
			    {
			    	String [] inval = datas[i+1].split(":");
			    	vectors[i+1] = Double.parseDouble(inval[1]);
			    }
			    dataset.add(vectors);
			}
			
			labeldata.put(labels, dataset);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return labeldata;
	}
}
