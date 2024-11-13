package com.example.verttuin.Foutjes.login;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.verttuin.R;
import com.example.verttuin.databinding.FragmentLoginBinding;
import com.example.verttuin.ui.home.HomeFragment;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    Button Login_Button;
    Button CreateRedirect_Button;
    Button test;
    EditText emailloginbox;
    EditText passwordloginbox;
    boolean success = false;

    public interface BooleanListener {
        void onBooleanChanged(boolean value);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginViewModel LoginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        Login_Button = binding.LoginBtn;
        CreateRedirect_Button = binding.CreateRedirectBtn;

        emailloginbox = binding.EmailLoginText;
        passwordloginbox = binding.PasswordLoginText;



        Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailInput = emailloginbox.getText().toString();
                String passwordInput = passwordloginbox.getText().toString();

                emailloginbox.setText("");
                passwordloginbox.setText("");

                // Run network operation in a background thread
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        DataOutputStream wr = null;
                        InputStream is = null;

                        try {
                            URL url = new URL("http://192.168.68.92:8080/login");
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setDoOutput(true);
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                            // Create JSON object and write it to the request body
                            JSONObject jsonParam = new JSONObject();
                            jsonParam.put("email", emailInput);
                            jsonParam.put("password", passwordInput);

                            wr = new DataOutputStream(connection.getOutputStream());
                            wr.writeBytes(jsonParam.toString());
                            wr.flush();

                            // Get response code and read the response
                            int responseCode = connection.getResponseCode();
                            is = (responseCode >= 200 && responseCode <= 399)
                                    ? connection.getInputStream()
                                    : connection.getErrorStream();

                            // Read the response from the input stream
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line.trim());
                            }

                            // Log the response
                            Log.d("HTTP Response", "Response Code: " + responseCode);
                            Log.d("HTTP Response", "Response Body: " + response.toString());


                            JSONObject jsonResponse = new JSONObject(response.toString());
                            String message = jsonResponse.getString("message");



                            if ("Login successful".equals(message)) {

                                FragmentManager fragmentManager = getParentFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();

// Replace the current fragment with another one (or a new instance of the same fragment)
                                transaction.replace(R.id.container, new HomeFragment()); // Replace with another fragment
                                transaction.addToBackStack(null); // Optional: add to back stack if you want to navigate back to this fragment
                                transaction.commit();

//                                Fragment homeFragment = new HomeFragment(); // Initialize the target fragment
//                                FragmentManager fragmentManager = getParentFragmentManager(); // Get FragmentManager
//                                FragmentTransaction transaction = fragmentManager.beginTransaction();
//
//                                // Replace FragmentA with FragmentB and add to back stack
//                                transaction.replace(R.id.container, homeFragment);
//                                transaction.addToBackStack(null); // Optional: so you can navigate back
//                                transaction.commit(); // Commit the transaction
                            }
                            else {
                                Log.e("HTTP Response", "Login failed");
                                emailloginbox.setError("Login gefaald");
                            }


                        } catch (Exception e) {
                            Log.e("HTTP Error", "Error occurred", e);

                        } finally {
                            // Close resources safely
                            try {
                                if (wr != null) {
                                    wr.close(); // Close the DataOutputStream
                                }
                                if (is != null) {
                                    is.close(); // Close the InputStream
                                }
                                if (connection != null) {
                                    connection.disconnect(); // Close the connection
                                }
                            } catch (IOException e) {
                                Log.e("HTTP Error", "Error closing resources", e);
                            }
                        }
                    }
                });
            }
        });




        CreateRedirect_Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Fragment creationFragment = new CreationFragment(); // Initialize the target fragment
                FragmentManager fragmentManager = getParentFragmentManager(); // Get FragmentManager
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Replace FragmentA with FragmentB and add to back stack
                transaction.replace(R.id.container, creationFragment);
                transaction.addToBackStack(null); // Optional: so you can navigate back
                transaction.commit(); // Commit the transaction
                }
        });

        return root;
    }



    @Override
        public void onDestroyView () {
            super.onDestroyView();
            binding = null;
        }
    }
