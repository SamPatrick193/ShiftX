package com.shiftx.git;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.*;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class ShiftService {
	public Map<Integer, DayInfoVO> processShiftPattern(String shiftPattern) throws ServiceException {

		String[] dayWisePattern = shiftPattern.split(",");
		// 0900-1730-B30

		Map<Integer, DayInfoVO> daysMap = new HashMap<>();

		for (int i = 0; i < dayWisePattern.length; i++) {
			if (!dayWisePattern[i].equals("OFF")) {
				daysMap.put(i + 1, processDayPattern(dayWisePattern[i]));
			}
		}

		findShiftType(daysMap);

		isValidShift(daysMap);

		return daysMap;

	}

	public String findShiftType(Map<Integer, DayInfoVO> daysMap) throws ServiceException {
		String patternType = null;

		if (daysMap.size() > 1) {
			String eachDayShift = null;

			for (Map.Entry<Integer, DayInfoVO> entry : daysMap.entrySet()) {

				String shiftTiming = entry.getValue().getStartTime() + ":" + entry.getValue().getEndTime();
				if (shiftTiming != null) {
					eachDayShift = shiftTiming;
					break;
				}
			}

			if (eachDayShift != null) {
				boolean variable = false;
				for (DayInfoVO dayInfo : daysMap.values()) {
					String shift = dayInfo.getStartTime() + ":" + dayInfo.getEndTime();
					if (shift.length() > 3 && !shift.equals("OFF") && !shift.equals(eachDayShift)) {
						variable = true;
						break;
					}
				}
				patternType = variable ? "Variable" : "Regular";
			} else {
				throw new ServiceException("No valid shift timing found");
			}
		}

		return patternType;
	}

	public DayInfoVO processDayPattern(String pattern) throws ServiceException {
		DayInfoVO dayInfoVO = parseDayInfo(pattern);

		dayInfoVO.setShiftTiming(pattern);
		dayInfoVO.setStartTime(convertStringTimeToInstant(dayInfoVO.getStartTimeStr()));
		dayInfoVO.setEndTime(convertStringTimeToInstant(dayInfoVO.getEndTimeStr()));
		dayInfoVO.setBreakTime(convertBreakStringTimeToInstant(dayInfoVO.getBreakMinsStr()));
		dayInfoVO.setShiftTimeInMinutes(minutesBetween(dayInfoVO.getStartTime(), dayInfoVO.getEndTime()));
		return dayInfoVO;
	}

	public DayInfoVO parseDayInfo(String pattern) throws ServiceException {
		if (pattern == null) {
			throw new ServiceException("Shift Pattern should not be null");
		}

		if (pattern.length() != 13) {
			throw new ServiceException("Invalid String Pattern");
		}

		DayInfoVO dayInfoVO = new DayInfoVO();
		String[] dayInfo = pattern.split("-");

		if (dayInfo.length != 3) {
			throw new ServiceException("Invalid Pattern");
		}
		dayInfoVO.setStartTimeStr(dayInfo[0]);
		dayInfoVO.setEndTimeStr(dayInfo[1]);
		dayInfoVO.setBreakMinsStr(dayInfo[2].substring(1));
		return dayInfoVO;
	}

	public Instant convertStringTimeToInstant(String data) throws ServiceException {
		Instant instantFromStringTime;
		if (data == null) {
			throw new ServiceException("Pattern should not be null");
		}

		try {
			// 09:00 Parsed Value from String to Integer
			int intHr = Integer.parseInt(data.substring(0, 2));
			int intMins = Integer.parseInt(data.substring(2, 4));

			LocalTime localTime = LocalTime.of(intHr, intMins);
			LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), localTime);
			instantFromStringTime = localDateTime.toInstant(ZoneOffset.UTC);
		} catch (Exception e) {
			throw new ServiceException("Invalid Data of Start Time|End Time");
		}
		return instantFromStringTime;

	}

	public Instant convertBreakStringTimeToInstant(String data) throws ServiceException {
		LocalDateTime breakDateTime;
		if (data == null) {
			throw new ServiceException("Pattern should not be null");
		} else if (data.length() != 2) {
			throw new ServiceException("Invalid Break Time Pattern");
		} else {
			LocalTime breakTime = LocalTime.parse("00:" + data);
			LocalDate currentDate = LocalDate.now();
			breakDateTime = LocalDateTime.of(currentDate, breakTime);
		}
		return breakDateTime.toInstant(ZoneOffset.UTC);
	}

	public int minutesBetween(Instant fromTime, Instant toTime) throws ServiceException {
		long minutes = 0;
		if (fromTime == null || toTime == null) {
			throw new ServiceException("StartTime|EndTime cannot be null");
		}
		if (fromTime.isAfter(toTime)) {
			// 23:00 to 24
			long minutesToEndOfDay = Duration.ofHours(24).toMinutes()
					- fromTime.atZone(ZoneOffset.UTC).toLocalTime().toSecondOfDay() / 60;
			// 00:00 to 09:30
			long minutesToStartOfDay = toTime.atZone(ZoneOffset.UTC).toLocalTime().toSecondOfDay() / 60;
			minutes = minutesToEndOfDay + minutesToStartOfDay;
		} else {
			Duration duration = Duration.between(fromTime, toTime);
			minutes = duration.toMinutes();
		}

		return (int) minutes;
	}

	public boolean isValidShift(Map<Integer, DayInfoVO> daysMap) {
		Duration duration;
		boolean isValid = true;

		for (int i = 1; i < 7; i++) {
			DayInfoVO currentDay = daysMap.get(i);
			DayInfoVO nextDay = daysMap.get(i + 1);

			Instant currentDayEndTime = currentDay != null ? currentDay.getEndTime() : null;
			Instant nextDayStartTime = nextDay != null ? nextDay.getStartTime() : null;

			if (currentDayEndTime == null || nextDayStartTime == null) {
				isValid = true;
			} else {
				duration = Duration.between(currentDayEndTime, nextDayStartTime);
				if (duration.isNegative()) {
					duration = duration.plus(Duration.ofHours(24));
				}
				if (duration.toMinutes() < 8 * 60) {
					isValid = false;
					break;
				}
			}

		}

		return isValid;

	}
}
