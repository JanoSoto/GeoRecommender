package managedbeans;

import entities.Correlation;
import entities.RecommenderUser;
import entities.Venue;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import managedbeans.util.Prediction;
import managedbeans.util.SortPredictionByRating;
import sessionbeans.CorrelationFacadeLocal;
import sessionbeans.RatingFacadeLocal;
import sessionbeans.RecommenderUserFacadeLocal;
import sessionbeans.VenueFacadeLocal;

/**
 *
 * @author JAno
 */
@Named("predictionController")
@SessionScoped
public class PredictionController implements Serializable{
    
    @EJB
    private VenueFacadeLocal ejbVenue;
    @EJB
    private RatingFacadeLocal ejbRating;
    @EJB
    private RecommenderUserFacadeLocal ejbUser;
    @EJB
    private CorrelationFacadeLocal ejbCorrelation;
    
    private List<Prediction> topKPredictions = null;

    public List<Prediction> getTopKPredictions() {
        return topKPredictions;
    }

    public void setTopKPredictions(List<Prediction> topKPredictions) {
        this.topKPredictions = topKPredictions;
    }
    
    
    public PredictionController(){
        
    }
    
    public List<Prediction> getTopKItems(int k, RecommenderUser user){
        List<Prediction> topK = new ArrayList();
        
        double avgRatingUser = ejbRating.getAverageRatingByUser(user.getId());
        List<Correlation> vecindario = ejbCorrelation.getTopFiveCorrelations(user.getId());
        double avgCorrelation = getCorrelationAverage(vecindario);
        List<Prediction> allPredictions = new ArrayList();
        
        List<Venue> allVenues = ejbVenue.findAll();
        System.out.println("CALCULANDO LAS PREDICCIONES...");
        for(Venue venue : allVenues){
            double sumaVecindario = 0;
            for(Correlation vecino : vecindario){
                double usersCorrelation = 0;
                if(!Objects.equals(user.getId(), vecino.getUser1().getId())){
                    if((usersCorrelation = ejbCorrelation.getCorrelationByUsers(user.getId(), vecino.getUser1().getId())) == -1){
                        usersCorrelation = ejbCorrelation.getCorrelationByUsers(vecino.getUser1().getId(), user.getId());
                    }
                    sumaVecindario += (ejbRating.getRatingByUserAndVenue(vecino.getUser1().getId(), venue.getId()) - ejbRating.getAverageRatingByUser(vecino.getUser1().getId())) * usersCorrelation;
                }
                else {
                    if((usersCorrelation = ejbCorrelation.getCorrelationByUsers(user.getId(), vecino.getUser2().getId())) == -1){
                        usersCorrelation = ejbCorrelation.getCorrelationByUsers(vecino.getUser2().getId(), user.getId());
                    }
                    sumaVecindario += (ejbRating.getRatingByUserAndVenue(vecino.getUser2().getId(), venue.getId()) - ejbRating.getAverageRatingByUser(vecino.getUser2().getId())) * usersCorrelation;
                }
            }
            allPredictions.add(new Prediction(venue, avgRatingUser + (sumaVecindario / avgCorrelation)));
        }
        System.out.println("OBTENIENDO LAS TOP K PREDICCIONES");
        allPredictions.sort(new SortPredictionByRating());
        for(int i=0; i<k; i++){
            topK.add(allPredictions.get(i));
        }
        System.out.println("PROCESO TERMINADO");
        return topK;
    }
    
    public double getCorrelationAverage(List<Correlation> vecindario){
        double suma = 0;
        
        for(Correlation corr : vecindario){
            suma += corr.getCorrelation_value();
        }
        
        return suma/vecindario.size();
    }
    
    public void getTopK(int k, RecommenderUser user){
        topKPredictions = getTopKItems(k, user);
    }
}
