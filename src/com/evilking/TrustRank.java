package com.evilking;

public class TrustRank {
	private static double[][] H = null;	//����ͼ
	private static double[][] U = null;	//��������ͼ
	private static double[] e = null;
	
	private static int n = 7;
	private static double alpha = 0.85;
	private static double iterError = 0.01;
	
	private static void init(){
		H = new double[n][n];
		U = new double[n][n];
		
		H[0] = new double[]{0,1,0,0,0,0,0};
		H[1] = new double[]{0,0,1.0/2,1.0/2,0,0,0};
		H[2] = new double[]{0,1,0,0,0,0,0};
		H[3] = new double[]{0,0,0,0,1,0,0};
		H[4] = new double[]{0,0,0,0,0,1.0/2,1.0/2};
		H[5] = new double[]{0,0,1,0,0,0,0};
		H[6] = new double[]{0,0,0,0,0,0,0};

		U[0] = new double[]{0,0,0,0,0,0,0};
		U[1] = new double[]{1.0/2,0,1.0/2,0,0,0,0};
		U[2] = new double[]{0,1.0/2,0,0,0,1.0/2,0};
		U[3] = new double[]{0,1,0,0,0,0,0};
		U[4] = new double[]{0,0,0,1,0,0,0};
		U[5] = new double[]{0,0,0,0,1,0,0};
		U[6] = new double[]{0,0,0,0,1,0,0};

		e = new double[]{1,1,1,1,1,1,1};
	}
	
	private static double[] trustRank(double[] v,int M){
		double[] r_k = v;
		
		double[] r_k_1 = null;
		int k = 0;
		while (k < M) {
			r_k_1 = multiMatrix(r_k, H);
			for(int i = 0;i < r_k_1.length; i++ ){
				r_k_1[i] = r_k_1[i]*alpha + (1-alpha)*v[i];
			}

			if(iteratorError(r_k, r_k_1) < iterError){
				break;
			}			
			r_k = r_k_1;
		}
		return r_k;
	}
	
	
	private static double[] selectSeed(int M){
		double[] s_k = e;
		
		double[] s_k_1 = null;
		int k = 0;
		while (k < M) {
			s_k_1 = multiMatrix(s_k, U);
			for(int i = 0;i < s_k_1.length; i++ ){
				s_k_1[i] = s_k_1[i]*alpha + (1-alpha)/n;
			}

			if(iteratorError(s_k, s_k_1) < iterError){
				break;
			}			
			s_k = s_k_1;
		}
		return s_k;
	}
	
	private static double iteratorError(double[] sk,double[] sk1){
		double error = 0;
		
		for(int i = 0;i< sk.length;i++){
			error += Math.abs(sk[i] - sk1[i]);
		}
		return error;
	}
	
	private static double[] multiMatrix(double[] s,double[][] m){
		// 1*n   n*n
		double[] r = new double[m[0].length];
		
		if(s.length != m.length){
			System.out.println("error=======�����������ߴ粻һ��=====");
		}
		
		for (int i = 0; i < m[0].length; i++) {
			for (int j = 0; j < s.length; j++) {
				r[i] += s[j] * m[j][i];
			}
		}
		
		return r;
	}
	
	private static int[] sort(double[] s){
		int[] index = new int[s.length];
		for(int i = 0;i<index.length;i++){
			index[i] = i;
		}
		
		for(int i = 0; i< s.length;i++){
			for(int j = i;j<s.length;j++){
				if(s[i] < s[j]){
					double t = s[i];
					s[i] = s[j];
					s[j] = t;
					
					int k = index[i];
					index[i] = index[j];
					index[j] = k;
				}
			}
		}
		return index;
	}
	
	public static void main(String[] args) {
		init();
		double[] seedsTmp = selectSeed(20);
		
		System.out.println("===========����PageRank������£�");

		for(int i = 0;i < seedsTmp.length; i++){
			System.out.printf("%3.2f  ",seedsTmp[i]);
		}
		System.out.println("\n===========ѡ�� 3 ����ѡ�������£�");
		int[] index = sort(seedsTmp);
		
		int k = 3;
		
		for(int i = 0;i < index.length && i < k; i++){
			System.out.printf("%d, %3.2f  \n",index[i],seedsTmp[i]);
		}
		System.out.println("===========�˹�ȷ�Ϻ����ѡȡǰ 2 ���������£�");
		
		k = 2;	//�����ε�����ҳ����
		
		double[] v = new double[n];
		for(int i=0;i < 2;i++){
			v[index[i]] = 1.0/k;
		}
		
		for(int i = 0;i < v.length; i++){
			System.out.printf("%3.2f  ",v[i]);
		}
		
		System.out.println("\n===========TrustRank ������£�");
		
		double[] r = trustRank(v, 20);
		
		for(int i = 0;i < r.length; i++){
			System.out.printf("%3.2f  ",r[i]);
		}
		
		System.out.println("\n===========ѡ�� 3 ����ѡ�������£�");
		index = sort(r);
		
		k = 3;
		
		for(int i = 0;i < index.length && i < k; i++){
			System.out.printf("%d, %3.2f  \n",index[i],r[i]);
		}
		
	}

}
