/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Column;
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
    @NamedQuery(name="Rating.findByUser", query="SELECT r FROM Rating r WHERE r.user.id = :user_id"),
    @NamedQuery(name="Rating.getAverageRatingByUser", query="SELECT AVG(r.rating_venue) FROM Rating r WHERE r.user.id = :user_id"),
    @NamedQuery(name="Rating.getRatingByUserAndVenue", query="SELECT r.rating_venue FROM Rating r WHERE r.user.id = :user_id AND r.venue.id = :venue_id")
})
public class Rating implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private RecommenderUser user;
    @ManyToOne
    private Venue venue;

    @Column(name = "rating_venue")
    private int rating_venue;
    
    public Rating(){
        
    }
    
    public Rating(Long id, Venue venue, RecommenderUser user, int rating_venue){
        this.id = id;
        this.venue = venue;
        this.user = user;
        this.rating_venue = rating_venue;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating_venue;
    }

    public void setRating(int rating) {
        this.rating_venue = rating;
    }    

    public RecommenderUser getRecommenderUser() {
        return user;
    }

    public void setRecommenderUser(RecommenderUser user) {
        this.user = user;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
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
        if (!(object instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Rating[ id=" + id + " ]";
    }
    
}
