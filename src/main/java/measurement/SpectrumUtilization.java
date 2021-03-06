package measurement;

import java.util.HashMap;
import java.util.Set;

import network.ControlPlane;
import network.Core;
import network.Link;
import network.Mesh;
import request.RequestForConnection;
import simulationControl.parsers.SimulationRequest;
import simulationControl.resultManagers.SpectrumUtilizationResultManager;

/**
 * This class stored the metrics related to the use of spectrum.
 * 
 * @author Iallen
 */
public class SpectrumUtilization extends Measurement {
    public final static String SEP = "-";

    private Mesh mesh;

    private double utilizationGen;
    private int numberObservations;
    private HashMap<String, Double> utilizationPerLink;
    private int[] desUtilizationPerSlot;
    private Integer maxSlotsByLinks;

    /**
     * Creates a new instance of SpectrumUtilization
     * 
     * @param loadPoint int
     * @param replication int
     * @param mesh Mesh
     */
    public SpectrumUtilization(int loadPoint, int replication, Mesh mesh) {
        super(loadPoint, replication);
        this.mesh = mesh;
        utilizationGen = 0.0;
        numberObservations = 0;
        utilizationPerLink = new HashMap<String, Double>();

        maxSlotsByLinks = mesh.maximumSlotsByLinks();
        desUtilizationPerSlot = new int[maxSlotsByLinks];

		resultManager = new SpectrumUtilizationResultManager();
    }

    /**
     * Adds a new usage observation of spectrum utilization
     * 
     * @param cp ControlPlane
     * @param success boolean
     * @param request RequestForConnection
     */
    public void addNewObservation(ControlPlane cp, boolean success, RequestForConnection request) {
        this.newObsUtilization();
    }

    @Override
    public String getFileName() {
        return SimulationRequest.Result.FILE_SPECTRUM_UTILIZATION;
    }

    /**
     * Observation of the use of the spectrum resource of the network
     */
    private void newObsUtilization() {
        // General use and per link
        Double utGeral = 0.0;
        Double utLink;
        for (Link link : mesh.getLinkList()){
        	for (Core core : link.getCores()){
	            utGeral += core.getUtilization();
	
	            utLink = this.utilizationPerLink.get(core.getSource().getName() + SEP + core.getDestination().getName());
	            if (utLink == null) utLink = 0.0;
	            utLink += core.getUtilization();
	            this.utilizationPerLink.put(core.getSource().getName() + SEP + core.getDestination().getName(), utLink);
	
	            // Calculate slot unusability
	
	            for (int[] faixa : core.getFreeSpectrumBands(0)) {
	//                if(faixa[1]>400){
	//                    Link l = link;
	//                    int a = 2+2;
	//                }
	                incrementarDesUtFaixa(faixa);
	            }
        	}
        }

        utGeral = utGeral / (double) mesh.getLinkList().size();

        this.utilizationGen += utGeral;

        this.numberObservations++;
    }
    
    /*
    private void newObsUtilization() {
        // General use and per link
        Double utGeral = 0.0;
        Double utLink;
        for (Link link : mesh.getLinkList()) {
            utGeral += link.getUtilization();

            utLink = this.utilizationPerLink.get(link.getSource().getName() + SEP + link.getDestination().getName());
            if (utLink == null) utLink = 0.0;
            utLink += link.getUtilization();
            this.utilizationPerLink.put(link.getSource().getName() + SEP + link.getDestination().getName(), utLink);

            // Calculate slot unusability

            for (int[] faixa : link.getFreeSpectrumBands(0)) {
//                if(faixa[1]>400){
//                    Link l = link;
//                    int a = 2+2;
//                }
                incrementarDesUtFaixa(faixa);
            }
        }

        utGeral = utGeral / (double) mesh.getLinkList().size();

        this.utilizationGen += utGeral;

        this.numberObservations++;
    }*/

    /**
	 * This method increases slot utilization
	 * 
	 * @param band int[]
	 */
    private void incrementarDesUtFaixa(int faixa[]) {
        int i;
        for (i = faixa[0] - 1; i < faixa[1]; i++) {
            desUtilizationPerSlot[i]++;
        }
    }

    /**
     * Returns the HashMap key set
     * The key set corresponds to the links that were analyzed by the metric
     * 
     * @return
     */
    public Set<String> getLinkSet() {
        return this.utilizationPerLink.keySet();
    }

    /**
     * Returns the utilization
     * 
     * @return
     */
    public double getUtilizationGen() {
        return this.utilizationGen / (double) this.numberObservations;
    }

    /**
     * Returns the utilization for a given link passed by parameter
     * 
     * @param link
     * @return
     */
    public double getUtilizationPerLink(String link) {
        return this.utilizationPerLink.get(link) / (double) this.numberObservations;
    }

    /**
     * Returns the utilization for a given slot passed by parameter
     * 
     * @param Slot
     * @return
     */
    public double getUtilizationPerSlot(int Slot) {
        double desUt = (double) desUtilizationPerSlot[Slot - 1] / ((double) this.numberObservations * mesh.getLinkList().size());

        return 1 - desUt;
    }

    /**
     * Returns the maximum slots by links
     * 
     * @return int
     */
    public int getMaxSlotsByLinks(){
    	return maxSlotsByLinks;
    }
}
