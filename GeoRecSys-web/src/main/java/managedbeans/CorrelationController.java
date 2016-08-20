package managedbeans;

import entities.Correlation;
import entities.Rating;
import entities.RecommenderUser;
import managedbeans.util.JsfUtil;
import managedbeans.util.JsfUtil.PersistAction;
import sessionbeans.CorrelationFacadeLocal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import sessionbeans.RatingFacadeLocal;
import sessionbeans.RecommenderUserFacadeLocal;

@Named("correlationController")
@SessionScoped
public class CorrelationController implements Serializable {

    @EJB
    private CorrelationFacadeLocal ejbFacade;
    @EJB
    private RatingFacadeLocal ejbRating;
    @EJB
    private RecommenderUserFacadeLocal ejbUser;
    private List<Correlation> items = null;
    private Correlation selected;

    public CorrelationController() {
    }

    public Correlation getSelected() {
        return selected;
    }

    public void setSelected(Correlation selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CorrelationFacadeLocal getFacade() {
        return ejbFacade;
    }

    public Correlation prepareCreate() {
        selected = new Correlation();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CorrelationCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CorrelationUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CorrelationDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Correlation> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Correlation getCorrelation(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Correlation> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Correlation> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Correlation.class)
    public static class CorrelationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CorrelationController controller = (CorrelationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "correlationController");
            return controller.getCorrelation(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Correlation) {
                Correlation o = (Correlation) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Correlation.class.getName()});
                return null;
            }
        }

    }
    
    public void calculateCorrelation(){
        //List<List<Rating>> ratings = new ArrayList<>();
        List<RecommenderUser> users = ejbUser.findAll();
        //for(RecommenderUser user : users){
          //  ratings.add(ejbRating.findByUser(user.getId()));
        //}
        int size = users.size();
        Long id = 1L;
        for(int i = 0; i < users.size(); i++){
            for(int j = i; j < users.size(); j++){
                if(!users.get(i).equals(users.get(j))){
                    double [][] array = getArraysToCalculateCorrelation(ejbRating.findByUser(users.get(i).getId()), ejbRating.findByUser(users.get(j).getId()));
                    try{
                        double corr = new PearsonsCorrelation().correlation(array[0], array[1]);                        
                        System.out.println("Calculando la correlación de: \n"+Arrays.toString(array[0])+"\n"+Arrays.toString(array[1])+"\nResultado: "+corr);
                        if(Double.isNaN(corr)){
                            getFacade().create(new Correlation(id, users.get(i), users.get(j), 0));
                            //getFacade().create(new Correlation(user1, user2, 0));
                        }
                        else {
                            getFacade().create(new Correlation(id, users.get(i), users.get(j), corr));
                            //getFacade().create(new Correlation(user1, user2, corr));
                        }

                        id++;
                    }
                    catch(MathIllegalArgumentException e){
                    }
                    System.out.println("VAN: "+id+", FALTAN: "+(size-id));
                }
            }
        }
        /*
        for(RecommenderUser user1 : users){
            for(RecommenderUser user2 : users){
                if(!user1.equals(user2)){
                    double [][] array = getArraysToCalculateCorrelation(ejbRating.findByUser(user1.getId()), ejbRating.findByUser(user2.getId()));
                    try{
                        double corr = new PearsonsCorrelation().correlation(array[0], array[1]);                        
                        System.out.println("Calculando la correlación de: \n"+Arrays.toString(array[0])+"\n"+Arrays.toString(array[1])+"\nResultado: "+corr);
                        if(Double.isNaN(corr)){
                            getFacade().create(new Correlation(id, user1, user2, 0));
                            //getFacade().create(new Correlation(user1, user2, 0));
                        }
                        else {
                            getFacade().create(new Correlation(id, user1, user2, corr));
                            //getFacade().create(new Correlation(user1, user2, corr));
                        }

                        id++;
                    }
                    catch(MathIllegalArgumentException e){
                    }
                    System.out.println("VAN: "+id+", FALTAN: "+(size-id));             
                }
            }
        }*/
    }
    
    public double[][] getArraysToCalculateCorrelation(List<Rating> user1, List<Rating> user2){
        System.out.println("Listas a juntar:\n"+user1.toString()+"\n"+user2.toString());
        List<Rating> merge = mergeList(user1, user2);
        System.out.println("Merge realizado: "+merge.toString());
        
        double[][] userArrays = new double[2][merge.size()];
        
        int i = 0;
        for(Rating rating : merge){
            boolean flag_u1 = false;
            boolean flag_u2 = false;
            for(Rating rt1 : user1){
                if(Objects.equals(rt1.getVenue().getId(), rating.getVenue().getId())){
                    flag_u1 = true;
                    //break;
                }
            }
            if(flag_u1){
                userArrays[0][i] = (double) rating.getRating();
            }
            else{
                userArrays[0][i] = 0.0;
            }
            
            for(Rating rt2 : user2){
                if(Objects.equals(rt2.getVenue().getId(), rating.getVenue().getId())){
                    flag_u2 = true;
                    //break;
                }
            }
            if(flag_u2){
                userArrays[1][i] = (double) rating.getRating();
            }
            else{
                userArrays[1][i] = 0.0;
            }
            i++;
        }
        
        return userArrays;
    }    
    
    public List<Rating> mergeList(List<Rating> list1, List<Rating> list2){
        List<Rating> merge;
        merge = new ArrayList();
        
        for(Rating rating : list1){
            merge.add(rating);
        }
        
        for(Rating rt : list2){
            boolean flag = true;
            for(Rating rating : list1){
                if(Objects.equals(rt.getVenue().getId(), rating.getVenue().getId())){
                    flag = false;
                    break;
                }
            }
            if(flag){
                merge.add(rt);
            }
        }
        
        return merge;
    }
}
