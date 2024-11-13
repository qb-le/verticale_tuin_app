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
import com.example.verttuin.databinding.FragmentCreationBinding;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;


public class CreationFragment extends Fragment {

    EditText emailcreatebox;
    EditText passwordcreatebox;
    Button Create_button;
    Button LoginRedirect_button;
    EditText namecreatebox;


    private static final String TAG = "PostRequestActivity";

    private FragmentCreationBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CreationViewModel creationViewModel =
                new ViewModelProvider(this).get(CreationViewModel.class);



        View view = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentCreationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        namecreatebox = binding.NameCreateText;
        emailcreatebox = binding.EmailCreateText;
        passwordcreatebox = binding.PasswordCreateText;
        Create_button = binding.CreateBtn;
        LoginRedirect_button = binding.LoginRedirectBtn;





        Create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameInput = namecreatebox.getText().toString();
                String emailInput = emailcreatebox.getText().toString();
                String passwordInput = passwordcreatebox.getText().toString();

                namecreatebox.setText("");
                emailcreatebox.setText("");
                passwordcreatebox.setText("");

                // Run network operation in a background thread
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        DataOutputStream wr = null;
                        InputStream is = null;

                        try {
                            URL url = new URL("http://192.168.68.92:8080/users");
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setDoOutput(true);
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                            // Create JSON object and write it to the request body
                            JSONObject jsonParam = new JSONObject();
                            jsonParam.put("name", nameInput);
                            jsonParam.put("email", emailInput);
                            jsonParam.put("password", passwordInput);
                            jsonParam.put("regionID", 1);

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

        LoginRedirect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment loginFragment = new LoginFragment(); // Initialize the target fragment
                FragmentManager fragmentManager = getParentFragmentManager(); // Get FragmentManager
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Replace FragmentA with FragmentB and add to back stack
                transaction.replace(R.id.container, loginFragment);
                transaction.addToBackStack(null); // Optional: so you can navigate back
                transaction.commit(); // Commit the transaction
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}