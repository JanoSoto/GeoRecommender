/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Venue;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JAno
 */
@Local
public interface VenueFacadeLocal {

    void create(Venue venue);

    void edit(Venue venue);

    void remove(Venue venue);

    Venue find(Object id);

    List<Venue> findAll();

    List<Venue> findRange(int[] range);

    int count();
    
}
