package northseattlecollege.ASLBuddy;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;

/**
 * Class that updates the status by querying database using get and post data to update the
 * Interpreter status
 * Created by samueliox on 10/31/2016.
 */

public class InterpreterStatus {
    String userId;
    public InterpreterStatus(String userId){
        //figuring out who is currently logged in
        //pass in the userid as a parameter of the constructor
        this.userId = userId;
    }

    public void setVideoStatus(){
        //call api here
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonObj obj = mapper.readValue(new URL("http://54.69.18.19/pingConvoHOH?hohUserId=1&ConvoId=9"), JsonObj.class);
            System.out.println(obj.getMessage());
            System.out.println(obj.getSuccess());
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public boolean getVideoStatus(){
        //simulate network taking awhile
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
        return true;
    }

    public void setLocationStatus(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            //just a random endpoint for example
            JsonObj obj = mapper.readValue(new URL("http://54.69.18.19/pingUser?userId=" + userId), JsonObj.class);
            System.out.println(obj.getMessage());
            System.out.println(obj.getSuccess());
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public boolean getLocationStatus(){
        //simulate network taking awhile
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        return true;
    }
    public void setSkypeName(String skypeName){
        ObjectMapper mapper = new ObjectMapper();
        try {
            //just a random endpoint for example
            JsonObj obj = mapper.readValue(new URL("http://54.69.18.19/setSkypeName?userId=1&skypeName=" + skypeName), JsonObj.class);
            System.out.println(obj.getMessage());
            System.out.println(obj.getSuccess());
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public boolean getSkypeName(){
        //simulate network taking awhile
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        return true;
    }
}

