package GA_RBF;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ga_rbf extends JFrame implements KeyListener{
	int size =500;//����������
	double mutate_prob=0.4;//���ܲv
	double cross_prob=0.6;//��t�v
	int loop=100000;//���N����
	double ratio=0.1;
	double error=0;
	double err_min = 1000;
	double print;
	int J=3;
	int p=3;
	int dim =4;
	ArrayList<Double> best = new ArrayList<Double>();
	double theta = 0.4;
	ArrayList<Double> y = new ArrayList<Double>();//�����X
	ArrayList<Integer> dna_in_pool = new ArrayList<Integer>();
	ArrayList<ArrayList<Double>> x = new ArrayList<ArrayList<Double>>();
	ArrayList<Double> x_row = new ArrayList<Double>();
	int data[];
	String s[];//all files
	ArrayList<String> temp = new ArrayList<String>();
	ArrayList<String> temp2 = new ArrayList<String>();
	JButton enter = new JButton("��J");
	JTextArea mp = new JTextArea("���ܲv");
	JTextArea cp = new JTextArea("��t�v");
	JTextArea lp = new JTextArea("���N����");
	JTextArea amount = new JTextArea("�ڸs�j�p");
	Individual ind[];
	Individual ind_child[];
	ga_rbf()
	{
		
		this.setSize(25,200);
		this.add(mp);
		 mp.setBounds(0, 20, 100, 20);
		this.add(cp);
		 cp.setBounds(0, 50, 100, 20);
		this.add(lp);
		 lp.setBounds(0, 80, 100, 20);
		this.add(amount);
		 amount.setBounds(0, 110, 100, 20);
		this.add(enter);
		 enter.setBounds(0,140,100,30);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);
	    this.setLocationRelativeTo(null);
	    this.setLayout(null);
	    this.setVisible(true);
	    try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scan();
		func();//���W�e y
		create();
		//test();
		double err_last=0;
		double min=1000;
		int count=0;
		for(int i=0;i<loop;i++)
		{
			if(count==10)
				break;
			calculate();//�p��A�����
			
			if(err_min*80<=2&&err_min*80>=1)
				break;
			if(err_last==err_min)
				count++;
			else
				count=0;
			pool();//�ƻs
			crossover();//��t
			mutate();//�ܲ�
			replace();//child replace parent
			err_last=err_min;
		}
		System.out.println("err: "+err_min +" "+ best);	
		
	}
	public void replace()
	{
		for(int i=0;i<size;i++)
		{
			ArrayList<Double> dna = new ArrayList<Double>();
			dna = ind_child[i].get_dna();
			ind[i].setDna(dna);
		}
	}
	public void test()
	{
		ArrayList<Double> a = new ArrayList<Double>();//�����X
		a.add(0.163568);
		a.add(0.851471);
		a.add(1.594151);
		a.add(1.379580);
		a.add(13.092824);
		a.add(12.611789);
		a.add(30.000000);
		a.add(0.000368);
		a.add(18.715240);
		a.add(0.000850);
		a.add(2.708967);
		a.add(30.000000);
		a.add(6.307677);
		a.add(7.584529);
		a.add(10.000000);
		ind[0].setDna(a);
		double[][] m = new double[J+1][p+1];
		double[] w = new double[J+1];
		double[] fi = new double[J+1];
		m = ind[0].getm();
		w = ind[0].getw();
		fi = ind[0].getfi();
		double delta = 0;
		double temp = 0;//f
		double fitness = 0;
		double out = 0;//rbf��X=>F
		double min_error=1000;
		error =0;
		for(int list=0;list<x.size();list++)
		{
			out =0;
			
			for(int j=1;j<=J;j++)
			{
				temp = 0;
				for(int d=1;d<=p;d++)
				{
					//System.out.println(x.get(list).get(d-1));
					temp += (x.get(list).get(d-1)-m[j][d])*(x.get(list).get(d-1)-m[j][d]);
				}
				//System.out.println(temp);
				delta = Math.exp(-temp/(2*fi[j]*fi[j]));
				out += w[j]*delta;
				//System.out.println(w[j]+" "+delta);
			}
			out += theta;
			//System.out.println("Y : "+y.get(list));
			//System.out.println("out : "+out);
			fitness += (y.get(list)-out)*(y.get(list)-out)/2;
			error += Math.abs((y.get(list)-out)/x.size());;
		}
		//System.out.println();
		System.out.println("E :"+fitness);
		System.out.println("error : "+error);
	}
	public void create()
	{
		ind = new Individual[size];
		for(int i=0;i<size;i++)
		{
			ind[i] = new Individual();
		}
	}
	public void calculate()
	{
		for(int i=0;i<size;i++)
		{
			double[][] m = new double[J+1][p+1];
			double[] w = new double[J+1];
			double[] fi = new double[J+1];
			m = ind[i].getm();
			w = ind[i].getw();
			fi = ind[i].getfi();
			double delta = 0;
			double temp = 0;//f
			double fitness = 0;
			double out = 0;//rbf��X=>F
			error =0;
			for(int list=0;list<x.size();list++)
			{
				out =0;
				for(int j=1;j<=J;j++)
				{
					temp = 0;
					for(int d=1;d<=p;d++)
					{
						//System.out.println(x.get(list).get(d-1));
						temp += (x.get(list).get(d-1)-m[j][d])*(x.get(list).get(d-1)-m[j][d]);
					}
					//System.out.println(temp);
					delta = Math.exp(-temp/(2*fi[j]*fi[j]));
					out += w[j]*delta;
					//System.out.println(w[j]+" "+delta);
				}
				out += theta;
				//System.out.println("Y : "+y.get(list));
				//System.out.println("out : "+out);
				fitness += (y.get(list)-out)*(y.get(list)-out)/2;
				error += Math.abs((y.get(list)-out)/y.size());
			}
			//System.out.println();
			//System.out.println("E :"+fitness);
			ind[i].setFitness(fitness);
			if(error<err_min){
				err_min = error;
				best = ind[i].get_dna();
				print = out;
			}
				
			
		}
		//System.out.println("error : "+err_min);
		
	}
	public void pool()
	{
		dna_in_pool.clear();
		ind_child = new Individual[size];
		for(int i=0;i<size;i++)
			ind_child[i] = new Individual();
		for(int i=0;i<size;i++)
		{
			Random r = new Random();
			int a = r.nextInt(size);
			int b = r.nextInt(size);
			if(ind[a].getFitness()<ind[b].getFitness())//�O�d�n��
				dna_in_pool.add(a);
			else
				dna_in_pool.add(b);	
		}
	}
	public void crossover()
	{
		for(int i=0;i<size;i+=2)
		{
			Random r = new Random();
			double c = r.nextDouble();
			
			ArrayList<Double> dna1 = new ArrayList<Double>();
			ArrayList<Double> dna2 = new ArrayList<Double>();
			//System.out.println(dna_in_pool);
			int choice1 = dna_in_pool.get(i);
			int choice2 = dna_in_pool.get(i+1);
			dna1 = ind[choice1].get_dna();
			dna2 = ind[choice2].get_dna();
			if(c<cross_prob)
			{
				for(int j=0;j<dna1.size();j++)
				{
					double c2 = r.nextDouble();
					//System.out.println("1: "+dna1);
					//System.out.println("2: "+dna2);
					if(c2<0.5)
					{
						//System.out.println((dna1.get(j)+ratio*(dna1.get(j)-dna2.get(j))));
						dna1.set(j, dna1.get(j)+ratio*(dna1.get(j)-dna2.get(j)));
						//System.out.print(dna1.get(j)+" ");
						dna2.set(j, dna2.get(j)-ratio*(dna1.get(j)-dna2.get(j)));
						//System.out.print(dna1.get(j)+" ");
					}
					else
					{
						//System.out.println(dna1.get(j)+" "+(dna1.get(j)+ratio*(dna2.get(j)-dna1.get(j))));
						dna1.set(j, dna1.get(j)+ratio*(dna2.get(j)-dna1.get(j)));
						dna2.set(j, dna2.get(j)-ratio*(dna2.get(j)-dna1.get(j)));
					}
				}
			}
			ind_child[i].setDna(dna1);
			ind_child[i+1].setDna(dna2);
			//System.out.println(ind_child[i].get_dna());
			
		}
	}
	public void mutate()
	{
		for(int i=0;i<size;i++)
		{
			Random r = new Random();
			double c = r.nextDouble();//determine mutate or not
			if(c<mutate_prob){
				//System.out.println("aa: "+ind_child[i].get_dna());
				ind_child[i].mutate();
				//System.out.println("bb: "+ind_child[i].get_dna());
			}
		}
	}
	public void func()
	{
		for(int i=0;i<y.size();i++)
		{
			y.set(i, (y.get(i)+40)/80);
			//System.out.println(y.get(i));
		}
			
	}
	
	
	
	public void scan()
	{
		File f = new File("data3d");
		if(f.isDirectory())//�YŪ���쪺�O��Ƨ�
		{
			s=f.list();//�ɮ�list
			//System.out.println(s.length+s[0]);
		}
		data = new int[s.length];
		for(int i=0;i<s.length;i++)
		{
			File file = new File("data3d/"+s[i]);
			
	        Scanner sc = null;
			try {
				sc = new Scanner(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        int count = 0;
	        while (sc.hasNext()) 
	        {
	        	count++;
	        	String str = sc.next();
	        	//System.out.println(str);
	        	if(count%4==0)
	        		temp.add(str);
	        	else
	        		temp2.add(str);
	        }
	        data[i] = count/dim;
	        int index=0;
	        for(int j=0;j<data[i];j++)
	        {
	        	y.add(Double.parseDouble(temp.get(j)));
	        	x_row = new ArrayList<Double>();
	        	for(int k=1;k<p+1;k++)
	        	{
	        		
	        		x_row.add(Double.parseDouble(temp2.get(index)));
	        		index++;
	        	}
	        	x.add(x_row);
	        }
	        
	        temp2.clear();
	        temp.clear();
		}
		//System.out.println(x.get(60).get(2));
	}
	@Override
	public void keyPressed(KeyEvent a) {
		// TODO Auto-generated method stub
		if(a.getSource()==enter)
		{
			mutate_prob = Double.parseDouble(mp.getText());
			cross_prob = Double.parseDouble(cp.getText());
			loop = Integer.parseInt(lp.getText());
			size = Integer.parseInt(amount.getText());
		}
	}
	@Override
	public void keyReleased(KeyEvent a) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent a) {
		// TODO Auto-generated method stub
		
	}
}
