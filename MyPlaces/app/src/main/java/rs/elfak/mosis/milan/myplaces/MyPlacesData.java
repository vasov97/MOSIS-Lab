package rs.elfak.mosis.milan.myplaces;

import java.util.ArrayList;

public class MyPlacesData {
    ArrayList<MyPlace> myPlaces;

    private MyPlacesData(){
        this.myPlaces=new ArrayList<MyPlace>();

    }
    private static class SingletonHolder{
        public static final MyPlacesData instance=new MyPlacesData();
    }

    public static MyPlacesData getInstance(){
        return SingletonHolder.instance;
    }

    public ArrayList<MyPlace> getMyPlaces(){
        return this.myPlaces;
    }

    public void addNewPlace(MyPlace place){
        this.myPlaces.add(place);
    }

    public MyPlace getPlace(int index){
        return this.myPlaces.get(index);
    }

    public MyPlace deletePlace(int index){
        return this.myPlaces.remove(index);
    }


}
