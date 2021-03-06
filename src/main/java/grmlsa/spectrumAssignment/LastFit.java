package grmlsa.spectrumAssignment;

import java.util.List;

import network.Circuit;
import network.ControlPlane;
import util.IntersectionFreeSpectrum;

/**
 * This class represents the spectrum allocation technique called Last Fit.
 * This technique chooses the last free spectrum band that accommodates the request.
 *
 * @author Iallen
 */
public class LastFit implements SpectrumAssignmentAlgorithmInterface {

    @Override
    public boolean assignSpectrum(int numberOfSlots, Circuit circuit, ControlPlane cp) {
    	List<int[]> composition = IntersectionFreeSpectrum.merge(circuit.getRoute(), circuit.getGuardBand(), coreAssignment());
    	
        int chosen[] = policy(numberOfSlots, composition, circuit, cp);
        circuit.setSpectrumAssigned(chosen);
        
        if (chosen == null)
        	return false;

        return true;
    }
    
    @Override
    public int[] policy(int numberOfSlots, List<int[]> freeSpectrumBands, Circuit circuit, ControlPlane cp){
        int maxAmplitude = circuit.getPair().getSource().getTxs().getMaxSpectralAmplitude();
        if(numberOfSlots>maxAmplitude) return null;
    	int chosen[] = null;
        int band[] = null;
        
        for (int i = freeSpectrumBands.size() - 1; i >= 0; i--) {
            band = freeSpectrumBands.get(i);
            
            if (band[1] - band[0] + 1 >= numberOfSlots) {
                chosen = band.clone();
                chosen[0] = chosen[1] - numberOfSlots + 1;//It is not necessary to allocate the entire band, just the amount of slots required
                break;
            }
        }

        return chosen;
    }
    
    private int coreAssignment() {
    	return 0;
    }
}