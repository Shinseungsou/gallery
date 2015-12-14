package com.jfsiot.hsgallery.util.dialog;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.model.ImageData;
import com.jfsiot.hsgallery.app.model.UseLog;
import com.jfsiot.hsgallery.util.data.db.UseLogManager;
import com.jfsiot.hsgallery.util.data.image.ImageShow;

import java.util.List;

import timber.log.Timber;

/**
 * Created by SSS on 2015-12-14.
 */
public class ShareDialog {

    private static MaterialDialog shareDialog;

    public static MaterialDialog build(Context context, List<ImageData> images){
        String[] shareListOnly = {context.getString(R.string.share_kakao), context.getString(R.string.share_facebook), context.getString(R.string.share_instagram)};
        String[] shareListSome = {context.getString(R.string.share_kakao), context.getString(R.string.share_facebook)};
        Timber.d("share : %s %s %s", images.size(), images.size() > 1, images.size() > 1 ? shareListSome.length : shareListOnly.length);

        shareDialog = new MaterialDialog.Builder(context)
                .title(R.string.dialog_title_share)
                .content(R.string.dialog_content_share)
                .negativeText(R.string.cancel_normal)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                })
                .items(images.size() > 1 ? shareListSome : shareListOnly)
                .itemsCallback((dialog, itemView, which, text) -> {
                    switch (which) {
                        case 0:
                            ImageShow.getInstance().sendKaKao(context, images);
                            UseLogManager.getInstance().addLogList(images, new String(), UseLog.getShareString(UseLog.Share.KAKAO));
                            break;
                        case 1:
                            ImageShow.getInstance().sendFacebook(context, images);
                            UseLogManager.getInstance().addLogList(images, new String(), UseLog.getShareString(UseLog.Share.FACEBOOK));
                            break;
                        case 2:
                            ImageShow.getInstance().sendInstagram(context, images.get(0));
                            UseLogManager.getInstance().addLogList(images, new String(), UseLog.getShareString(UseLog.Share.INSTAGRAM));
                            break;
                        default:
                            break;
                    }
                })
                .build();
        return shareDialog;
    }
    public static void show(){
        if(shareDialog != null)
            shareDialog.show();
    }
}
