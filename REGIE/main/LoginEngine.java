package REGIE.main;

public class LoginEngine {
    /*  
    Login the system. It simply use static method. 
    If the userId and password is correct, an User object is returned.
    Else a null is returned. 
    */
    static public User login(String userId, String password) {
        DatabaseProxy db = DatabaseProxy.getInstance();
        User user = db.validateUser(userId, password);
        return user; 
    }
}