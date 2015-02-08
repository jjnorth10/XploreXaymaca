package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by Dennis on 2/8/2015.
 */
@Entity
public class LocationType {
    @Id
    public long id;
    @Constraints.Required
    public String type;
    @Constraints.Required
    public String description;
    @JsonIgnore
    @OneToMany(mappedBy="locationType")
    public List<Location> locations;

    public LocationType() {
    }

    public LocationType(String type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
    }

    public LocationType(String type, String description, List<Location> locations) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.locations = locations;
    }
}
