package bjdodo.ie_city_bus.utils;

import java.time.Instant;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import bjdodo.ie_city_bus.service.TrafficService;
import bjdodo.ie_city_bus.service.TrafficService.TripSectionPassage;

public class TripSectionHistoryContainerTest {
	@Test
	public void testBasics() {

		TripSectionHistoryContainer historyContainer = new TripSectionHistoryContainer(
				p -> p.getStop2Time().isBefore(Instant.parse("2017-08-01T12:12:00Z")));

		historyContainer.recordSectionPassage("3", 2L, Instant.parse("2017-08-01T12:00:00Z"),
				1, Instant.parse("2017-08-01T12:02:00Z"),
				2, Instant.parse("2017-08-01T12:03:00Z"));

		historyContainer.recordSectionPassage("5", 1L, Instant.parse("2017-08-01T12:05:00Z"),
				1, Instant.parse("2017-08-01T12:12:00Z"),
				2, Instant.parse("2017-08-01T12:13:00Z"));

		historyContainer.recordSectionPassage("5", 1L, Instant.parse("2017-08-01T12:05:00Z"),
				2, Instant.parse("2017-08-01T12:13:00Z"),
				3, Instant.parse("2017-08-01T12:14:00Z"));

		List<TripSectionPassage> sections = historyContainer.getSectionPassageHistory(Pair.of(1L, 2L));

		Assert.assertEquals(2, sections.size());
		Assert.assertEquals(Instant.parse("2017-08-01T12:00:00Z"), sections.get(0).getTripScheduledStartTime());
		Assert.assertEquals(Instant.parse("2017-08-01T12:02:00Z"), sections.get(0).getStop1Time());
		Assert.assertEquals(2L, sections.get(0).getTripId());
		Assert.assertEquals(1, sections.get(0).getStop1Number());
		Assert.assertEquals(2, sections.get(0).getStop2Number());
		Assert.assertEquals(Instant.parse("2017-08-01T12:03:00Z"), sections.get(0).getStop2Time());
		Assert.assertEquals("3", sections.get(0).getRouteShortName());

		Assert.assertEquals(Instant.parse("2017-08-01T12:05:00Z"), sections.get(1).getTripScheduledStartTime());
		Assert.assertEquals(Instant.parse("2017-08-01T12:12:00Z"), sections.get(1).getStop1Time());
		Assert.assertEquals(1L, sections.get(1).getTripId());
		Assert.assertEquals(1, sections.get(1).getStop1Number());
		Assert.assertEquals(2, sections.get(1).getStop2Number());
		Assert.assertEquals(Instant.parse("2017-08-01T12:13:00Z"), sections.get(1).getStop2Time());
		Assert.assertEquals("5", sections.get(1).getRouteShortName());

		sections = historyContainer.getSectionPassageHistory(Pair.of(2L, 3L));

		Assert.assertEquals(1, sections.size());
		Assert.assertEquals(Instant.ofEpochSecond(1501589580), sections.get(0).getStop1Time());
		Assert.assertEquals(1L, sections.get(0).getTripId());
		Assert.assertEquals(2, sections.get(0).getStop1Number());
		Assert.assertEquals(3, sections.get(0).getStop2Number());
		Assert.assertEquals(Instant.ofEpochSecond(1501589640), sections.get(0).getStop2Time());
		Assert.assertEquals(Instant.ofEpochSecond(1501589100), sections.get(0).getTripScheduledStartTime());
		Assert.assertEquals("5", sections.get(0).getRouteShortName());

		sections = historyContainer.getSectionPassageHistory(Pair.of(3L, 4L));

		Assert.assertEquals(0, sections.size());

	}

	@Test
	public void testDeletingOld() {

		TripSectionHistoryContainer historyContainer = new TripSectionHistoryContainer(
				p -> p.getStop2Time().isBefore(Instant.now().minusSeconds(60 * 45)));

		Instant now = Instant.now();

		historyContainer.recordSectionPassage("3", 2L, now.minusSeconds(60 * 60), // 1 hour
				1, now.minusSeconds(60 * 60).plusSeconds(60),
				2, now.minusSeconds(60 * 60).plusSeconds(2 * 60));

		historyContainer.recordSectionPassage("5", 1L, now.minusSeconds(30 * 60), // 1/2 hour
				1, now.minusSeconds(30 * 60).plusSeconds(60),
				2, now.minusSeconds(30 * 60).plusSeconds(2 * 60));

		historyContainer.recordSectionPassage("3", 3L, now.minusSeconds(5), 1, now.minusSeconds(4), 2,
				now.minusSeconds(3));

		List<TrafficService.TripSectionPassage> sections = historyContainer.getSectionPassageHistory(Pair.of(1L, 2L));

		Assert.assertEquals(3, sections.size());

		historyContainer.cleanup();
		sections = historyContainer.getSectionPassageHistory(Pair.of(1L, 2L));

		Assert.assertEquals(2, sections.size());
	}
	
	@Test
	public void testOverwriting() {
		
		TripSectionHistoryContainer historyContainer = new TripSectionHistoryContainer(
				p -> p.getStop2Time().isBefore(Instant.parse("2017-08-01T12:12:00Z")));
		
		historyContainer.recordSectionPassage("3", 2L, Instant.parse("2017-08-01T12:00:00Z"),
				1, Instant.parse("2017-08-01T12:02:00Z"),
				2, Instant.parse("2017-08-01T12:03:00Z"));

		historyContainer.recordSectionPassage("5", 1L, Instant.parse("2017-08-01T12:05:00Z"),
				1, Instant.parse("2017-08-01T12:12:00Z"),
				2, Instant.parse("2017-08-01T12:13:00Z"));

		historyContainer.recordSectionPassage("5", 1L, Instant.parse("2017-08-01T12:05:00Z"),
				2, Instant.parse("2017-08-01T12:13:00Z"),
				3, Instant.parse("2017-08-01T12:14:00Z"));
		
		// now overwrite with new values
		
		historyContainer.recordSectionPassage("5", 1L, Instant.parse("2017-08-01T12:05:00Z"),
				1, Instant.parse("2017-08-01T12:17:00Z"),
				2, Instant.parse("2017-08-01T12:18:00Z"));

		historyContainer.recordSectionPassage("5", 1L, Instant.parse("2017-08-01T12:05:00Z"),
				2, Instant.parse("2017-08-01T12:18:00Z"),
				3, Instant.parse("2017-08-01T12:19:00Z"));
		
		List<TripSectionPassage> sections = historyContainer.getSectionPassageHistory(Pair.of(1L, 2L));
		
		// for (int idx = 0; idx < sections.size(); ++idx) {
		// System.out.println(
		// AppTest.getAssertText(sections.get(idx), TripSectionPassage.class,
		// "sections.get(" + idx + ")"));
		// }
//		

		Assert.assertEquals(Instant.parse("2017-08-01T12:00:00Z"), sections.get(0).getTripScheduledStartTime());
		Assert.assertEquals(Instant.parse("2017-08-01T12:02:00Z"), sections.get(0).getStop1Time());
		Assert.assertEquals(2L, sections.get(0).getTripId());
		Assert.assertEquals(1L, sections.get(0).getStop1Number());
		Assert.assertEquals(2L, sections.get(0).getStop2Number());
		Assert.assertEquals(Instant.parse("2017-08-01T12:03:00Z"), sections.get(0).getStop2Time());
		Assert.assertEquals("3", sections.get(0).getRouteShortName());

		Assert.assertEquals(Instant.parse("2017-08-01T12:05:00Z"), sections.get(1).getTripScheduledStartTime());
		Assert.assertEquals(Instant.parse("2017-08-01T12:17:00Z"), sections.get(1).getStop1Time());
		Assert.assertEquals(1L, sections.get(1).getTripId());
		Assert.assertEquals(1L, sections.get(1).getStop1Number());
		Assert.assertEquals(2L, sections.get(1).getStop2Number());
		Assert.assertEquals(Instant.parse("2017-08-01T12:18:00Z"), sections.get(1).getStop2Time());
		Assert.assertEquals("5", sections.get(1).getRouteShortName());
		
				
		sections = historyContainer.getSectionPassageHistory(Pair.of(2L, 3L));

//		for (int idx=0; idx<sections.size(); ++idx) {
//			System.out.println(AppTest.getAssertText(sections.get(0), TripSectionPassage.class, "sections.get(" + idx + ")"));
//		}
//		
		Assert.assertEquals("5", sections.get(0).getRouteShortName());
		Assert.assertEquals(Instant.parse("2017-08-01T12:05:00Z"), sections.get(0).getTripScheduledStartTime());
		Assert.assertEquals(Instant.parse("2017-08-01T12:18:00Z"), sections.get(0).getStop1Time());
		Assert.assertEquals(1L, sections.get(0).getTripId());
		Assert.assertEquals(2L, sections.get(0).getStop1Number());
		Assert.assertEquals(3L, sections.get(0).getStop2Number());
		Assert.assertEquals(Instant.parse("2017-08-01T12:19:00Z"), sections.get(0).getStop2Time());
		
	}
}
