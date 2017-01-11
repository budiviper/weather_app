package weather.budi.com.weatherapps.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import weather.budi.com.weatherapps.utils.Constants;
import weather.budi.com.weatherapps.utils.StringUtils;

public class StringCookieRequest extends StringRequest {
    Context context;

    /**
     * Constantsructs a JsonCookieRequest object.
     *
     * @param method        the HTTP method to use, see {@link Method Request.Method}, use GET or POST respectively
     * @param url           to fetch the JSON from
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors.
     * @param context       Application context, for saving cookies
     */

    public StringCookieRequest(int method, String url,
                               Listener<String> listener, ErrorListener errorListener,
                               Context context) {
        super(method, url, listener, errorListener);
        this.context = context;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        Map<String, String> headers = response.headers;
        String cookie = headers.get("Set-Cookie");
        if (!StringUtils.isEmpty(cookie)) {
            if (!Constants.COOKIE.equals(cookie) || StringUtils.isEmpty(Constants.COOKIE))
                Constants.COOKIE = cookie;
        }

        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();

        if (!StringUtils.isEmpty(Constants.COOKIE))
            headers.put("Cookie", Constants.COOKIE);

        return headers;
    }
}