package models;

public class CreateUserBody {

    String name;
    String job;
    String extraField;

    //Constructor - used fields
    public CreateUserBody(String name, String job){
        this.name = name;
        this.job = job;
    }

    public CreateUserBody(String name, String job, String extraField){
        this.name = name;
        this.job = job;
        this.extraField = extraField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getExtraField() {
        return job;
    }

    public void setExtraField(String job) {
        this.job = job;
    }
}