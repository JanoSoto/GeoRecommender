package managedbeans;

import entities.Rating;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import managedbeans.util.JsfUtil;
import managedbeans.util.JsfUtil.PersistAction;
import sessionbeans.RatingFacadeLocal;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
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
import org.primefaces.model.UploadedFile;
import sessionbeans.RecommenderUserFacadeLocal;
import sessionbeans.VenueFacadeLocal;

@Named("ratingController")
@SessionScoped
public class RatingController implements Serializable {

    @EJB
    private RatingFacadeLocal ejbFacade;
    @EJB
    private RecommenderUserFacadeLocal ejbUser;
    @EJB
    private VenueFacadeLocal ejbVenue;
    private List<Rating> items = null;
    private Rating selected;

    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public RatingController() {
    }

    public Rating getSelected() {
        return selected;
    }

    public void setSelected(Rating selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private RatingFacadeLocal getFacade() {
        return ejbFacade;
    }

    public Rating prepareCreate() {
        selected = new Rating();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("RatingCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("RatingUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("RatingDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Rating> getItems() {
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

    public Rating getRating(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Rating> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Rating> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Rating.class)
    public static class RatingControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RatingController controller = (RatingController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ratingController");
            return controller.getRating(getKey(value));
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
            if (object instanceof Rating) {
                Rating o = (Rating) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Rating.class.getName()});
                return null;
            }
        }

    }
    
    public void uploadFile() throws IOException{
        
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputstream()));
        String linea;
        String [] str_split;
        Long id = 1L;
        while((linea = br.readLine()) != null){
            str_split = linea.split(",");
            if(ejbUser.find(Long.parseLong(str_split[0])) != null && ejbVenue.find(Long.parseLong(str_split[1])) != null){
                getFacade().edit(new Rating(id, ejbVenue.find(Long.parseLong(str_split[1])), ejbUser.find(Long.parseLong(str_split[0])), Integer.parseInt(str_split[2])));
                id++;
            }     
        }
        JsfUtil.addSuccessMessage("Ratings guardados con éxito");
    }

}
