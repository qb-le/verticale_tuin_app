package com.example.verttuin.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.verttuin.R;
import com.example.verttuin.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    Button rollbtn;
    int randomnumber;
    private TextView roulettetxt;


    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        roulettetxt = binding.editTextText;
        rollbtn = binding.rollBtn;
        rollbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomnumber = (int)(Math.random() * 50 + 1);
                String randomnumberstring = randomnumber + "";
                roulettetxt.setText(randomnumberstring);
            }
        });
        return root;
    }

    public void Check(){
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}