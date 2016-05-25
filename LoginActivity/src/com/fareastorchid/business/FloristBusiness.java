package com.fareastorchid.business;

import org.json.JSONArray;

public interface FloristBusiness {
    public static final int ERRCODE_SUCCESS = 0;
    public static final int ERRCODE_WRONG_APIKEY = 101;
    public static final int ERRCODE_SESSION_EXPIRE = 102;
    public static final int ERRCODE_UNAME_DONT_EXIST = 103;
    public static final int ERRCODE_WRONG_PARAMS = 104;
    public static final int ERRCODE_WRONG_PASSWORD = 105;
    public static final int ERRCODE_NOSUCHMETHOD = 110;
    public static final int ERRCODE_MISSING_PARAMS = 111;
    public static final int ERRCODE_SERVER_ERROR = 112;
    public static final int ERRCODE_API_ACCESS_DENIED = 113;
    public static final int ERRCODE_LOGINFAIL_OUTOFLIMIT = 201;
    public static final int ERRCODE_TOO_LONG_CHARS = 204;
    public static final int ERRCODE_UPLOAD_FILE_IS_TOO_BIG = 301;
    public static final int ERRCODE_DUPLICATE_UPLOAD_CHUNK = 303;
    public static final int ERRCODE_WRONG_FORMAT_FILE = 324;
    public static final int ERRCODE_ACCESS_INFO_DENIED = 500;
    public static final int ERRCODE_ITEM_NOT_FOUND = 1001;

    public FloristBusinessListener getBusinessListener();

    public void setBusinessListener(FloristBusinessListener businessListener);

    public void removeListener();

    public void getAllBanner();

    public void getAllMainCat();

    public void getItemFeoByMainCate(int mainCatId, int cateTypeId, String sortBy, int pageIndex);

    public void getItemFeoByItemId(int itemId);

    public void userFeoLogin(String userName, String password);

    public void userFeoChangePW(String userName, String oldPW, String newPW);

    public void userFeoResetPWCode(String email);

    public void userFeoUpdateProfile(String id_user, String fullname, String email, String contactNumber, String billing_address, String shopName);

    public void searchItemByName(String searchType, String itemName, int pageIndex);

    public void getAllCountry();

    public void getAllColor();

    public void getItemFeoByColorId(int colorId, String sortBy, int pageIndex);

    public void getItemFeoByCountryId(int countryId, String sortBy, int pageIndex);

    public void pushOrderItems(JSONArray array, String userId, String guestName, String guestPhone, String guestPhone2, String guestAddress, String remark, String deliveryAddress, String deliveryTime);

    public void getOrderHistory(String userId, int pageIndex);

    public void getOrderHistoryDetail(String transId);
    
    public void updateOrderHistoryRemark(String transId, String remark);

    public void getForecastList();

    public void getForecastListDetail(String country_code, String date_arrival);

    public void getDeliveryAddress(String user_id);

    public void addDeliveryAddress(String user_id, String address);

    public void deleteDeliveryAddress(String delivery_id ,String user_id);

    public void updateDeliveryAddress(String delivery_id, String user_id, String address);
    
    public void updateOutletAddress(String id, String address, String name, String contact);

    public void getReportByCatId(String idSubcat, String priceListType);

    public void sendContact(String name, String email, String contact_no, String address, String pos_code, String subject, String order_no, String message);

    public void getByMainCat(int mainCatId);

    public void sendRegister(String name, String contactDetail);

    public void getPriceListSubCate(String priceListType);
    
    public void getLastVersion();
}
