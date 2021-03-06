package com.rks.musicx.misc.utils;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.palette.BitmapPalette;
import com.palette.GlidePalette;
import com.rks.musicx.R;
import com.rks.musicx.misc.widgets.BlurArtwork;
import com.rks.musicx.misc.widgets.CircleImageView;

import static com.rks.musicx.misc.utils.Constants.BlurView;

/*
 * Created by Coolalien on 6/28/2016.
 */

public class ArtworkUtils {


    private static ArtworkUtils sInstance;
    private Context mcontext;

    public ArtworkUtils(Context context) {
        this.mcontext = context;
    }

    public static void init(Context context) {
        sInstance = new ArtworkUtils(context);
    }

    public static ArtworkUtils getInstance() {
        return sInstance;
    }

    public static Uri uri(long key) {
        Uri albumCover = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(albumCover, key);
    }


    public static Bitmap getEmbedArtwork(String path) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        byte[] art;
        Bitmap bitmap = null;
        mediaMetadataRetriever.setDataSource(path);
        try {
            art = mediaMetadataRetriever.getEmbeddedPicture();
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
        } catch (Exception e) {
            Log.d("AlbumArtwork", "oops Error Buddy", e);
        }
        return bitmap;
    }

    public static void ArtworkLoader(Context context, String title, long key, CircleImageView imageView) {
        Glide.with(context)
                .load(uri(key))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(300, 300)
                .into(imageView);
    }

    public static void ArtworkLoader(Context context, String title, String path, CircleImageView imageView) {

        Glide.with(context)
                .load(path)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(300, 300)
                .into(imageView);
    }

    public static void ArtworkNetworkLoader(Context context, String title, String key, CircleImageView imageView) {

        Glide.with(context)
                .load(key)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(300, 300)
                .into(imageView);
    }

    public static void ArtworkLoader(Context context, String title, long key, ImageView imageView) {
        Glide.with(context)
                .load(uri(key))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(300, 300)
                .into(imageView);
    }

    public static void ArtworkLoaderPalette(Context context, String title, long key, ImageView imageView, palette palettework) {
        Glide.with(context)
                .load(uri(key))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(300, 300)
                .listener(GlidePalette.with(uri(key).toString()).intoCallBack(new BitmapPalette.CallBack() {
                    @Override
                    public void onPaletteLoaded(@Nullable Palette palette) {
                        palettework.palettework(palette);
                    }
                }))
                .into(imageView);
    }

    public static void ArtworkLoaderPalette(Context context, String title, String key, ImageView imageView, palette palettework) {
        Glide.with(context)
                .load(key)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(300, 300)
                .listener(GlidePalette.with(key).intoCallBack(new BitmapPalette.CallBack() {
                    @Override
                    public void onPaletteLoaded(@Nullable Palette palette) {
                        palettework.palettework(palette);
                    }
                }))
                .into(imageView);
    }

    public static void ArtworkLoaderBitmapPalette(Context context, String title, String key, palette palettework, bitmap bitmapwork) {
        Glide.with(context)
                .load(key)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(300, 300)
                .listener(GlidePalette.with(key).intoCallBack(new BitmapPalette.CallBack() {
                    @Override
                    public void onPaletteLoaded(@Nullable Palette palette) {
                        palettework.palettework(palette);
                    }
                }))
                .into(new Target<Bitmap>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        bitmapwork.bitmapfailed(drawableToBitmap(errorDrawable));
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        bitmapwork.bitmapwork(resource);
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {

                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(Request request) {

                    }

                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                });
    }

    public static void ArtworkLoaderBitmapPalette(Context context, String title, long key, palette palettework, bitmap bitmapwork) {
        Glide.with(context)
                .load(uri(key))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(300, 300)
                .listener(GlidePalette.with(uri(key).toString()).intoCallBack(new BitmapPalette.CallBack() {
                    @Override
                    public void onPaletteLoaded(@Nullable Palette palette) {
                        palettework.palettework(palette);
                    }
                }))
                .into(new Target<Bitmap>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        bitmapwork.bitmapfailed(drawableToBitmap(errorDrawable));
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        bitmapwork.bitmapwork(resource);
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {

                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {

                    }

                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void setRequest(Request request) {

                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                });
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private static AsyncTask<String, Void, String> getBlurArtwork(Context context, int radius, Bitmap bitmap, ImageView imageView) {

        return new BlurArtwork(context, radius, bitmap, imageView).execute("BlurredArtwork");
    }

    public static void blurPreferances(Context context, Bitmap blurBitmap, ImageView imageView) {
        String blurView = Extras.getInstance().getmPreferences().getString(BlurView, Constants.Zero);
        switch (blurView) {
            case Constants.Zero:
                getBlurArtwork(context, 5, blurBitmap, imageView);
                break;
            case Constants.One:
                getBlurArtwork(context, 10, blurBitmap, imageView);
                break;
            case Constants.Two:
                getBlurArtwork(context, 15, blurBitmap, imageView);
                break;
            case Constants.Three:
                getBlurArtwork(context, 20, blurBitmap, imageView);
                break;
            case Constants.Four:
                getBlurArtwork(context, 25, blurBitmap, imageView);
                break;
            default:
                getBlurArtwork(context, 5, blurBitmap, imageView);
        }
    }

    public static Bitmap getDefaultArtwork(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
    }
}

