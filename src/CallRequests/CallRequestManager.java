package CallRequests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/*
 * Classe para gerênciar a requisções que chegam e saem da rede
 */
public class CallRequestManager {
    
    private transient int numbOfEstCallReq;
    private transient List<CallRequest> callReqList;

    public CallRequestManager() {		
		this.callReqList = new ArrayList<CallRequest>();
		this.numbOfEstCallReq = 0;
	}
	
    /**
	 * Método para remover todas as requisições com o tempo esgotado na rede
	 * 
     * @param acumulatedTime
     */
    public void removeCallRequest(final double acumulatedTime) {

        Iterator<CallRequest> iterator = this.callReqList.iterator();

        while(iterator.hasNext()){
			CallRequest callRequest = iterator.next();
			double time = callRequest.getDecayTime();	

			if(time < acumulatedTime){

				// Diminuir os slots que estão sendo usados
				callRequest.getRoute().decreasesSlotsOcupy(callRequest.getFrequencySlots());

				callRequest.desallocate();
				iterator.remove();
			}			 
		}
    }

    /**
	  * M�todo para adicionar um nova conex�o a lista.
	  * @param callRequest
	  * @author Andr� 			
	  */	 
	 public void addCall(final CallRequest callRequest){
		this.callReqList.add(callRequest);
		this.numbOfEstCallReq++;		 
	}

    public void desallocateAllRequests(){
		removeCallRequest(Double.MAX_VALUE);
	}

    public void eraseCallList() {
		this.callReqList = new ArrayList<CallRequest>();
		this.numbOfEstCallReq = 0;
    }
}
