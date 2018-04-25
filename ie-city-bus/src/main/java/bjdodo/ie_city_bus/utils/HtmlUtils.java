package bjdodo.ie_city_bus.utils;

import java.io.StringWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import bjdodo.ie_city_bus.controller.data.StopPassageDetail;
import bjdodo.ie_city_bus.model.ActiveTrip;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class HtmlUtils {
	
	public static ZoneId ZONE_ID = ZoneId.of("Europe/Dublin");
	public static DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZONE_ID);
	public static DateTimeFormatter DT_FORMAT_SHORT = DateTimeFormatter.ofPattern("HH:mm").withZone(ZONE_ID);
	
	private static final Logger log = LoggerFactory.getLogger(HtmlUtils.class);
	
	public static String getActiveTripsHtml(List<ActiveTrip> activeTrips) {
		
		if (activeTrips == null) {
			return "No active trips";
		}
		
		String titleText = "Active Trips";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		
		try {
			docBuilder = dbf.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("html");
			doc.appendChild(rootElement);
	
			Element head = doc.createElement("head");
			
			Element style = doc.createElement("style");
			style.setTextContent("p{margin: 0; padding: 0;}");
			head.appendChild(style);
			
			rootElement.appendChild(head);
			
//			Element meta = doc.createElement("meta");
//			head.appendChild(meta);
			
			Element title = doc.createElement("title");
			title.setTextContent(titleText);
			head.appendChild(title);
			
			Element body = doc.createElement("body");
			rootElement.appendChild(body);
			
			// trip content
			activeTrips.forEach(trip -> {
				
				Element link = doc.createElement("a");
				link.setAttribute("href", "trip/" + trip.getTripId());
				link.setTextContent(trip.getRouteShortName());
				body.appendChild(link);
				
				Element bold = doc.createElement("b");
				
				if (trip.getScheduledStart() != null) {
					
					bold.setTextContent(" " + DT_FORMAT_SHORT.format(trip.getScheduledStart()));
					
				} else {
					log.error("Unexpected: Scheduled Start is null");
					bold.setTextContent(" NULL Scheduled Start");
				}
				
				body.appendChild(bold);
				
				Element pFrom = doc.createElement("p");
				
				if (trip.getActualStart() != null) {
					pFrom.setTextContent("From: " + trip.getOriginStopName() + " @ " + DT_FORMAT.format(trip.getActualStart()));
				} else {
					log.error("Unexpected: Actual Start is null");
					pFrom.setTextContent("From: " + trip.getOriginStopName() + " @ " + "NULL Actual Start");
				}
				body.appendChild(pFrom);
				
				Element pTo = doc.createElement("p");
				
				if (trip.getActualFinish() != null) {
					pTo.setTextContent("To: " + trip.getDestinationStopName() + ", expected: @ " + DT_FORMAT.format(trip.getActualFinish()));
				} else {
					log.error("Unexpected: Actual Finish is null");
					pTo.setTextContent("To: " + trip.getDestinationStopName() + ", expected: @ " + "NULL Actual Finish");
				}
				body.appendChild(pTo);
				
				Element pNear = doc.createElement("p");
				pNear.setTextContent("Near: " + trip.getNearestStopPointName());
				body.appendChild(pNear);
				
				long delaySeconds = trip.getActualFinish().getEpochSecond() - trip.getScheduledFinish().getEpochSecond();
				long delayMin = Math.floorDiv(delaySeconds, 60);
				
				Element pDelay = doc.createElement("p");
				pDelay.setTextContent("Delay: " + delayMin + " min");
				body.appendChild(pDelay);
				
				Element br = doc.createElement("br");
				body.appendChild(br);
			});
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			
			// add doctype
			DOMImplementation domImpl = doc.getImplementation();
			
			//  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

			DocumentType doctype = domImpl.createDocumentType("doctype",
			    "-//W3C//DTD XHTML 1.0 Frameset//EN",
			    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd");
			
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
			
			
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");

			log.info("xml result: " + output);
			
			return output;
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Failed to generate html for Active Trips";
	}
	
	public static String getStopPassageDetailsHtml(List<StopPassageDetail> stopPassageDetails) {
		
		String titleText = "Stop Passage Details";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		
		try {
			docBuilder = dbf.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("html");
			doc.appendChild(rootElement);
	
			Element head = doc.createElement("head");
			rootElement.appendChild(head);
			
//			Element meta = doc.createElement("meta");
//			head.appendChild(meta);
			
			Element title = doc.createElement("title");
			title.setTextContent(titleText);
			head.appendChild(title);
			
			Element body = doc.createElement("body");
			rootElement.appendChild(body);
			
			//Element p = doc.createElement("p");
			//body.appendChild(p);
			
			Element table = doc.createElement("table");
			table.setAttribute("border", "1");
			table.setAttribute("style", "border-collapse: collapse");
			
			Element trHeader = doc.createElement("tr");
			Element thStop = doc.createElement("th");
			thStop.setTextContent("Stop");
			thStop.setAttribute("align", "left");
			Element thScheduled = doc.createElement("th");
			thScheduled.setTextContent("Sch.");
			Element thActual = doc.createElement("th");
			thActual.setTextContent("Act.");
			Element thDistance = doc.createElement("th");
			thDistance.setTextContent("Dist.");
			
			trHeader.appendChild(thStop);
			trHeader.appendChild(thScheduled);
			trHeader.appendChild(thActual);
			trHeader.appendChild(thDistance);
			
			table.appendChild(trHeader);
			
			// stop passage content
			stopPassageDetails.forEach(spd -> {
				
				Element tr = doc.createElement("tr");
				
				addTdCell(doc, tr, spd.getStopNumber() + " " + spd.getStopName());
				addTdCell(doc, tr, formatTime(spd.getScheduledDeparture(), spd.getScheduledArrival(), false));
				
				if (spd.isActualEstimated()) {
					addTdCell(doc, tr, "Est: " + formatTime(spd.getActualDeparture(), spd.getActualArrival(), false));
				} else {
					addTdCell(doc, tr, formatTime(spd.getActualDeparture(), spd.getActualArrival(), true));
				}
				
				addTdCellRightAlign(doc, tr, spd.getMetersFromVehicle() + " m");
				
				table.appendChild(tr);
			});
			
			body.appendChild(table);
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			
			// add doctype
			DOMImplementation domImpl = doc.getImplementation();
			
			//  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

			DocumentType doctype = domImpl.createDocumentType("doctype",
			    "-//W3C//DTD XHTML 1.0 Frameset//EN",
			    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd");
			
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
			
			
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");

			System.err.println("xml result: \n" + output);
			
			return output;
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Failed to generate html for Stop Passage Details";

	}
	
	public static String formatTime(Instant instant1, Instant instant2, boolean withSeconds) {
		
		DateTimeFormatter dtFormatter;
		if (withSeconds) {
			dtFormatter = DT_FORMAT;
		} else {
			dtFormatter = DT_FORMAT_SHORT;
		}

		if (instant1 == null) {
			
			if (instant2 == null) {
				return "N/A";
			} 
				
			return dtFormatter.format(instant2);
		}
		
		return dtFormatter.format(instant1);
	}
	
	private static void addTdCell(Document doc, Element table, String text) {
		
		Element td = doc.createElement("td");
		td.setTextContent(text);
		table.appendChild(td);
	}
	
	private static void addTdCellRightAlign(Document doc, Element table, String text) {
		
		Element td = doc.createElement("td");
		td.setAttribute("align", "right");
		td.setTextContent(text);
		table.appendChild(td);
	}
}