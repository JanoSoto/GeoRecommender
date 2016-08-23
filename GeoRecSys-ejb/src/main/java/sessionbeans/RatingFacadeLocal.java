/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Rating;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JAno
 */
@Local
public interface RatingFacadeLocal {

    void create(Rating rating);

    void edit(Rating rating);

    void remove(Rating rating);

    Rating find(Object id);

    List<Rating> findAll();

    List<Rating> findRange(int[] range);

    int count();
    
    List<Rating> findByUser(Long user_id);
    
    double getAverageRatingByUser(Long user_id);
    
    int getRatingByUserAndVenue(Long user_id, Long venue_id);
}
