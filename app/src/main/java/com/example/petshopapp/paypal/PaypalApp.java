package com.example.petshopapp.paypal;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

public class PaypalApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PayPalCheckout.setConfig(new CheckoutConfig(
                this,
                "AT9all-Y7gGi5pPmCAbHYsFZap-89ms_ygCfZgTvTcM59B9p5j9t1vpqUgPdUUleCt16CxOga0lpZSW3",
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                "com.example.petshopapp://paypalpay"

        ));
    }
}
