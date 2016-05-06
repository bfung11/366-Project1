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
    private String[] adminDocChoices = {"List Doctors", "Create New Doctor", "Delete Doctor", "Change Doctor Password"};
    private String[] adminTechChoices = {"List Technicians", "Create New Technician", "Delete Technician", "Change Technician Password"};
    
    private String[] docChoices = {"View Schedule", "Choose Preferred Workday", "Choose Time Off"};
    private String[] techChoices = {"View Schedule", "Choose Preferred Workday", "Choose Time Off"};
    
    private String choiceAdminDoc;
    private String choiceAdminTech;
    
    private String choiceDoc;
    private String choiceTech;

    /*
     * Drop down options
    */
    
    // Get Admin Doctor options
    public String[] getADChoices() {
        return this.adminDocChoices;
    }

    // Set Admin Doctor options
    public void setADchoices(String[] choices) {
        this.adminDocChoices = choices;
    }

    // Get Admin Technician Choices
    public String[] getATChoices() {
        return this.adminTechChoices;
    }

    // Set Admin Technician Choices
    public void setATChoices(String[] choices) {
        this.adminTechChoices = choices;
    }
    
    public String[] getTChoices () {
        return this.techChoices;
    }
    
    public void setTChoices (String[] choices) {
        this.techChoices = choices;
    }
    
    public String[] getDChoices () {
        return this.docChoices;
    }
    
    public void setDChoices (String[] choices) {
        this.docChoices = choices;
    }
    
    /*
     * Drop down selection get() and set()
     */
    
    // Get Admin Doctor dropdown choice
    public String getChoiceAdminDoc() {
        return this.choiceAdminDoc;
    }

    // Set Admin Doctor dropdown choice
    public void setChoiceAdminDoc(String choice) {
        this.choiceAdminDoc = choice;
    }
    
    // Get Admin Technician dropdown choice
    public String getChoiceAdminTech() {
        return this.choiceAdminTech;
    }

    // Set Admin Technician dropdown choice
    public void setChoiceAdminTech(String choice) {
        this.choiceAdminTech = choice;
    }
    
        // Get Admin Technician dropdown choice
    public String getChoiceTech() {
        return this.choiceTech;
    }

    // Set Admin Technician dropdown choice
    public void setChoiceTech(String choice) {
        this.choiceTech = choice;
    }
    
        // Get Admin Technician dropdown choice
    public String getChoiceDoc() {
        return this.choiceDoc;
    }

    // Set Admin Technician dropdown choice
    public void setChoiceDoc(String choice) {
        this.choiceDoc = choice;
    }

    public String doctorList() {
        switch (this.choiceAdminDoc) {
            case "View Schedule":
                return "docViewSchedule";
            case "Choose Preferred Workday":
                return "docPrefWorkday";
            case "Choose Time Off":
                return "docTimeOff";
            default:
                return null;
        }
    }
    
    public String techList() {
        switch (this.choiceAdminDoc) {
            case "View Schedule":
                return "techViewSchedule";
            case "Choose Preferred Workday":
                return "techPrefWorkday";
            case "Choose Time Off":
                return "techTimeOff";
            default:
                return null;
        }
    }
    
    public String doctorAdminList() {
        switch (this.choiceAdminDoc) {
            case "List Doctors":
                return "listDoctors";
            case "Create New Doctor":
                return "newDoctor";
            case "Delete Doctor":
                return "deleteDoctor";
            case "Change Doctor Password":
                return "changeDocPassword";
            default:
                return null;
        }
    }
    
    public String techAdminList() {
        switch (this.choiceAdminTech) {
            case "List Technicians":
                return "listTechnicians";
            case "Create New Technician":
                return "newTechnician";
            case "Delete Technician":
                return "deleteTechnician";
            case "Change Technician Password":
                return "changeTechPassword";
            default:
                return null;
        }
    }

}
