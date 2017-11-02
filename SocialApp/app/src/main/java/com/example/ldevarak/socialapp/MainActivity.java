package com.example.ldevarak.socialapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ldevarak.socialapp.database.DatabaseUtils;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean isMenuClick = false;
    private String isFav = "false";
    private RelativeLayout linkView;
    private TextView homeView;
    private RelativeLayout profileView;
    private TextView userDisplayNameTxt;
    private TextView userDisplayEmailTxt;
    private CircleImageView imageView;

    DatabaseUtils databaseUtils;
    private LinearLayout linksView;
    // boolean displayLinks = false;


    private ImageView favIcon;
    private Button plusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseUtils = new DatabaseUtils(getApplication());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String firstName = "";
        String lastName = "";
        String email = "";
        String id = "";
        int count = 0;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        homeView = (TextView) findViewById(R.id.home_view);
        profileView = (RelativeLayout) findViewById(R.id.profile_view);

        View headerView = navigationView.getHeaderView(0);
        userDisplayNameTxt = (TextView) headerView.findViewById(R.id.userDisplayName_txt);
        userDisplayEmailTxt = (TextView) headerView.findViewById(R.id.userDisplayEmail_txt);
        imageView = (CircleImageView) headerView.findViewById(R.id.imageView);


        if (intent != null) {
            Bundle bundle = intent.getExtras();
            firstName = bundle.getString("firstName");
            lastName = bundle.getString("lastName");
            email = bundle.getString("email");
            id = bundle.getString("id");
            Bitmap bitmap = (Bitmap) bundle.get("bitmap");

            UserObject userObject = new UserObject();
            userObject.firstname = firstName;
            userObject.lastname = lastName;
            userObject.email = email;

            count = new TableControllerUser(getApplication()).count(email);

            if (count <= 0) {
                boolean createSuccessful = new TableControllerUser(getApplication()).create(userObject);
            }

            homeView.setText("Welcome to " + firstName + " " + lastName);
            userDisplayNameTxt.setText(firstName + "" + lastName);
            userDisplayEmailTxt.setText(email);
            imageView.setImageBitmap(bitmap);
        }

        linkView = (RelativeLayout) findViewById(R.id.link_view);
        plusBtn = (Button) findViewById(R.id.plus_btn);
        final Button cancelBtn = (Button) findViewById(R.id.cancel_btn);
        final Button addBtn = (Button) findViewById(R.id.add_btn);
        final EditText urlTxt = (EditText) findViewById(R.id.url_txt);
        final EditText linkName = (EditText) findViewById(R.id.link_name);
        final LinearLayout addView = (LinearLayout) findViewById(R.id.add_view);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        List<String> categories = new ArrayList<String>();
        categories.add("Media");
        categories.add("Education");
        categories.add("Ecomerce");
        categories.add("Helth care");
        categories.add("Personal");
        categories.add("Travel");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(dataAdapter);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkName.setText("");
                urlTxt.setText("");
                addView.setVisibility(View.VISIBLE);
                plusBtn.setVisibility(View.GONE);

            }
        });


        TextView firstNameTxt = (TextView) findViewById(R.id.firstName_txt);
        TextView lastNameTxt = (TextView) findViewById(R.id.lastName_txt);
        TextView emailText = (TextView) findViewById(R.id.email_txt);
        Button editBtn = (Button) findViewById(R.id.edit_btn);

        //countval = new TableControllerUser(getApplication()).count(email);
        if (count > 0) {
            UserObject user = new TableControllerUser(getApplication()).readSingleRecord(email);
            firstName = user.firstname;
            lastName = user.lastname;
            email = user.email;
        }
        firstNameTxt.setText("First Name: " + firstName);
        lastNameTxt.setText("Last Name: " + lastName);
        emailText.setText("Email: " + email);

        final String emailValue = email;


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.edit_user_details, null, false);
                final EditText firstNameEditText = (EditText) formElementsView.findViewById(R.id.d_fname_txt);
                final EditText lastNameEditText = (EditText) formElementsView.findViewById(R.id.d_lname_txt);
                final EditText emailEditText = (EditText) formElementsView.findViewById(R.id.d_email_txt);

                UserObject user = new TableControllerUser(getApplication()).readSingleRecord(emailValue);
                final String firstName1 = user.firstname;
                final String lastName1 = user.lastname;
                final String email1 = user.email;

                firstNameEditText.setText(firstName1);
                lastNameEditText.setText(lastName1);
                emailEditText.setText(email1);
               // new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, android.R.style.Theme_Dialog))
                new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, android.R.style.Theme))
                        .setView(formElementsView)
                        .setTitle("Edit User")
                        .setPositiveButton("Update",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String userFirstname = firstNameEditText.getText().toString();
                                        String userLastname = lastNameEditText.getText().toString();
                                        String userEmail = emailEditText.getText().toString();
                                        UserObject userObject = new UserObject();
                                        userObject.firstname = userFirstname;
                                        userObject.lastname = userLastname;
                                        userObject.email = userEmail;
                                        boolean createSuccessful = false;
                                        createSuccessful = new TableControllerUser(getApplication()).update(userObject);
                                        if (createSuccessful) {
                                            TextView firstNameTxt = (TextView) findViewById(R.id.firstName_txt);
                                            TextView lastNameTxt = (TextView) findViewById(R.id.lastName_txt);
                                            TextView emailText = (TextView) findViewById(R.id.email_txt);
                                            firstNameTxt.setText("First Name: " + userFirstname);
                                            lastNameTxt.setText("Last Name: " + userLastname);
                                            emailText.setText("Email: " + userEmail);
                                            Toast.makeText(getApplication(), "User information is updated successfully.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplication(), "Unable to save User information.", Toast.LENGTH_SHORT).show();
                                        }
                                        //dialog.cancel();
                                    }


                                }).show();
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView.setVisibility(View.GONE);
                plusBtn.setVisibility(View.VISIBLE);

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = linkName.getText().toString();
                String url = urlTxt.getText().toString();
                String category = spinner.getSelectedItem().toString();

                if (name != null && !name.equals("") && url != null && !url.equals("")) {
                    LinksObject links = new LinksObject();
                    links.linkname = name;
                    links.url = url;
                    links.category = category;
                    links.favourite = "false";

                    // databaseUtils.addLinks(links);
                    boolean createSuccessful = false;
                    createSuccessful = new TableControllerLinks(getApplication()).create(links);

                    addView.setVisibility(View.GONE);
                    plusBtn.setVisibility(View.VISIBLE);
                    displayLinksView(false, 0, null);
                } else if (name == null || name.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter Name", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter URL", Toast.LENGTH_SHORT).show();
                }

            }
        });

        displayLinksView(false, 0, null);
    }


    public void displayLinksView(boolean displayAfterDelete, Integer linkId, String cateogory) {
        // final List<Links> links = databaseUtils.getLinks();
        //  displayLinks = true;
        List<LinksObject> links =null;
        linksView = (LinearLayout) findViewById(R.id.links_view);
        if(cateogory!=null){
            links = new TableControllerLinks(getApplication()).categoryList(cateogory);
        }else{
            links = new TableControllerLinks(getApplication()).read();

        }

        LayoutInflater inflater = getLayoutInflater();

        if(links!=null && links.size()>0){
            linksView.removeAllViews();
            if (links != null && links.size() > 0) {

                for (int i = 0; i < links.size(); i++) {
                    final View myLayout = inflater.inflate(R.layout.link_layout, linksView, false);
                    TextView noData = (TextView) myLayout.findViewById(R.id.no_data);

                    TextView nameTxt = (TextView) myLayout.findViewById(R.id.name_txt);
                    final TextView urlsTxt = (TextView) myLayout.findViewById(R.id.urls_txt);
                    TextView categoryTxt = (TextView) myLayout.findViewById(R.id.category_txt);
                    favIcon = (ImageView) myLayout.findViewById(R.id.fav_icon);
                   // Button button = (Button) myLayout.findViewById(R.id.edit_delete_btn);
                    LinearLayout linkList = (LinearLayout) myLayout.findViewById(R.id.link_list);

                    linkList.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    if (displayAfterDelete == true) {
                        if (linkId == links.get(i).id) {
                            links.remove(links.get(i));
                        }
                        if (linkId != links.get(i).id) {
                            nameTxt.setText(links.get(i).getLinkname());
                            urlsTxt.setText(links.get(i).getUrl());
                            categoryTxt.setText(links.get(i).getCategory());
                            isFav = links.get(i).getFavourite();
                            if (isFav.equalsIgnoreCase("true")) {
                                favIcon.setImageResource(R.drawable.favorite);
                            } else {
                                favIcon.setImageResource(R.drawable.un_favorite);

                            }
                            myLayout.setTag(links.get(i).id);
                            myLayout.setOnLongClickListener(new EditOrDeleteLinkListner());
                        }
                    } else {
                        nameTxt.setText(links.get(i).getLinkname());
                        urlsTxt.setText(links.get(i).getUrl());
                        categoryTxt.setText(links.get(i).getCategory());
                        isFav = links.get(i).getFavourite();
                        if (isFav.equalsIgnoreCase("true")) {
                            favIcon.setImageResource(R.drawable.favorite);
                        } else {
                            favIcon.setImageResource(R.drawable.un_favorite);

                        }
                        myLayout.setTag(links.get(i).id);
                        myLayout.setOnLongClickListener(new EditOrDeleteLinkListner());

                    }
                    // button.setTag(links.get(i).id);
                    // button.setOnClickListener(new EditOrDeleteLinkListner());
                    favIcon.setTag(links.get(i).id);
                    favIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String linkId = view.getTag().toString();
                            LinksObject linksObject = new TableControllerLinks(getApplicationContext()).readSingleRecord(Integer.parseInt(linkId));
                            String isFav = linksObject.getFavourite();
                            Log.d("isFav",isFav);
                            if (isFav.equals("true")) {
                                linksObject.favourite = "false";
                                favIcon.setImageResource(R.drawable.un_favorite);
                            } else {
                                linksObject.favourite = "true";
                                favIcon.setImageResource(R.drawable.favorite);
                            }
                            boolean successful = new TableControllerLinks(getApplicationContext()).update(linksObject);

                            linksView.removeAllViews();
                            displayLinksView(false, 0, null);

                        }
                    });
                    myLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final SimpleTooltip simpleTooltip = new SimpleTooltip.Builder(getApplication())
                                    .anchorView(v)
                                    .text(urlsTxt.getText().toString())
                                    .gravity(Gravity.BOTTOM)
                                    .animated(true)
                                    .transparentOverlay(true)
                                    .build();

                            simpleTooltip.show();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    simpleTooltip.dismiss();
                                }
                            }, 5000);
                           // return false;
                        }
                    });
                    linksView.addView(myLayout);

                }
            }

        }else{
            linksView.removeAllViews();
            final View myLayout = inflater.inflate(R.layout.link_layout, linksView, false);
            TextView noData = (TextView) myLayout.findViewById(R.id.no_data);
            LinearLayout linkList = (LinearLayout) myLayout.findViewById(R.id.link_list);
            linkList.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
            noData.setText("No data found.");
            linksView.addView(myLayout);

        }




    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            homeView.setVisibility(View.VISIBLE);
            linkView.setVisibility(View.GONE);
            profileView.setVisibility(View.GONE);
            // Handle the camera action
        } else if (id == R.id.nav_profile) {
            profileView.setVisibility(View.VISIBLE);
            linkView.setVisibility(View.GONE);
            homeView.setVisibility(View.GONE);
        } else if (id == R.id.nav_link) {
            plusBtn.setVisibility(View.VISIBLE);
            linkView.setVisibility(View.VISIBLE);
            profileView.setVisibility(View.GONE);
            homeView.setVisibility(View.GONE);
            displayLinksView(false, 0, null);

        }

        else if (id == R.id.nav_media) {
            categoryView("Media");
        }
        else if (id == R.id.nav_education) {
            categoryView("Education");
        }
        else if (id == R.id.nav_ecomerce) {
            categoryView("Ecomerce");
        }
        else if (id == R.id.nav_healthcare) {
            categoryView("Health care");
        }
        else if (id == R.id.nav_personal) {
            categoryView("Personal");
        }
        else if (id == R.id.nav_travel) {
            categoryView("Travel");
        }


        else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            databaseUtils.deleteLinks();
            databaseUtils.deleteUserDetails();
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void categoryView(String cateogory) {
        plusBtn.setVisibility(View.GONE);
        linkView.setVisibility(View.VISIBLE);
        profileView.setVisibility(View.GONE);
        homeView.setVisibility(View.GONE);
        displayLinksView(false, 0, cateogory);
    }

}
