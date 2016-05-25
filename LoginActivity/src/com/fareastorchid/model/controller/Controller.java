package com.fareastorchid.model.controller;

import android.app.Activity;

import com.fareastorchid.AboutUsActivity;
import com.fareastorchid.ContactUsActivity;
import com.fareastorchid.DeliveryAddressActivity;
import com.fareastorchid.DeliveryTimeActivity;
import com.fareastorchid.FAQActivity;
import com.fareastorchid.FlowerDetailActivity;
import com.fareastorchid.ForecastActivity;
import com.fareastorchid.ForecastByCountryActivity;
import com.fareastorchid.LoginActivity;
import com.fareastorchid.MainActivity;
import com.fareastorchid.MyAccountActivity;
import com.fareastorchid.OrderHistoryActivity;
import com.fareastorchid.OrderHistoryDetailActivity;
import com.fareastorchid.ReserveActivity;
import com.fareastorchid.SearchActivity;
import com.fareastorchid.ShipmentActivity;
import com.fareastorchid.ShopbyColorActivity;
import com.fareastorchid.TCActivity;

public final class Controller {
    private static final Object SYNC_OBJ = new Object();
    private static Controller instance;
    private static MainActivity mainActivity = null;
    private static LoginActivity loginActivity = null;
    private static SearchActivity searchActivity = null;
    private static ShipmentActivity shipmentActivity = null;
    private static ShopbyColorActivity shopbyColorActivity = null;
    private static ForecastByCountryActivity shopbyCountryActivity = null;
    private static FlowerDetailActivity flowerDetailActivity = null;
    private static MyAccountActivity myAccountActivity = null;
    private static ReserveActivity reserveActivity = null;
    private static OrderHistoryActivity orderHistoryActivity = null;
    private static OrderHistoryDetailActivity orderHistorydetailActivity = null;
    private static ForecastActivity forecastActivity = null;
    private static AboutUsActivity aboutUsActivity = null;
    private static ContactUsActivity contactUsActivity = null;
    private static FAQActivity faqActivity = null;
    private static DeliveryAddressActivity deliveryAddressActivity = null;
    private static DeliveryTimeActivity deliveryTimeActivity = null;
    private static TCActivity tcActivity = null;

    private Controller() {
    }

    private static Controller getInstance() {
        if (instance == null)
            instance = new Controller();

        return instance;
    }

    public static Activity getActInstance(Class<?> classType) {
        synchronized (SYNC_OBJ) {
            getInstance();

            if (classType == MainActivity.class) {
                return mainActivity;
            } else if (classType == LoginActivity.class) {
                return loginActivity;
            } else if (classType == SearchActivity.class) {
                return searchActivity;
            } else if (classType == ShipmentActivity.class) {
                return shipmentActivity;
            } else if (classType == ShopbyColorActivity.class) {
                return shopbyColorActivity;
            } else if (classType == ForecastByCountryActivity.class) {
                return shopbyCountryActivity;
            } else if (classType == FlowerDetailActivity.class) {
                return flowerDetailActivity;
            } else if (classType == MyAccountActivity.class) {
                return myAccountActivity;
            } else if (classType == ReserveActivity.class) {
                return reserveActivity;
            } else if (classType == OrderHistoryActivity.class) {
                return orderHistoryActivity;
            } else if (classType == OrderHistoryDetailActivity.class) {
                return orderHistorydetailActivity;
            } else if (classType == ForecastActivity.class) {
                return forecastActivity;
            } else if (classType == AboutUsActivity.class) {
                return aboutUsActivity;
            } else if (classType == ContactUsActivity.class) {
                return contactUsActivity;
            } else if (classType == FAQActivity.class) {
                return faqActivity;
            } else if (classType == DeliveryAddressActivity.class) {
                return deliveryAddressActivity;
            } else if (classType == DeliveryTimeActivity.class) {
                return deliveryTimeActivity;
            } else if (classType == TCActivity.class) {
                return tcActivity;
            } else {
                throw new UnsupportedOperationException(
                        "Not yet setup activity");
            }
        }
    }

    public static void updateActInstance(Activity activity) {
        synchronized (SYNC_OBJ) {
            getInstance();

            if (activity instanceof MainActivity) {
                mainActivity = (MainActivity) activity;
            } else if (activity instanceof LoginActivity) {
                loginActivity = (LoginActivity) activity;
            } else if (activity instanceof SearchActivity) {
                searchActivity = (SearchActivity) activity;
            } else if (activity instanceof ShipmentActivity) {
                shipmentActivity = (ShipmentActivity) activity;
            } else if (activity instanceof ShopbyColorActivity) {
                shopbyColorActivity = (ShopbyColorActivity) activity;
            } else if (activity instanceof ForecastByCountryActivity) {
                shopbyCountryActivity = (ForecastByCountryActivity) activity;
            } else if (activity instanceof FlowerDetailActivity) {
                flowerDetailActivity = (FlowerDetailActivity) activity;
            } else if (activity instanceof MyAccountActivity) {
                myAccountActivity = (MyAccountActivity) activity;
            } else if (activity instanceof ReserveActivity) {
                reserveActivity = (ReserveActivity) activity;
            } else if (activity instanceof OrderHistoryActivity) {
                orderHistoryActivity = (OrderHistoryActivity) activity;
            } else if (activity instanceof OrderHistoryDetailActivity) {
                orderHistorydetailActivity = (OrderHistoryDetailActivity) activity;
            } else if (activity instanceof ForecastActivity) {
                forecastActivity = (ForecastActivity) activity;
            } else if (activity instanceof AboutUsActivity) {
                aboutUsActivity = (AboutUsActivity) activity;
            } else if (activity instanceof ContactUsActivity) {
                contactUsActivity = (ContactUsActivity) activity;
            } else if (activity instanceof FAQActivity) {
                faqActivity = (FAQActivity) activity;
            } else if (activity instanceof DeliveryAddressActivity) {
                deliveryAddressActivity = (DeliveryAddressActivity) activity;
            } else if (activity instanceof DeliveryTimeActivity) {
                deliveryTimeActivity = (DeliveryTimeActivity) activity;
            } else if (activity instanceof TCActivity) {
                tcActivity = (TCActivity) activity;
            } else {
                throw new UnsupportedOperationException(
                        "Not yet setup activity");
            }
        }
    }
}
