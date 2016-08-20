package entities;

import entities.RecommenderUser;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-08-20T19:58:42")
@StaticMetamodel(Correlation.class)
public class Correlation_ { 

    public static volatile SingularAttribute<Correlation, RecommenderUser> user1;
    public static volatile SingularAttribute<Correlation, RecommenderUser> user2;
    public static volatile SingularAttribute<Correlation, Double> correlation_value;
    public static volatile SingularAttribute<Correlation, Long> id;

}