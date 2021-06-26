package network;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


/**
 * This class represents a link in SDM network
 * 
 * @author Jurandir
 */
public class Link implements Serializable {
	public static final int NUMBEROFCORES = 7;
	
//	private Core cores[];
	private ArrayList<Core> cores;
	
	
    private Oxc source;
    private Oxc destination;
    private double cost;
//    private Spectrum spectrum;
    private double distance;
    
//    private HashSet<Circuit> circuitList;

    /**
     * Creates a new instance of Link.
     *
     * @param s             Oxc New value of property source.
     * @param d             Oxc New value of property destination.
     * @param numberOfSlots int New value of property number of slots
     * @param spectrumBand  double New value of property spectrum band
     * @param distance      double New Value of distance
     */
    public Link(Oxc s, Oxc d, int numberOfSlots, double spectrumBand, double distance) {
//        this.cores = new Core[NUMBEROFCORES];
 //       startCores(s, d, numberOfSlots, spectrumBand, distance);
    	this.source = s;
        this.destination = d;
//        this.spectrum = new Spectrum(numberOfSlots, spectrumBand);
        this.distance = distance;
        
//        this.circuitList = new HashSet<Circuit>();
        this.cores = new ArrayList<Core>();
        startCores2(s, d, numberOfSlots, spectrumBand, distance);
    }

    /**
     * Is node x destination of this link.
     *
     * @param x Oxc
     * @return true if Oxc x is destination of this Link; false otherwise.
     */
    public boolean adjacent(Oxc x) {
        if (destination == x) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method occupies a certain range of spectrum defined in the parameter
     *
     * @param interval - int[] - Vector of two positions, the first refers to the first slot 
     *                           and the second to the last slot to be used
     * @return boolean
     */
 //   public boolean useSpectrum(int interval[], int guardBand) throws Exception {
 //       return spectrum.useSpectrum(interval, guardBand);
 //   }

    /**
     * Releases a certain range of spectrum being used
     *
     * @param spectrumBand int[]
     */
 //   public void liberateSpectrum(int spectrumBand[], int guardBand) throws Exception {
 //       spectrum.freeSpectrum(spectrumBand, guardBand);
 //   }

    /**
     * Getter for property destination.
     *
     * @return Oxc destination
     */
    public Oxc getDestination() {
        return destination;
    }

    /**
     * Setter for property destination.
     *
     * @param destination Oxc New value of property destination.
     */
    public void setDestination(Oxc destination) {
        this.destination = destination;
    }

    /**
     * Setter for property source.
     *
     * @param source Oxc New value of property source.
     */
    public void setSource(Oxc source) {
        this.source = source;
    }

    /**
     * Getter for property source.
     *
     * @return Oxc source
     */
    public Oxc getSource() {
        return source;
    }

    /**
     * Getter for property cost.
     *
     * @return double cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * Setter for property Cost.
     *
     * @param cost double new cost.
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Returns the distance of this link
     *
     * @return double
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns the name of the link in the format <source, destination>
     *
     * @return String
     */
    public String getName() {
        return "<" + getSource().getName() + "," + getDestination().getName() + ">";
    }

    /**
     * Returns the list of spectrum bands available on the link
     *
     * @return List<int[]>
     */
 //   public List<int[]> getFreeSpectrumBands(int guardBand) {
 //       return spectrum.getFreeSpectrumBands(guardBand);
 //   }
    
    /**
     * Returns the bandwidth of a slot
     * 
     * @return the slotSpectrumBand
     */
 //   public double getSlotSpectrumBand() {
 //       return spectrum.getSlotSpectrumBand();
 //   }

    /**
     * Returns the number of slots in the link
     * 
     * @return int the numOfSlots
     */
  //  public int getNumOfSlots() {
  //      return spectrum.getNumOfSlots();
  //  }
    
    /**
     * Returns the number of used slots
     * 
	 * @return int
	 */
//	public int getUsedSlots(){
//		return spectrum.getUsedSlots();
//	}

    /**
     * Returns link usage
     *
     * @return Double
     */
 //   public Double getUtilization() {
 //       return this.spectrum.utilization();
 //   }
	
	/**
	 * Returns the list of circuits that use this link
	 * 
	 * @return the listRequests
	 */
//	public HashSet<Circuit> getCircuitList() {
//		return circuitList;
//	}

	/**
	 * Sets the list of circuits that use this link
	 * 
	 * @param listRequests the listRequests to set
	 */
//	public void setCircuitList(HashSet<Circuit> circuitList) {
//		this.circuitList = circuitList;
//	}
	
	/**
	 * Adds a circuit to the list of circuits that use this link
	 * 
	 * @param circuit Circuit
	 */
//	public void addCircuit(Circuit circuit){
//		if(!circuitList.contains(circuit)){
//			circuitList.add(circuit);
//		}
//	}
	
	/**
	 * Removes a circuit from the list of circuits using this link
	 * 
	 * @param circuit Circuit
	 */
//	public void removeCircuit(Circuit circuit){
//		circuitList.remove(circuit);
//	}
	
	
//	private void startCores(Oxc s, Oxc d, int numberOfSlots, double spectrumBand, double distance) {
//		for(int i=0; i<NUMBEROFCORES; i++) {
//			this.cores[i] = new Core(s, d, numberOfSlots, spectrumBand, distance, i);
//		}
//	}
	
	private void startCores2(Oxc s, Oxc d, int numberOfSlots, double spectrumBand, double distance) {
		for(int i=0; i<NUMBEROFCORES; i++) {
			this.cores.add(i, new Core(s, d, numberOfSlots, spectrumBand, distance, i));
		}
	}
	
	public Core returnRandonCore() {
		Random gerador = new Random();
		//return cores[gerador.nextInt(7)];
		return cores.get(gerador.nextInt(7));
	}
	
	public Core getCore(int coreNumber) {
		for(Core core : this.cores) {
			if(core.getId() == coreNumber) {
				return core;
			}
		}
		return null;
	}
	
//	public Core getCore(int coreNumber) {
//		//return cores[coreNumber];
//		return cores.get(coreNumber);
//	}
	

	
	public ArrayList<Integer> indexOfAdjacentsCores(int id){
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
	
	public int quantAdjacentsCores(int id) {
		if (id == 0) {
			return 6;
		}else {
			return 3;
		}
	}
	
	private boolean isAdjacent(Core core1, Core core2) {
		ArrayList<Integer> indexOfAdjacents = indexOfAdjacentsCores(core1.getId());
		
		if(indexOfAdjacents.contains(core2.getId())) {
			return true;
		}
		
		return false;
	}
	
	public ArrayList<Core> coresAdjacents(int id){
		ArrayList<Core> coresAdj = new ArrayList<Core>();
		
		for(Core core : this.cores) {
			if(isAdjacent(getCore(id), core)) {
				coresAdj.add(core);
			}
			//if(isAdjacent(this.cores.get(id), core)) {
			//	coresAdj.add(core);
			//}
		}
		
		return coresAdj;
	}
	
	

	
//	public Core[] listOfAdjacentsCores(int id) {
//		Core[] adjacentCores = new Core[quantAdjacentsCores(id)];
//		int[] indexAdjacentsCores = indexOfAdjacentsCores(id);
		
//		for(int i : indexAdjacentsCores) {
//			adjacentCores = this.cores[i];
//		}
//	}
	/**
	 * 
	 * Return the total number of slots in link
	 * 
	 * @return number of slots
	 */
	public int totalSlots() {
		int cont = 0;
		
		for(Core core : cores) {
			cont = cont + core.getNumOfSlots();
		}
		
		return cont;
	}
	
	public ArrayList<Core> getCores() {
		return cores;
	}
	
}
