package network;

import java.io.Serializable;
import java.util.ArrayList;

import grmlsa.Route;
import grmlsa.modulation.Modulation;

/**
 * This class calcule crosstalk
 * reference -> [LOBATO et al 2019], [OLIVEIRA 2018]
 * 
 * @author jurandir
 *
 */
public class Crosstalk implements Serializable{
	public static final double PROPAGATIONCONSTANT = 10000000.0; // B in 1/m. Value from [LOBATO et al 2019]
	public static final double BENDINGRADIUS = 0.01; // R in m. Value from [LOBATO et al 2019]
	//public static final double COUPLINGCOEFFICIENTS = 0.00584; // K in m-1. Value from [LOBATO et al 2019] baixo xt
	public static final double COUPLINGCOEFFICIENTS = 0.012; // K in m-1. Value from [LOBATO et al 2019] alto xt
	public static final double COREPITCH = 0.000045; // D in m. Value from [LOBATO et al 2019]
	public static final double SIGNALPOWER = 10.0;
	
//	public static final double XTBPSK = -16.0; //XT threshold levels, in dB. [OLIVEIRA 2018]
//	public static final double XTQPSK = -18.0; //XT threshold levels, in dB. [OLIVEIRA 2018]
//	public static final double XT8QAM = -21.0; //XT threshold levels, in dB. [OLIVEIRA 2018]
//	public static final double XT16QAM = -24.0; //XT threshold levels, in dB. [OLIVEIRA 2018]
//	public static final double XT32QAM = -28.0; //XT threshold levels, in dB. [OLIVEIRA 2018]
//	public static final double XT64QAM = -32.0; //XT threshold levels, in dB. [OLIVEIRA 2018]
	
//	public static final double XT4QAM = -20.7; //XT threshold levels, in dB. [LOBATO 2019]
//	public static final double XT8QAM = -24.78; //XT threshold levels, in dB. [LOBATO 2019]
//	public static final double XT16QAM = -27.36; //XT threshold levels, in dB. [LOBATO 2019]
//	public static final double XT32QAM = -30.39; //XT threshold levels, in dB. [LOBATO 2019]
//	public static final double XT64QAM = -33.29; //XT threshold levels, in dB. [LOBATO 2019]
	
	public static final double XTBPSK = -14.0; //XT threshold levels, in dB. [Ehsani Moghaddam 2019]
	public static final double XTQPSK = -18.5; //XT threshold levels, in dB. [Ehsani Moghaddam 2019]
	public static final double XT8QAM = -21.0; //XT threshold levels, in dB. [Ehsani Moghaddam 2019]
	public static final double XT16QAM = -25.0; //XT threshold levels, in dB. [Ehsani Moghaddam 2019]
	public static final double XT32QAM = -27.0; //XT threshold levels, in dB. [Ehsani Moghaddam 2019]
	public static final double XT64QAM = -34.0; //XT threshold levels, in dB. [Ehsani Moghaddam 2019]
	
	private double h;
	
	public Crosstalk() {
		this.h = calculateH();
	}
	
	/**
	 * calculate h, from [LOBATO et al 2019]
	 * 
	 * @return h
	 */
	private double calculateH() {
		return ((2*COUPLINGCOEFFICIENTS*COUPLINGCOEFFICIENTS*BENDINGRADIUS)/(PROPAGATIONCONSTANT*COREPITCH));		
	}
	
	public double calculaCrosstalk(Circuit circuit) {
		Route rota = circuit.getRoute();
		double totalXT = 0;
		
		for(Link link: rota.getLinkList()) {
			totalXT = totalXT + calculeCrosstalkInLink(circuit, link);
		}
		
		//if (circuit.getSource().getName().equals("1") && circuit.getDestination().getName().equals("2")) {
		//	System.out.println("Crosstalk para o circuito: "+circuit.getSource().getName()+"-"+circuit.getDestination().getName()+" tendo o core: "+circuit.getIndexCore()+" e slots: "+circuit.getSpectrumAssigned()[0]+"-"+circuit.getSpectrumAssigned()[1]+" eh "+calculeLog(totalXT)+" Com mudulação: "+circuit.getModulation().getName()+" portanto: "+isAdmissible(circuit, calculeLog(totalXT)));
		//}
		
		//imprimeTeste(circuit, "1", "2", totalXT);
		
		if(totalXT == 0) {
			totalXT =  0.000000000001; //greatest value
		}
		
		
		return calculeLog(totalXT);
	}
	
	public boolean isAdmissible(Circuit circuit) {
		double xt = calculaCrosstalk(circuit);
		double xtThreshold = xtThreshold(circuit.getModulation());
		
		if(calculeLog(xt) > xtThreshold) {
			return false;
		}else {
			return true;
		}
	}
	
	public boolean isAdmissible(Circuit circuit, double xt) {
		double xtThreshold = xtThreshold(circuit.getModulation());
		
		if(xt > xtThreshold) {
			return false;
		}else {
			return true;
		}
	}

	//OLIVEIRA 2018 and [Ehsani Moghaddam 2019]
	public double xtThreshold(Modulation modulation) {
		switch(modulation.getName()) {
			case "BPSK":
				return XTBPSK;
			case "QPSK":
				return XTQPSK;
			case "8QAM":
				return XT8QAM;
			case "16QAM":
				return XT16QAM;
			case "32QAM":
				return XT32QAM;
			case "64QAM":
				return XT64QAM;
			default:
				return 0.0;
		}
	}
	
	//LOBATO 2019
//	public double xtThreshold(Modulation modulation) {
//		switch(modulation.getName()) {
//			case "4QAM":
//				return XT4QAM;
//			case "8QAM":
//				return XT8QAM;
//			case "16QAM":
//				return XT16QAM;
//			case "32QAM":
//				return XT32QAM;
//			case "64QAM":
//				return XT64QAM;
//			default:
//				return 0.0;
//		}
//	}
	
	private double calculeLog(double valor) {
		return (10*(Math.log10(valor)));
	}
	
//	private double calculeIsoij(Circuit circuit) {
//		return 0.5;
//	}
	
	private double calculeCrosstalkInLink(Circuit circuit, Link link) {
		
		double xtInLink = 0;
		ArrayList<Core> adjacentsCores = link.coresAdjacents(circuit.getIndexCore());
		
		for(Core core : adjacentsCores) {
			//xtInLink = xtInLink + ((SIGNALPOWER*h*link.getDistance()*1000)/SIGNALPOWER);
			xtInLink = xtInLink + ((calculeIsoij(circuit, core)*SIGNALPOWER*h*link.getDistance()*1000)/SIGNALPOWER);
		}
		
		return xtInLink;
	}
	
	//private double calculeCrosstalkInLink(Circuit circuit, Link link) {
	//	return (calculeIsoij(circuit)*SIGNALPOWER*h*link.getDistance())/SIGNALPOWER;
	//}
	
	
	private int sizeSpectrumAllocate(int[] spectrum) {
		return spectrum[1]-spectrum[0]+1;
	}
	
	private double calculeIsoij(Circuit circuit, Core core) {
		//int[] spectrum1 = circuit.getSpectrumAssigned();
		double nsoij = 0; // number of overlapping slots between i and j
		double nsj = 0; //number of slots of the connection j 
		int quantInter = 0;
		
		
		for(Circuit circuit2 : core.getCircuitList()) {
			if(isIntersection(circuit.getSpectrumAssigned(), circuit2.getSpectrumAssigned())) {
				nsj = nsj + sizeSpectrumAllocate(circuit2.getSpectrumAssigned());
				nsoij = nsoij + numberOfOverlapping(circuit.getSpectrumAssigned(), circuit2.getSpectrumAssigned());
				quantInter++;
				//nsoij = nsoij + numberOfOverlapping(spectrum1, circuit2.getSpectrumAssigned());
			}
		}
		
		//System.out.println("-------------");
		//System.out.println("--->"+nsoij);
		//System.out.println("--->"+nsj);
		//System.out.println("-------------");
		
		if(quantInter==0) {
			return 0;
		}else {
			return nsoij/nsj;
		}
	}

	private boolean isIntersection(int[] spectrum1, int[] spectrum2) {
		ArrayList<Integer> slots2 = new ArrayList<Integer>();
		
		for(int i=spectrum2[0]; i<=spectrum2[1]; i++) {
			slots2.add(i);
		}
				
		for(int i=spectrum1[0]; i<=spectrum1[1]; i++) {
			if(slots2.contains(i)) {
				return true;
			}
		}
		
		return false;
	}
	
	private int numberOfOverlapping(int[] spectrum1, int[] spectrum2) {
		int numberOfOverlapping = 0;
		
		for(int i=spectrum1[0]; i<=spectrum1[1]; i++) {
			if ((i>=spectrum2[0]) && (i<=spectrum2[1])) {
				numberOfOverlapping++;
			}
		}
		//System.out.println(numberOfOverlapping);
				
		return numberOfOverlapping;
	}
	
	private void imprimeTeste(Circuit circuit, String origem, String destino, double totalXT) {
		if (circuit.getSource().getName().equals(origem) && circuit.getDestination().getName().equals(destino)) {
			System.out.println("\n\n");
			for(Link link : circuit.getRoute().getLinkList()) {
				System.out.println("Link :"+link.getSource().getName()+"-"+link.getDestination().getName());
				for(Core core : link.getCores()) {
					System.out.print("Core: "+core.getId());
					for(int[] i : core.getFreeSpectrumBands(circuit.getGuardBand())) {
						System.out.print("||"+i[0]+"-"+i[1]);
					}
					System.out.println("");
				}
			}
			System.out.println("Crosstalk para o circuito tendo o core: "+circuit.getIndexCore()+" e slots: "+circuit.getSpectrumAssigned()[0]+"-"+circuit.getSpectrumAssigned()[1]+" eh "+calculeLog(totalXT)+" Com mudulação: "+circuit.getModulation().getName()+" portanto: "+isAdmissible(circuit, calculeLog(totalXT)));
			//System.out.println("Crosstalk para o circuito: "+circuit.getSource().getName()+"-"+circuit.getDestination().getName()+" tendo o core: "+circuit.getIndexCore()+" e slots: "+circuit.getSpectrumAssigned()[0]+"-"+circuit.getSpectrumAssigned()[1]+" eh "+calculeLog(totalXT)+" Com mudulação: "+circuit.getModulation().getName()+" portanto: "+isAdmissible(circuit, calculeLog(totalXT)));
		
		}
	}
}
