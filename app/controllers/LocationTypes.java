package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;

import controllers.ResponseHelper;

import models.Location;
import models.LocationType;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class LocationTypes extends Controller {

	public LocationTypes() {
		// TODO Auto-generated constructor stub
	}
	public static Result newLocationType(String type,String description){
		LocationType locationType = new LocationType(type,description);
		Ebean.save(locationType);
		return ResponseHelper.jsonResponse(ResponseHelper.OK, locationType, "Location Type Added");
	}
	
	public static Result deleteLocationType(long id){
		LocationType locationType = Ebean.find(LocationType.class, id);
		if(locationType!=null){
			Ebean.delete(locationType);
			return ResponseHelper.jsonResponse(ResponseHelper.OK, Json.newObject(), "Location deleted");
		}
		return ResponseHelper.jsonResponse(ResponseHelper.BadRequest, Json.newObject(), "Location not found");

	}
	
	public static Result updateLocationType(long id,String type,String description){
		LocationType locationType = Ebean.find(LocationType.class, id);
		if(locationType!=null){
			locationType.type=type;
			locationType.description=description;
			Ebean.update(locationType);
			return ResponseHelper.jsonResponse(ResponseHelper.OK, locationType, "Location Type updated");
		}
		return ResponseHelper.jsonResponse(ResponseHelper.BadRequest, Json.newObject(), "Location Type not found");
		
	}
	public static Result list(){
		List<LocationType> locationTypes = Ebean.find(LocationType.class).findList();
		return ResponseHelper.jsonResponse(ResponseHelper.OK, locationTypes, "Request executed successfully");
	}
	
		
	

}
