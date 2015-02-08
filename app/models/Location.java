package models;

import com.avaje.ebean.Ebean;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Dennis on 2/8/2015.
 */
@Entity
public class Location extends Model {
    @Id
    public long id;
    @Constraints.Required
    public String name;
    public String description;
    @Constraints.Required
    public double latitude;
    @Constraints.Required
    public double longitude;
    public String image;

    @ManyToOne
    public LocationType locationType;

    public Location(String name, String description, double latitude, double longitude, long locationType, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.locationType = Ebean.find(LocationType.class,locationType);
    }
}
