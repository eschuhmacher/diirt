/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.util.time;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * 
 * @author shroffk
 */
public class TimeParserTest {

    @Test
    public void getNow() {
	Timestamp ts = TimeParser.getTimeStamp("now");
	Assert.assertTrue("Failed to obtain Timestamp corresponding to now ",
		ts != null && ts instanceof Timestamp);
    }

    /**
     * Test the times TimeDuration ← relative
     */
    @Test
    public void getTimeDuration() {
	// "last min", "last hour", "last day", "last week"
	TimeDuration lastMin = TimeParser.getTimeDuration("last min");
	Assert.assertEquals("Failed to get TimeDuration for last min", 60,
		lastMin.getSec());
	TimeDuration lastHour = TimeParser.getTimeDuration("last hour");
	Assert.assertEquals("Failed to get TimeDuration for last hour",
		60 * 60, lastHour.getSec());
	TimeDuration lastDay = TimeParser.getTimeDuration("last day");
	Assert.assertEquals("Failed to get TimeDuration for last day",
		60 * 60 * 24, lastDay.getSec());
	TimeDuration lastWeek = TimeParser.getTimeDuration("last week");
	Assert.assertEquals("Failed to get TimeDuration for last week",
		60 * 60 * 24 * 7, lastWeek.getSec());

	// "last 5 mins", "last 5 hours", "last 5 days", "last 5 weeks"
	TimeDuration last5Min = TimeParser.getTimeDuration("last 5 mins");
	Assert.assertEquals("Failed to get TimeDuration for last 5 mins",
		60 * 5, last5Min.getSec());
	TimeDuration last5Hour = TimeParser.getTimeDuration("last 5 hours");
	Assert.assertEquals("Failed to get TimeDuration for last 5 hours",
		60 * 60 * 5, last5Hour.getSec());
	TimeDuration last5Day = TimeParser.getTimeDuration("last 5 days");
	Assert.assertEquals("Failed to get TimeDuration for last 5 days",
		60 * 60 * 24 * 5, last5Day.getSec());
	TimeDuration last5Week = TimeParser.getTimeDuration("last 5 weeks");
	Assert.assertEquals("Failed to get TimeDuration for last 5 weeks", 60
		* 60 * 24 * 7 * 5, last5Week.getSec());

	// "1 min ago", "1 hours ago", "1 days ago", "1 weeks ago"
	TimeDuration oneMinAgo = TimeParser.getTimeDuration("1 min ago");
	Assert.assertEquals("Failed to get TimeDuration for 1 min ago", 60,
		oneMinAgo.getSec());
	TimeDuration oneHourAgo = TimeParser.getTimeDuration("1 hour ago");
	Assert.assertEquals("Failed to get TimeDuration for 1 hour ago",
		60 * 60, oneHourAgo.getSec());
	TimeDuration oneDayAgo = TimeParser.getTimeDuration("1 day ago");
	Assert.assertEquals("Failed to get TimeDuration for 1 days ago",
		60 * 60 * 24, oneDayAgo.getSec());
	TimeDuration oneWeekAgo = TimeParser.getTimeDuration("1 week ago");
	Assert.assertEquals("Failed to get TimeDuration for 1 week ago",
		60 * 60 * 24 * 7, oneWeekAgo.getSec());

	// "5 mins ago", "5 hours ago", "5 days ago", "5 weeks ago"
	TimeDuration fiveMinsAgo = TimeParser.getTimeDuration("5 mins ago");
	Assert.assertEquals("Failed to get TimeDuration for 5 mins ago",
		60 * 5, fiveMinsAgo.getSec());
	TimeDuration fiveHoursAgo = TimeParser.getTimeDuration("5 hours ago");
	Assert.assertEquals("Failed to get TimeDuration for 5 hours ago",
		60 * 60 * 5, fiveHoursAgo.getSec());
	TimeDuration fiveDaysAgo = TimeParser.getTimeDuration("5 days ago");
	Assert.assertEquals("Failed to get TimeDuration for 5 days ago",
		60 * 60 * 24 * 5, fiveDaysAgo.getSec());
	TimeDuration fiveWeeksAgo = TimeParser.getTimeDuration("5 weeks ago");
	Assert.assertEquals("Failed to get TimeDuration for 5 week ago", 60
		* 60 * 24 * 7 * 5, fiveWeeksAgo.getSec());

	// Check case insensitivity Last 4 Mins, Last 4 Hours, Last 4 Days, Last
	// 4 WEEKS
	TimeDuration last4Min = TimeParser.getTimeDuration("Last 4 Mins");
	Assert.assertEquals("Failed to get TimeDuration for Last 4 Mins",
		60 * 4, last4Min.getSec());
	TimeDuration last4Hour = TimeParser.getTimeDuration("Last 4 Hours");
	Assert.assertEquals("Failed to get TimeDuration for Last 4 Hours",
		60 * 60 * 4, last4Hour.getSec());
	TimeDuration last4Day = TimeParser.getTimeDuration("Last 4 Day");
	Assert.assertEquals("Failed to get TimeDuration for Last 4 Day",
		60 * 60 * 24 * 4, last4Day.getSec());
	TimeDuration last4Week = TimeParser.getTimeDuration("Last 4 WEEKS");
	Assert.assertEquals("Failed to get TimeDuration for Last 4 WEEKS", 60
		* 60 * 24 * 7 * 4, last4Week.getSec());

	// Check incorrect units in terms of plurality last 3 min, last 3 hour,
	// last 3 day, last 3 week
	TimeDuration last3Min = TimeParser.getTimeDuration("last 3 min");
	Assert.assertEquals("Failed to get TimeDuration for last 3 min",
		60 * 3, last3Min.getSec());
	TimeDuration last3Hour = TimeParser.getTimeDuration("last 3 hour");
	Assert.assertEquals("Failed to get TimeDuration for last 3 hour",
		60 * 60 * 3, last3Hour.getSec());
	TimeDuration last3Day = TimeParser.getTimeDuration("last 3 day");
	Assert.assertEquals("Failed to get TimeDuration for last 3 day",
		60 * 60 * 24 * 3, last3Day.getSec());
	TimeDuration last3Week = TimeParser.getTimeDuration("last 3 week");
	Assert.assertEquals("Failed to get TimeDuration for last 3 week", 60
		* 60 * 24 * 7 * 3, last3Week.getSec());

	// Check missing space between time quantity and unit last 2mins, last
	// 2hours, last 2days, last 2weeks, 2mins ago, 2hours ago, 2days ago,
	// 2weeks ago
	TimeDuration last2Mins = TimeParser.getTimeDuration("last 2mins");
	Assert.assertEquals("Failed to get TimeDuration for last 2mins",
		60 * 2, last2Mins.getSec());
	TimeDuration last2Hours = TimeParser.getTimeDuration("last 2hours");
	Assert.assertEquals("Failed to get TimeDuration for last 2hours",
		60 * 60 * 2, last2Hours.getSec());
	TimeDuration last2Days = TimeParser.getTimeDuration("last 2days");
	Assert.assertEquals("Failed to get TimeDuration for last 2days",
		60 * 60 * 24 * 2, last2Days.getSec());
	TimeDuration last2Weeks = TimeParser.getTimeDuration("last 2weeks");
	Assert.assertEquals("Failed to get TimeDuration for last 2weeks", 60
		* 60 * 24 * 7 * 2, last2Weeks.getSec());
	TimeDuration twoMinsAgo = TimeParser.getTimeDuration("2mins ago");
	Assert.assertEquals("Failed to get TimeDuration for 2mins ago", 60 * 2,
		twoMinsAgo.getSec());
	TimeDuration twoHoursAgo = TimeParser.getTimeDuration("2hours ago");
	Assert.assertEquals("Failed to get TimeDuration for 2hours ago",
		60 * 60 * 2, twoHoursAgo.getSec());
	TimeDuration twoDaysAgo = TimeParser.getTimeDuration("2days ago");
	Assert.assertEquals("Failed to get TimeDuration for 2days ago",
		60 * 60 * 24 * 2, twoDaysAgo.getSec());
	TimeDuration twoWeeksAgo = TimeParser.getTimeDuration("2weeks ago");
	Assert.assertEquals("Failed to get TimeDuration for 2weeks ago", 60
		* 60 * 24 * 7 * 2, twoWeeksAgo.getSec());

    }

    /**
     * Test the times TimeInterval ← relative
     */
    @Test
    public void getTimeInterval() {
	// "last min", "last hour", "last day", "last week"
	TimeInterval lastMin = TimeParser.getTimeInterval("last min");
	Assert.assertEquals("Failed to get TimeInterval for last min", 60,
		lastMin.getStart().durationBetween(lastMin.getEnd()).getSec());
	TimeInterval lastHour = TimeParser.getTimeInterval("last hour");
	Assert.assertEquals(
		"Failed to get TimeInterval for last hour",
		(60 * 60),
		(lastHour.getStart().durationBetween(lastMin.getEnd()).getSec()),
		1);
	TimeInterval lastDay = TimeParser.getTimeInterval("last day");
	Assert.assertEquals("Failed to get TimeInterval for last day",
		(60 * 60 * 24),
		(lastDay.getStart().durationBetween(lastMin.getEnd()).getSec()),1);
	TimeInterval lastWeek = TimeParser.getTimeInterval("last week");
	Assert.assertEquals(
		"Failed to get TimeInterval for last week",
		(60 * 60 * 24 * 7),
		(lastWeek.getStart().durationBetween(lastMin.getEnd()).getSec()),1);

	// "last 5 mins", "last 5 hours", "last 5 days", "last 5 weeks"
	TimeInterval last5Min = TimeParser.getTimeInterval("last 5 mins");
	Assert.assertEquals("Failed to get TimeInterval for last 5 mins",
		(60 * 5), (last5Min.getStart()
			.durationBetween(lastMin.getEnd()).getSec()),1);
	TimeInterval last5Hour = TimeParser.getTimeInterval("last 5 hours");
	Assert.assertEquals("Failed to get TimeInterval for last 5 hours",
		(60 * 60 * 5),
		(last5Hour.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval last5Day = TimeParser.getTimeInterval("last 5 days");
	Assert.assertEquals(
		"Failed to get TimeInterval for last 5 days",
		(60 * 60 * 24 * 5),
		(last5Day.getStart().durationBetween(lastMin.getEnd()).getSec()),1);
	TimeInterval last5Week = TimeParser.getTimeInterval("last 5 weeks");
	Assert.assertEquals("Failed to get TimeInterval for last 5 weeks",
		(60 * 60 * 24 * 7 * 5),
		(last5Week.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);

	// "1 min ago", "1 hours ago", "1 days ago", "1 weeks ago"
	TimeInterval oneMinAgo = TimeParser.getTimeInterval("1 min ago");
	Assert.assertEquals("Failed to get TimeInterval for 1 min ago", (60),
		(oneMinAgo.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval oneHourAgo = TimeParser.getTimeInterval("1 hour ago");
	Assert.assertEquals("Failed to get TimeInterval for 1 hour ago",
		(60 * 60),
		(oneHourAgo.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval oneDayAgo = TimeParser.getTimeInterval("1 day ago");
	Assert.assertEquals("Failed to get TimeInterval for 1 days ago",
		(60 * 60 * 24),
		(oneDayAgo.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval oneWeekAgo = TimeParser.getTimeInterval("1 week ago");
	Assert.assertEquals("Failed to get TimeInterval for 1 week ago",
		(60 * 60 * 24 * 7),
		(oneWeekAgo.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);

	// "5 mins ago", "5 hours ago", "5 days ago", "5 weeks ago"
	TimeInterval fiveMinsAgo = TimeParser.getTimeInterval("5 mins ago");
	Assert.assertEquals("Failed to get TimeInterval for 5 mins ago",
		(60 * 5),
		(fiveMinsAgo.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval fiveHoursAgo = TimeParser.getTimeInterval("5 hours ago");
	Assert.assertEquals("Failed to get TimeInterval for 5 hours ago",
		(60 * 60 * 5),
		(fiveHoursAgo.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval fiveDaysAgo = TimeParser.getTimeInterval("5 days ago");
	Assert.assertEquals("Failed to get TimeInterval for 5 days ago",
		(60 * 60 * 24 * 5),
		(fiveDaysAgo.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval fiveWeeksAgo = TimeParser.getTimeInterval("5 weeks ago");
	Assert.assertEquals("Failed to get TimeInterval for 5 week ago",
		(60 * 60 * 24 * 7 * 5), (fiveWeeksAgo.getStart()
			.durationBetween(lastMin.getEnd()).getSec()),1);

	// Check case insensitivity Last 4 Mins, Last 4 Hours, Last 4 Days, Last
	// 4 WEEKS
	TimeInterval last4Min = TimeParser.getTimeInterval("Last 4 Mins");
	Assert.assertEquals("Failed to get TimeInterval for Last 4 Mins",
		(60 * 4), (last4Min.getStart()
			.durationBetween(lastMin.getEnd()).getSec()),1);
	TimeInterval last4Hour = TimeParser.getTimeInterval("Last 4 Hours");
	Assert.assertEquals("Failed to get TimeInterval for Last 4 Hours",
		(60 * 60 * 4),
		(last4Hour.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval last4Day = TimeParser.getTimeInterval("Last 4 Day");
	Assert.assertEquals(
		"Failed to get TimeInterval for Last 4 Day",
		(60 * 60 * 24 * 4),
		(last4Day.getStart().durationBetween(lastMin.getEnd()).getSec()),1);
	TimeInterval last4Week = TimeParser.getTimeInterval("Last 4 WEEKS");
	Assert.assertEquals("Failed to get TimeInterval for Last 4 WEEKS",
		(60 * 60 * 24 * 7 * 4),
		(last4Week.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);

	// Check incorrect units in terms of plurality last 3 min, last 3 hour,
	// last 3 day, last 3 week
	TimeInterval last3Min = TimeParser.getTimeInterval("last 3 min");
	Assert.assertEquals("Failed to get TimeInterval for last 3 min",
		(60 * 3), (last3Min.getStart()
			.durationBetween(lastMin.getEnd()).getSec()),1);
	TimeInterval last3Hour = TimeParser.getTimeInterval("last 3 hour");
	Assert.assertEquals("Failed to get TimeInterval for last 3 hour",
		(60 * 60 * 3),
		(last3Hour.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval last3Day = TimeParser.getTimeInterval("last 3 day");
	Assert.assertEquals(
		"Failed to get TimeInterval for last 3 day",
		(60 * 60 * 24 * 3),
		(last3Day.getStart().durationBetween(lastMin.getEnd()).getSec()),1);
	TimeInterval last3Week = TimeParser.getTimeInterval("last 3 week");
	Assert.assertEquals("Failed to get TimeInterval for last 3 week",
		(60 * 60 * 24 * 7 * 3),
		(last3Week.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);

	// Check missing space between time quantity and unit last 2mins, last
	// 2hours, last 2days, last 2weeks, 2mins ago, 2hours ago, 2days ago,
	// 2weeks ago
	TimeInterval last2Mins = TimeParser.getTimeInterval("last 2mins");
	Assert.assertEquals("Failed to get TimeInterval for last 2mins",
		(60 * 2),
		(last2Mins.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval last2Hours = TimeParser.getTimeInterval("last 2hours");
	Assert.assertEquals("Failed to get TimeInterval for last 2hours",
		(60 * 60 * 2),
		(last2Hours.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval last2Days = TimeParser.getTimeInterval("last 2days");
	Assert.assertEquals("Failed to get TimeInterval for last 2days",
		(60 * 60 * 24 * 2),
		(last2Days.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval last2Weeks = TimeParser.getTimeInterval("last 2weeks");
	Assert.assertEquals("Failed to get TimeInterval for last 2weeks",
		(60 * 60 * 24 * 7 * 2),
		(last2Weeks.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval twoMinsAgo = TimeParser.getTimeInterval("2mins ago");
	Assert.assertEquals("Failed to get TimeInterval for 2mins ago",
		(60 * 2),
		(twoMinsAgo.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval twoHoursAgo = TimeParser.getTimeInterval("2hours ago");
	Assert.assertEquals("Failed to get TimeInterval for 2hours ago",
		(60 * 60 * 2),
		(twoHoursAgo.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval twoDaysAgo = TimeParser.getTimeInterval("2days ago");
	Assert.assertEquals("Failed to get TimeInterval for 2days ago",
		(60 * 60 * 24 * 2),
		(twoDaysAgo.getStart().durationBetween(lastMin.getEnd())
			.getSec()),1);
	TimeInterval twoWeeksAgo = TimeParser.getTimeInterval("2weeks ago");
	Assert.assertEquals("Failed to get TimeInterval for 2weeks ago",
		(60 * 60 * 24 * 7 * 2), (twoWeeksAgo.getStart()
			.durationBetween(lastMin.getEnd()).getSec()),1);
    }
}
