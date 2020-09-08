/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentdatabase.model.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import studentdatabase.data.DataAccess;
import studentdatabase.model.*;

/**
 *
 * @author elshaday
 */
public class Controller {

    private DataAccess dataAccess;
    private List<Term> terms;

    public Controller() throws SQLException {
        dataAccess = new DataAccess();
        terms = dataAccess.getTerms();
    }

    public Student addStudent(String firstname, String lastname, String email, String phone) throws SQLException {
        Student student = new Student();
        student.setFirstname(firstname);
        student.setLastname(lastname);
        student.setEmail(email);
        student.setPhone(phone);
        student = dataAccess.saveStudent(student);
        System.out.println(student.toString());
        return student;
    }
    

    public void addStudentAvailability(int studentId, boolean term1, boolean term2, boolean term3, boolean term4) {
        try {
          
            if (term1) {
                Availability av = new Availability();
                av.setStudentId(studentId);
                av.setTermId("term1");
                dataAccess.saveAvailability(av);
                System.out.print(studentId);
            }
            if (term2) {
                Availability av = new Availability();
                av.setStudentId(studentId);
                av.setTermId("term2");
                dataAccess.saveAvailability(av);
                System.out.print(studentId);
            }
            if (term3) {
                Availability av = new Availability();
                av.setStudentId(studentId);
                av.setTermId("term3");
                dataAccess.saveAvailability(av);
                System.out.print(studentId);
            }
            if (term4) {
                Availability av = new Availability();
                av.setStudentId(studentId);
                av.setTermId("term4");
                dataAccess.saveAvailability(av);
                System.out.print(studentId);
            }
        } catch (SQLException ex) {

        }

    }
    
    public void addCareerChoice(int studentId, int careerId) throws SQLException{
        CareerChoice choice = new CareerChoice();
        choice.setStudentId(studentId);
        choice.setCareerId(careerId);
        dataAccess.saveCareerOption(choice);
    }
    public List<Career> getAllCareers() throws SQLException{
        return dataAccess.getCareers();
    }
    
    public boolean generateSchedule(String termId) throws SQLException{
        int careerId = dataAccess.getMostOccurringCareerId(termId);
        System.out.println("cId: "+ careerId);
        if(careerId == -1){
            return false;
        }
        else{
            dataAccess.saveSchedule(careerId, termId);
            dataAccess.updateCareer(careerId, 1);
            dataAccess.updateScheduledStudents(careerId, termId);
            return true;
        }
    }

    /**
     * @return the terms
     */
    public List<Term> getTerms() {
        return terms;
    }
    
    public List<Career> getScheduleCareers(String termId){
        try {
            return dataAccess.getScheduleCareers(termId);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList();
    }
    
    public List<Student> getStudents(List<Career> careers, String careerName, String termId){
        System.out.println(careers);
         System.out.println(careerName);
        int careerId =  careers.stream()
                .filter(career -> career.getName().endsWith(careerName))
                .findFirst().get().getId();
        try {
            return dataAccess.getStudents(careerId, termId);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList();
    }
    
     public void clearCareers(){
        try {
            dataAccess.clearCareers();
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       public void clearAvailable(){
        try {
            dataAccess.clearAvailable();
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       public void clearSchedule(){
        try {
            dataAccess.clearSchedule();
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     public void deleteStudent(String email){
        try {
            dataAccess.deleteStudent(email);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     
     public List<Student> getAllStudents(){
        try {
            return dataAccess.getAllStudents();
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList();
     }
     
      
     public List<Student> getAvailableStudents(String termId){
        try {
            return dataAccess.getAvailableStudents(termId);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList();
     }
}
