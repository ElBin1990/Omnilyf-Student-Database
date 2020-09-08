/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentdatabase.model;

/**
 *
 * @author elshaday
 */
public class CareerChoice {
    private int careerId;
    private int studentId;

    /**
     * @return the careerId
     */
    public int getCareerId() {
        return careerId;
    }

    /**
     * @param careerId the careerId to set
     */
    public void setCareerId(int careerId) {
        this.careerId = careerId;
    }

    /**
     * @return the studentId
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
