/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author JAno
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Correlation.getTopFiveCorrelations", query="SELECT c FROM Correlation c WHERE c.user1.id = :user_id OR c.user2.id = :user_id ORDER BY c.correlation_value DESC LIMIT 5"),
    @NamedQuery(name="Correlation.getCorrelationByUsers", query="SELECT c.correlation_value FROM Correlation c WHERE c.user1.id = :user1_id AND c.user2.id = :user2_id")
})
public class Correlation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private RecommenderUser user1;
    @ManyToOne
    private RecommenderUser user2;
    
    private Double correlation_value;

    public Correlation(){
        
    }
    
    public Correlation(Long id, RecommenderUser user1, RecommenderUser user2, double corr){
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.correlation_value = corr;
    }
    
    public Correlation(RecommenderUser user1, RecommenderUser user2, double corr){
        this.user1 = user1;
        this.user2 = user2;
        this.correlation_value = corr;
    }
    
    public RecommenderUser getUser1() {
        return user1;
    }

    public void setUser1(RecommenderUser user1) {
        this.user1 = user1;
    }

    public RecommenderUser getUser2() {
        return user2;
    }

    public void setUser2(RecommenderUser user2) {
        this.user2 = user2;
    }

    public Double getCorrelation_value() {
        return correlation_value;
    }

    public void setCorrelation_value(Double correlation_value) {
        this.correlation_value = correlation_value;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Correlation)) {
            return false;
        }
        Correlation other = (Correlation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Correlation[ id=" + id + " ]";
    }
    
}
