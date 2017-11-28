package fr.uv1.utils;

/**
 * 
 * @author tmarcuzz <br>
 * <br>
 *         This class contains methods to convert a string to a MyCalendar object. <br>
 * 
 */

public class DateConvertor {

	/**
	 * Convert the string into a MyCalendar object
	 * The format has to be dd/mm/yyyy.
	 * 
	 * @param s
	 * 		the string to convert into a MyCalendar
	 * @return date
	 * 		the MyCalendar objet corresponding
	 */
	public static MyCalendar String2MyCalendar(String s){
	
		String[] newS = s.split("[ /]");
		
		MyCalendar date = new MyCalendar(Integer.parseInt(newS[2]),
				Integer.parseInt(newS[1]),Integer.parseInt(newS[0]));
		
		return date;
	}
    
	
}
