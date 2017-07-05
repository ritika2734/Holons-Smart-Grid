package com.htc.utilities;

/**
 * This class contains various constant values that are used in the application
 */
public class ConstantValues {
	
	public static final String MAINLINE="MAINLINE";//Main power line
	public static final String SUBLINE="SUBLINE";//Power line connected to holon object
	public static final String POWERSUBLINE = "POWERSUBLINE";//Power line connected to power source
	public static final String UNDEFINED = "No Power Source Assigned";
	public static final Integer HOLON_CO_RED = 1;//Id of red holon in database
	public static final Integer HOLON_CO_YELLOW = 2;//Id of yellow holon in database
	public static final Integer HOLON_CO_BLUE = 3;//Id of blue holon in database
	public static final Integer HOLON_CO_GREEN = 4;//Id of green holon in database
	public static final String ACCEPTED = "ACCEPTED"; //Request was accepted by holon object(Producer)
	public static final String REJECTED = "REJECTED"; //Request was rejected by holon object(Producer)
	public static final String PENDING = "PENDING"; //Request is pending with holon object(Producer)
	
	/*This means message request was processed by some other holon object (Producer) or Holon Coordinator*/
	public static final String PROCESSED = "PROCESSED BY SOME OTHER PRODUCER";
	public static final String CONNECTION_RESET = "CONNECTION RESET";
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILURE = "FAILURE";
	public static final String COMMUNICATION_MODE_DIRECT = "DIRECT via PEER";//Connection mode when energy is supplied using send mail module
	public static final String COMMUNICATION_MODE_COORDINATOR = "via HOLON COORDINATOR";//Connection mode when energy is supplied using distribute energy module
	
}
