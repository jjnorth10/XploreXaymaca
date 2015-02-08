package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class ResponseHelper extends Controller  {
	public static final int OK = 200;
	public static final int BadRequest = 400;
	public static final int NotFound = 404;
	public static final int InternalServerError = 500;

	public ResponseHelper() {
		// TODO Auto-generated constructor stub
	}

	
	public static Result jsonResponse(int status, Object data,String message){
               
		ObjectNode result = Json.newObject();
		result.put("status", status);
		result.put("data", Json.toJson(data));
		result.put("message", message);
		if(status == OK){
			return ok(result);
		}else if(status == BadRequest){
			return badRequest(result);
		}else if(status == NotFound){
			return notFound(result);
		}else{
			return internalServerError(result);
		}
		
	}
}
