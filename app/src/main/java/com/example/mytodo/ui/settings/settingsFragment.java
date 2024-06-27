package com.example.mytodo.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.mytodo.DatabaseHelper;
import com.example.mytodo.R;
import com.example.mytodo.databinding.FragmentSettingsBinding;

public class settingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button btnDeleteEarlierItems = view.findViewById(R.id.btnDeleteEarlierItems);

        dbHelper = new DatabaseHelper(getContext());

        btnDeleteEarlierItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteExpireTodoItems();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}