package projectWeekThree;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import de.fhpotsdam.unfolding.providers.*;
import processing.core.PApplet;



public class MyMapData extends PApplet{
	// Map
	UnfoldingMap map;
	
	// Providers
	AbstractMapProvider microsoft = new Microsoft.AerialProvider();
	AbstractMapProvider google = new Google.GoogleMapProvider();
	AbstractMapProvider openStreetMap = new OpenStreetMap.OpenStreetMapProvider();;
	
	
	// RSS feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// markers list
	List<Marker> quakeMarkers;
		
	
	public void setup() {
		// window size
				size(960, 700, OPENGL);
				
				// map
				map = new UnfoldingMap(this, 200, 50, 650, 600, openStreetMap);
				
				//
				MapUtils.createDefaultEventDispatcher(this, map);
				
				// List of earth quakes with "magnitude", 
				List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
				
				// list of makers on map
				quakeMarkers = new ArrayList<Marker>();
				for( PointFeature eq : earthquakes) {
					if((float) eq.getProperty("magnitude") <= 4.0) {
						SimplePointMarker spm = new SimplePointMarker(eq.getLocation(), eq.getProperties());
						spm.setRadius(5);
						quakeMarkers.add(spm);
					}else if( (float) eq.getProperty("magnitude") > 4.0 && ((float) eq.getProperty("magnitude")< 5.0)) {
						SimplePointMarker spm = new SimplePointMarker(eq.getLocation(), eq.getProperties());
						spm.setRadius(10);
						quakeMarkers.add(spm);
					} else {
						SimplePointMarker spm = new SimplePointMarker(eq.getLocation(), eq.getProperties());
						spm.setRadius(15);
						quakeMarkers.add(spm);
				}
			
			
				for( Marker mk: quakeMarkers) {
					mk.setColor(color(0,0,0));
					float mg = (float) mk.getProperty("magnitude");
					if ( mg <= 4.0) {
						mk.setColor(color(0,0,255));
					}else if(mg >4 && mg < 5) {
						mk.setColor(color(255,255,0));
					} else {
						mk.setColor(color(255,0,0));
					}
				}
				
				map.addMarkers(quakeMarkers);
	
			}
	}
			
			public void draw() {
				background(0);
				map.draw();
				addKey();
				
			}
			private void addKey() {	
				// Remember you can use Processing's graphics methods here
				fill(255, 250, 240);
				rect(25, 50, 150, 250);
				
				fill(0);
				textAlign(LEFT, CENTER);
				textSize(12);
				text("Earthquake Key", 50, 75);
				
				fill(color(255, 0, 0));
				ellipse(50, 125, 15, 15);
				fill(color(255, 255, 0));
				ellipse(50, 175, 10, 10);
				fill(color(0, 0, 255));
				ellipse(50, 225, 5, 5);
				
				fill(0, 0, 0);
				text("5.0+ Magnitude", 75, 125);
				text("4.0+ Magnitude", 75, 175);
				text("Below 4.0", 75, 225);
			}
}
