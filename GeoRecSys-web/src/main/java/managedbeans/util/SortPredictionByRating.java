/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans.util;

import java.util.Comparator;

/**
 *
 * @author JAno
 */
public class SortPredictionByRating implements Comparator<Prediction>{

    @Override
    public int compare(Prediction o1, Prediction o2) {
        return (int) (o2.getRating() - o1.getRating());
    }
    
}
