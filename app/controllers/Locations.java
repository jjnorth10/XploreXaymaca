package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.google.common.io.Files;
import models.Location;
import models.LocationType;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.*;
import java.util.List;
import java.util.Map;
import views.html.*;


/**
 * Created by Dennis on 2/8/2015.
 */
public class Locations extends Controller {
    private static final Form<Location> locationForm = Form.form(Location.class);


    public Locations() {
        // TODO Auto-generated constructor stub
    }
    public static Result newLocation(String name,String description,double latitude,double longitude,long type,String image){

        if(name==null || name.isEmpty() || description==null || description.isEmpty() || latitude==0.0 || longitude==0.0 || type==0){
            ResponseHelper.jsonResponse(ResponseHelper.BadRequest, Json.newObject(), "Missing Parameter");

        }
        Location location = new Location(name,description,latitude,longitude,type,image);
        Ebean.save(location);
        return ResponseHelper.jsonResponse(ResponseHelper.OK, location, "Location added");

    }
    public static Result deleteLocation(long id){
        if(id==0){
            return ResponseHelper.jsonResponse(ResponseHelper.BadRequest, Json.newObject(), "Id required");
        }
        Location location = Ebean.find(Location.class, id);
        if(location!=null){
            Ebean.delete(location);
            return ResponseHelper.jsonResponse(ResponseHelper.OK, Json.newObject(), "Location deleted");
        }
        return ResponseHelper.jsonResponse(ResponseHelper.BadRequest, Json.newObject(), "Location not found");

    }

    public static Result updateLocation(long id,String name,String description,double latitude,double longitude,long type,String image){
        if(id==0){
            return ResponseHelper.jsonResponse(ResponseHelper.BadRequest, Json.newObject(), "Id required");
        }
        Location location = Ebean.find(Location.class, id);
        if(location!=null){
            if(name!=null && !name.isEmpty()){
                location.name=name;
            }
            if(description!=null && !description.isEmpty()){
                location.description=description;
            }
            if(latitude!=0.0){
                location.latitude=latitude;
            }
            if(longitude!=0.0){
                location.longitude=longitude;
            }
            if(type!=0){
                location.locationType=Ebean.find(LocationType.class, type);
            }
            if(image!=null&& !image.isEmpty()){
                location.image=image;
            }
            Ebean.update(location);
            return ResponseHelper.jsonResponse(ResponseHelper.OK, location, "Location updated");
        }
        return ResponseHelper.jsonResponse(ResponseHelper.NotFound, Json.newObject(), "Location not found");

    }
    public static Result list(){
        List<Location> locations = Ebean.find(Location.class).findList();
        return ResponseHelper.jsonResponse(ResponseHelper.OK, locations, "Request executed successfully");
    }

    public static Result find(){
        try{
            Map<String, String[]> stringMap = request().queryString();
            ExpressionList<Location> query = Ebean.find(Location.class).where();

            for (Map.Entry<String, String[]> entry : stringMap.entrySet()) {
                query = query.eq(entry.getKey(), entry.getValue()[0]);
            }

            List<Location> list = query.findList();
            return ResponseHelper.jsonResponse(ResponseHelper.OK, list, "request was successfully executed");
        }catch(Exception e){
            return ResponseHelper.jsonResponse(ResponseHelper.InternalServerError, Json.newObject(), "Internal Server Error");
        }
    }

    public static Result addForm(){
        Long id = new Long(0);
        return ok(locations.render(locationForm,id));
    }

    public static Result saveForm(){
        Form<Location> boundForm = locationForm.bindFromRequest();
        if(boundForm.hasErrors()){
            flash("Error","Invalid Form Entry");
            return badRequest(locations.render(boundForm,new Long(0)));
        }
        Location location = boundForm.get();
        DynamicForm form = Form.form().bindFromRequest();
        try {
            location.locationType = Ebean.find(LocationType.class, form.get("location_type_id"));
        }catch(NumberFormatException e){

        }
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart part = body.getFile("picture");
        String basePathUrl="C:/Users/Dennis/Documents/UWICSMapProject/public/images";
        if(part!=null){
            try{
                File picture = part.getFile();
                String fileName = part.getFilename();
                location.image=fileName;
                OutputStream os = new FileOutputStream(basePathUrl+"/"+fileName);
                byte[] byteArray = Files.toByteArray(picture);
                os.write(byteArray);
                os.close();

            }catch(FileNotFoundException e){

            }catch(IOException e){

            }


        }


			/*if(part!=null){
				File picture = part.getFile();
				try{
					location.picture = Files.toByteArray(picture);
				}catch(IOException e){
					return ResponseHelper.jsonResponse(ResponseHelper.InternalServerError, Json.newObject(), "Error reading file upload");

				}

			}*/

        Ebean.save(location);

        flash("Success","Added Product");
        return redirect(controllers.routes.Locations.list());

    }
    public static Result updateForm(long id){

        DynamicForm form = Form.form().bindFromRequest();
        Location location = Ebean.find(Location.class, id);//boundForm.get();
        Http.MultipartFormData body = request().body().asMultipartFormData();
        String name = form.get("name");
        String description = form.get("description");
        location.name = name;
        location.description = description;
        location.latitude = Double.parseDouble(form.get("latitude"));
        location.longitude = Double.parseDouble(form.get("longitude"));
        location.locationType = Ebean.find(LocationType.class, form.get("location_type_id"));
        Http.MultipartFormData.FilePart part = body.getFile("picture");
        String basePathUrl="C:/Users/Dell/Documents/UWICSMapProject/public/images";
        if(part!=null){
            try{
                File picture = part.getFile();
                String fileName = part.getFilename();
                location.image=fileName;
                OutputStream os = new FileOutputStream(basePathUrl+"/"+fileName);
                byte[] byteArray = Files.toByteArray(picture);
                os.write(byteArray);
                os.close();

            }catch(FileNotFoundException e){

            }catch(IOException e){

            }


        }
        Ebean.update(location);

        flash("Success","Updated Product");
        return redirect(controllers.routes.Locations.list());


    }
    public static Result editForm(long id){
        if(id==0){
            return ResponseHelper.jsonResponse(ResponseHelper.BadRequest, Json.newObject(), "Id required");
        }
        Location location = Ebean.find(Location.class, id);
        if(location!=null){
            Form<Location> filledForm = locationForm.fill(location);
            return ok(locations.render(filledForm,id));

        }
        return notFound();

    }

    public static Result picture(long id) {
        if(id==0){
            return ResponseHelper.jsonResponse(ResponseHelper.BadRequest, Json.newObject(), "Id required");
        }
        Location location = Ebean.find(Location.class, id);
        if(location!=null){
            String name = location.image;//location.name+".jpg";
            String basePathUrl="C:/Users/Dennis/Documents/UWICSMapProject/public/images";
            File directory = new File("C:/Users/Dennis/Documents/UWICSMapProject/public/images");
            File[] files = directory.listFiles();
            for(File file : files){

                if(file.getName().equals(name)){
					/*try {
						byte[] byteArray;
						byteArray=Files.toByteArray(file);
						BufferedImage bufferedImage = ImageIO.read(new File(file.getName()));

						return ok(byteArray).as("image/png");
					} catch (IOException e) {
							// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
                    byte[] byteArray={};
                    try {
                        byteArray= Files.toByteArray(file);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return ok(byteArray).as("image/png");
                    //return ResponseHelper.jsonResponse(ResponseHelper.OK, byteArray, "Picture request successfull");

                }
            }
            return notFound("error");
        }
        return ResponseHelper.jsonResponse(ResponseHelper.BadRequest, Json.newObject(), "Location not found");



	/*	Long newId = Long.parseLong(id);
		final Location location = Ebean.find(Location.class, newId);
		if(location == null){
			return notFound();
		}
		return ok(location.picture);*/


    }
}
