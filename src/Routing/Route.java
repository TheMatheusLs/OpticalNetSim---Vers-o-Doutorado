package Routing;

import java.util.ArrayList;
import java.util.List;

import Network.Structure.OpticalLink;

public class Route {
    private List<Integer> path;
    private int originNone;
    private int destinationNone;

    /**
     * Conjuntos de Links que ligam a origem ao destino;
     */
    private List<OpticalLink> upLink;
    /**
     * Conjuntos de Links que ligam o destino a origem;
     */
    private List<OpticalLink> downLink;

    public Route() {		
        this.upLink = new ArrayList<OpticalLink>();
        this.downLink = new ArrayList<OpticalLink>();
    }

    public Route(List<Integer> path) {
        this.path = path;
    }

    public Route(List<Integer> path, int originNone, int destinationNone) {
        this.path = path;
        this.originNone = originNone;
        this.destinationNone = destinationNone;
    }

    public List<OpticalLink> getUpLink() {
        return upLink;
    }


    public void setUpLink(List<OpticalLink> upLink) {
        this.upLink = upLink;
    }


    public List<OpticalLink> getDownLink() {
        return downLink;
    }

    
    public void setDownLink(List<OpticalLink> downLink) {
        this.downLink = downLink;
    }


    @Override
    public String toString() {
        return "Route [Source = " + originNone + " Destination = " + destinationNone +   " path = " + path + "]\n";
    }
}
