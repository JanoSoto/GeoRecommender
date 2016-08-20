/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.RecommenderUser;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JAno
 */
@Local
public interface RecommenderUserFacadeLocal {

    void create(RecommenderUser user);

    void edit(RecommenderUser user);

    void remove(RecommenderUser user);

    RecommenderUser find(Object id);

    List<RecommenderUser> findAll();

    List<RecommenderUser> findRange(int[] range);

    int count();
    
}
