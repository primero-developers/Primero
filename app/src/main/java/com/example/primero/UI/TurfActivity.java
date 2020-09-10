package com.example.primero.UI;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.primero.Adapter.NestedSlotAdapter;
import com.example.primero.Models.SlotModel;
import com.example.primero.Models.TurfModel;
import com.example.primero.R;
import com.example.primero.Room.AppDatabase;
import com.example.primero.Utils.DatePickerFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class TurfActivity extends AppCompatActivity {

    private static final String TAG = "TurfActivity";

    Window window;

    ImageView turfImage, turfMap;
    TextView turfNameTxt,turfLocationTxt;
    TextView turfDesc;
    RecyclerView nestedSlotRecycler;
    RelativeLayout phonBtn;
    MaterialButton bookBtn;

    //ADAPTER
    NestedSlotAdapter nestedSlotAdapter;
    RecyclerView nestedRecycler;

    //UTILS
    AppDatabase mDb;

    //Variables
    String turfId;
    TurfModel turf;
    List<SlotModel> slotListModel = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_turf);

        mDb = AppDatabase.getDatabase();
        turfId = getIntent().getStringExtra("TURF_ID");

        turfImage = findViewById(R.id.turf_header_image);
        turfNameTxt = findViewById(R.id.turf_header_name);
        turfLocationTxt = findViewById(R.id.turf_header_location);
        turfMap = findViewById(R.id.turf_map_img);
        turfDesc = findViewById(R.id.details_content_txt);
        bookBtn = findViewById(R.id.book_now_btn);
        phonBtn = findViewById(R.id.phone_btn);
        nestedRecycler = findViewById(R.id.nested_slot_recycler);

        getTurfWithId(turfId);

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = DatePickerFragment.newInstance(turfId);
                newFragment.show(getSupportFragmentManager(),"datePicker");
            }
        });

        turfMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initGoogleMap();
            }
        });

        phonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailTurfDealer();
            }
        });

    }

    public void getTurfWithId(String id){
        turf = mDb.turfDao().getTurf(id);
        slotArrayToModel();
        setTurfUi();
    }

    public void setTurfUi(){

        turfNameTxt.setText(turf.getTurfName());
        turfLocationTxt.setText(turf.getTurfLocation());
        turfDesc.setText(turf.getTurfDesc());

        if (slotListModel != null && slotListModel.size() > 0){
            initNestedRecycler();
        }

        Glide.with(this)
                .load(turf.getImgUrl())
                .centerCrop()
                .placeholder(getResources().getDrawable(R.drawable.turf_2))
                .into(turfImage);
        Glide.with(TurfActivity.this)
                .load(turf.getTurfMapUrl())
                .centerCrop()
                .into(turfMap);

    }
    public void slotArrayToModel(){

        ArrayList<String> slots = turf.getSlots();
        ArrayList<String> slotPrice = turf.getSlotPrice();

        int size = slots.size();

        Log.d(TAG, "slotArrayToModel: Slots >> "+ slots + " Slot Price >> "+ slotPrice + " Size " + size);
        int i = 0;
        while (i < size){
            String time = slots.get(i);
            String price = slotPrice.get(i);
            slotListModel.add(
                    new SlotModel(time,price)
            );
            i++;
        }
    }

    public void initNestedRecycler(){
        int resId = R.anim.nested_recycler_layout_anim;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        nestedRecycler.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        nestedRecycler.setLayoutAnimation(animation);
        nestedSlotAdapter = new NestedSlotAdapter(this,slotListModel);
        nestedRecycler.setAdapter(nestedSlotAdapter);
        nestedRecycler.setLayoutManager(new LinearLayoutManager(
                TurfActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false));
    }

    public void initGoogleMap(){
        if (turf.getCoordinates() != null && turf.getCoordinates().size() == 2 ){

            Log.d(TAG, "initGoogleMap: COORDINATES CAPTURED WITH " + turf.getCoordinates());
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + turf.getCoordinates().get(0) + "," + turf.getCoordinates().get(1) + "(" + turf.getTurfName() + ")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }

        }
    }

    public void dailTurfDealer() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + turf.getTurfPhone()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
