/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.RecommenderUser;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author JAno
 */
@Stateless
public class RecommenderUserFacade extends AbstractFacade<RecommenderUser> implements RecommenderUserFacadeLocal {

    @PersistenceContext(unitName = "cl.usach_GeoRecSys-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecommenderUserFacade() {
        super(RecommenderUser.class);
    }
    
}
