/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Correlation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
}
