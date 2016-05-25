package com.fareastorchid.listener;

import com.fareastorchid.model.CateType;
import com.fareastorchid.model.MainCat;
import com.fareastorchid.model.PriceListSubCate;

public interface OnFloristListener {
    public void onChooseCatType(MainCat mainCat);

    public void onChooseSortBy(String sort);

    public void onChooseSubCat(CateType catType);

    public void onChoosePriceList(String type);

    public void onChooseSubPriceList(PriceListSubCate subCate);
}