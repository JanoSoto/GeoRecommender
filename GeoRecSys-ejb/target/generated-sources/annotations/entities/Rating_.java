package entities;

import entities.RecommenderUser;
import entities.Venue;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-23T03:08:53")
@StaticMetamodel(Rating.class)
public class Rating_ { 

    public static volatile SingularAttribute<Rating, Venue> venue;
    public static volatile SingularAttribute<Rating, Integer> rating_venue;
    public static volatile SingularAttribute<Rating, Long> id;
    public static volatile SingularAttribute<Rating, RecommenderUser> user;

}