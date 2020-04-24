package com.shumeipai.mobilecontroller.model;

import java.util.ArrayList;

public class CMarkerData implements Cloneable{

    public CMarkerData(double lat, double lng) {
        this.poiLat = lat;
        this.poiLng = lng;
    }

    public CMarkerData() {}
    /**
     * businessName : 科丰路加油站，商家名称
     * currentPrice : 5.66 当前价格
     * discount : 当前价格-指导价
     * discountId ?
     * distance : 0 距离当前多少米
     * guidePrice : 6.67 指导价格
     * icon : http://www.baidu.com/images.jpg 图标
     * infoLabel : 加油站 标签
     * labelColor : 123 标签颜色
     * occupyUserHeadImage : 占领人员头像列表
     * poiCode : 1 加油站 2 红包
     * poiGoodsName : #92 商品名称
     * poiId :
     * poiLat : 经度
     * poiLng : 纬度
     * poiTitle : 0.5 标题
     * poiTypeCode : 1
     * subtitle : 同城123人在附近瓜分福利
     */

    public int numberIndex;
    public String businessName;
    public double currentPrice;
    public double discount;
    public long discountId;
    public int distance;
    public String goodsNameColor;
    public double guidePrice;
    public String icon; //
    public String infoLabel;
    public String labelColor;
    public String mapIcon;//
    public ArrayList<String> occupyUserHeadImage;
    public int poiCode;//2 加油站 1 红包
    public String poiGoodsName;
    public String poiId;
    public double poiLat;
    public double poiLng;
    public String  poiTitle;
    public int poiTypeCode;
    public String subtitle;

    public boolean selected = false;

    public int templateCode; //  1 红包 2 加油站 ,ui 模板
    public int iconCode;//poi小图标类型  会从接口获取
    public String buttonText; //按钮文案
    public long poiTypeId;//商家，福袋，加油站 类型
    public String company;//单位

    public int getNumberIndex() {
        return numberIndex;
    }

    public void setNumberIndex(int numberIndex) {
        this.numberIndex = numberIndex;
    }

    public String getGoodsNameColor(){
        return goodsNameColor;
    }

    public void setGoodsNameColor(String goodsNameColor){
        this.goodsNameColor = goodsNameColor;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public long getPoiTypeId() {
        return poiTypeId;
    }

    public void setPoiTypeId(long poiTypeId) {
        this.poiTypeId = poiTypeId;
    }

    public int getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(int templateCode) {
        this.templateCode = templateCode;
    }

    public int getIconCode() {
        return iconCode;
    }

    public void setIconCode(int iconCode) {
        this.iconCode = iconCode;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public double getDiscount(){
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(long discountId) {
        this.discountId = discountId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getGuidePrice() {
        return guidePrice;
    }

    public void setGuidePrice(double guidePrice) {
        this.guidePrice = guidePrice;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getInfoLabel() {
        return infoLabel;
    }

    public void setInfoLabel(String infoLabel) {
        this.infoLabel = infoLabel;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public ArrayList<String> getOccupyUserHeadImage() {
        return occupyUserHeadImage;
    }

    public void setOccupyUserHeadImage(ArrayList<String> list) {
        this.occupyUserHeadImage = list;
    }

    public int getPoiCode() {
        return poiCode;
    }

    public void setPoiCode(int poiCode) {
        this.poiCode = poiCode;
    }

    public String getPoiGoodsName() {
        return poiGoodsName;
    }

    public void setPoiGoodsName(String poiGoodsName) {
        this.poiGoodsName = poiGoodsName;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public double getPoiLat() {
        return poiLat;
    }

    public void setPoiLat(double poiLat) {
        this.poiLat = poiLat;
    }

    public double getPoiLng() {
        return poiLng;
    }

    public void setPoiLng(double poiLng) {
        this.poiLng = poiLng;
    }

    public String getPoiTitle() {
        return poiTitle;
    }

    public void setPoiTitle(String poiTitle) {
        this.poiTitle = poiTitle;
    }

    public int getPoiTypeCode() {
        return poiTypeCode;
    }

    public void setPoiTypeCode(int poiTypeCode) {
        this.poiTypeCode = poiTypeCode;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getMapIcon() {
        return mapIcon;
    }

    public void setMapIcon(String mapIcon) {
        this.mapIcon = mapIcon;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public CMarkerData clone(){
        try {
            return (CMarkerData) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }
}
