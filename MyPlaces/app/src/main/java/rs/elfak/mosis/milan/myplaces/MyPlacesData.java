package rs.elfak.mosis.milan.myplaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPlacesData
{
    public interface ListUpdatedEventListener
    {
        void onListUpdated();
    }
    ArrayList<MyPlace> myPlaces;
    private HashMap<String,Integer> myPlacesKeyIndexMapping;
    private DatabaseReference databaseReference;
    private static final String FIREBASE_CHILD = "my-places";
    ListUpdatedEventListener updateListener;

    private MyPlacesData()
    {
        this.myPlaces=new ArrayList<MyPlace>();
        this.myPlacesKeyIndexMapping = new HashMap<String,Integer>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        databaseReference.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);

    }

    public void setEventListener(ListUpdatedEventListener listener)
    {
        updateListener=listener;
    }

    ValueEventListener parentEventListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
        {
            if(updateListener !=null)
                updateListener.onListUpdated();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError)
        {

        }
    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName)
        {
          String myPlaceKey = dataSnapshot.getKey();
          if(!myPlacesKeyIndexMapping.containsKey(myPlaceKey))
          {
              MyPlace myPlace = dataSnapshot.getValue(MyPlace.class);
              myPlace.key =myPlaceKey;
              myPlaces.add(myPlace);
              myPlacesKeyIndexMapping.put(myPlaceKey,myPlaces.size()-1);
              if(updateListener!=null)
                  updateListener.onListUpdated();
          }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName)
        {
            String myPlaceKey = dataSnapshot.getKey();
            MyPlace myPlace = dataSnapshot.getValue(MyPlace.class);
            myPlace.key =myPlaceKey;
            if(myPlacesKeyIndexMapping.containsKey(myPlaceKey))
            {
                int index = myPlacesKeyIndexMapping.get(myPlaceKey);
                myPlaces.set(index,myPlace);
            }
            else
            {
                myPlaces.add(myPlace);
                myPlacesKeyIndexMapping.put(myPlaceKey,myPlaces.size()-1);
            }
            if(updateListener !=null)
                updateListener.onListUpdated();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
        {
            String myPlaceKey = dataSnapshot.getKey();
            if(myPlacesKeyIndexMapping.containsKey(myPlaceKey))
            {
                int index = myPlacesKeyIndexMapping.get(myPlaceKey);
                myPlaces.remove(index);
                recreateKeyIndexMapping();
                if(updateListener !=null)
                    updateListener.onListUpdated();
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private static class SingletonHolder{
        public static final MyPlacesData instance=new MyPlacesData();
    }

    public static MyPlacesData getInstance(){
        return SingletonHolder.instance;
    }

    public ArrayList<MyPlace> getMyPlaces(){
        return this.myPlaces;
    }

    public void addNewPlace(MyPlace place)
    {
        String key = databaseReference.push().getKey();
        this.myPlaces.add(place);
        myPlacesKeyIndexMapping.put(key,myPlaces.size()-1);
        databaseReference.child(FIREBASE_CHILD).child(key).setValue(place);
        place.key = key;
    }

    public MyPlace getPlace(int index){
        return this.myPlaces.get(index);
    }

    public void deletePlace(int index)
    {
        databaseReference.child(FIREBASE_CHILD).child(myPlaces.get(index).key).removeValue();
        this.myPlaces.remove(index);
        recreateKeyIndexMapping();
    }

    public void updatePlace(int index,String nme,String desc,String lng,String lat)
    {
        MyPlace place = myPlaces.get(index);
        place.name = nme;
        place.description =desc;
        place.latitude =lat;
        place.longitude = lng;
        databaseReference.child(FIREBASE_CHILD).child(place.key).setValue(place);
    }

    private void recreateKeyIndexMapping()
    {
        myPlacesKeyIndexMapping.clear();
        for(int i=0;i<myPlaces.size();i++)
            myPlacesKeyIndexMapping.put(myPlaces.get(i).key,i);
    }


}
