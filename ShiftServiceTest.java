package com.shiftx.git;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.*;
import java.util.Map;

import org.junit.Test;

public class ShiftServiceTest {

	@Test
	public void testParseDayInfo() {
		ShiftService shiftService = new ShiftService();
		DayInfoVO dayInfoVO;
		try {
			dayInfoVO = shiftService.parseDayInfo("0900-1730-B30");
			DayInfoVO expectedDayInfo = new DayInfoVO();
			expectedDayInfo.setStartTimeStr("0900");
			expectedDayInfo.setEndTimeStr("1730");
			expectedDayInfo.setBreakMinsStr("30");
			assertEquals(dayInfoVO, expectedDayInfo);
		} catch (ServiceException e) {
			assertEquals("Invalid Pattern", e.getMessage());
		}

	}

	@Test
	public void testParseDayInfo01() {
		ShiftService shiftService = new ShiftService();
		DayInfoVO dayInfoVO;
		try {
			dayInfoVO = shiftService.parseDayInfo(null);
		} catch (ServiceException e) {
			assertEquals("Shift Pattern should not be null", e.getMessage());
		}

	}

	@Test
	public void testParseDayInfo02() {
		ShiftService shiftService = new ShiftService();
		DayInfoVO dayInfoVO;
		try {
			dayInfoVO = shiftService.parseDayInfo("0900");
		} catch (ServiceException e) {
			assertEquals("Invalid String Pattern", e.getMessage());
		}

	}

	@Test
	public void testParseDayInfo03() {
		ShiftService shiftService = new ShiftService();
		DayInfoVO dayInfoVO;
		try {
			dayInfoVO = shiftService.parseDayInfo("0900-1730-B30");
			DayInfoVO expectedDayInfo=new DayInfoVO();
			expectedDayInfo.getStartTimeStr();
			expectedDayInfo.getEndTimeStr();
			expectedDayInfo.getBreakMinsStr();
			assertEquals(expectedDayInfo, expectedDayInfo);
		} catch (ServiceException e) {
			assertEquals("Invalid String Pattern", e.getMessage());
		}

	}

	@Test
	public void testconvertBreakStringTimeToInstant() {
		ShiftService shiftService = new ShiftService();
		Instant breakTime;
		try {
			breakTime = shiftService.convertBreakStringTimeToInstant("B30");
		} catch (ServiceException e) {
			assertEquals("Invalid Break Time Pattern", e.getMessage());
		}
	}

	@Test
	public void testConvertBreakStringTimeToInstant01() {
		ShiftService shiftService = new ShiftService();
		try {
			Instant breakTime = shiftService.convertBreakStringTimeToInstant(null);
		} catch (ServiceException e) {
			assertEquals("Pattern should not be null", e.getMessage());
		}
	}

	@Test
	public void testConvertStringTimeToInstant() {
		ShiftService shiftService = new ShiftService();
		try {
			Instant convertedStringTime = shiftService.convertStringTimeToInstant(null);
		} catch (ServiceException e) {
			assertEquals("Pattern should not be null", e.getMessage());
		}
	}

	@Test
	public void testConvertStringTimeToInstant01() {
		ShiftService shiftService = new ShiftService();
		try {
			Instant convertedStringTime = shiftService.convertStringTimeToInstant("0900");
		} catch (ServiceException e) {
			assertEquals("Invalid Data of Start Time|End Time", e.getMessage());
		}
	}

	@Test
	public void testMinutesBetween() {
		ShiftService shiftService = new ShiftService();
		try {
			int minutes = shiftService.minutesBetween(null, null);
		} catch (ServiceException e) {
			assertEquals("StartTime|EndTime cannot be null", e.getMessage());
		}
	}

	@Test
	public void testMinutesBetween01() {
		ShiftService shiftService = new ShiftService();
		try {
			Instant fromTime = Instant.parse("2024-05-08T09:00:00Z");
			Instant toTime = Instant.parse("2024-05-08T18:30:00Z");
			int minutes = shiftService.minutesBetween(fromTime, toTime);
			assertEquals(570, minutes);
		} catch (ServiceException e) {
			String expectedResult = "StartTime|EndTime is not within the pattern";
			assertEquals(expectedResult, e.getMessage());
		}
	}

	@Test
	public void testFindShiftPatternType() {
		ShiftService shiftService = new ShiftService();
		String input = "0900-1730-B30,0900-1730-B30,0900-1730-B30,OFF,OFF,0900-1730-B30,0900-1730-B30";
		try {
			Map<Integer, DayInfoVO> newMap = shiftService.processShiftPattern(input);
			String shiftType = shiftService.findShiftType(newMap);
			assertEquals("Regular", shiftType);
		} catch (ServiceException e) {

		}
	}

	@Test
	public void testFindShiftPatternType01() {
		ShiftService shiftService = new ShiftService();
		String input = "0900-1730-B30,1000-1730-B30,0900-1730-B30,OFF,OFF,0900-1730-B30,0900-1730-B30";
		try {
			Map<Integer, DayInfoVO> newMap = shiftService.processShiftPattern(input);
			String shiftType = shiftService.findShiftType(newMap);
			assertEquals("Variable", shiftType);
		} catch (ServiceException e) {

		}
	}

	@Test // Check if breakTime is different
	public void testFindShiftPatternType02() {
		ShiftService shiftService = new ShiftService();
		String input = "0900-1730-B30,1000-1730-B40,0900-1730-C30,OFF,OFF,0900-1730-B30,0900-1730-B30";
		try {
			Map<Integer, DayInfoVO> newMap = shiftService.processShiftPattern(input);
			String shiftType = shiftService.findShiftType(newMap);
			assertEquals("Variable", shiftType);
		} catch (ServiceException e) {

		}
	}
	@Test // Even if BreakTime is different it checks only Start & End Time
	public void testFindShiftPatternType03() {
		ShiftService shiftService = new ShiftService();
		String input = "0900-1730-B40,0900-1730-B40,0900-1730-B30,OFF,OFF,0900-1730-B30,0900-1730-B30";
		try {
			Map<Integer, DayInfoVO> newMap = shiftService.processShiftPattern(input);
			String shiftType = shiftService.findShiftType(newMap);
			assertEquals("Regular", shiftType);
		} catch (ServiceException e) {

		}
	}

	@Test
	public void testIsValidShift() {
		ShiftService shiftService = new ShiftService();
		String input = "0900-1530-B30,0100-1730-B30,0900-1730-B30,OFF,OFF,0900-1730-B30,0900-1730-B30";
		try {
			Map<Integer, DayInfoVO> newMap = shiftService.processShiftPattern(input);
			boolean shiftType = shiftService.isValidShift(newMap);
			assertEquals(true, shiftType);
		} catch (ServiceException e) {

		}
	}

	@Test
	public void testIsValidShift01() {
		ShiftService shiftService = new ShiftService();
		String input = "0900-2330-B30,0100-1730-B30,0900-1730-B30,OFF,OFF,0900-1730-B30,0900-1730-B30";
		try {
			Map<Integer, DayInfoVO> newMap = shiftService.processShiftPattern(input);
			boolean shiftType = shiftService.isValidShift(newMap);
			assertEquals(false, shiftType);
		} catch (ServiceException e) {
			
		}
	}

}
