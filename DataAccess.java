/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentdatabase.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import studentdatabase.model.*;

/**
 *
 * @author elshaday
 */
public class DataAccess {

    private Connection dbConnection;

    public DataAccess() throws SQLException {
        System.out.println("counter ");
        this.dbConnection = DBInstance.getInstance().getConnection();
    }

    public List<Term> getTerms() throws SQLException{
        String sql = "select * from term;";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        List<Term> terms = new ArrayList();
        while(result.next()){
            System.out.println("counter ");
            Term term = new Term();
            term.setTermId(result.getString("termId"));
            term.setStartDate(result.getDate("start_date"));
            term.setEndDate(result.getDate("end_date"));
            terms.add(term);
        }
        return terms;
    }

    public Student saveStudent(Student student) throws SQLException {
        String sql = "insert into student (firstname, lastname, email, phone) values (?,?,?,?);";
        PreparedStatement ps = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, student.getFirstname());
        ps.setString(2, student.getLastname());
        ps.setString(3, student.getEmail());
        ps.setString(4, student.getPhone());

        int success = ps.executeUpdate();
        if (success != 0) {
            System.out.println("success");
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student.setId(generatedKeys.getInt(1));
                    return student;
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            catch(Exception ex){
                System.out.println(ex);
            }
        }
        throw new SQLException();
    }
   
    
    public void saveAvailability(Availability av) throws SQLException{
        String sql = "insert into Availability (studentId, termId) values (?,?);";
        PreparedStatement ps = dbConnection.prepareStatement(sql);

        ps.setInt(1, av.getStudentId());
        ps.setString(2, av.getTermId());
        ps.execute();
    }
    
    public void saveCareerOption(CareerChoice choice) throws SQLException{
        String sql = "insert into career_options (studentId, careerId) values (?,?);";
        PreparedStatement ps = dbConnection.prepareStatement(sql);

        ps.setInt(1, choice.getStudentId());
        ps.setInt(2, choice.getCareerId());
        ps.execute();
    }
   
           
    
    public void saveSchedule(int careerId, String termId) throws SQLException{
        String sql = "insert into schedule (careerId, termId) values (?,?);";
        PreparedStatement ps = dbConnection.prepareStatement(sql);

        ps.setInt(1, careerId);
        ps.setString(2, termId);
        ps.execute();
    }
    
    public void updateCareer(int careerId, int scheduled) throws SQLException{
        String sql = "update career set scheduled = ? where id = ?;";
        PreparedStatement ps = dbConnection.prepareStatement(sql);

        ps.setInt(1,scheduled);
        ps.setInt(2, careerId);
        ps.execute();
    }
    
    
    public int getMostOccurringCareerId(String termId) throws SQLException{
        String sql = "SELECT careerId from career_options, availability, career where\n" +
                     "career_options.studentId = availability.studentId and \n" +
                     "career_options.careerId = career.id and  \n" +
                      "availability.active = 0 and \n" +
                      "career.scheduled = 0 and \n" +
                      "termId = ? group by careerId order by count(*) desc limit 1 ;";

        PreparedStatement ps = dbConnection.prepareStatement(sql);

        ps.setString(1, termId);
        ResultSet result = ps.executeQuery();
        int careerId = -1;
        while(result.next()){
            careerId = result.getInt("careerId");
        }
        return careerId;
    }
    
    public void updateAvailability(int id, String termId) throws SQLException{
        String sql = "update availability set active = 1 where studentId = ? and termId =?";
        PreparedStatement ps = dbConnection.prepareStatement(sql);

        ps.setInt(1, id);
        ps.setString(2, termId);
        ps.execute();
    }
    
    public List<Integer> updateScheduledStudents(int careerId, String termId) throws SQLException{
        List<Integer> list = new ArrayList();
        String sql = "select student.id as id from student,career_options,availability,career where "
                + "career_options.studentId = student.id and "
                + "career_options.careerId = ? and "
                + "availability.studentId = student.id and "
                + "availability.termId = ?";
        PreparedStatement ps = dbConnection.prepareStatement(sql);

        ps.setInt(1, careerId);
        ps.setString(2, termId);
        ResultSet result = ps.executeQuery();
    
        while(result.next()){
            updateAvailability(result.getInt("id"), termId);
        }
        return list;
    }
    
    public List<Career> getCareers() throws SQLException{
        List<Career> careers = new ArrayList();
        String sql = "select * from career;";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            Career career = new Career();
            career.setId(result.getInt("id"));
            career.setName(result.getString("name"));
            careers.add(career);
        }
        return careers;
    }
    
    
    public List<Career> getScheduleCareers(String termId) throws SQLException{
        List<Career> careers = new ArrayList();
        String sql = "select career.id as id,name from schedule, career where schedule.careerId = career.id and termId =? ;";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setString(1, termId);
        ResultSet result = ps.executeQuery();
        while(result.next()){
            Career career = new Career();
            career.setId(result.getInt("id"));
            career.setName(result.getString("name"));
            careers.add(career);
        }
        return careers;
    }
    
    public List<Student> getStudents(int careerId, String termId) throws SQLException{
        System.out.println("getStudnets "+ careerId + termId);
        List<Student> students = new ArrayList();
        String sql = "select firstname, lastname, email, phone from student, career_options, availability where" +
"            student.id = career_options.studentId and" +
"            career_options.careerId = ? and" +
"            availability.studentid = student.id and" +
"            availability.termId = ?;";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setInt(1, careerId);
        ps.setString(2,termId);
        
        ResultSet result = ps.executeQuery();
        while(result.next()){
           Student student = new Student();
           student.setFirstname(result.getString("firstname"));
           student.setLastname(result.getString("lastname"));
           student.setEmail(result.getString("email"));
           student.setPhone(result.getString("phone"));
           
           students.add(student);
        }
        System.out.println(students);
        return students;
    }
    
    public void clearCareers() throws SQLException{
        String sql = "update career set scheduled = ? ";
        PreparedStatement ps = dbConnection.prepareStatement(sql);

        ps.setInt(1,0);
        ps.execute();
    }
     public void clearAvailable() throws SQLException{
        String sql = "update availability set active = ? ";
        PreparedStatement ps = dbConnection.prepareStatement(sql);

        ps.setInt(1,0);
        ps.execute();
    }
     
      public void clearSchedule() throws SQLException{
        String sql = "delete from schedule";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.execute();
    }
    
    public List<Student> getAllStudents() throws SQLException{

        List<Student> students = new ArrayList();
        String sql = "select * from student;";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        
        ResultSet result = ps.executeQuery();
        while(result.next()){
           Student student = new Student();
           student.setFirstname(result.getString("firstname"));
           student.setLastname(result.getString("lastname"));
           student.setEmail(result.getString("email"));
           student.setPhone(result.getString("phone"));
           
           students.add(student);
        }
        System.out.println(students);
        return students;
    }
    
     public List<Student> getAvailableStudents(String termId) throws SQLException{

        List<Student> students = new ArrayList();
        String sql = "select firstname, lastname, email, phone from student, availability where student.id = availability.studentid \n" +
"and termId =? and active = 0;";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setString(1, termId);
        
        ResultSet result = ps.executeQuery();
        while(result.next()){
           Student student = new Student();
           student.setFirstname(result.getString("firstname"));
           student.setLastname(result.getString("lastname"));
           student.setEmail(result.getString("email"));
           student.setPhone(result.getString("phone"));
           
           students.add(student);
        }
        System.out.println(students);
        return students;
    }
    
    public void deleteStudent(String email) throws SQLException{
        System.out.println(email);
        
        String sql ="delete student,career_options, availability from student\n" +
                    "inner join career_options on career_options.studentid = student.id \n" +
                    "inner join availability on availability.studentid = student.id\n" +
                    "where email=?";
       
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setString(1,email);
        ps.execute();
    }
}
