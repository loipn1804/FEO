package com.fareastorchid.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.pierry.simpletoast.SimpleToast;

import com.fareastorchid.MainActivity;
import com.fareastorchid.R;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.business.FloristBusiness;
import com.fareastorchid.business.FloristBusinessImpl;
import com.fareastorchid.business.FloristBusinessListener;
import com.fareastorchid.data.GlobalData;
import com.fareastorchid.listener.OnAddressListener;
import com.fareastorchid.listener.OnSpecialRequestListener;
import com.fareastorchid.model.DelAddress;
import com.fareastorchid.model.controller.Controller;
import com.fareastorchid.util.PQT;

import java.util.List;

public class DeliveryAddressAdapter extends BaseAdapter {
	
	public interface UpdateOutletAddressCbk {
		public void updateOutletAddress(String id, String address,
				String name, String contact);
	}
	private UpdateOutletAddressCbk addressCbk;
	
	public LayoutInflater inflater;
	private List<DelAddress> listDelAddresss;
	private OnAddressListener listener;
	private SharedPreferences sharedpreferences;
	private boolean isFormMyAccount;
	private Activity activity;

	

	public DeliveryAddressAdapter(Activity context, boolean isFormMyAccount, UpdateOutletAddressCbk addressCbk) {
		super();
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listener = (OnAddressListener) context;
		sharedpreferences = context.getSharedPreferences(
				GlobalData.UserPREFERENCES, Context.MODE_PRIVATE);
		this.isFormMyAccount = isFormMyAccount;
		this.activity = context;
		this.addressCbk = addressCbk;
	}

	public List<DelAddress> getListDelAddress() {
		return listDelAddresss;
	}

	public void setListDelAddresss(List<DelAddress> listDelAddresss) {
		this.listDelAddresss = listDelAddresss;
	}

	@Override
	public int getCount() {
		if (listDelAddresss != null)
			return listDelAddresss.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (listDelAddresss != null)
			return listDelAddresss.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		try {

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(
						R.layout.view_delivery_address_item, null);
				holder.item = (RelativeLayout) convertView
						.findViewById(R.id.layout_current_address);
				holder.tvDetail = (TextView) convertView
						.findViewById(R.id.delivery_detail);
				holder.ivEdit = (ImageView) convertView
						.findViewById(R.id.delivery_edit);
				holder.ivCheck = (ImageView) convertView
						.findViewById(R.id.delivery_checkbox);
				holder.tvAddress = (TextView) convertView
						.findViewById(R.id.delivery_address);
				holder.delivery_contact = (TextView) convertView
						.findViewById(R.id.delivery_contact);
				holder.ivDel = (ImageView) convertView
						.findViewById(R.id.delivery_address_del);
				holder.imv111 = (ImageView) convertView
						.findViewById(R.id.imv111);
				holder.imv222 = (ImageView) convertView
						.findViewById(R.id.imv222);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (listDelAddresss != null && position >= 0
					&& position < listDelAddresss.size()) {
				final DelAddress address = listDelAddresss.get(position);
				if (address != null && position < listDelAddresss.size()) {
					if (!address.getAddress().equalsIgnoreCase("")) {
						holder.tvDetail.setText(address.getAddress());
					}
					if (address.isState()) {
						holder.ivCheck
								.setBackgroundResource(R.drawable.del_checked);
					} else {
						holder.ivCheck
								.setBackgroundResource(R.drawable.del_check);
					}
				}

				// if (GlobalData.isLogin) {
				// holder.tvAddress.setText(sharedpreferences.getString(GlobalData.ShopName,
				// ""));
				// }
				holder.tvAddress.setText(address.getUserId());
				holder.delivery_contact.setText(address.getContact());

				holder.item.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// GlobalData.chosenAddress = address.getAddress();
						// resetState();
						// address.setState(true);
						// notifyDataSetChanged();
						if (!isFormMyAccount) {
							GlobalData.chosenAddress = address.getAddress();
							for (int i = 0; i < GlobalData.listOrderItem.size(); i++) {
								// if (chkTerm.isChecked()) {
								GlobalData.listOrderItem.get(i).setAddress(
										GlobalData.chosenAddress);
								// } else if (i == GlobalData.editPos) {
								// GlobalData.listOrderItem.get(i).setAddress(GlobalData.chosenAddress);
								// break;
								// }
							}
							activity.finish();
						} else {
							addressCbk.updateOutletAddress(address.getId(), address.getAddress(), address.getUserId(), address.getContact());
						}
					}
				});

				if (GlobalData.isLogin) {
					holder.ivEdit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							GlobalData.edtAddress = address;
							if (listener != null) {
								listener.editAddress();
							}
						}
					});
					if (position > 0) {
						holder.ivDel.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (listener != null) {
									listener.deleteAddress(address.getId());
								}
							}
						});
					} else {
						holder.ivDel.setVisibility(View.INVISIBLE);
					}
				} else {
					holder.ivEdit.setVisibility(View.INVISIBLE);
					holder.ivDel.setVisibility(View.INVISIBLE);
				}
				holder.ivEdit.setVisibility(View.INVISIBLE);
				holder.ivDel.setVisibility(View.INVISIBLE);
			}

			if (!isFormMyAccount) {
				if (position == 0) {
					holder.delivery_contact.setVisibility(View.INVISIBLE);
					holder.imv222.setVisibility(View.INVISIBLE);
				} else {
					holder.delivery_contact.setVisibility(View.VISIBLE);
					holder.imv222.setVisibility(View.VISIBLE);
				}
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	private void resetState() {
		for (int i = 0; i < listDelAddresss.size(); i++) {
			DelAddress address = listDelAddresss.get(i);
			address.setState(false);
		}
	}

	public static class ViewHolder {
		public RelativeLayout item;
		public ImageView ivCheck;
		public TextView tvDetail, tvAddress, delivery_contact;
		public ImageView ivEdit;
		public ImageView ivDel;
		public ImageView imv111;
		public ImageView imv222;
	}
	
	
	
	
}
