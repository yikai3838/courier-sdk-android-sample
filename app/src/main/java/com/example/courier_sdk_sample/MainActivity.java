package com.example.courier_sdk_sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aimazing.couriersdk.ActivateCallback;
import com.aimazing.couriersdk.ActivateError;
import com.aimazing.couriersdk.Activation;
import com.aimazing.couriersdk.GetPaymentRecordsCallback;
import com.aimazing.couriersdk.GetPaymentRecordsError;
import com.aimazing.couriersdk.Payment;
import com.aimazing.couriersdk.PaymentCallback;
import com.aimazing.couriersdk.PaymentError;
import com.aimazing.couriersdk.PaymentRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Activation sdkActivation;
    PaymentRecord paymentRecord;
    Payment payment;

    EditText transactionIDEditText, paymentAmountEditText;

    TextView resultTextView;

    Button getSupportedMobileWalletsButton, ecodAuthorizeButton, getPaymentRecordsButton, isActivatedButton, activateButton;
    Button launchPaymentPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdkActivation = new Activation(this);
        paymentRecord = new PaymentRecord(this);
        payment = new Payment(this);

        transactionIDEditText = findViewById(R.id.transactionIDEditText);
        paymentAmountEditText = findViewById(R.id.paymentAmountEditText);

        resultTextView = findViewById(R.id.resultTextView);

        launchPaymentPageButton = findViewById(R.id.launchPaymentPageButton);
        launchPaymentPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment.launchPaymentPage(transactionIDEditText.getText().toString(), paymentAmountEditText.getText().toString(), new PaymentCallback() {
                    @Override
                    public void onSuccess(JSONObject _info) {
                        Log.e("onSuccess", _info.toString());
                        resultTextView.setText(_info.toString());
                    }

                    @Override
                    public void onFailed(JSONObject _info) {
                        Log.e("onFailed", _info.toString());
                        resultTextView.setText(_info.toString());
                    }

                    @Override
                    public void onError(PaymentError _error) {
                        Log.e("onError", _error.toString());
                        resultTextView.setText(_error.toString());
                    }
                });
            }
        });

        getPaymentRecordsButton = findViewById(R.id.getPaymentRecordsButton);
        getPaymentRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentRecord.get(new GetPaymentRecordsCallback() {
                    @Override
                    public void onSuccess(final JSONArray _paymentRecords) {
                        Log.e("getPaymentRecordsButton", "onSuccess()");

                        try {
                            Log.e("getPaymentRecordsButton", "_paymentRecords.length() " + _paymentRecords.length());
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    resultTextView.setText("paymentRecord Length: " + _paymentRecords.length());
                                }
                            });


                            for (int i = 0; i < _paymentRecords.length(); i++) {
                                JSONObject tmpJsonObject = _paymentRecords.getJSONObject(i);

                                Log.e("getPaymentRecordsButton", "_paymentRecords[" + i + "] " + tmpJsonObject.toString());
                            }
                        } catch (JSONException e) {
                            Log.e("getPaymentRecordsButton", e.toString());
                        }
                    }

                    @Override
                    public void onError(GetPaymentRecordsError _error) {
                        Log.e("getPaymentRecordsButton", "onError() " + _error);
                        resultTextView.setText("onError " + _error);
                    }
                });
            }
        });

        isActivatedButton = findViewById(R.id.isActivatedButton);
        isActivatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("isActivatedButton", "clicked");
                resultTextView.setText("isActivated() " + sdkActivation.isActivated());
            }
        });

        activateButton = findViewById(R.id.activateButton);
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("activateButton", "clicked");

                sdkActivation.activate("0", new ActivateCallback() {
                    @Override
                    public void onSuccess() {
                        Log.e("activateButton", "onSuccess()");
                        resultTextView.setText("activate() onSuccess()");
                    }

                    @Override
                    public void onError(ActivateError err) {
                        Log.e("activateButton", "onError(): " + err);
                        resultTextView.setText("activate() onError()" + err);
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Process the result from PaymentPageActivity.

        if (requestCode == 38)
            payment.bridgePaymentResult(data);

    }
}
