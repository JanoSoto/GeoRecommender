/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Correlation;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JAno
 */
@Local
public interface CorrelationFacadeLocal {

    void create(Correlation correlation);

    void edit(Correlation correlation);

    void remove(Correlation correlation);

    Correlation find(Object id);

    List<Correlation> findAll();

    List<Correlation> findRange(int[] range);

    int count();
    
}
