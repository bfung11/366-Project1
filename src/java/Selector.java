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

    private String[] docChoices = {"Create New Doctor", "List All Doctors", "Find Doctor", "Delete Doctor"};
    private String[] techChoices = {"Create New Technician", "List All Technicians", "Find Technician", "Delete Technician"};
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
                return "newDoctor";
            case "List All Doctors":
                return "listDoctors";
            case "Find Doctor":
                return "findDoctor";
            case "Delete Doctor":
                return "deleteDoctor";
            default:
                return null;
        }
    }
    
    public String techList() {
        switch (this.choiceTech) {
            case "Create New Technician":
                return "newTechnician";
            case "List All Technicians":
                return "listTechnicians";
            case "Find Technician":
                return "findTechnician";
            case "Delete Technician":
                return "deleteTechnician";
            default:
                return null;
        }
    }

}
