package grmlsa.spectrumAssignment;

import java.util.List;

import network.Circuit;
import network.ControlPlane;
import network.Link;
import util.IntersectionFreeSpectrum;

/**
 * This class represents the spectrum allocation technique called First Fit.
 * This technique chooses the first free spectrum band that accommodates the request.
 *
 * @author Iallen
 */
public class FirstFit implements SpectrumAssignmentAlgorithmInterface {
	private int coreOfTheTime;
	
	public FirstFit() {
		this.coreOfTheTime = 0;
	}
	
    @Override
    public boolean assignSpectrum(int numberOfSlots, Circuit circuit, ControlPlane cp) {
        int chosenCore = coreAssignment();
    	List<int[]> composition = IntersectionFreeSpectrum.merge(circuit.getRoute(), circuit.getGuardBand(), chosenCore);
        
        int chosen[] = policy(numberOfSlots, composition, circuit, cp);
        circuit.setSpectrumAssigned(chosen);
        circuit.setIndexCore(chosenCore);
        
       // System.out.println("teste1");
        
       // teste(circuit, chosen, chosenCore);
        
        if (chosen == null)
        	return false;

        return true;
    }
    
    @Override
    public int[] policy(int numberOfSlots, List<int[]> freeSpectrumBands, Circuit circuit, ControlPlane cp){
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
    
    private int coreAssignment() {
    	if (coreOfTheTime == 6) {
    		coreOfTheTime = 0;
    		return 6;
    	}else {
    		int temp = coreOfTheTime;
    		coreOfTheTime++;
    		return temp;
    	}
    }
    
//    private int coreAssignment() {
//    	return 1;
//    }
    
    private void teste(Circuit circuit, int[] chosen, int chosenCore) {
		for(Link link : circuit.getRoute().getLinkList()) {
	    	System.out.println("Link: "+link.getSource().getName()+"-"+link.getDestination().getName());
			System.out.println("primeiro slot: "+chosen[0]);
			System.out.println("ultimo slot: "+chosen[1]);
			System.out.println("core: "+chosenCore);
			System.out.println("quant. de slots: "+ (chosen[1]-chosen[0]+1));
			System.out.println("----");			
			
		}   	

    }
}