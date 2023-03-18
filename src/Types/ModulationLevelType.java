package Types;
/**
 * Descreve os tipos de formato de modula��o 
 * considerados no simulador.
 * @author Andr� 
 */
public enum ModulationLevelType {
	
	SIXTYFOUR_QAM(1, "64-QAM", 14.8, 64),
	THIRTYTWO_QAM(2, "32-QAM", 12.6, 32),
	SIXTEEN_QAM(3, "16-QAM", 10.5, 16),
	EIGHT_QAM(4,"8-QAM", 8.6, 8),
	FOUR_QAM(5, "4-QAM", 6.8, 4);
	
	/**
	 * C�digo do formato de modula��o.
	 * @author Andr� 			
	 */		
	private int code;
	/**
	 * SNR por bit do formato de modula��o.
	 * @author Andr� 			
	 */	
	private double snrIndB;
	/**
	 * Descri��o do formato de modula��o.
	 * @author Andr� 			
	 */	
	private String description;
	/**
	 * Constela��o do formato de modula��o.
	 * @author Andr� 			
	 */		
	private int constelation;
	/**
	 * Construtor da classe.
	 * @param code
	 * @param description
	 * @param constelation
	 * @author Andr�
	 */		
	private ModulationLevelType(final int code, final String description, final double snrIndB, final int constelation){
		this.code = code;
		this.snrIndB = snrIndB;
		this.description = description;
		this.constelation = constelation;
	}
	/**
	 * M�todo para retornar o c�digo do tipo de formato de
	 * modula��o.
	 * @return code
	 * @author Andr� 			
	 */
	public int getCode() {
		return this.code;
	}
	/**
	 * M�todo para retornar a descri��o do tipo de formato de
	 * modula��o.
	 * @return description
	 * @author Andr� 			
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * M�todo para retornar a SNR por bit do formato de
	 * modula��o.
	 * @return snrIndB
	 * @author Andr� 			
	 */	
	public double getSNRIndB() {
		return this.snrIndB;
	}
	/**
	 * M�todo para retornar a constela��o do formato de
	 * modula��o.
	 * @return constelation
	 * @author Andr� 			
	 */		
	public int getConstelation() {
		return this.constelation;
	}
}