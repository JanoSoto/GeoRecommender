/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Rating;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JAno
 */
@Stateless
public class RatingFacade extends AbstractFacade<Rating> implements RatingFacadeLocal {

    @PersistenceContext(unitName = "cl.usach_GeoRecSys-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RatingFacade() {
        super(Rating.class);
    }

    @Override
    public List<Rating> findByUser(Long user_id) {
        Query query = em.createNamedQuery("Rating.findByUser").setParameter("user_id", user_id);
        try{
            return (List<Rating>) query.getResultList();
        }
        catch(NoResultException e){
            return new ArrayList<>();
        }
    }

    @Override
    public double getAverageRatingByUser(Long user_id) {
        Query query = em.createNamedQuery("Rating.getAverageRatingByUser").setParameter("user_id", user_id);
        try{
            return (double) query.getSingleResult();
        }
        catch(NoResultException e){
            return -1;
        }
    }

    @Override
    public double getRatingByUserAndVenue(Long user_id, Long venue_id) {
        Query query = em.createNamedQuery("Rating.getRatingByUserAndVenue");
        query.setParameter("user_id", user_id);
        query.setParameter("venue_id", venue_id);
        try{
            return (double) query.getSingleResult();
        }
        catch(NoResultException e){
            return -1;
        }
    }
    
}
