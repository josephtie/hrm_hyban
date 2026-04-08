//package com.nectux.mizan.hyban.utils;
//
//import java.math.BigDecimal;
//
//public class LivreDePaieGratification2 {
//
//	private String A;
//
//	private Float B;
//
//	private BigDecimal C;
//
//	private BigDecimal D;
//
//	private BigDecimal E;
//
//	private BigDecimal F;
//
//	private BigDecimal G;
//
//	private BigDecimal H;
//
//	private BigDecimal I;
//
//	private BigDecimal J;
//
//	private BigDecimal K;
//
//	private BigDecimal L;
//
//	private BigDecimal M;
//
//	private BigDecimal N;
//
//	private BigDecimal O;
//
//	private BigDecimal P;
//
//	private BigDecimal Q;
//
//	private BigDecimal R;
//
//	private BigDecimal S;
//
//	private BigDecimal T;
//
//	private BigDecimal U;
//
//	private BigDecimal V;
//
//	private BigDecimal W;
//
//	private BigDecimal X;
//
//	private BigDecimal Y;
//
//	private String Z;
//
//	private String AA;
//
//	private int AB;
//
//	private String AC;
//
//	public LivreDePaieGratification2(String nomComplet, Float nombrePart, BigDecimal gratificationBase, BigDecimal indemniteTransport, BigDecimal indemniteFinCarriere, String dateEntree, String situationMatrimoniale, int nombreEnfant, String fonction) {
//		super();
//		this.A = nomComplet;
//		this.B = nombrePart;
//		this.C = Math.rint(gratificationBase);
//		this.D = C;
//		this.E = D * 1.2 / 100;
//		this.F = Math.rint(calculCN());
//		this.G = Math.rint(calculIGR());
//		this.H = Math.rint(E + F + G);
//		this.I = Math.rint(calculCNPS());
//		this.J = Math.rint(H + I);
//		this.K = Math.rint(indemniteTransport);
//		this.L = Math.rint(D - J + K);
//		this.M = Math.rint(D);
//		this.N = Math.rint(D * 1.2 / 100);
//		this.O = Math.rint(D * 0.4 / 100);
//		this.P = Math.rint(D * 1.2 / 100);
//		this.Q = Math.rint(N + O + P);
//		this.R = Math.rint(calculPrestationFamiliale());
//		this.S = Math.rint(calculAccidentTravail());
//		this.T = Math.rint(calculRetraite());
//		this.U = Math.rint(indemniteFinCarriere);
//		this.V = Math.rint(R + S + T + U);
//		this.W = Math.rint(Q + V);
//		this.X = Math.rint(M + Q + V);
//		this.Y = Math.rint(X * 13);
//
//		this.Z = dateEntree;
//		this.AA = situationMatrimoniale;
//		this.AB = nombreEnfant;
//		this.AC = fonction;
//	}
//
//	public BigDecimal calculCN(){
//		if(D > 250000.0)
//			return (D - 250000.0) * 8 / 100 + 4700;
//		else if(D > 162500.0)
//			return (D - 162500.0) * 4 / 100 + 1200;
//		else if(D > 62500.0)
//			return D * 1.2 / 100 - 750;
//		else
//			return 0.0;
//	}
//
//	public BigDecimal calculIGR(){
//		B = Math.abs(B);
//		BigDecimal G = ((D * 80 / 100 - E - F) / B) * 85 / 100;
//		if(G > 842167.0)
//			return G * B * 60 / 160 - 98633.0 * B;
//		else if(G > 389084.0)
//			return G * B * 45 / 145 - 44181.0 * B;
//		else if(G > 220334.0)
//			return G * B * 35 / 135 - 24306.0 * B;
//		else if(G > 126584.0)
//			return G * B * 25 / 125 - 11250.0 * B;
//		else if(G > 81584.0)
//			return G * B * 20 / 120 - 7031.0 * B;
//		else if(G > 45584.0)
//			return G * B * 15 / 115 - 4076.0 * B;
//		else if(G > 25000.0)
//			return G * B * 10 / 110 - 2273.0 * B;
//		else
//			return 0.0;
//	}
//
//	public BigDecimal calculCNPS(){
//		if(D < 1647315.0)
//			return D * 6.3 / 100;
//		else
//			return 1647315 * 6.3 / 100;
//	}
//
//	public BigDecimal calculPrestationFamiliale(){
//		if(D > 70000.0)
//			return 70000.0 * 5.75 / 100;
//		else if(D > 0)
//			return D * 5.75 / 100;
//		else
//			return 0.0;
//	}
//
//	public BigDecimal calculAccidentTravail(){
//		if(D > 70000.0)
//			return 70000.0 * 2 / 100;
//		else if(D > 0)
//			return D * 2 / 100;
//		else
//			return 0.0;
//	}
//
//	public BigDecimal calculRetraite(){
//		if(D < 1647315.0)
//			return D * 7.7 / 100;
//		else
//			return 1647315 * 7.7 / 100;
//	}
//
//	public String getA() {
//		return A;
//	}
//
//	public void setA(String a) {
//		A = a;
//	}
//
//	public Float getB() {
//		return B;
//	}
//
//	public void setB(Float b) {
//		B = b;
//	}
//
//	public BigDecimal getC() {
//		return C;
//	}
//
//	public void setC(BigDecimal c) {
//		C = c;
//	}
//
//	public BigDecimal getD() {
//		return D;
//	}
//
//	public void setD(BigDecimal d) {
//		D = d;
//	}
//
//	public BigDecimal getE() {
//		return E;
//	}
//
//	public void setE(BigDecimal e) {
//		E = e;
//	}
//
//	public BigDecimal getF() {
//		return F;
//	}
//
//	public void setF(BigDecimal f) {
//		F = f;
//	}
//
//	public BigDecimal getG() {
//		return G;
//	}
//
//	public void setG(BigDecimal g) {
//		G = g;
//	}
//
//	public BigDecimal getH() {
//		return H;
//	}
//
//	public void setH(BigDecimal h) {
//		H = h;
//	}
//
//	public BigDecimal getI() {
//		return I;
//	}
//
//	public void setI(BigDecimal i) {
//		I = i;
//	}
//
//	public BigDecimal getJ() {
//		return J;
//	}
//
//	public void setJ(BigDecimal j) {
//		J = j;
//	}
//
//	public BigDecimal getK() {
//		return K;
//	}
//
//	public void setK(BigDecimal k) {
//		K = k;
//	}
//
//	public BigDecimal getL() {
//		return L;
//	}
//
//	public void setL(BigDecimal l) {
//		L = l;
//	}
//
//	public BigDecimal getM() {
//		return M;
//	}
//
//	public void setM(BigDecimal m) {
//		M = m;
//	}
//
//	public BigDecimal getN() {
//		return N;
//	}
//
//	public void setN(BigDecimal n) {
//		N = n;
//	}
//
//	public BigDecimal getO() {
//		return O;
//	}
//
//	public void setO(BigDecimal o) {
//		O = o;
//	}
//
//	public BigDecimal getP() {
//		return P;
//	}
//
//	public void setP(BigDecimal p) {
//		P = p;
//	}
//
//	public BigDecimal getQ() {
//		return Q;
//	}
//
//	public void setQ(BigDecimal q) {
//		Q = q;
//	}
//
//	public BigDecimal getR() {
//		return R;
//	}
//
//	public void setR(BigDecimal r) {
//		R = r;
//	}
//
//	public BigDecimal getS() {
//		return S;
//	}
//
//	public void setS(BigDecimal s) {
//		S = s;
//	}
//
//	public BigDecimal getT() {
//		return T;
//	}
//
//	public void setT(BigDecimal t) {
//		T = t;
//	}
//
//	public BigDecimal getU() {
//		return U;
//	}
//
//	public void setU(BigDecimal u) {
//		U = u;
//	}
//
//	public BigDecimal getV() {
//		return V;
//	}
//
//	public void setV(BigDecimal v) {
//		V = v;
//	}
//
//	public BigDecimal getW() {
//		return W;
//	}
//
//	public void setW(BigDecimal w) {
//		W = w;
//	}
//
//	public BigDecimal getX() {
//		return X;
//	}
//
//	public void setX(BigDecimal x) {
//		X = x;
//	}
//
//	public BigDecimal getY() {
//		return Y;
//	}
//
//	public void setY(BigDecimal y) {
//		Y = y;
//	}
//
//	public String getZ() {
//		return Z;
//	}
//
//	public void setZ(String z) {
//		Z = z;
//	}
//
//	public String getAA() {
//		return AA;
//	}
//
//	public void setAA(String aA) {
//		AA = aA;
//	}
//
//	public int getAB() {
//		return AB;
//	}
//
//	public void setAB(int aB) {
//		AB = aB;
//	}
//
//	public String getAC() {
//		return AC;
//	}
//
//	public void setAC(String aC) {
//		AC = aC;
//	}
//
//	@Override
//	public String toString() {
//		return "LivreDePaieGratification2 [A=" + A + ", B=" + B + ", C=" + C + ", D=" + D + ", E=" + E + ", F=" + F
//				+ ", G=" + G + ", H=" + H + ", I=" + I + ", J=" + J + ", K=" + K + ", L=" + L + ", M=" + M + ", N=" + N
//				+ ", O=" + O + ", P=" + P + ", Q=" + Q + ", R=" + R + ", S=" + S + ", T=" + T + ", U=" + U + ", V=" + V
//				+ ", W=" + W + ", X=" + X + ", Y=" + Y + ", Z=" + Z + ", AA=" + AA + ", AB=" + AB + ", AC=" + AC + "]";
//	}
//
//
//}
