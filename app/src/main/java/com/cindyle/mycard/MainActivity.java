package com.cindyle.mycard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.cindyle.mycard.contacts.ContactsFragment;
import com.cindyle.mycard.record.RecordFragment;
import com.example.mycard.R;
import com.cindyle.mycard.getcards.GetCardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private GetCardFragment cardFragment;
    private ContactsFragment contacts;
    private RecordFragment record;
    private Fragment currentTab;
    private BottomNavigationView btnNav;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNav = findViewById(R.id.btn_nav);

        if(cardFragment == null){
            cardFragment = new GetCardFragment();
        }
        if(contacts == null){
            contacts = new ContactsFragment();
        }
        if (record == null) {
            record = new RecordFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, cardFragment)
                .commit();

        btnNav.setSelectedItemId(R.id.nav1);
        currentTab = cardFragment;

        btnNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav1) {
                switchTab(cardFragment);
                return true;
            }
            if (item.getItemId() == R.id.nav2) {
                switchTab(record);
                record.setObserve();
                return true;
            }
            if (item.getItemId() == R.id.nav3) {
                switchTab(contacts);
                return true;
            }
            return false;
        });
    }

    public void openRecord(long cardInfo){
        btnNav.setSelectedItemId(R.id.nav1);
        currentTab = cardFragment;

        cardFragment.getCardInfo(cardInfo);
    }

    private void switchTab(Fragment tab) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentTab != tab) {
            if (!tab.isAdded()) {
                // 沒加就加
                transaction.hide(currentTab).add(R.id.frameLayout, tab).commit();
            } else {
                // 有加就直接show,hide
                transaction.hide(currentTab).show(tab).commit();
            }
            currentTab = tab;
        }
    }
}