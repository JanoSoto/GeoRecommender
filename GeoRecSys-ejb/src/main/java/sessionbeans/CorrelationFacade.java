/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Correlation;
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
public class CorrelationFacade extends AbstractFacade<Correlation> implements CorrelationFacadeLocal {

    @PersistenceContext(unitName = "cl.usach_GeoRecSys-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CorrelationFacade() {
        super(Correlation.class);
    }

    @Override
    public List<Correlation> getTopFiveCorrelations(Long user_id) {
        Query query = em.createNamedQuery("Correlation.getTopFiveCorrelations").setParameter("user_id", user_id);
        query.setMaxResults(5);
        try{
            return (List<Correlation>) query.getResultList();
        }
        catch(NoResultException e){
            return new ArrayList();
        }
    }

    @Override
    public double getCorrelationByUsers(Long user1_id, Long user2_id) {
        Query query = em.createNamedQuery("Correlation.getCorrelationByUsers");
        query.setParameter("user1_id", user1_id);
        query.setParameter("user2_id", user2_id);
        try{
            return (double) query.getSingleResult();
        }
        catch(NoResultException e){
            return -1;
        }
    }
    
}
