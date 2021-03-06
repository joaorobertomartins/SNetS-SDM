package simulationControl.parsers;

import java.io.Serializable;

/**
 * This class represents the Simulation configuration file, its representation in entity form is 
 * important for the storage and transmission of this type of configuration in the JSON format.
 * 
 * Created by Iallen on 04/05/2017.
 */
public class SimulationConfig {

    private int requests;
    private int rmlsaType;
    private String routing;
    private String spectrumAssignment;
    private String coreAndSpectrumAssignment;
    private String integratedRmlsa;
    private String modulationSelection;
    private String grooming;
    private int loadPoints;
    private int replications;
    private Metrics activeMetrics = new Metrics();
    private String regeneratorAssignment;
    private int networkType;
    private int threads = 1;

    public static class Metrics implements Serializable {

        public boolean BlockingProbability = true;
        public boolean BandwidthBlockingProbability = true;
        public boolean SpectrumUtilization = true;
        public boolean SpectrumSizeStatistics = true;
 //       public boolean ExternalFragmentation = true;
        public boolean RelativeFragmentation = true;
        public boolean TransmittersReceiversRegeneratorsUtilization = true;
        public boolean EnergyConsumption = true;
        public boolean ModulationUtilization = true;
        public boolean ConsumedEnergy = true;
        public boolean GroomingStatistics = true;
    }

    /**
     * Returns the minimum number of requests
     * 
     * @return int
     */
    public int getRequests() {
        return requests;
    }

    /**
     * Sets the minimum number of requests
     * 
     * @param requests int
     */
    public void setRequests(int requests) {
        this.requests = requests;
    }

    /**
     * Returns the RMLSA type
     * 
     * @return int
     */
    public int getRmlsaType() {
        return rmlsaType;
    }

    /**
     * Sets the RMLSA type
     * 
     * @param rmlsaType int
     */
    public void setRmlsaType(int rmlsaType) {
        this.rmlsaType = rmlsaType;
    }

    /**
     * Returns the routing algorithm
     * 
     * @return String
     */
    public String getRouting() {
        return routing;
    }

    /**
     * Sets the routing algorithm
     * 
     * @param routing String
     */
    public void setRouting(String routing) {
        this.routing = routing;
    }

    /**
     * Returns the spectrum assignment algorithm
     * 
     * @return String
     */
    public String getSpectrumAssignment() {
        return spectrumAssignment;
    }
    
    /**
     * Returns the core and spectrum assignment algorithm
     * 
     * @return String
     */
    public String getCoreAndSpectrumAssignment() {
        return coreAndSpectrumAssignment;
    }


    /**
     * Sets the spectrum assignment algorithm
     * 
     * @param spectrumAssignment String
     */
    public void setSpectrumAssignment(String spectrumAssignment) {
        this.spectrumAssignment = spectrumAssignment;
    }
    
    /**
     * Sets the Core and spectrum assignment algorithm
     * 
     * @param spectrumAssignment String
     */
    public void setCoreAndSpectrumAssignment(String coreAndSpectrumAssignment) {
        this.coreAndSpectrumAssignment = coreAndSpectrumAssignment;
    }

    /**
     * Returns the integrated RMLSA algorithm
     * 
     * @return String
     */
    public String getIntegratedRmlsa() {
        return integratedRmlsa;
    }

    /**
     * Sets the integrated RMLSA algorithm
     * 
     * @param integratedRmlsa String
     */
    public void setIntegratedRmlsa(String integratedRmlsa) {
        this.integratedRmlsa = integratedRmlsa;
    }

    /**
     * Returns the modulation
     * 
     * @return String
     */
    public String getModulationSelection() {
        return modulationSelection;
    }

    /**
     * Sets the modulation
     * 
     * @param modulationSelection String
     */
    public void setModulationSelection(String modulationSelection) {
        this.modulationSelection = modulationSelection;
    }

    /**
     * Returns the grooming algorithm
     * 
     * @return String
     */
    public String getGrooming() {
        return grooming;
    }

    /**
     * Sets the grooming algorithm
     * 
     * @param grooming String
     */
    public void setGrooming(String grooming) {
        this.grooming = grooming;
    }

    /**
     * Return the number of load points
     * 
     * @return int
     */
    public int getLoadPoints() {
        return loadPoints;
    }

    /**
     * Sets the number of load points
     * 
     * @param loadPoints int
     */
    public void setLoadPoints(int loadPoints) {
        this.loadPoints = loadPoints;
    }

    /**
     * Returns the number of replications
     * 
     * @return int
     */
    public int getReplications() {
        return replications;
    }

    /**
     * Sets the number of replications
     * 
     * @param replications int
     */
    public void setReplications(int replications) {
        this.replications = replications;
    }

    /**
     * get the active metrics in this simulation
     * 
     * @return
     */
    public Metrics getActiveMetrics() {
        return activeMetrics;
    }

    /**
     * set the active metrics in this simulation
     * 
     * @return
     */
    public void setActiveMetrics(Metrics activeMetrics) {
        this.activeMetrics = activeMetrics;
    }

	/**
	 * Returns the regenerators assignment algorithm
	 * 
	 * @return the regeneratorAssignment
	 */
	public String getRegeneratorAssignment() {
		return regeneratorAssignment;
	}

	/**
	 * Sets the regenerators assignment algorithm
	 * 
	 * @param regeneratorAssignment the regeneratorAssignment to set
	 */
	public void setRegeneratorAssignment(String regeneratorAssignment) {
		this.regeneratorAssignment = regeneratorAssignment;
	}

	/**
	 * Returns the network type
	 * 
	 * @return the networkType
	 */
	public int getNetworkType() {
		return networkType;
	}

	/**
	 * Sets the network type
	 * 
	 * @param networkType the networkType to set
	 */
	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}
