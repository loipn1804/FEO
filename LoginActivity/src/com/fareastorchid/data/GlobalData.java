package com.fareastorchid.data;

import android.os.Environment;
import android.util.Log;

import com.fareastorchid.model.DelAddress;
import com.fareastorchid.model.Discount;
import com.fareastorchid.model.FlowerDetailColor;
import com.fareastorchid.model.ForecastCountry;
import com.fareastorchid.model.OrderItem;
import com.fareastorchid.util.PQT;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

@SuppressWarnings("unchecked")
public class GlobalData {
	public final static String APP_NAME = "Far East Orchid";
	public final static String EFFECT_SAVED_FOLDER = "FarEastOrchid";
	public final static String NETWORK_ERROR = "NETWORK_ERROR";
	public final static String FILE_NOT_FOUND = "FileNotFoundException";

	public static final boolean LOG = true;
	public static final boolean TEST_MEM = false;
	public static final boolean LOG_REQUEST = true;

	public static final boolean debugViewLogApp = false;
	public static final String isFirstTime = "firstLaunch";
	public static final String UserPREFERENCES = "UserPrefs";
	public static final String UserName = "username";
	public static final String Position = "position";
	public static final String Id = "id";
	public static final String Email = "email";
	public static final String FullName = "name";
	public static final String Status = "status";
	public static final String ShopName = "shopname";
	public static final String Office_No = "address";
	public static final String BillingAddress = "billing_address";
	public static final String ContactNumber = "phone";
	public static final String Level = "level";
	public static final String Outlet = "outlet";
	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_FORECAST = 1;
	public static final int TYPE_COUNTRY = 2;
	public static final int TYPE_COLOR = 3;
	public static final int TYPE_FRESH = 4;
	public static final int TYPE_FOLIAGE = 5;
	public static final int TYPE_ACCESS = 6;
	public static final int TYPE_FESTIVE = 7;
	public static final int FROM_MAIN = 0;
	public static final int FROM_DETAIL = 1;
	public static boolean shouldGetMainCat = false;
	public static boolean shouldGetItemByColor = false;
	public static boolean shouldeGetItem = false;
	public static boolean isLogin = false;
	public static OrderItem orderItem = new OrderItem();
	public static OrderItem editOrderItem = new OrderItem();
	public static int editPosition = 0;
	public static List<OrderItem> listOrderItem = new ArrayList<OrderItem>();
	public static List<ForecastCountry> listCountries = new ArrayList<ForecastCountry>();
	public static List<FlowerDetailColor> listColor = new ArrayList<FlowerDetailColor>();
	public static int currentType = 0;
	public static int currentColorId = -1;
	public static int currentCountryId = -1;
	public static int currentMainCat = 1;
	public static String currentPriceListName = "New Shipment";
	// public static HashMap<String , OrderItem> listOrderItem = new
	// HashMap<String ,OrderItem>();

	public static DelAddress edtAddress = new DelAddress();
	public static int editPos = -1;
	public static String chosenAddress = "";
	public static String chosenTimeSlot = "";
	public static String sortBy = "AZ";
	public static int subCate = 1;
	public static Double itemPrice = 0.0;

	public static boolean isFromPDFview = false;
	public static String searchType = "item_name";
	private static HashMap<URL, JarFile> jarCache;
	static {
		try {
			Class<?> jarURLConnectionImplClass = Class.forName("org.apache.harmony.luni.internal.net.www.protocol.jar.JarURLConnectionImpl");
			final Field jarCacheField = jarURLConnectionImplClass.getDeclaredField("jarCache");
			jarCacheField.setAccessible(true);
			// noinspection unchecked
			jarCache = (HashMap<URL, JarFile>) jarCacheField.get(null);
		} catch (Exception e) {
			// ignored
		}
	}

	public static void clearGlobalData() {
		try {
			if (PQT.isListDirty(listOrderItem))
				listOrderItem.clear();

			if (PQT.isListDirty(listCountries))
				listCountries.clear();

			if (PQT.isListDirty(listColor))
				listColor.clear();

			GlobalData.isLogin = false;
			shouldGetMainCat = false;
			shouldGetItemByColor = false;
			shouldeGetItem = false;
			orderItem = new OrderItem();
			currentType = 0;
			currentColorId = -1;
			currentCountryId = -1;
			currentMainCat = 1;
			currentPriceListName = "New Shipment";
			edtAddress = new DelAddress();
			editPos = -1;
			chosenAddress = "";
			chosenTimeSlot = "";
			sortBy = "AZ";
			subCate = 1;
			itemPrice = 0.0;
			searchType = "item_name";

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void clearGlobalDataWithoutLogout() {
		try {
			if (PQT.isListDirty(listOrderItem))
				listOrderItem.clear();

			if (PQT.isListDirty(listCountries))
				listCountries.clear();

			if (PQT.isListDirty(listColor))
				listColor.clear();

			GlobalData.isLogin = true;
			shouldGetMainCat = false;
			shouldGetItemByColor = false;
			shouldeGetItem = false;
			orderItem = new OrderItem();
			currentType = 0;
			currentColorId = -1;
			currentCountryId = -1;
			currentMainCat = 1;
			currentPriceListName = "New Shipment";
			edtAddress = new DelAddress();
			editPos = -1;
			chosenAddress = "";
			chosenTimeSlot = "";
			sortBy = "AZ";
			subCate = 1;
			itemPrice = 0.0;
			searchType = "item_name";

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateDiscountPrice(String itemCode) {
		List<OrderItem> tmpListOrderItem = new ArrayList<OrderItem>();
		List<Discount> tmpListDiscount = new ArrayList<Discount>();
		int totalItem = 0;

		for (OrderItem orderItem : listOrderItem) {
			String code = orderItem.getItem().getCode_name();
			if (code.equalsIgnoreCase(itemCode)) {
				int a = 1;
				int b = 1;
				int c = a + b;
			}
			if (orderItem.getItem().getCode_name().equalsIgnoreCase(itemCode)) {
				totalItem += orderItem.getQuantity();
				tmpListOrderItem.add(orderItem);

				if (tmpListDiscount.isEmpty())
					tmpListDiscount = orderItem.getItem().getLsDiscount();
			}
		}

		if (tmpListOrderItem.isEmpty())
			return;

		int latestQty = 0;
		double basePrice = 0;
		double sellingPrice = 0;

		if (isLogin) {
			basePrice = Double.valueOf(tmpListOrderItem.get(0).getItem().getTrader_price());
		} else {
			basePrice = Double.valueOf(tmpListOrderItem.get(0).getItem().getWholesale_price());
		}
		
		sellingPrice = basePrice;
		
		for (Discount discount : tmpListDiscount) {
			int curQty = Integer.valueOf(discount.getTotal_item());
			if (totalItem >= curQty && curQty > latestQty) {
				latestQty = curQty;
				sellingPrice = basePrice - Double.valueOf(discount.getAmount());
			}
		}

		for (OrderItem orderItem : tmpListOrderItem) {
			orderItem.setBasePrice(basePrice);
//			orderItem.setSellingPrice(basePrice * orderItem.getQuantity());
			orderItem.setSellingPrice(sellingPrice);
		}
	}

	public static String getExternalStorageDirectory() {
		String labanFolder = "/LabanUtility/";
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		File myNewFolder = new File(extStorageDirectory + labanFolder);
		myNewFolder.mkdir();
		return extStorageDirectory + labanFolder;
	}

	public static String getCacheExternalStorageDirectory() {
		getExternalStorageDirectory();

		String labanFolder = "/LabanUtility/cache/";
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		File myNewFolder = new File(extStorageDirectory + labanFolder);
		myNewFolder.mkdir();
		return extStorageDirectory + labanFolder;
	}

	public static String getBackgroundExternalStorageDirectory() {
		getExternalStorageDirectory();

		String labanFolder = "/LabanUtility/background/";
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		File myNewFolder = new File(extStorageDirectory + labanFolder);
		myNewFolder.mkdir();
		return extStorageDirectory + labanFolder;
	}

	public static String getDataExternalStorageDirectory() {
		getExternalStorageDirectory();

		String labanFolder = "/LabanUtility/data/";
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		File myNewFolder = new File(extStorageDirectory + labanFolder);
		myNewFolder.mkdir();
		return extStorageDirectory + labanFolder;
	}

	public static void createNoMediaFile(String path) {
		try {
			String strFile = path + ".nomedia";
			File file = new File(strFile);
			if (!file.exists())
				file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void clearHashMapInJarCache() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// HACK
					// http://stackoverflow.com/questions/14610350/android-memory-leak-in-apache-harmonys-jarurlconnectionimpl
					if (jarCache != null) {
						try {
							for (final Iterator<Map.Entry<URL, JarFile>> iterator = jarCache.entrySet().iterator(); iterator.hasNext();) {
								final Map.Entry<URL, JarFile> e = iterator.next();
								final URL url = e.getKey();
								if (url.toString().endsWith(".apk")) {
									Log.i("clearHashMapInJarCache", "Removing static hashmap entry for " + url);
									try {
										final JarFile jarFile = e.getValue();
										jarFile.close();
										iterator.remove();
									} catch (Exception f) {
										Log.e("clearHashMapInJarCache", "Error removing hashmap entry for " + url, f);
									}
								}
							}
						} catch (Exception e) {
							// ignored
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

}