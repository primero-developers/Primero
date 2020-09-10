package com.example.primero.UI;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.primero.Adapter.FavListAdapter;
import com.example.primero.Adapter.SearchAdapter;
import com.example.primero.Adapter.TurfListAdapter;
import com.example.primero.Models.FavModel;
import com.example.primero.Models.TurfModel;
import com.example.primero.Models.UserModel;
import com.example.primero.R;
import com.example.primero.Repos.UserRepo;
import com.example.primero.Room.AppDatabase;
import com.example.primero.SessionManager;
import com.example.primero.Utils.DialogClick;
import com.example.primero.Utils.TurfClick;
import com.example.primero.ViewModel.MainViewModel;
import com.example.primero.ViewModel.UserViewModel;
import com.example.primero.Worker.TurfWorker;
import com.example.primero.Worker.UserWorker;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements DialogClick, TurfClick {

    private static final String TAG = "MainActivity";

    //UI
    DrawerLayout drawer;
    NavigationView navView;
    View headerView;
        //// HEADER LAYOUT
    CircleImageView profileImg;
    RelativeLayout profileImgEdit;
    LinearLayout userNameParent,userPhoneParent;
    TextView userNameTxt,userPhoneTxt;
        //// APP BAR LAYOUT
    LinearLayout actionBar;
    RelativeLayout hamBtn,searchBox;
    ImageView hamIcon;
    SearchView searchInput;
    RelativeLayout toolbarLogo;
    TextView toolbarLogoTxt,searchLabel;
    RelativeLayout seachRecyclerParent;
    RecyclerView searchRecycler;
        //// CONTENT MAIN
    RelativeLayout favListParent,turfListParent;
    TextView favListTitle,turfListTitle;
    RecyclerView favRecycler,mainRecycler;

    //UTILS
    Window window;
    SessionManager sessionManager;
    AppDatabase mDb;
    ExecutorService executorService;
    Future preLoadImgFuture;
    MainViewModel viewModel;
    UserViewModel userViewModel;
    UserRepo userRepo;
    //VARIABLES
    private List<TurfModel> turfList = new ArrayList<>();
    private List<String> imgUrls = new ArrayList<>();
    private MutableLiveData<List<TurfModel>> searchedListLive = new MutableLiveData<>();
    private MutableLiveData<List<TurfModel>> turfListLive = new MutableLiveData<>();
    private MutableLiveData<List<FavModel>> favListLive = new MutableLiveData<>();
    private List<FavModel> favListUnLive = new ArrayList<>();
    private MutableLiveData<List<String>> favTurfId = new MutableLiveData<>();
    private List<TurfModel> searchedListUnLive = new ArrayList<>();
    Boolean IS_FRIST_TIME = true;
    Boolean IS_FAV_FIRST_TIME = true;
    Boolean searchFocus = false;
    Boolean SEARCH_PARENT_ACTIVE = false;
    UserModel user;
    Boolean IS_FAV_HIDDEN = true;

    //ADAPTERS
    SearchAdapter searchAdapter;
    TurfListAdapter turfListAdapter;
    FavListAdapter favListAdapter;

    //ANIMATION
    Animation slideUp,slideDown;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_main);

        /* DECLARATIONS */
        // UI INITS
        drawer = findViewById(R.id.drawer);
        navView = findViewById(R.id.nav_view);
        headerView = navView.getHeaderView(0);
            //HEADER LAYOUT
        profileImg = headerView.findViewById(R.id.profile_img);
        profileImgEdit = headerView.findViewById(R.id.profile_img_edit);
        userNameParent = headerView.findViewById(R.id.user_name_parent);
        userPhoneParent = headerView.findViewById(R.id.user_phone_parent);
        userNameTxt = headerView.findViewById(R.id.user_name_txt);
        userPhoneTxt = headerView.findViewById(R.id.user_phone_txt);
            // APP BAR LAYOUT
        actionBar = findViewById(R.id.action_bar);
        hamBtn = findViewById(R.id.ham_button);
        hamIcon = findViewById(R.id.ham_icon);
        searchInput = findViewById(R.id.search_input);
        searchBox = findViewById(R.id.search_box);
        toolbarLogo = findViewById(R.id.toolbar_logo);
        toolbarLogoTxt = findViewById(R.id.toolbar_logo_txt);
        seachRecyclerParent = findViewById(R.id.search_recycler_parent);
        searchRecycler = findViewById(R.id.search_recycler);
        searchLabel = findViewById(R.id.search_label);
            //CONTENT MAIN
        favListParent = findViewById(R.id.fav_list_parent);
        turfListParent = findViewById(R.id.turf_list_parent);
        turfListTitle = findViewById(R.id.turf_list_title);
        favListTitle = findViewById(R.id.fav_list_title);
        mainRecycler = findViewById(R.id.main_recycler);
        favRecycler = findViewById(R.id.fav_recycler);

        //UTILS
        sessionManager = SessionManager.getInstance();
        mDb = AppDatabase.getDatabase();
        executorService = Executors.newSingleThreadExecutor();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userRepo =new UserRepo(this);
        /* DECLARATIONS END */


        //APP BAR MECHANISM
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbarLogo.animate().alpha(0).start();
                toolbarLogo.setVisibility(View.GONE);
                searchBox.setVisibility(View.VISIBLE);
                searchBox.animate().alpha(1).start();
            }
        }, 2000);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                return true;
            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if (searchFocus)
                    removeSearchDialog();
                    updateUserData();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        //SEARCH MECHANISM
        searchInput.setIconifiedByDefault(true);
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchFocus)
                {
                    searchFocus = true;
                    searchDialog();
                }
            }
        });
        hamBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (searchFocus)
                    removeSearchDialog();
                else
                    drawer.openDrawer(GravityCompat.START);
            }
        });
        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        slideDown = new ScaleAnimation(1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
        slideDown.setFillAfter(true);
        slideDown.setDuration(200);

        slideUp = new ScaleAnimation(1f, 1f, 1f, 0f, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
        slideUp.setFillAfter(true);
        slideUp.setDuration(200);

        searchAdapter = new SearchAdapter(searchedListLive);
        searchRecycler.setAdapter(searchAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));


        //ACTUAL SEARCHING
        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    searchedListUnLive.clear();
                    for (TurfModel turfs : turfList) {
                        if (turfs.getTurfName().toLowerCase().contains(newText.toLowerCase()) || turfs.getTurfLocation().toLowerCase().contains(newText.toLowerCase())) {
                            searchedListUnLive.add(turfs);
                        }
                    }
                    if (!searchedListUnLive.equals(searchedListLive.getValue())){
                        Log.d(TAG, "onQueryTextChange: DIFF TEXT INPUT ");
                        searchedListLive.setValue(searchedListUnLive);
                    }
                    if (!searchedListLive.getValue().isEmpty()) {
                        if (!SEARCH_PARENT_ACTIVE){
                            seachRecyclerParent.setVisibility(View.VISIBLE);
                            seachRecyclerParent.startAnimation(slideDown);
                            SEARCH_PARENT_ACTIVE = true;
                        }

                    }
                } else {
                    searchedListUnLive.clear();
                    searchedListLive.setValue(searchedListUnLive);
                    seachRecyclerParent.setVisibility(View.GONE);
                    seachRecyclerParent.startAnimation(slideUp);
                    SEARCH_PARENT_ACTIVE = false;
                }
                return true;
            }
        });


        //USER EDITING
        userNameParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: CHANGE NAME CLIKCED");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("Change");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                ChangeNameFragment dialogFragment = new ChangeNameFragment(MainActivity.this);
                dialogFragment.show(ft,"Change");
            }
        });

    }

    /* ONCREATE END */

    /* ON START */
    @Override
    protected void onStart() {
        super.onStart();

        //OBSERVERS
        mDb.turfDao().getAllTurf().observe(this, new Observer<List<TurfModel>>() {
            @Override
            public void onChanged(List<TurfModel> turfModels) {
                viewModel.setTurfList(turfModels);
            }
        });
        mDb.userDao().getUser().observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                userViewModel.setUser(userModel);
            }
        });
        mDb.favTurfDao().getFavTurfs().observe(this, new Observer<List<FavModel>>() {
            @Override
            public void onChanged(List<FavModel> favModels) {
                Log.d(TAG, "onChanged: FRESH WORK GOT FAV FROM ROOM "+ favModels);
                viewModel.setFavList(favModels);
            }
        });

        userViewModel.getUser().observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                user = userModel;
                updateUserData();
            }
        });

        viewModel.getTurfLive().observe(this, new Observer<List<TurfModel>>() {
            @Override
            public void onChanged(List<TurfModel> turfModels) {

                if (turfModels != null){
                    turfList = turfModels;
                    if (turfList != turfListLive.getValue()){
                        turfListLive.setValue(turfList);
                    }
                }
                if (IS_FRIST_TIME){
                    if (sessionManager.FETCH_STATUS.equals("CACHED")){
                        if (turfListLive.getValue() != null)
                            initMainRecycler();
                        initRefreshWork();
                    } else if (sessionManager.FETCH_STATUS.equals("REMOTE")){
                        if (turfListLive.getValue() != null)
                            initMainRecycler();
                        preLoadImages();
                    }
                } else {
                    turfListAdapter.notifyDataSetChanged();
                }

            }
        });

        viewModel.getFavTurf().observe(this, new Observer<List<FavModel>>() {
            @Override
            public void onChanged(List<FavModel> favModels) {
                if (favModels != null){
                    favListUnLive = favModels;
                    if (favListUnLive != favListLive.getValue()){
                        favListLive.setValue(favListUnLive);
                    }

                    if (IS_FAV_FIRST_TIME){
                        initFavRecycler();
                    } else {
                        favListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        favListLive.observe(this, new Observer<List<FavModel>>() {
            @Override
            public void onChanged(List<FavModel> favModels) {
                if (favModels.size() > 0){
                    if (IS_FAV_HIDDEN)
                        showFavList();
                } else {
                    if (!IS_FAV_HIDDEN)
                        hideFavList();
                }
            }
        });

        searchedListLive.observe(this, new Observer<List<TurfModel>>() {
            @Override
            public void onChanged(List<TurfModel> turfModels) {
                Log.d(TAG, "onChanged: SEARCH adapter change with "+ turfModels);
                searchAdapter.notifyDataSetChanged();
            }
        });



        // OBSERVERS END //
    }

    /* ON START ENDS */

    public void searchDialog(){
        searchLabel.setVisibility(View.INVISIBLE);
        searchInput.setVisibility(View.VISIBLE);
        searchInput.onActionViewExpanded();
        hamIcon.setImageResource(R.drawable.back_arrow);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT);
    }

    public void removeSearchDialog(){
        searchFocus = false;
        searchInput.clearFocus();
        searchInput.onActionViewCollapsed();
        searchInput.setVisibility(View.GONE);
        searchLabel.setVisibility(View.VISIBLE);
        hamIcon.setImageResource(R.drawable.ham_1);
    }

    public void showFavList(){
        favListParent.setVisibility(View.VISIBLE);
        IS_FAV_HIDDEN = false;
    }
    public void hideFavList(){
        favListParent.setVisibility(View.GONE);
        IS_FAV_HIDDEN =true;
    }

    public void initMainRecycler(){

        mainRecycler.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        int resId = R.anim.recycler_layout_anim;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        mainRecycler.setLayoutAnimation(animation);
        turfListAdapter = new TurfListAdapter(turfListLive, this);
        mainRecycler.setAdapter(turfListAdapter);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public void initFavRecycler(){

        int resId = R.anim.fav_layout_anim;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        favRecycler.setLayoutAnimation(animation);
        favListAdapter = new FavListAdapter(this,favListLive);
        favRecycler.setAdapter(favListAdapter);
        favRecycler.setLayoutManager(new LinearLayoutManager(
                MainActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false));
        IS_FAV_FIRST_TIME = false;
    }

    public void updateUserData(){

        UserModel model = user;

        String name = model.getName();
        String phone = model.getPhone();
        String imgUrl = model.getProfileImg();

        userNameTxt.setText(name);
        userPhoneTxt.setText(phone);

        Glide.with(this)
                .load(imgUrl)
                .centerCrop()
                .into(profileImg);
    }

    public void initRefreshWork(){

        OneTimeWorkRequest SetUpUser =
                new OneTimeWorkRequest.Builder(UserWorker.class)
                        .setInitialDelay(1, TimeUnit.SECONDS)
                        .build();
        OneTimeWorkRequest getTurfList =
                new OneTimeWorkRequest.Builder(TurfWorker.class)
                        .setInitialDelay(2, TimeUnit.SECONDS)
                        .build();

        WorkManager.getInstance(this)
                .beginWith(SetUpUser)
                .then(getTurfList)
                .enqueue();

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(getTurfList.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        Log.d(TAG, "onChanged: REFRESHER CALLED with get turf status " + workInfo.getState() + " " + workInfo.getProgress());
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            Log.d(TAG, "REFRESH SUCCESS" + workInfo.getOutputData());
                            Toast.makeText(MainActivity.this, "Refreshing ... ", Toast.LENGTH_SHORT).show();
                            IS_FRIST_TIME = false;
                            preLoadImages();
                        }
                    }
                });
    }

    public void preLoadImages(){

        imgUrls = mDb.turfDao().getImageUrl();
        if (imgUrls != null && imgUrls.size() > 0){

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (final String s : imgUrls){
                        Glide.with(MainActivity.this).load(s)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .preload();
                    }
                }
            };

            preLoadImgFuture = executorService.submit(runnable);
        }
    }

    @Override
    public void dialogClick(String key,String value) {
        if (key.equals("Name")){
            userRepo.setUserName(value);
        }
    }

    @Override
    public void onTurfClick(int position) {
        Log.d(TAG, "onTurfClick: CLICK LISTNER clicked" + position);
        RecyclerView.ViewHolder viewHolder = mainRecycler.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(MainActivity.this, TurfActivity.class);
        intent.putExtra("TURF_ID", turfList.get(position).getTurfId());
        if (viewHolder != null){

            Pair<View, String> titlePair = Pair.create((View) viewHolder.itemView.findViewById(R.id.turf_title), "TITLE_TRANSITION");
            Pair<View, String> locationPair = Pair.create((View) viewHolder.itemView.findViewById(R.id.turf_location), "LOCATION_TRANSITION");
            Pair<View, String> parentPair = Pair.create((View) viewHolder.itemView.findViewById(R.id.turf_list_parent), "PARENT_TRANSITION");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    titlePair, locationPair ,parentPair);
            startActivity(intent, options.toBundle());

        }

    }
}
