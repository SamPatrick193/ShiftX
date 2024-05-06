package Task1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ShiftTest1 {

	@Test
    public void t_ShiftTiming() {
        Shift shift = new Shift();
        String result = shift.ShiftTiming("0900-1800-B30", "Day2");
        assertEquals("Day2--Start: 09:00 End: 18:00 Break Time: 00:30 Duration (with break): PT8H30M", result);
    }

    @Test
    public void t_Pattern() {
        Shift shift = new Shift();
        String pattern = "0900-1830-B30,0900-1730-B30,OFF,0900-1730-B30,0900-1730-B30,0900-1730-B30,OFF";
        String result = shift.Pattern(pattern);
        assertEquals("Variable", result);
    }

    @Test
    public void t_calculateBetweenDays() {
        Shift shift = new Shift();
        String result = shift.calculateBetweenDays("1730", "0900");
        assertEquals("Duration between days 15:30", result);
    }
    @Test
    public void t_CalculateDurationBtDays() {
        Shift shift = new Shift();
        int endTotal = 225;
        String expectedDuration = "Duration between days 3:45";
        String actualDuration = shift.calculateDurationBtDays(endTotal);
        assertEquals(expectedDuration, actualDuration);
    }
    
    @Test
    public void t_ParseInt() {
        Shift shift = new Shift();

        int[] case1 = shift.parseInt("0800", "1600");
        int[] expected1 = {8, 0, 16, 0};
        assertArrayEquals(expected1, case1);

        int[] case2 = shift.parseInt("0130", "2345");
        int[] expected2 = {1, 30, 23, 45};
        assertArrayEquals(expected2, case2);
    }

}
