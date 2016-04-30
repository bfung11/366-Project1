/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author stanchev
 */
@Named(value = "selector")
@ManagedBean
@SessionScoped
public class Selector implements Serializable {

    private String[] docChoices = {"Create New Doctor", "List All Customers", "Find Customer", "Delete Customer"};
    private String[] techChoices = {"Create New Doctor", "List All Customers", "Find Customer", "Delete Customer"};
    private String choiceDoc;
    private String choiceTech;

    public String[] getDchoices() {
        return this.docChoices;
    }

    public void setDchoices(String[] choices) {
        this.docChoices = choices;
    }

    public String[] getTchoices() {
        return this.techChoices;
    }

    public void setTchoices(String[] choices) {
        this.techChoices = choices;
    }
    
    public String getChoiceDoc() {
        return this.choiceDoc;
    }

    public void setChoiceDoc(String choice) {
        this.choiceDoc = choice;
    }
    
    public String getChoiceTech() {
        return this.choiceTech;
    }

    public void setChoiceTech(String choice) {
        this.choiceTech = choice;
    }

    public String doctorList() {
        switch (this.choiceDoc) {
            case "Create New Doctor":
                return "newCustomer";
            case "List All Doctors":
                return "listCustomers";
            case "Find Doctor":
                return "findCustomer";
            case "Delete Doctor":
                return "deleteCustomer";
            default:
                return null;
        }
    }
    
    public String techList() {
        switch (this.choiceTech) {
            case "Create New Technician":
                return "newCustomer";
            case "List All Technicians":
                return "listCustomers";
            case "Find Technician":
                return "findCustomer";
            case "Delete Technician":
                return "deleteCustomer";
            default:
                return null;
        }
    }

}
