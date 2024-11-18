package models;

public class UserModel {
    String email;
    String username;
    String password;
    boolean confirmed;


    public UserModel(String username, String password, String email, boolean confirmed){
        this.username = username;
        this.password = password;
        this.email = email;
        this.confirmed = confirmed;
    }

    public UserModel(String username, String password, String email){
        this(username, password, email, true);
    }
    //Getter
    public String getUsername(){return username;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public Boolean getConfirmed(){return confirmed;}
    //Setter
    public void setUsername(String newUsername){this.username = newUsername;}
    public void setEmail(String newEmail){this.email = newEmail;}
    public void setPassword(String newPassword){this.password = newPassword;}
    public void setConfirmed(Boolean confirmed){this.confirmed = confirmed;}
}
