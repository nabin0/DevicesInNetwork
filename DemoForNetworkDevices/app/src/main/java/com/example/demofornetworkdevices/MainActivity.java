package com.example.demofornetworkdevices;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.druk.servicebrowser.R;
import com.github.druk.rx2dnssd.BonjourService;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private TextView deviceDetails;
    private AppCompatButton btnFetchDevices;
    private List<RegTypeBrowserViewModel.BonjourDomain> bonjourDomainList = new ArrayList<>();
    private HashSet<BonjourService> bonjourServices = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceDetails = findViewById(R.id.deviceDetails);
        btnFetchDevices = findViewById(R.id.btnFetchDevices);

        RegTypeBrowserViewModel viewModel = new ViewModelProvider(this).get(RegTypeBrowserViewModel.class);
        ServiceBrowserViewModel serviceBrowserViewModel = new ViewModelProvider(this).get(ServiceBrowserViewModel.class);
        getDomainList(viewModel);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getDeviceDetails(serviceBrowserViewModel);
            }
        }, 3000);


        btnFetchDevices.setOnClickListener(view -> {
            deviceDetails.setText("");
            getDeviceDetails(serviceBrowserViewModel);

            for (BonjourService service : bonjourServices) {
                StringBuilder dnsRecords = new StringBuilder();
                Map<String, String> dnsDetails = service.getTxtRecords();
                List<String> keys = new ArrayList<>(dnsDetails.keySet());
                for (int i = 0; i < keys.size(); i++) {
                    String key = keys.get(i);
                    String value = dnsDetails.get(key);
                    dnsRecords.append(key + ": " + value + "\n");
                }
                serviceBrowserViewModel.resolveIPRecords(service, (service1) -> {

                    String ip4Address = "";
                    String ip6Address = "";

                    for (InetAddress inetAddress : service1.getInetAddresses()) {
                        if (inetAddress instanceof Inet4Address) {
                            ip4Address = inetAddress.getHostAddress() + ":" + service1.getPort();
                            Log.d("TAG", "UpdateIpAddress: " + inetAddress.getHostAddress());
                        } else {
                            ip6Address = inetAddress.getHostAddress() + ":" + service1.getPort();
                        }
                    }

                    String deviceDetail = "Friendly Name: " + service.getServiceName() + "\nModelName: " + service.getHostname() + "\nAddress:IPv4 " + ip4Address + "\nAddress:IPv6 " + ip6Address + "\nPort: " + service.getPort() + "\n" + dnsRecords + "\n\n\n";
                    deviceDetails.append(deviceDetail);

                });
            }
        });
    }

    void getDomainList(RegTypeBrowserViewModel viewModel) {
        final Consumer<Collection<RegTypeBrowserViewModel.BonjourDomain>> servicesAction = services -> {
            bonjourDomainList.clear();
            for (RegTypeBrowserViewModel.BonjourDomain bonjourDomain : services) {
                if (bonjourDomain.serviceCount > 0) {
                    bonjourDomainList.add(bonjourDomain);
                }
            }
            Log.d("TAG", "Domain list : " + bonjourDomainList.size());
        };

        viewModel.startDiscovery(servicesAction, throwable -> {
        });
    }

    void getDeviceDetails(ServiceBrowserViewModel serviceBrowserViewModel) {
        for (RegTypeBrowserViewModel.BonjourDomain domain : bonjourDomainList) {
            String regType = domain.getServiceName() + "." + domain.getRegType().split(Config.REG_TYPE_SEPARATOR)[0] + ".";
            serviceBrowserViewModel.startDiscovery(regType, "local.", service -> {
                if (!service.isLost()) {
                    bonjourServices.add(service);
                } else {
                    bonjourServices.remove(service);
                }
                Log.d("TAG", "Devices List : " + bonjourServices.size());
            }, throwable -> {
            });
        }
    }
}