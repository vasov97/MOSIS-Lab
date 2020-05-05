package rs.elfak.mosis.milan.myplaces;

public class MyPlace {
    String name;
    String description;
    String longitude;
    String latitude;
    long ID;

    public MyPlace(String name,String description){
        this.name=name;
        this.description=description;
    }
    public MyPlace(String name){
        this(name,"");
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description=description;
    }
    @Override
    public String toString(){
        return this.name;
    }
    public String getLongitude(){return longitude;}
    public void setLongitude(String longtitude){this.longitude=longtitude;}
    public String getLatitude(){return latitude;}
    public void setLatitude(String latitude){this.latitude=latitude;}

    public long getID(){return ID;}
    public void setID(long ID){this.ID = ID;}
}
