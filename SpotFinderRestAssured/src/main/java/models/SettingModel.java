package models;

public class SettingModel {
    String theme;
    String language;
    public SettingModel(String theme, String language){
        this.theme = theme;
        this.language = language;
    }
    //Getter
    public String getTheme(){return theme;}
    public String getLanguage(){return language;}

    //Setter
    public void setTheme(String theme){this.theme = theme;}
    public void setLanguage(String language){this.language = language;}
}
