package rs.elfak.mosis.milan.myplaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditMyPlaceActivity extends AppCompatActivity implements View.OnClickListener{
    boolean editmode=true;
    int position=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_place);
        try{
            Intent listIntent=getIntent();
            Bundle posBundle=listIntent.getExtras();
            if(posBundle!=null){
                position=posBundle.getInt("position");
            }
            else{
                editmode=false;
            }
        }
        catch(Exception e){
            editmode=false;
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Button finishedButton=findViewById(R.id.editmyplace_finished_button);
        finishedButton.setOnClickListener(this);
        Button cancelButton=findViewById(R.id.editmyplace_cancel_button);
        cancelButton.setOnClickListener(this);
        finishedButton.setEnabled(false);
        finishedButton.setText("Add");
        EditText nameEditText=findViewById(R.id.editmyplace_name_edit);
        if(!editmode){
            finishedButton.setEnabled(false);
            finishedButton.setText("Add");

        }
        else if(position>=0){
            finishedButton.setText("Save");
            MyPlace place=MyPlacesData.getInstance().getPlace(position);
            nameEditText.setText(place.getName());
            EditText descEditText=findViewById(R.id.editmyplace_desc_edit);
            descEditText.setText(place.getDescription());
        }

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               finishedButton.setEnabled(s.length()>0);
            }
        });
       Button locationButton = findViewById(R.id.editmyplace_location_button);
       locationButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.editmyplace_finished_button:{
                EditText etName=findViewById(R.id.editmyplace_name_edit);
                String nme=etName.getText().toString();
                EditText etDesc=findViewById(R.id.editmyplace_desc_edit);
                String desc=etDesc.getText().toString();
                EditText latEdit=findViewById(R.id.editmyplace_lat_edit);
                String lat=latEdit.getText().toString();
                EditText lonEdit=findViewById(R.id.editmyplace_lon_edit);
                String lon=lonEdit.getText().toString();
                if(!editmode){
                    MyPlace place=new MyPlace(nme,desc);
                    place.setLatitude(lat);
                    place.setLongitude(lon);
                    MyPlacesData.getInstance().addNewPlace(place);

                }
                else{
                    MyPlace place=MyPlacesData.getInstance().getPlace(position);
                    place.setName(nme);
                    place.setDescription(desc);
                    place.setLatitude(lat);
                    place.setLongitude(lon);
                }

                setResult(Activity.RESULT_OK);
                finish();
                break;
            }
            case R.id.editmyplace_cancel_button:{
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            }
            case R.id.editmyplace_location_button:
            {
                Intent i = new Intent(this,MyPlacesMapsActivity.class);
                i.putExtra("state",MyPlacesMapsActivity.SELECT_COORDINATES);
                startActivityForResult(i,1);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_edit_my_place,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.show_map_item)
            Toast.makeText(this,"Show Map!",Toast.LENGTH_SHORT).show();
        else if(id==R.id.my_places_list_item) {
            Intent i = new Intent(this, MyPlacesList.class);
            startActivity(i);
        }
        else if(id==R.id.about_item){
            Intent i=new Intent(this,About.class);
            startActivity(i);
        }
        else if(id==R.id.home){
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

@Override
protected void onActivityResult(int requestCode,int resultCode,Intent data)
{
    super.onActivityResult(requestCode,resultCode,data);
    try
    {
       if(resultCode == Activity.RESULT_OK)
       {
           String lon = data.getExtras().getString("lon");
           EditText lonText = findViewById(R.id.editmyplace_lon_edit);
           lonText.setText(lon);
           String lat = data.getExtras().getString("lat");
           EditText latText = findViewById(R.id.editmyplace_lat_edit);
           latText.setText(lat);

       }
    }
    catch(Exception e)
    {

    }
}
}
