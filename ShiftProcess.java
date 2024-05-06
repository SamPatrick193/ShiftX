package Task1;

import java.time.*;

class Shift {
    String timing;
    String days;

    public String ShiftTiming(String shTime, String day) {
        this.timing = shTime;
        this.days = day;
        String shifttime = null;
        System.out.println("Received ShiftTiming: "+timing+"& Day "+day);

        if (timing.equalsIgnoreCase("OFF")) {
            System.out.println("Happy Holiday!!!");
            shifttime = day + "--OFF";
        } else {
            System.out.println("Entering timing Parse Block...");
            shifttime = parseShiftTime(timing, day);
        }

        return shifttime;
    }

    private String parseShiftTime(String timing, String day) {
        String[] time = timing.split("-");
        String startTime = time[0];
        String endTime = time[1];
        String breakTime = time[2];

        // Conversion of String to Integer to LocalTime Object
        LocalTime start_time = parseTime(startTime);
        LocalTime end_time = parseTime(endTime);
        int breakTimeMinutes = parseBreakTime(breakTime);
        System.out.println("breakTimeMinutes" + breakTimeMinutes);

        // Creating Duration Object from LocalTime Object
        Duration duration = calculateDuration(start_time, end_time, breakTimeMinutes);

        return day + "--Start: " + start_time + " End: " + end_time + " Break Time: " + formatBreakTime(breakTimeMinutes)
                + " Duration (with break): " + duration;
    }

    private LocalTime parseTime(String time) {
        int hours = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(2, 4));
        return LocalTime.of(hours, minutes);
    }

    private int parseBreakTime(String breakTime) {
        return Integer.parseInt(breakTime.substring(1, 3));
    }

    private Duration calculateDuration(LocalTime startTime, LocalTime endTime, int breakTimeMinutes) {
        Duration duration = Duration.between(startTime, endTime);
        long durationSeconds = duration.getSeconds() - (breakTimeMinutes * 60);
        return Duration.ofSeconds(durationSeconds);
    }

    private String formatBreakTime(int breakTimeMinutes) {
        return String.format("%02d:%02d", breakTimeMinutes / 60, breakTimeMinutes % 60);
    }

    public String Pattern(String details) {
        System.out.println("Entering Shift Pattern Identification Block");
        String[] sp = details.split(",");

        String patterns;
        boolean equal = true;
        if (sp.length > 1) {
            String dailyDays = null;

            for (String shift : sp) {
                System.out.println("Length of sp is " + shift.length());
                System.out.println("shift of sp is " + shift);
                if (!shift.equals("OFF")) {
                    dailyDays = shift;
                    System.out.println("dailyDays of sp is " + dailyDays);
                    break;
                }
            }

            if (dailyDays != null) {
                for (String shift : sp) {
                    System.out.println("After Break Statement Shift  is " + shift);
                    if (shift.length() > 3 && !shift.equals("OFF") && !shift.equals(dailyDays)) {
                        equal = false;
                        break;
                    }
                }
            } else {
                equal = true;
            }
        }

        if (equal)
            return patterns = "Regular";
        return patterns = "Variable";
    }
    
    public String calculateBetweenDays(String endTime, String startTime) {
        System.out.println("----------Method Duration Calculation Starts----------------");
        System.out.println("Inward Input Endtime"+endTime+"Inward startTime"+startTime);
        String finalDuration = null;
        int prevEndHour = 0, prevEndMinute = 0;
        int prevStartHour = 0, prevStartMinute = 0;
        int b = 24, endTotal = 0;

        if (!startTime.equals("OFF") && !endTime.equals("OFF")) {
            int[] parsedTiming = parseInt(startTime, endTime);
            prevStartHour = parsedTiming[0];
            prevStartMinute = parsedTiming[1];
            prevEndHour = parsedTiming[2];
            prevEndMinute = parsedTiming[3];
            System.out.println("Start Hour: " + prevStartHour + " Start Minute: " + prevStartMinute);

            if (prevEndHour > prevStartHour) {
                endTotal = (b - prevEndHour + prevStartHour) * 60;
                if (prevEndMinute > prevStartMinute) {
                    endTotal -= (prevEndMinute - prevStartMinute);
                } else {
                    endTotal += (prevStartMinute - prevStartMinute);
                }
            } else if (prevEndHour < prevStartHour) {
                endTotal = (prevStartHour - prevEndHour) * 60;
                if (prevEndMinute > prevStartMinute) {
                    endTotal += (prevEndMinute - prevStartMinute);
                } else {
                    endTotal += (prevStartMinute - prevEndMinute);
                }
            }

            int hours = endTotal / 60;
            int minutes = endTotal % 60;

            finalDuration = "Duration between days " + hours + ":" + minutes;
        } else {
            System.out.println("Off days Calculation...");
            if (startTime.equals("OFF")) {
                System.out.println("End Time: " + endTime + ", Start Time: " + startTime);
                startTime = "2400";
                int[] parsedTiming = parseInt(startTime, endTime);
                prevStartHour = parsedTiming[0];
                prevStartMinute = parsedTiming[1];
                prevEndHour = parsedTiming[2];
                prevEndMinute = parsedTiming[3];
                System.out.println("Parsed prevEndHour: " + prevEndHour);
                System.out.println("Parsed prevEndMinute: " + prevEndMinute);
                endTotal = (b * 60) - (prevEndHour * 60) - prevEndMinute;
                endTotal = endTotal + (prevStartHour * 60) + prevStartMinute;
            }
            if (endTime.equals("OFF")) {
                endTime = "2400";
                int[] parsedTiming = parseInt(startTime, endTime);
                prevStartHour = parsedTiming[0];
                prevStartMinute = parsedTiming[1];
                prevEndHour = parsedTiming[2];
                prevEndMinute = parsedTiming[3];
                endTotal = (prevEndHour * 60) + prevEndMinute + (prevStartHour * 60) + prevStartMinute;
            }
            finalDuration = calculateDurationBtDays(endTotal);
        }
        return finalDuration;
    }

    public String calculateDurationBtDays(int endTotal) {
        int hours = endTotal / 60;
        int minutes = endTotal % 60;
        String finalDuration = "Duration between days " + hours + ":" + minutes;
        return finalDuration;
    }

    public int[] parseInt(String startTime, String endTime) {
        int[] parsedTiming = new int[4];
    	parsedTiming[0] = Integer.parseInt(startTime.substring(0, 2));
        parsedTiming[1] = Integer.parseInt(startTime.substring(2, 4));
        parsedTiming[2] = Integer.parseInt(endTime.substring(0, 2));
        parsedTiming[3] = Integer.parseInt(endTime.substring(2, 4));
        return parsedTiming;
    }


}

public class ShiftProcess {
	
	public static void main(String args[]) {
		
		String[] days = {"Day1", "Day2", "Day3", "Day4", "Day5", "Day6", "Day7"};	
		String details="OFF,0900-1730-B30,0900-1700-B30,0900-1730-B30,0900-1730-B30,OFF,0900-1730-B30";
		System.out.println(details.length());
		Shift s1=new Shift();
		
		//Conversion of String to String Array
		String[] shiftDetails = details.split(",");
		for(String d:shiftDetails) {
			System.out.println(d.toUpperCase()+"---"+d.length());
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
            
            String duration = s1.calculateBetweenDays(endTime, startTime);
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
