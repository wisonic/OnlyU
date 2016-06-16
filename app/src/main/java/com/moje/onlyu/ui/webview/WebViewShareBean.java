//package com.moje.onlyu.ui.webview;
//
//import android.app.Activity;
//import android.support.v4.app.FragmentManager;
//import android.text.TextUtils;
//
//import com.alibaba.fastjson.JSON;
//import com.manyi.lovehouse.R;
//import com.manyi.lovehouse.common.logCollect.EventLog;
//import com.manyi.lovehouse.common.share.ShareUtil;
//import com.manyi.lovehouse.common.utils.ToastUtil;
//import com.manyi.lovehouse.ui.dialog.FragmentHelper;
//import com.manyi.lovehouse.ui.message.MessageDetailShareDialog;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.bean.SocializeEntity;
//import com.umeng.socialize.bean.StatusCode;
//import com.umeng.socialize.controller.listener.SocializeListeners;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.weixin.media.CircleShareContent;
//import com.umeng.socialize.weixin.media.WeiXinShareContent;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class WebViewShareBean implements MessageDetailShareDialog.ShareToWhereListener {
//
//    public static final String SHARE_IMAGE_URL = "share_image";
//    public static final String SHARE_IMG_RES_ID = "share_img_res_id";
//    public static final String SHARE_TITLE = "share_title";
//    public static final String SHARE_CONTENT = "share_content";
//    public static final String SHOWSHARE_DIRECT = "showshare_direct";
//
//    private String shareUrl;
//    private String shareImageUrl;
//    private int shareIcon;
//    private String shareTitle;
//    private String shareContent;
//
//    private int defaultIconShare = R.drawable.app_icon_for_share;
//
//    protected FragmentHelper mFragmentHelper;
//    private MessageDetailShareDialog mShareDialog;
//    private Activity context;
//
//    public WebViewShareBean(Activity context) {
//        this.context = context;
//        init();
//    }
//
//    private String getShareUrl() {
//        if (null != shareUrl) {
//            if (shareUrl.startsWith("http")) {
//                return shareUrl;
//            } else {
//                return "http://" + shareUrl;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public void shareToWechat() {
//        WeiXinShareContent content = new WeiXinShareContent();
//        UMImage shareImage;
//        content.setShareContent(getShareContent());
//        content.setTitle(getShareTitle());
//        content.setTargetUrl(getShareUrl());
//        if (!TextUtils.isEmpty(getShareImageUrl())) {
//            shareImage = new UMImage(context, getShareImageUrl());
//        } else {
//            if (shareIcon == 0) {
//                shareImage = new UMImage(context, defaultIconShare);
//            } else {
//                shareImage = new UMImage(context, shareIcon);
//            }
//        }
//        content.setShareMedia(shareImage);
//        ShareUtil.getInstance(context).shareToWeiXin(content, mShareCallBackListener);
//
//        upEventLog("weixin");
//
//    }
//
//    @Override
//    public void shareToWechatCircle() {
//        CircleShareContent content = new CircleShareContent();
//        UMImage shareImage;
//        content.setShareContent(getShareContent());
//        content.setTitle(getShareTitle());
//        content.setTargetUrl(getShareUrl());
//        if (!TextUtils.isEmpty(getShareImageUrl())) {
//            shareImage = new UMImage(context, getShareImageUrl());
//        } else {
//            if (shareIcon == 0) {
//                shareImage = new UMImage(context, defaultIconShare);
//            } else {
//                shareImage = new UMImage(context, shareIcon);
//            }
//        }
//        content.setShareMedia(shareImage);
//        ShareUtil.getInstance(context).shareToWeiXinCircle(content, mShareCallBackListener);
//
//        upEventLog("friend");
//    }
//
//    @Override
//    public void cancel() {
//        upEventLog("close");
//    }
//
//    private SocializeListeners.SnsPostListener mShareCallBackListener = new SocializeListeners
//            .SnsPostListener() {
//        @Override
//        public void onStart() {
//            showToastMesg(context.getResources().getString(R.string.house_share_start));
//        }
//
//        @Override
//        public void onComplete(SHARE_MEDIA share_media, int eCode, SocializeEntity
//                socializeEntity) {
//            if (eCode == StatusCode.ST_CODE_SUCCESSED) {
//                showToastMesg(context.getResources().getString(R.string.house_share_sucess));
//            } else {
//                showToastMesg(context.getResources().getString(R.string.house_share_fail));
//            }
//        }
//    };
//
//    public void showShareDialog(FragmentManager fragmentManager) {
//        if (mFragmentHelper == null) {
//            mFragmentHelper = new FragmentHelper(fragmentManager);
//        }
//        if (mShareDialog == null) {
//            mShareDialog = new MessageDetailShareDialog();
//        }
//        mShareDialog.setOnClickListener(this);
//        mFragmentHelper.showDialog(null, mShareDialog);
//
//        upEventLog("share");
//
//    }
//
//    private void upEventLog(String action){
//        Map map = new HashMap();
//        map.put("url",getShareUrl());
//        map.put("action",action);
//        EventLog.upEventLog(String.valueOf(626), JSON.toJSONString(map));
//    }
//
//    public void init(){
//        shareUrl = "";
//        shareImageUrl = "";
//        shareIcon = 0;
//        shareTitle = "";
//        shareContent = "";
//    }
//
//    public void showToastMesg(String msg) {
//        ToastUtil.show(context, msg);
//    }
//
//    public String getShareContent() {
//        return shareContent;
//    }
//
//    public String getShareTitle() {
//        return shareTitle;
//    }
//
//    public void setShareUrl(String shareUrl) {
//        this.shareUrl = shareUrl;
//    }
//
//    public String getShareImageUrl() {
//        return shareImageUrl;
//    }
//
//    public void setShareImageUrl(String shareImageUrl) {
//        this.shareImageUrl = shareImageUrl;
//    }
//
//    public int getShareIcon() {
//        return shareIcon;
//    }
//
//    public void setShareIcon(int shareIcon) {
//        this.shareIcon = shareIcon;
//    }
//
//    public void setShareTitle(String shareTitle) {
//        this.shareTitle = shareTitle;
//    }
//
//    public void setShareContent(String shareContent) {
//        this.shareContent = shareContent;
//    }
//
//}
//
