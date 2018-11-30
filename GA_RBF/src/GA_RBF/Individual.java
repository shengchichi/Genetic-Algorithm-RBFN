package GA_RBF;

import java.util.ArrayList;
import java.util.Random;

public class Individual {

	private int size = 500;
	private int J = 3;
	private int p = 3;
	private double w[];
	private double m[][];
	private double fi[];
	private double fitnessValue;
	private double noise;
	private ArrayList<Double> dna = new ArrayList<Double>();
	public Individual() 
	{
		random();
	}
	public void mutate()
	{
		dna.clear();
		Random r = new Random();
		for(int i=1;i<=J;i++)
		{
			while(true)//確保參數範圍
			{
				noise = r.nextDouble()*2-1;//-1~1
				w[i] += noise;
				if(w[i]>1||w[i]<0)
				{
					w[i] -= noise;
				}
				else
					break;
			}
			dna.add(w[i]);
		}
		for(int i=1;i<J+1;i++)
		{
			for(int j=1;j<=p;j++)
			{
				 while(true)
				 {
					 //System.out.println(m[i][j]+" "+noise);
					 noise = r.nextDouble()*60-30;//-30~30
					 m[i][j]+=noise;
					 if(m[i][j]<0||m[i][j]>30)
						 m[i][j]-=noise;
					 else
						 break;
				 }
				 dna.add(m[i][j]);
				
			}
		}
		for(int i=1;i<J+1;i++)
		{
			while(true)//確保參數範圍
			{
				noise = r.nextDouble()*20-10;//-10~10
				fi[i] += noise;
				if(fi[i]>10||fi[i]<0)
				{
					fi[i] -= noise;
				}
				else
					break;
			}
			dna.add(fi[i]);
		}
		//System.out.println(dna);
	}
	public void setDna(ArrayList<Double> dna)
	{
		this.dna = new ArrayList<Double>(dna);;
		dna_decode();
	}
	private void dna_decode()
	{
		int index=0;
		for(int j=1;j<=J;j++){
			if(dna.get(index)>1)
			{
				w[j] = 1;
				dna.set(index,1.0);
			}	
			else if(dna.get(index)<0){
				dna.set(index,0.0);
				w[j] = 0;
			}
			else
				w[j] = dna.get(index);
			index++;
		}
		for(int s=1;s<J+1;s++)
		{
			for(int t=1;t<=p;t++)
			{
				if(dna.get(index)>30){
					dna.set(index,30.0);
					m[s][t] = 30;
				}
				else if(dna.get(index)<0){
					dna.set(index,0.0);
					m[s][t] = 0;
				}
				else
					m[s][t] = dna.get(index);
				index++;
			}
		}
		for(int k=1;k<=J;k++){
			if(dna.get(index)>10){
				dna.set(index,10.0);
				fi[k] = 10;
			}
			else if(dna.get(index)<0){
				dna.set(index,0.0);
				fi[k] = 0;
			}
			else
				fi[k] = dna.get(index);
			index++;
		}

	}
	public void setFitness(double fitness)
	{
		fitnessValue = fitness;
	}
	public double getFitness()
	{
		return fitnessValue;
	}
	public void setp(int p)
	{
		this.p = p;
	}
	public ArrayList<Double> get_dna()
	{
		return dna;
	}
	public double[] getw()
	{
		return w; 
	}
	public double[] getfi()
	{
		return fi; 
	}
	public double[][] getm()
	{
		return m; 
	}
	public void random()
	{
		w = new double[J+1];
		m = new double[J+1][p+1];
		fi = new double[J+1];
		for(int i=1;i<J+1;i++)
		{
			 Random ran = new Random();
			 w[i] = ran.nextDouble();//0~1
			 
			 dna.add(w[i]);
		}
		for(int i=1;i<J+1;i++)
		{
			for(int j=1;j<=p;j++)
			{
				 Random ran = new Random();
				 m[i][j] = ran.nextDouble()*30;//0~30
				 dna.add(m[i][j]);
			}
		}
		for(int i=1;i<J+1;i++)
		{
			 Random ran = new Random();
			 fi[i] = ran.nextDouble()*10;//0~10
			 dna.add(fi[i]);
		}
	}
	
}
