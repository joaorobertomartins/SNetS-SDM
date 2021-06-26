package grmlsa.csa;

import java.util.List;

import network.Circuit;
import network.ControlPlane;
import network.Crosstalk;
import util.IntersectionFreeSpectrum;

// Algoritmo proposto em: "Inter-core crosstalk aware greedy algorithm for spectrum and core assignment in space division multiplexed elastic optical networks" 
// Autores: Fabricio R.L. Lobato, Antonio Jacob, Jhonatan Rodrigues, Adolfo V.T. Cartaxo, J.C.W.A. Costa.
// Revista: Optical Switching and Networking
// Ano: 2019
//

public class XtAwareGreedyAlgorithm implements CoreAndSpectrumAssignmentAlgorithmInterface{
	private int coreOfTheTime;
	private int coreCandidate;
	private int coreBestCandidate;
	private int SpectrumCandidate[];
	private Circuit circuitCandidate;
	private Circuit circuitBestCandidate;
	private double deltaXtCandidate;
	private double deltaXtBestCandidate;
		
	public XtAwareGreedyAlgorithm() {
		this.coreOfTheTime = 0;
		this.coreCandidate = 0;
		this.coreBestCandidate = 0;
		//this.circuitCandidate = new Circuit();
		//this.circuitBestCandidate = new Circuit();
		//this.deltaXtCandidate = 0.0;
		//this.deltaXtBestCandidate = 0.0;
	}
	
	@Override
	public boolean assignSpectrum(int numberOfSlots, Circuit circuit, ControlPlane cp) {
		//circuitBestCandidate.setSpectrumAssigned(null);
		//circuitBestCandidate.setIndexCore(0);
		this.circuitCandidate = new Circuit();
		this.circuitBestCandidate = new Circuit();
		this.deltaXtCandidate = 0.0;
		this.deltaXtBestCandidate = 0.0;

		
		
		while (coreOfTheTime < cp.getMesh().getLinkList().get(0).NUMBEROFCORES) {
			List<int[]> composition = IntersectionFreeSpectrum.merge(circuit.getRoute(), circuit.getGuardBand(), coreOfTheTime);
			
			policy(numberOfSlots, composition, circuit, cp);
			
			coreAssignment();
		}
		
		coreOfTheTime = 0;
		
		if (circuitBestCandidate.getSpectrumAssigned() == null) {
	       //System.out.println("opa");
	       return false;
		}
		
		circuit.setSpectrumAssigned(circuitBestCandidate.getSpectrumAssigned());
        circuit.setIndexCore(circuitBestCandidate.getIndexCore());
        
        
        //System.out.println("opaaaaaaaaa");
        return true;

	}

	@Override
	public int coreAssignment() {
		int temp = this.coreOfTheTime;
		this.coreOfTheTime++;
		return temp;
	}

	@Override
	public int[] policy(int numberOfSlots, List<int[]> freeSpectrumBands, Circuit circuit, ControlPlane cp) {
		int maxAmplitude = circuit.getPair().getSource().getTxs().getMaxSpectralAmplitude();
        if(numberOfSlots> maxAmplitude) return null;
    	//int chosen[] = null;
    	//double xtNew = 0.0;
    	//circuitCandidate = (Circuit) circuit.clone();
    	int guardSpectrum[] = circuit.getSpectrumAssigned();
    	int guardCoreIndex = circuit.getIndexCore();
    	double guardXt = circuit.getXt();
		
		for (int[] band : freeSpectrumBands) {
	    	
			for (int i = band[0]; i <= band[1]; i++) {
				int chosen[] = null;
	            if (band[1] - i + 1 >= numberOfSlots) {
	                chosen = band.clone();
	                chosen[0] = i;
	                chosen[1] = chosen[0] + numberOfSlots - 1;//It is not necessary to allocate the entire band, just the amount of slots required
	                
	                circuitCandidate = circuit;
	                
	                circuitCandidate.setSpectrumAssigned(chosen);
	                circuitCandidate.setIndexCore(coreOfTheTime);
	                
	                
	                //circuit.get
	                
	                //circuitCandidate.setRoute(circuit.getRoute());
	                circuitCandidate.setXt(cp.getMesh().getCrosstalk().calculaCrosstalk(circuitCandidate));
	                //System.out.println(circuitCandidate.getXt());
	                
	                if (circuitCandidate.getXt() != 0) {
	                	if(circuitCandidate.getXtAdmissible()) {
	                		//deltaXtCandidate = cp.getMesh().getCrosstalk().xtThreshold(circuit.getModulation()) - circuitCandidate.getXt();
	                		
	                		//System.out.println(cp.getMesh().getCrosstalk().xtThreshold(circuit.getModulation())+"-"+circuitCandidate.getXt());
	                		//deltaXtCandidate = Math.abs(cp.getMesh().getCrosstalk().xtThreshold(circuit.getModulation())) - Math.abs(circuitCandidate.getXt());
	                		deltaXtCandidate = cp.getMesh().getCrosstalk().xtThreshold(circuit.getModulation()) - circuitCandidate.getXt();
	                		
	                		//System.out.println("----------------------");
	                		//System.out.println(deltaXtCandidate+">="+deltaXtBestCandidate);
	                		//System.out.println(deltaXtCandidate>=deltaXtBestCandidate);
	                		//System.out.println("----------------------");
	                		
	                		//if (circuitBestCandidate.getSpectrumAssigned() == null) {
	                		//	deltaXtBestCandidate = deltaXtCandidate;
	                		//	circuitBestCandidate.setSpectrumAssigned(chosen);
	                        //    circuitBestCandidate.setIndexCore(coreOfTheTime);
	                		//}
	                		
	                		
	                		if (deltaXtCandidate >= deltaXtBestCandidate) {
	                			deltaXtBestCandidate = deltaXtCandidate;
	                			circuitBestCandidate.setSpectrumAssigned(circuitCandidate.getSpectrumAssigned());
	                            circuitBestCandidate.setIndexCore(circuitCandidate.getIndexCore());
	                		}
	                	}
	                }
	            }
			}
            
        }
		
		circuit.setSpectrumAssigned(guardSpectrum);
    	circuit.setIndexCore(guardCoreIndex);
    	circuit.setXt(guardXt);
		
		return null;
	}

}
