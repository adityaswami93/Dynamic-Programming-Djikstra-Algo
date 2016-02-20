// Copy paste this Java Template and save it as "Supermarket.java"
import java.util.*;
import java.io.*;

// write your matric number here: A0100248W
// write your name here: ADITYA SWAMI
// write list of collaborators here: Fb posts
// year 2015 hash code: JESg5svjYpIsmHmIjabX (do NOT delete this line)

class Supermarket {
	private int N; // number of items in the supermarket. V = N+1
	private int K; // the number of items that Steven has to buy
	private int[] shoppingList; // indices of items that Steven has to buy
	private int[][] T; // the complete weighted graph that measures the direct
						// walking time to go from one point to another point in
						// seconds
	private int INF = Integer.MAX_VALUE;
	private int[][] meemoo; // meemoo table
	private PriorityQueue<IntegerPair> PQ;

	// if needed, declare a private data structure here that
	// is accessible to all methods in this class
	// --------------------------------------------

	public Supermarket() {
		// Write necessary code during construction
		//
		// write your answer here

	}

	int Query() {
		int ans = 0;

		// You have to report the quickest shopping time that is measured
		// since Steven enters the supermarket (vertex 0),
		// completes the task of buying K items in that supermarket as ordered
		// by Grace,
		// then, reaches the cashier of the supermarket (back to vertex 0).
		//
		// write your answer here
		meemoo = new int[K + 1][(int) Math.pow(2, K + 1)];
		for (int[] row : meemoo) {
			Arrays.fill(row, -1);
		}
		// floydWarshallAlgo();
		djikstraAlgo(0);

		for (int i = 0; i < K; i++) {
			djikstraAlgo(shoppingList[i]);
		}
		// System.out.printf("cost = %d\n",T[0][2]);
		ans = tsp(0, 1);
		return ans;
	}

	// You can add extra function if needed
	// --------------------------------------------

	public int tsp(int u, int m) {
		if (m == (1 << (K + 1)) - 1) {// all vertices have been visited
			// System.out.printf("u=%d\n",shoppingList[u-1]);
			return T[shoppingList[u - 1]][0];
		}
		if (meemoo[u][m] != -1) { // computed previously
			return meemoo[u][m];
		}

		meemoo[u][m] = INF; // general case.
		for (int i = 0; i < K + 1; i++) {
			if (((i != u) && ((m & (1 << i))) == 0)) {
				if (u == 0)
					meemoo[u][m] = Math.min(meemoo[u][m],
							T[0][shoppingList[i - 1]] + tsp(i, m | (1 << i)));
				else
					meemoo[u][m] = Math.min(
							meemoo[u][m],
							T[shoppingList[u - 1]][shoppingList[i - 1]]
									+ tsp(i, m | (1 << i)));
			}
		}
		return meemoo[u][m];

	}

	private void djikstraAlgo(int source) {
		PQ = new PriorityQueue<IntegerPair>();
		IntegerPair temp;
		// initSSSP(source);
		PQ.add(new IntegerPair(0, source));
		int u, d;
		int neighbour, weight;
		while (!PQ.isEmpty()) {
			temp = PQ.poll();
			d = temp.first();
			u = temp.second();
			if (d == T[source][u]) {

				for (int i = 0; i < N + 1; i++) {
					neighbour = i;
					weight = T[u][i];
					if (i != u)
						relax(source, u, neighbour, weight);
				}
			}

		}
	}

	private void relax(int source, int u, int v, int w_u_v) {
		if (T[source][v] >= T[source][u] + w_u_v) {
			T[source][v] = T[source][u] + w_u_v;
			T[v][source] = T[source][v];
			PQ.add(new IntegerPair(T[source][u] + w_u_v, v));
		}
	}

	void run() throws Exception {
		// do not alter this method to standardize the I/O speed (this is
		// already very fast)
		IntegerScanner sc = new IntegerScanner(System.in);
		PrintWriter pw = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(System.out)));

		int TC = sc.nextInt(); // there will be several test cases
		while (TC-- > 0) {
			// read the information of the complete graph with N+1 vertices
			N = sc.nextInt();
			K = sc.nextInt(); // K is the number of items to be bought

			shoppingList = new int[K];
			for (int i = 0; i < K; i++)
				shoppingList[i] = sc.nextInt();

			T = new int[N + 1][N + 1];
			for (int i = 0; i <= N; i++)
				for (int j = 0; j <= N; j++)
					T[i][j] = sc.nextInt();

			pw.println(Query());
		}

		pw.close();
	}

	public static void main(String[] args) throws Exception {
		// do not alter this method
		Supermarket ps6 = new Supermarket();
		ps6.run();
	}
}

class IntegerScanner { // coded by Ian Leow, using any other I/O method is not
						// recommended
	BufferedInputStream bis;

	IntegerScanner(InputStream is) {
		bis = new BufferedInputStream(is, 1000000);
	}

	public int nextInt() {
		int result = 0;
		try {
			int cur = bis.read();
			if (cur == -1)
				return -1;

			while ((cur < 48 || cur > 57) && cur != 45) {
				cur = bis.read();
			}

			boolean negate = false;
			if (cur == 45) {
				negate = true;
				cur = bis.read();
			}

			while (cur >= 48 && cur <= 57) {
				result = result * 10 + (cur - 48);
				cur = bis.read();
			}

			if (negate) {
				return -result;
			}
			return result;
		} catch (IOException ioe) {
			return -1;
		}
	}
}

class IntegerPair implements Comparable<IntegerPair> {
	Integer _first, _second;

	public IntegerPair(Integer f, Integer s) {
		_first = f;
		_second = s;
	}

	public int compareTo(IntegerPair o) {
		if (!this.first().equals(o.first()))
			return this.first() - o.first();
		else
			return this.second() - o.second();
	}

	Integer first() {
		return _first;
	}

	Integer second() {
		return _second;
	}
}