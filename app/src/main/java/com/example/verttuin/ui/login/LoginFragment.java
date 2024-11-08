package com.example.verttuin.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.verttuin.R;
import com.example.verttuin.databinding.FragmentLoginBinding;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LoginFragment extends Fragment {

    EditText emailbox;
    EditText passwordbox;
    Button login_button;
    EditText namebox;
    private static final String TAG = "PostRequestActivity";

    private FragmentLoginBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginViewModel loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        emailbox = binding.emailTxt;
        passwordbox = binding.passwordTxt;
        login_button = binding.loginBtn;
        namebox = binding.editTextText2;

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        DataOutputStream wr = null;
                        InputStream is = null;

                        try {
                            URL url = new URL("http://192.168.68.133:8080/users");
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setDoOutput(true);
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                            // Connect to the server
                            connection.connect();

                            // Send request
                            wr = new DataOutputStream(connection.getOutputStream());
                            // Sending the JSON-like string data
                            wr.writeBytes("{\"name\": \"Tom\", \"email\": \"tom@example.com\", \"password\": \"mypassword\"}");
                            wr.flush();  // Ensure all data is sent before closing the output stream

                            // Get response
                            int response = connection.getResponseCode();

                            if (response >= 200 && response <= 399) {
                                // Success: Read response input stream (if necessary)
                                is = connection.getInputStream();
                                // You could read the input stream if needed:
                                // String responseBody = readStream(is);
                            } else {
                                // Failure: Read error stream (if necessary)
                                is = connection.getErrorStream();
                                // You could read the error stream to get more details
                                // String errorResponse = readStream(is);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
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
                                e.printStackTrace();
                            }
                        }
                    }
                });
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