package rs.elfak.mosis.milan.myplaces;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyPlacesList extends AppCompatActivity
{
    static int NEW_PLACE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(MyPlacesList.this,EditMyPlaceActivity.class);
               startActivityForResult(i,NEW_PLACE);
           }
       });


        ListView myPlacesList= findViewById(R.id.my_places_list);
        myPlacesList.setAdapter(new ArrayAdapter<MyPlace>(this,android.R.layout.simple_list_item_1,MyPlacesData.getInstance().getMyPlaces()));
        myPlacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Bundle posBundle=new Bundle();
                posBundle.putInt("position",i);
                Intent intent=new Intent(MyPlacesList.this,ViewMyPlaceActivity.class);
                intent.putExtras(posBundle);
                startActivity(intent);
            }
        });
        myPlacesList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)menuInfo;
                MyPlace place=MyPlacesData.getInstance().getPlace(info.position);
                menu.setHeaderTitle(place.getName());
                menu.add(0,1,1,"ViewPlace");
                menu.add(0,2,2,"EditPlace");
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_my_places_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.show_map_item)
            Toast.makeText(this, "Show Map!", Toast.LENGTH_SHORT).show();
        else if (id == R.id.new_place_item){
            Intent i=new Intent(this,EditMyPlaceActivity.class);
            startActivityForResult(i,NEW_PLACE);
        }
        else if (id == R.id.about_item) {
            Intent i = new Intent(this, About.class);
            startActivity(i);
        }
        else if(id==R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            ListView myPlacesList=findViewById(R.id.my_places_list);
            myPlacesList.setAdapter(new ArrayAdapter<MyPlace>(this,android.R.layout.simple_list_item_1,MyPlacesData.getInstance().getMyPlaces()));
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Bundle posBundle=new Bundle();
        posBundle.putInt("position",info.position);
        Intent i=null;
        if(item.getItemId()==1){
            i=new Intent(this,ViewMyPlaceActivity.class);
            i.putExtras(posBundle);
            startActivity(i);
        }
        else if(item.getItemId()==2){
            i=new Intent(this,EditMyPlaceActivity.class);
            i.putExtras(posBundle);
            startActivity(i);
        }
        return super.onContextItemSelected(item);
    }


}
