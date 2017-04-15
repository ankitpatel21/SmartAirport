package com.example.nirav.smartairport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private TextView Uname;

    private Toolbar mToolbar;

    private FragmentTransaction fragmentTransaction;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(getApplicationContext(),BagChkService.class));

        firebaseAuth = FirebaseAuth.getInstance();

        mToolbar=(Toolbar)findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerlayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        mToggle=new ActionBarDrawerToggle(this,mDrawerlayout,R.string.open,R.string.close);

        mDrawerlayout.addDrawerListener(mToggle);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container,new HomeFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Home");

        navigationView=(NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.nav_account:
                        fragmentTransaction =getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new ProfileFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Profile");
                        item.setChecked(true);
                        mDrawerlayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.nav_home:
                        fragmentTransaction =getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new HomeFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Home");
                        item.setChecked(true);
                        mDrawerlayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.nav_logout:
                        mDrawerlayout.closeDrawer(Gravity.LEFT);
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Are you sure you want to logout?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        signOut();
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        if (user == null) {
                                            startActivity(new Intent(getApplicationContext(), Main_home.class));
                                            finish();
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.setTitle("Logout");
                        alertDialog.show();
                        break;

                    case R.id.nav_barcode:
                        fragmentTransaction =getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new ticket_scan());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Ticket Scan");
                        item.setChecked(true);
                        mDrawerlayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.nav_ticket_details:
                        Ticket_details td = new Ticket_details();
                        Bundle arg = new Bundle();
                        arg.putString("t_barcode_no", "");
                        td.setArguments(arg);
                        fragmentTransaction =getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,td).commit();
                        getSupportActionBar().setTitle("Ticket Details");
                        item.setChecked(true);
                        mDrawerlayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.nav_bag_scan:
                        fragmentTransaction =getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new BagVerificationFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Bag Barcode Scan");
                        item.setChecked(true);
                        mDrawerlayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.nav_feedback:
                        startActivity(new Intent(getApplicationContext(),FeedBack.class));


                }
                return true;
            }
        });
    }

    public void signOut()
    {
        firebaseAuth.signOut();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
