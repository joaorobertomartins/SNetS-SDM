package grmlsa.csa;

import java.util.ArrayList;
import java.util.List;

import network.Circuit;
import network.ControlPlane;
import util.IntersectionFreeSpectrum;

public class CorePrioritizationFirstFit implements CoreAndSpectrumAssignmentAlgorithmInterface{
	private int[] coreOfTheTime = new int[7];
	private int[] pesos = new int[7];
	private int contador;
	
	public CorePrioritizationFirstFit(){
		pesos[0] = 0;
		pesos[1] = 0;
		pesos[2] = 0;
		pesos[3] = 0;
		pesos[4] = 0;
		pesos[5] = 0;
		pesos[6] = 0;
		
		
		
		
		coreOfTheTime[0] = 6;
		coreOfTheTime[1] = 4;
		coreOfTheTime[2] = 2;
		coreOfTheTime[3] = 3;
		coreOfTheTime[4] = 1;
		coreOfTheTime[5] = 5;
		coreOfTheTime[6] = 0;
		
		contador = 0;
	}
	
	
	@Override
	public boolean assignSpectrum(int numberOfSlots, Circuit circuit, ControlPlane cp) {
		int chosenCore = coreAssignment();
    	List<int[]> composition = IntersectionFreeSpectrum.merge(circuit.getRoute(), circuit.getGuardBand(), chosenCore);
        
        int chosen[] = policy(numberOfSlots, composition, circuit, cp);
        circuit.setSpectrumAssigned(chosen);
        circuit.setIndexCore(chosenCore);
        
       //System.out.println("\n\nCorePrioritization FF");
       //System.out.println("Core escolhido: "+chosenCore);
       //System.out.println("Faixa de slots: "+chosen[0]+"-"+chosen[1]);
        
        if (chosen == null)
        	return false;

        return true;
	}

	@Override
	public int coreAssignment() {
		int escolha;
		escolha = contador;
		
		if (contador == 6) {
    		contador = 0;
    		return coreOfTheTime[escolha];
    	}else {
    		contador++;
    		return coreOfTheTime[escolha];
    	}
	}


	@Override
	public int[] policy(int numberOfSlots, List<int[]> freeSpectrumBands, Circuit circuit, ControlPlane cp) {
		int maxAmplitude = circuit.getPair().getSource().getTxs().getMaxSpectralAmplitude();
        if(numberOfSlots> maxAmplitude) return null;
    	int chosen[] = null;
    	
        for (int[] band : freeSpectrumBands) {
        	
            if (band[1] - band[0] + 1 >= numberOfSlots) {
                chosen = band.clone();
                chosen[1] = chosen[0] + numberOfSlots - 1;//It is not necessary to allocate the entire band, just the amount of slots required
                break;
            }
        }
        
       // System.out.println("teste2");
        
        return chosen;
	}
	
//	private int returnPrioridade() {
//		int core;
//		
//		for(int i=6; i>=0; i--) {
//			
//		}
//	}
	
	private void incrementAdjacents(int id) {
		ArrayList<Integer> listof = returnAdjacents(id);
		
		for(int i : listof) {
			pesos[i]++;
		}
	}
	
	private ArrayList<Integer> returnAdjacents(int id) {
		ArrayList<Integer> listof = new ArrayList<Integer>();
		
		if(id == 0) {
			listof.add(1); // all
			listof.add(2); // all
			listof.add(3); // all
			listof.add(4); // all
			listof.add(5); // all
			listof.add(6); // all
			return listof;
		}
		
		if(id == 6) {
			listof.add(0); //central
			listof.add(5); //anterior
			listof.add(1); //proximo
			return listof;
		}
		
		if(id == 1) {
			listof.add(0); //central
			listof.add(6); //anterior
			listof.add(2); //proximo
			return listof;
		}
		
		if((id>1) && (id<6)) {
			listof.add(0); //central
			listof.add(id-1); //anterior
			listof.add(id+1); //proximo
			return listof;
		}
		
		return null;
	}
}
