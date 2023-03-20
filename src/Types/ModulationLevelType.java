package Types;
/**
 * Descreve os tipos de formato de modulação 
 * considerados no simulador.
 * @author André 
 */
public enum ModulationLevelType {
	
	SIXTYFOUR_QAM(1, "64-QAM", 14.8, 64),
	THIRTYTWO_QAM(2, "32-QAM", 12.6, 32),
	SIXTEEN_QAM(3, "16-QAM", 10.5, 16),
	EIGHT_QAM(4,"8-QAM", 8.6, 8),
	FOUR_QAM(5, "4-QAM", 6.8, 4);
	
	/**
	 * Código do formato de modulação.
	 * @author Andr� 			
	 */		
	private int code;
	/**
	 * SNR por bit do formato de modulação.
	 * @author André 			
	 */	
	private double snrIndB;
	/**
	 * Descrição do formato de modulação.
	 * @author André 			
	 */	
	private String description;
	/**
	 * Constelação do formato de modulação.
	 * @author André 			
	 */		
	private int constelation;
	/**
	 * Construtor da classe.
	 * @param code
	 * @param description
	 * @param constelation
	 * @author André
	 */		
	private ModulationLevelType(final int code, final String description, final double snrIndB, final int constelation){
		this.code = code;
		this.snrIndB = snrIndB;
		this.description = description;
		this.constelation = constelation;
	}
	/**
	 * M�todo para retornar o c�digo do tipo de formato de modulação
	 * @return code
	 * @author André 			
	 */
	public int getCode() {
		return this.code;
	}
	/**
	 * M�todo para retornar a descri��o do tipo de formato de modulação
	 * @return description
	 * @author André 			
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * Método para retornar a SNR por bit do formato de modulação.
	 * @return snrIndB
	 * @author André 			
	 */	
	public double getSNRIndB() {
		return this.snrIndB;
	}
	/**
	 * Método para retornar a constela��o do formato de modulação
	 * @return constelation
	 * @author André 			
	 */		
	public int getConstelation() {
		return this.constelation;
	}
}