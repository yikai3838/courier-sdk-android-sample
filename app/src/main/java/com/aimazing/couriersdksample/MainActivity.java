package com.aimazing.couriersdksample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aimazing.couriersdk.ActivateCallback;
import com.aimazing.couriersdk.ActivateError;
import com.aimazing.couriersdk.Activation;
import com.aimazing.couriersdk.EcodAuthorizeCallback;
import com.aimazing.couriersdk.EcodAuthorizeError;
import com.aimazing.couriersdk.GetPaymentRecordsCallback;
import com.aimazing.couriersdk.GetPaymentRecordsError;
import com.aimazing.couriersdk.GetSupportedMobileWalletsCallback;
import com.aimazing.couriersdk.GetSupportedMobileWalletsError;
import com.aimazing.couriersdk.Payment;
import com.aimazing.couriersdk.PaymentRecord;
import com.aimazing.couriersdk.VoidTransactionCallback;
import com.aimazing.couriersdk.VoidTransactionError;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    Button buttonButton, activationButton, mobileWalletButton, transactionRecordButton, paymentAuthorizationButton, voidTransactionButton;

    Activation activation;
    Payment payment;
    PaymentRecord paymentRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        activation = new Activation(this);
        payment = new Payment(this);
        paymentRecord = new PaymentRecord(this);

        activationButton = (Button)findViewById(R.id.activationButton);
        mobileWalletButton = (Button)findViewById(R.id.mobileWalletButton);
        transactionRecordButton = (Button)findViewById(R.id.transactionRecordButton);
        paymentAuthorizationButton = (Button)findViewById(R.id.paymentAuthorizationButton);
        voidTransactionButton = (Button)findViewById(R.id.voidTransactionButton);

        buttonButton = (Button)findViewById(R.id.buttonButton);

        activationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Hello", "onClick");
                Log.e("Hello", "" + activation.isActivated());

                activation.activate("userid", new ActivateCallback() {
                    @Override
                    public void onSuccess() {
                        Log.e("Hello", "onSuccess()");
                    }

                    @Override
                    public void onError(ActivateError activateError) {
                        Log.e("Hello", "onError(): " + activateError);
                    }
                });
            }
        });

        mobileWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Hello", "onClick");

                payment.getSupportedMobileWallets(new GetSupportedMobileWalletsCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        Log.e("Hello", "onSuccess(): " + jsonArray.toString());
                    }

                    @Override
                    public void onError(GetSupportedMobileWalletsError getSupportedMobileWalletsError) {
                        Log.e("Hello", "onError(): " + getSupportedMobileWalletsError);
                    }
                });
            }
        });

        transactionRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Hello", "onClick");

                paymentRecord.get(new GetPaymentRecordsCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        Log.e("Hello", "onSuccess(): " + jsonArray.toString());
                    }

                    @Override
                    public void onError(GetPaymentRecordsError getPaymentRecordsError) {
                        Log.e("Hello", "onError(): " + getPaymentRecordsError);
                    }
                });
            }
        });

        paymentAuthorizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "paymentAuthorizationButton");

                payment.ecodAuthorize(
                        "elb573826179", "Tokopedia", "Sudomo Gunawan",
                        "Jl Letjen Haryono MT Kav 10 Ged Mugi Griya Lt 5 Suite 502, Dki Jakarta",
                        "+622127899765", "2", "BARCODE", "8000407319799600", "2000",
                        new EcodAuthorizeCallback() {
                            @Override
                            public void onSuccess(JSONObject jsonObject) {
                                Log.e("onSuccess", jsonObject.toString());
                            }

                            @Override
                            public void onFailed(JSONObject jsonObject) {
                                Log.e("onFailed", jsonObject.toString());
                            }

                            @Override
                            public void onError(EcodAuthorizeError ecodAuthorizeError) {
                                Log.e("onFailed", ecodAuthorizeError.toString());

                            }
                        }
                );
            }
        });

        voidTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment.voidTransaction("c359650ffcacfb9f6c1e417e2876cae2", "3838438",
                    new VoidTransactionCallback() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            Log.e("onSuccess", jsonObject.toString());
                        }

                        @Override
                        public void onFailed(JSONObject jsonObject) {
                            Log.e("onFailed", jsonObject.toString());
                        }

                        @Override
                        public void onError(VoidTransactionError voidTransactionError) {
                            Log.e("onFailed", voidTransactionError.toString());
                        }
                    }
                );
            }
        });

        buttonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("HEY", "HEY");

                tt();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void tt() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String total = "";
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet("http://www.vogella.com");
                    HttpResponse response = client.execute(request);

                    Log.e("After response", total);

                    // Get the response
                    BufferedReader rd = new BufferedReader
                            (new InputStreamReader(
                                    response.getEntity().getContent()));

                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        Log.e("total", total);
                        total = total + line;
                    }

                    Log.e("total", total);
                } catch (IOException e) {

                }
            }
        }).start();

    }
}
