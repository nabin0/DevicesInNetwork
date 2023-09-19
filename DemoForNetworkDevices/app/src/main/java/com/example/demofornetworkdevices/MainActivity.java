package com.example.demofornetworkdevices;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.druk.servicebrowser.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView deviceDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceDetails = findViewById(R.id.deviceDetails);
        ServiceBrowserViewModel viewModel = new ViewModelProvider(this).get(ServiceBrowserViewModel.class);
        viewModel.startDiscovery("_airplay._tcp.", "local.", service -> {
            if (!service.isLost()) {
                Log.d("TAG", "Device: " + service.toString());

                StringBuilder dnsRecords = new StringBuilder();
                Map<String, String> dnsDetails = service.getTxtRecords();
                List<String> keys = new ArrayList<>(dnsDetails.keySet()); // Convert keys to a list for indexing
                for (int i = 0; i < keys.size(); i++) {
                    String key = keys.get(i);
                    String value = dnsDetails.get(key);
                    dnsRecords.append(key + ": " + value + "\n");
                }

                String deviceDetail = "Friendly Name: " + service.getServiceName() +
                        "\nModelName: " + service.getHostname() +
                        "\nHostAddress:IPv4 " + service.getInetAddresses().toString() +
                        "\nPort: " + service.getPort() +
                        "\n" + dnsRecords.toString() +
                        "\n\n\n";
                deviceDetails.append(deviceDetail);

            }
        }, throwable -> {
            Log.e("TAG", "Error: ", throwable);
        });
    }
}