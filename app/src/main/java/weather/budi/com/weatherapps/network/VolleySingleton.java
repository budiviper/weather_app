package weather.budi.com.weatherapps.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;

import java.util.Date;

import weather.budi.com.weatherapps.utils.Constants;
import weather.budi.com.weatherapps.utils.SPref;
import weather.budi.com.weatherapps.utils.Utils;

public class VolleySingleton {
    private static final int cacheSize = 1024 * 1024 * 30;
    private static VolleySingleton mInstance = null;
    private static Context context;
    private static final int socketTimeout = 15000;//15 seconds - change to what you want
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private ImageLoader.ImageCache mImageCache;

    private VolleySingleton(Context context) {
        init(context);
    }

    public static Context getContext() {
        return context;
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    private void init(Context context) {
        this.context = context;
        mRequestQueue = getRequestQueue();
        mImageCache = new ImageLoader.ImageCache() {

            private final BitmapLruCache mCache = new BitmapLruCache(cacheSize);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        };

        mImageLoader = new ImageLoader(this.mRequestQueue, mImageCache);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), cacheSize);

            // CLEAR CACHE EVERY 1 MINUTE - BEGIN


            long defValue=0;

            long duration =  SPref.getPref(getContext(), Constants.SP_LAST_CACHE_TIME,defValue);

            Date d = new Date();
            long currDate = d.getTime();

            if ((currDate - duration >= 1*60*1000) || duration==0) {
                cache.clear();
                SPref.setPref(getContext(),Constants.SP_LAST_CACHE_TIME,currDate);
            }
            // CLEAR CACHE EVERY 1 MINUTE - END

            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);

            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return this.mRequestQueue;
    }

    public void addRequestQue(final int id, final String url, final Context ctx, Object tag, final VolleyResultListener listener) {
        listener.onStart(id);

        if(true==Constants.MODE_DEV)
            System.out.println("URL:" + url);


        try {
            StringCookieRequest reqQueue = Constants.currentRequestString = new StringCookieRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            try {
                                int jsonType = Utils.isJSONValid(s);
                                if (jsonType < 0) {
                                    listener.onSuccess(id, s);
                                } else {
                                    if (jsonType == 0) {
                                        listener.onSuccess(id, s);
                                    } else {
                                        listener.onSuccess(id, s);
                                    }
                                    listener.onFinish(id);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    listener.onError(-1, "");
                    StringBuilder sb = new StringBuilder();
                    if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                        sb.append("timeout");
                    } else if (volleyError instanceof AuthFailureError) {
                        sb.append("auth failure");
                    } else if (volleyError instanceof ServerError) {
                        sb.append("server error");
                    } else if (volleyError instanceof NetworkError) {
                        sb.append("network error");
                    } else if (volleyError instanceof ParseError) {
                        sb.append("parse error");
                    } else {
                        switch (volleyError.networkResponse.statusCode) {
                            case 500:
                                sb.append("Bad Response 500.");
                                break;
                            default:
                                sb.append("Network Error.");
                                break;
                        }
                    }
                    Toast.makeText(ctx, "error " + sb.toString(), Toast.LENGTH_LONG).show();
                }
            }, ctx);
            Constants.currentRequestString.setTag(tag);
            reqQueue.setTag(tag);

            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 3, 10.f);
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            reqQueue.setRetryPolicy(policy);
            mRequestQueue.add(reqQueue);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public ImageLoader getImageLoader() {
        return this.mImageLoader;
    }

    public ImageLoader.ImageCache getImageCache() {
        return this.mImageCache;
    }
}