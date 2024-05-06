package Task1;

import java.time.*;

class Shift{
	String timing;
	String days;
	


	public String ShiftTiming(String shTime, String day) {
	    this.timing = shTime;
	    this.days = day;
	    String shifttime = null;

	    if (timing.equalsIgnoreCase("OFF")) {
	    	System.out.println("Happy Holiday!!!");
	        shifttime = day + "--OFF";
	    } 
	    else {
	        System.out.println("Entering timing Parse Block...");
	        String[] time = timing.split("-");
	        String startTime = time[0];
	        String endTime = time[1];
	        String breakTime = time[2];
	        
	        //Conversion of String to Integer
	        int shour = Integer.parseInt(startTime.substring(0, 2));
	        int sminute = Integer.parseInt(startTime.substring(2, 4));
	        int ehour = Integer.parseInt(endTime.substring(0, 2));
	        int eminute = Integer.parseInt(endTime.substring(2, 4));
	        int btime = Integer.parseInt(breakTime.substring(1, 3));
	        
	        //Conversion of Integer to LocalTime Object
	        LocalTime start_time = LocalTime.of(shour, sminute);
	        LocalTime end_time = LocalTime.of(ehour, eminute);
	        LocalTime break_time = LocalTime.of(0, btime);
	        
	        //Creating Duration Object from LocalTime Object
	        Duration duration = Duration.between(start_time, end_time);

	        long durationSeconds = duration.getSeconds();
	        durationSeconds -= btime * 60; // Convert break time from minutes to seconds
	        duration = Duration.ofSeconds(durationSeconds);
	        System.out.println("duration(With Break Time)"+duration);
	        
	        

	        shifttime = day + "--Start: " + start_time + " End: " + end_time + " Break Time: "+break_time +" Duration (with break): " + duration;
	    }

	    return shifttime;
	}

	
	public String Pattern(String details) {
	    System.out.println("Entering Shift Pattern Identification Block");
	    String[] sp = details.split(",");
	    
	    String patterns;
	    boolean equal = true;    
	    if (sp.length > 1) {
	        String dailyDays = null;

	        for (String shift : sp) {
	        	System.out.println("Length of sp is "+shift.length());
	    	    System.out.println("shift of sp is "+shift);
	            if (!shift.equals("OFF")) {
	            	dailyDays = shift;
	                System.out.println("dailyDays of sp is "+dailyDays);
	                break;
	            }
	        }

	        if (dailyDays != null) {
	            for (String shift : sp) {
	            	System.out.println("After Break Statement Shift  is "+shift);
	                if (shift.length() > 3 && !shift.equals("OFF") && !shift.equals(dailyDays)) {
	                    equal = false;
	                    break;
	                }
	            }
	        } else {
	            equal = true;
	        }
	    }
	    
	    if(equal)
	        return patterns = "Regular";
	    return patterns = "Variable";    
	}


	//											17:30          09:00				
	public String calculateShiftDuration(String endTime, String startTime) {
	    System.out.println("----------Method Duration Calculation Starts----------------");
	    String finalDuration;
	    int prevEndHour = 0,prevEndMinute=0;
	    int prevStartHour =0,prevStartMinute=0;
	    int b = 24,endTotal=0;
//	    System.out.println("Inside endtime"+endTime+"Inside starttime"+startTime);

	    if (!startTime.equals("OFF") & !endTime.equals("OFF")) {
	        int endHour = Integer.parseInt(endTime.substring(0, 2));
	        int endMinute = Integer.parseInt(endTime.substring(2, 4));
	        System.out.println("End Hour: " + endHour + " End Minute: " + endMinute);

	        int startHour = Integer.parseInt(startTime.substring(0, 2));
	        int startMinute = Integer.parseInt(startTime.substring(2, 4));
	        System.out.println("Start Hour: " + startHour + " Start Minute: " + startMinute);


	        if (endHour > startHour) {
	            endTotal = (b - endHour + startHour) * 60;
	            if (endMinute > startMinute) {
	                endTotal -= (endMinute - startMinute);
	            } else {
	                endTotal += (startMinute - endMinute);
	            }
	        } else if (endHour < startHour) {
	            endTotal = (startHour - endHour) * 60;
	            if (endMinute > startMinute) {
	                endTotal += (endMinute - startMinute);
	            } else {
	                endTotal += (startMinute - endMinute);
	            }
	        }

	        int hours = endTotal / 60;
	        int minutes = endTotal % 60;

	        finalDuration = "Duration between days " + hours + ":" + minutes;
	    } 
	    else 
	    {
	        System.out.println("Off days Calculation...");
	        if (startTime.equals("OFF")) {
	            System.out.println("End Time: " + endTime + ", Start Time: " + startTime);
	            startTime = "2400";

	            System.out.println("Replaced OFF Start Time: " + startTime);
	            prevStartHour = Integer.parseInt(startTime.substring(0, 2));
		        prevStartMinute = Integer.parseInt(startTime.substring(2));
	            prevEndHour = Integer.parseInt(endTime.substring(0, 2));
		        prevEndMinute = Integer.parseInt(endTime.substring(2));
	            
		        System.out.println("Parsed prevEndHour: " + prevEndHour);
		        System.out.println("Parsed prevEndMinute: " + prevEndMinute);
		        endTotal = (b * 60)-(prevEndHour * 60)-prevEndMinute;
		        endTotal = endTotal+(prevStartHour*60)+prevStartMinute;
	        }
//	        if (endTime.equals("OFF")) {
//	            System.out.println("End Time: " + endTime + ", Start Time: " + startTime);
//	            endTime = "0000";
//	            System.out.println("Replaced OFF End Time: " + endTime);
//	            System.out.println("Off End Time: " + endTime.length()+"& Off Start Time: " + startTime.length() );
//		        prevEndHour = Integer.parseInt(endTime.substring(0, 2));
//		        prevEndMinute = Integer.parseInt(endTime.substring(2));
//		        System.out.println("Parsed prevEndHour: " + prevEndHour);
//		        System.out.println("Parsed prevEndMinute: " + prevEndMinute);
//	        }
	        if (endTime.equals("OFF")) {
	        	endTime = "2400";
	        	prevEndHour = Integer.parseInt(endTime.substring(0, 2));
		        prevEndMinute = Integer.parseInt(endTime.substring(2,4));
		        prevStartHour = Integer.parseInt(startTime.substring(0, 2));
		        prevStartMinute = Integer.parseInt(startTime.substring(2,4));
		        endTotal = (prevEndHour*60) + prevEndMinute + prevStartHour*60 + prevStartMinute;
	        }
	    
			int hours = endTotal / 60;
	        int minutes = endTotal % 60;
	        
	        finalDuration = "Duration between days " + hours + ":" + minutes;
	    }

	    return finalDuration;
	    
	}

}

public class ShiftProcess {
	
	public static void main(String args[]) {
		
		String[] days = {"Day1", "Day2", "Day3", "Day4", "Day5", "Day6", "Day7"};	
		String details="OFF,0900-1730-B30,0900-1700-B30,0900-1730-B30,0900-1730-B30,OFF,0900-1730-B30";
				
		Shift s1=new Shift();
		
		//Conversion of String to String Array
		String[] shiftDetails = details.split(",");
		for(String d:shiftDetails) {
			System.out.println(d);
		}
		System.out.println("---------------------------------------------------");
		

		
		for(int i=0;i<days.length;i++) {
			days[i]=s1.ShiftTiming(shiftDetails[i],days[i]);
			System.out.println(days[i]);
			System.out.println("*******************************************");
		}
		
		
		for(int i = 0; i < days.length-1; i++) {
			System.out.println("Splitting Start Time and End Time");
			String startTime,endTime;
			String[] endTimeSplit = shiftDetails[i].split("-");
//			System.out.println("endTimeSplit:::"+endTimeSplit.length+"&&&"+endTimeSplit[i].length());
		    endTime = (endTimeSplit.length > 1) ? endTimeSplit[1] : "";
		    System.out.println("End..." + endTime);

			if(endTime.equals("")) {
				System.out.println("End Time is Null");
				endTime = "OFF";
			}
			
			String[] startTimeSplit = shiftDetails[i + 1].split("-");
			startTime = (startTimeSplit.length > 0) ? startTimeSplit[0] : "";
			System.out.println("Start..." + startTime);
			if(startTime.equals("")||startTime.equals("OFF")) {
				System.out.println("Start Time is Null");
				startTime = "OFF";
			}
			
			

            System.out.println("Final End Time: " + endTime + ", Final Start Time: " + startTime);
            
            String duration = s1.calculateShiftDuration(endTime, startTime);
            System.out.println("Duration between: " + days[i] + " end time and " + days[i + 1] + " start time:");
            System.out.println(duration);
            System.out.println("----------Method Duration Calculation Ends----------------");
        }
		
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$");
		String pat = s1.Pattern(details);
		System.out.println("Current Shift Pattern Type is "+pat);
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$");
		
		
	}
		
}
