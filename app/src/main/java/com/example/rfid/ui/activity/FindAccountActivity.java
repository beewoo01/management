package com.example.rfid.ui.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.view.View;

import com.example.rfid.R;
import com.example.rfid.databinding.ActivityFindAccountBinding;
import com.example.rfid.ui.fragment.FindIDFragment;
import com.example.rfid.ui.fragment.FindPWFragment;

import java.util.ArrayList;
import java.util.Objects;

public class FindAccountActivity extends BaseActivity {

    ActivityFindAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_account);

        FindAccountAdapter adapter = new FindAccountAdapter(getSupportFragmentManager());
        binding.viewpager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);

        Objects.requireNonNull(binding.tabLayout.getTabAt(0)).setText("아이디찾기");
        Objects.requireNonNull(binding.tabLayout.getTabAt(1)).setText("비밀번호찾기");

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    static class FindAccountAdapter extends FragmentPagerAdapter {

        public ArrayList<Fragment> items;
        public FindAccountAdapter(@NonNull FragmentManager fm) {
            super(fm);
            items = new ArrayList<>();
            items.add(new FindIDFragment());
            items.add(new FindPWFragment());
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
}