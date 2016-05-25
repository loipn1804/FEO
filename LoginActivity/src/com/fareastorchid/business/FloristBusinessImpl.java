package com.fareastorchid.business;

import com.fareastorchid.ContactUsActivity;
import com.fareastorchid.DeliveryAddressActivity;
import com.fareastorchid.FlowerDetailActivity;
import com.fareastorchid.ForecastActivity;
import com.fareastorchid.ForecastByCountryActivity;
import com.fareastorchid.LoginActivity;
import com.fareastorchid.MainActivity;
import com.fareastorchid.MyAccountActivity;
import com.fareastorchid.OrderHistoryActivity;
import com.fareastorchid.OrderHistoryDetailActivity;
import com.fareastorchid.R;
import com.fareastorchid.ReserveActivity;
import com.fareastorchid.SearchActivity;
import com.fareastorchid.connection.Request;
import com.fareastorchid.connection.RequestBackgroundWorker;
import com.fareastorchid.connection.RequestListener;
import com.fareastorchid.connection.UrlHelper;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.util.Connectivity;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.pierry.simpletoast.SimpleToast;

public class FloristBusinessImpl implements FloristBusiness {
	private FloristBusinessListener listener;

	public static FloristBusinessImpl getInstance() {
		return new FloristBusinessImpl();
	}

	public FloristBusinessListener getBusinessListener() {
		return this.listener;
	}

	public void setBusinessListener(FloristBusinessListener businessListener) {
		this.listener = businessListener;
	}

	public void removeListener() {
		this.listener = null;
	}

	@Override
	public void getAllBanner() {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class" }, new String[] { "banner_GetAll", "Banner" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MainActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void getAllMainCat() {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class" }, new String[] { "mainCat_GetAll", "MainCat" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MainActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void getItemFeoByMainCate(int mainCatId, int cateTypeId,
			String sortBy, int pageIndex) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);
				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(
				UrlHelper.FLORIST_API_DOMAIN,
				new String[] { "task", "class", "mainCatId", "cateTypeId",
						"sortBy", "pageIndex" },
				new String[] { "itemFeo_GetByMainCat", "ItemFeo",
						mainCatId + "", cateTypeId + "", sortBy, pageIndex + "" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MainActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void getItemFeoByItemId(int itemId) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);
				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "itemId" }, new String[] { "itemFeo_GetItemById",
				"ItemFeo", itemId + "" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(FlowerDetailActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void userFeoLogin(String userName, String password) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "username", "password" }, new String[] {
				"userFeo_Login", "UserFeo", userName, password });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(LoginActivity.class))) {
			RequestBackgroundWorker.queueRequestFirst(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void userFeoChangePW(String userName, String oldPW, String newPW) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "username", "password", "newPassword" }, new String[] {
				"userFeo_ChangePassword", "UserFeo", userName, oldPW, newPW });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MyAccountActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void userFeoResetPWCode(String email) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "email" }, new String[] {
				"userFeo_GenerateResetPwdCode", "UserFeo", email });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(LoginActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void searchItemByName(String searchType, String itemName,
			int pageIndex) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);
				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "fieldName", "fieldValue", "pageIndex" },
				new String[] { "itemFeo_SearchItemByName", "ItemFeo",
						searchType, itemName, pageIndex + "" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(SearchActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void getAllCountry() {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class" }, new String[] { "country_GetAllCountry", "Country" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MainActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}

	}

	@Override
	public void getAllColor() {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class" }, new String[] { "color_GetAllColor", "Color" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MainActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void getItemFeoByColorId(int colorId, String sortBy, int pageIndex) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);
				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "colorId", "sortBy", "pageIndex" }, new String[] {
				"itemFeo_GetByColorId", "ItemFeo", colorId + "", sortBy,
				pageIndex + "" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MainActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}

	}

	@Override
	public void getItemFeoByCountryId(int countryId, String sortBy,
			int pageIndex) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);
				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "countryId", "sortBy", "pageIndex" }, new String[] {
				"itemFeo_GetByCountryId", "ItemFeo", countryId + "", sortBy,
				pageIndex + "" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MainActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void pushOrderItems(JSONArray array, String userId,
			String guestName, String guestPhone, String guestPhone2,
			String guestAddress, String remark, String deliveryAddress,
			String deliveryTime) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);
				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "itemsData", "userId", "guest_name", "guest_phone",
				"guest_phone2", "guest_email", "remark", "delivery_address",
				"delivery_time" }, new String[] { "trans_OrderItems",
				"Transaction", array.toString(), userId, guestName, guestPhone,
				guestPhone2, guestAddress, remark, deliveryAddress,
				deliveryTime });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(ReserveActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}

	}

	@Override
	public void getOrderHistory(String userId, int pageIndex) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "userId", "pageIndex" },
				new String[] { "trans_GetOrderHistory", "Transaction", userId,
						pageIndex + "" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(OrderHistoryActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void getOrderHistoryDetail(String transId) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "id_transaction" }, new String[] {
				"trans_GetOrderDetail", "Transaction", transId });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(OrderHistoryDetailActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}

	}

	@Override
	public void userFeoUpdateProfile(String id_user, String fullname,
			String email, String contactNumber, String billing_address,
			String shopName) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "id_user", "email", "contact_number",
				"billing_address", "shop_name", "full_name" }, new String[] {
				"userFeo_UpdateProfile", "UserFeo", id_user, email,
				contactNumber, billing_address, shopName, fullname });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MyAccountActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void getForecastList() {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class" }, new String[] { "fore_GetForecastList", "Forecast" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(ForecastActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void getForecastListDetail(String country_code, String date_arrival) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "country_code", "date_arrival" }, new String[] {
				"fore_GetForecastDetailList", "Forecast", country_code,
				date_arrival });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(ForecastByCountryActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void getDeliveryAddress(String user_id) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "id_user" }, new String[] {
				"userFeo_GetDeliveryAddress", "UserFeo", user_id });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(DeliveryAddressActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}

	}

	@Override
	public void addDeliveryAddress(String user_id, String address) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "id_user", "address" }, new String[] {
				"userFeo_AddDeliveryAddress", "UserFeo", user_id, address });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(DeliveryAddressActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}

	}

	@Override
	public void updateDeliveryAddress(String delivery_id, String user_id,
			String address) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "id_deliadr", "id_user", "address" }, new String[] {
				"userFeo_UpdateDeliveryAddress", "UserFeo", delivery_id,
				user_id, address });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(DeliveryAddressActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}

	}

	@Override
	public void getReportByCatId(String idSubcat, String priceListType) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_REPORT, new String[] {
				"id_subcat", "priceListType" }, new String[] { idSubcat,
				priceListType });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MainActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void sendContact(String name, String email, String contact_no,
			String address, String pos_code, String subject, String order_no,
			String message) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "name", "email", "contact_no", "address",
				"postal_code", "subject", "order_no", "message" },
				new String[] { "cont_SendRequest", "Contact", name, email,
						contact_no, address, pos_code, subject, order_no,
						message });
		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(ContactUsActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}

	}

	@Override
	public void getByMainCat(int mainCatId) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "mainCatId" }, new String[] { "catType_GetByMainCat",
				"CatType", mainCatId + "" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MainActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void sendRegister(String name, String contactDetail) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(LoginActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "name", "contact_detail" }, new String[] {
				"cont_SendContactDetail", "Contact", name, contactDetail });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(LoginActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}

	}

	@Override
	public void getPriceListSubCate(String priceListType) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "priceListType" }, new String[] {
				"itemFeo_GetPriceListSubcat", "ItemFeo", priceListType });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MainActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}

	}

	@Override
	public void deleteDeliveryAddress(String delivery_id, String user_id) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "id_deliadr", "id_user" }, new String[] {
				"userFeo_DeleteDeliveryAddress", "UserFeo", delivery_id,
				user_id });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(DeliveryAddressActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}

	}

	@Override
	public void updateOutletAddress(String id, String address, String name,
			String contact) {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "id_outlet", "outlet_name", "outlet_address",
				"outlet_contact" }, new String[] { "update_outlet", "Outlet",
				id, name, address, contact });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(DeliveryAddressActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}

	}

	@Override
	public void updateOrderHistoryRemark(String transId, String remark) {
		// TODO Auto-generated method stub
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "id_transaction", "remark" }, new String[] {
				"trans_UpdateTransRemark", "Transaction", transId, remark });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(OrderHistoryDetailActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}

	@Override
	public void getLastVersion() {
		listener.onPreProcessed();
		Request request = new Request(new RequestListener() {
			public void onRequestFailed(ErrorMessage error_message) {
				listener.onErrorData(error_message);

				if (error_message != null
						&& error_message.getError_code() == ERROR.NETWORK_ERROR) {
					SimpleToast.error(
							Controller.getActInstance(MainActivity.class),
							R.string.no_internet_connection);
				}
			}

			public void onRequestComplete(JSONObject object) {
				listener.onDataProcessed(object);
			}
		});

		request.type = Request.POST;
		request.addParams(UrlHelper.FLORIST_API_DOMAIN, new String[] { "task",
				"class", "device" }, new String[] { "version_GetLast",
				"Version", "android" });

		if (Connectivity.isNetworkAvailable(true,
				Controller.getActInstance(MainActivity.class))) {
			RequestBackgroundWorker.queueRequest(request);
		} else {
			listener.onErrorData(new ErrorMessage(ERROR.NETWORK_ERROR,
					ERROR.NETWORK_ERROR_MSG));
		}
	}
}
