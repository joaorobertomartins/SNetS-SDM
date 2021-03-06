package util;

import java.util.ArrayList;
import java.util.List;

import grmlsa.Route;
import network.Core;
import network.Link;

/**
 * This class is responsible for performing the merge between lists of spectrum.
 * 
 * @author Iallen
 */
public class IntersectionFreeSpectrum {

	 /**
     * This method returns a list of available spectrum in both lists passed by parameter
     *
     * @param l1 List<int[]>
     * @param l2 List<int[]>
     * @return List<int[]>
     */
    public static List<int[]> merge(List<int[]> l1, List<int[]> l2) {
        List<int[]> res = new ArrayList<>();
        
        int indL1 = 0;
        int indL2 = 0;
        
        int aux1[] = null;
        int aux2[] = null;
        int aux3[];
        
        while (indL1 < l1.size() && indL2 < l2.size()) {
        	
            if (aux1 == null) aux1 = l1.get(indL1).clone();
            if (aux2 == null) aux2 = l2.get(indL2).clone();
            aux3 = new int[2];
            
            if (aux1[0] >= aux2[0]) {
                aux3[0] = aux1[0];
            } else {
                aux3[0] = aux2[0];
            }
            
            if (aux1[1] < aux2[0]) { // Intervals does not overlap, pick up the next free intervals in list 1
                indL1++;
                aux1 = null;
                continue;
            }
            
            if (aux2[1] < aux1[0]) { // Intervals does not overlap, pick up the next free intervals in list 2
                indL2++;
                aux2 = null;
                continue;
            }
            
            if (aux1[1] < aux2[1]) {
                aux3[1] = aux1[1];
                aux2[0] = aux1[1] + 1;
                indL1++;
                aux1 = null;
                res.add(aux3);
                continue;
            }
            
            if (aux2[1] < aux1[1]) {
                aux3[1] = aux2[1];
                aux1[0] = aux2[1] + 1;
                indL2++;
                aux2 = null;
                res.add(aux3);
                continue;
            }
            
            if (aux1[1] == aux2[1]) {
                aux3[1] = aux2[1];
                indL1++;
                indL2++;
                aux1 = null;
                aux2 = null;
                res.add(aux3);
            }
        }
        
        return res;
    }

    /**
     * Returns a list of available spectrum on all links in the route passed by parameter
     *
     * @param route Route
     * @return List<int[]>
     */
    public static List<int[]> merge(Route route, int guardBand, int indexCore) {
        List<Link> links = new ArrayList<>(route.getLinkList());
        List<int[]> composition = links.get(0).getCore(indexCore).getFreeSpectrumBands(guardBand);
        
        for (int i = 1; i < links.size(); i++) {
            composition = IntersectionFreeSpectrum.merge(composition, links.get(i).getCore(indexCore).getFreeSpectrumBands(guardBand));
        }
        
        return composition;
    }
    
 /*   public static List<int[]> merge(Route route, int guardBand) {
        List<Link> links = new ArrayList<>(route.getLinkList());
        List<int[]> composition = links.get(0).getFreeSpectrumBands(guardBand);
        
        for (int i = 1; i < links.size(); i++) {
            composition = IntersectionFreeSpectrum.merge(composition, links.get(i).getFreeSpectrumBands(guardBand));
        }
        
        return composition;
    }*/

    /**
     * Returns the adjacent range less than the range passed by parameter.
	 * Used in optical aggregation algorithms.
     * 
     * @param band int[]
     * @param bandsFree List<int[]>
     * @return int[]
     */
    public static int[] bandAdjacentDown(int band[], List<int[]> bandsFree, int guardBand) {
        for (int[] fl : bandsFree) {
            if (fl[1] == (band[0] - 1 - guardBand)) {
                return fl;
            }
        }
        return null;
    }

    /**
     * Returns the adjacent range higher than the range passed by parameter.
     * Used in optical aggregation algorithms.
     * 
     * @param band int[]
     * @param bandsFree List<int[]>
     * @return int[]
     */
    public static int[] bandAdjacentUpper(int band[], List<int[]> bandsFree, int guardBand) {
        for (int[] fl : bandsFree) {
            if (fl[0] == (band[1] + 1 + guardBand)) {
                return fl;
            }
        }
        return null;
    }

    /**
     * Returns the number of free slots from the upper slot band
     * 
     * @param band int[]
     * @param freeBands List<int[]>
     * @return int
     */
    public static int freeSlotsUpper(int band[], List<int[]> freeBands, int guardBand){
        int[] aux = bandAdjacentUpper(band, freeBands, guardBand);
        if(aux==null) return 0;
        else return aux[1] - aux[0] + 1;
    }

    /**
     * Returns the number of free slots from the down slot band
     * 
     * @param band int[]
     * @param freeBands List<int[]>
     * @return int
     */
    public static int freeSlotsDown(int band[], List<int[]> freeBands, int guardBand){
        int[] aux = bandAdjacentDown(band, freeBands, guardBand);
        if(aux==null) return 0;
        else return aux[1] - aux[0] + 1;
    }
    
}
