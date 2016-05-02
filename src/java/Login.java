
import java.io.Serializable;
import java.sql.SQLException;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Used Lubomir Stanchev's code as a basis for this code.
 */

/**
 *
 * @author Kevin Yang
 * @author Brian Fung
 * @author Justin Zaman
 * @author Lubomir Stanchev
 */

@Named(value = "login")
@SessionScoped
@ManagedBean
public class Login implements Serializable {

    private String username;
    private String password;
    private UIInput loginUI;

    public UIInput getLoginUI() {
        return loginUI;
    }

    public void setLoginUI(UIInput loginUI) {
        this.loginUI = loginUI;
    }

    // Changing getUsername to getBlah will make XHTML from login.username to login.blah
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void validate(
            FacesContext context, 
            UIComponent component, 
            Object value
    ) throws ValidatorException, SQLException {
        this.username = loginUI.getLocalValue().toString();
        this.password = value.toString();

        // TODO: check if user and password matches input
        if (!((this.username.equals("admin") && this.password.equals("admin")))) {
            FacesMessage errorMessage = new FacesMessage("Wrong username or password!");
            throw new ValidatorException(errorMessage);
        }
    }

    public String go() {
        this.invalidateUserSession();
        System.out.println("username " + this.username);
        Employee empl = EmployeeFactory.createEmployee(this.username);
        switch(EmployeeFactory.getType(this.username)) {
            case Employee.DOCTOR:
               return "startDoc";
            case Employee.TECHNICIAN:
                return "startTech";
            case Employee.ADMINISTRATOR:
                return "startAdmin";
            default:
                System.out.println("thisisatype " + (empl == null));
                return "startError";
        }
    }
    
    public void invalidateUserSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
    }

}