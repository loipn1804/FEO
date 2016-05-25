package com.fareastorchid.connection;

import android.util.Log;

import com.fareastorchid.business.ERROR;
import com.fareastorchid.business.ErrorMessage;
import com.fareastorchid.data.GlobalData;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Request {
    public static final String TAG = "Request";
    public static final int AUTO_RETRY_DEFAULT = 3;
    private int maxValueAutoRetry = AUTO_RETRY_DEFAULT;
    public static final int POST = 4;
    public int type = POST;
    public static int CONNECTION_TIMEOUT = 60000;
    public static int SOCKET_TIMEOUT = 60000;
    public static int LONG_CONNECTION_TIMEOUT = 90 * 1000;
    public static int LONG_SOCKET_TIMEOUT = 90 * 1000;
    public static int SOCKET_BUFFER_SIZE = 8192;
    private static int numOfRequests = 0;
    // private static HttpClient httpClient;
    // private static HttpClient longConnectionHttpClient;
    private static HttpParams httpParameters;
    private static boolean REUSE_CLIENT = false;
    private static DefaultHttpClient client;
    private static SocketFactory ssf;
    public String url;
    public List<NameValuePair> nameValuePairs;
    public int id;
    public String root;
    String methodName;
    String[] params;
    String[] values;
    private boolean enableAutoRetryNetwork = false;
    private int numberReAddtoQueue = 0;
    private int retryCount = 0;
    private boolean isTopPriority = false;
    private List<NameValuePair> mParams = new ArrayList<NameValuePair>();
    private RequestListener requestListener = null;
    static {
        httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(httpParameters, SOCKET_BUFFER_SIZE);
        HttpConnectionParams.setTcpNoDelay(httpParameters, true);
        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
    }

    public Request(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    private static DefaultHttpClient getClient() {

        if (client == null || !REUSE_CLIENT) {

            HttpParams httpParams = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SOCKET_TIMEOUT);
            HttpConnectionParams.setTcpNoDelay(httpParameters, true);

            HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
            ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(25));
            HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", ssf == null ? SSLSocketFactory.getSocketFactory() : ssf, 443));

            DefaultHttpClient cil = new DefaultHttpClient();
            ClientConnectionManager mgr = cil.getConnectionManager();
            ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, mgr.getSchemeRegistry());
            client = new DefaultHttpClient(cm, httpParams);

        }
        return client;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public boolean isTopPriority() {
        return isTopPriority;
    }

    public void setTopPriority(boolean isTopPriority) {
        this.isTopPriority = isTopPriority;
    }

    public void addParams(String root, String methodName, String[] params, String[] values) {
        this.root = root;
        this.methodName = methodName;
        this.params = params;
        this.values = values;
    }
    // private static HttpParams httpLongConnectionParameters;

    public void addParams(String url, String[] params, String[] values) {
        this.url = url;
        this.params = params;
        this.values = values;

        int len = params.length;
        for (int i = 0; i < len; ++i) {
            mParams.add(new BasicNameValuePair(params[i], values[i]));
        }
    }

    public void addParams(String url) {
        this.url = url;
    }

    public void setAutoRetry(boolean retry) {
        enableAutoRetryNetwork = retry;
    }

    public void setAutoRetry(boolean retry, int count) {
        this.enableAutoRetryNetwork = retry;
        // this.retryCount = count;
        this.maxValueAutoRetry = count;
    }

    public boolean equals(Object o) {
        Request other = (Request) o;
        return url.equals(other.url);
    }

    public void cancelRequest() {
        if (this.requestListener != null) {
            this.requestListener.onRequestFailed(new ErrorMessage(ERROR.UNKNOWN, ERROR.UNKNOWN_MSG));
        }
    }

    public void send() {
        this.id = ++numOfRequests;
        post();

    }

    public void processLabanApiReponse(JSONObject json) throws JSONException {
        JSONObject object = json.getJSONObject("root");
        String errorCode = object.getString("status");
        String message = object.getString("message");
        if (GlobalData.LOG_REQUEST)
            Log.d(TAG, "error code = " + errorCode);
        if (!errorCode.equalsIgnoreCase("success")) {

            processErrorCode(1, message);

        } else {
            requestListener.onRequestComplete(json);
        }
    }

    private void processErrorCode(int errorCode, String message) {
        if (errorCode == ERROR.SESSION_EXPIRE) // session key errors
        {
            if (GlobalData.LOG_REQUEST)
                Log.e(TAG, "Session key error");

            {
                // RequestBackgroundWorker.queueRequestFirst(Request.this);

                if (numberReAddtoQueue < maxValueAutoRetry) {
                    numberReAddtoQueue++;

                    if (GlobalData.debugViewLogApp) {
                        // Utils.showMess("reAdd to queue");
                    }

                    if (GlobalData.LOG_REQUEST)
                        Log.i(TAG, "Retry request " + id);

                    RequestBackgroundWorker.queueRequestFirst(Request.this);
                    return;
                } else {
                    numberReAddtoQueue = 0;
                }
            }
        }

        requestListener.onRequestFailed(new ErrorMessage(errorCode, message));
    }

    private void post() {
        HttpClient httpClientPost = getClient();

        if (GlobalData.LOG_REQUEST)
            Log.d(TAG, "Start request " + id);
        if (GlobalData.LOG_REQUEST)
            Log.d(TAG, "URL = " + url);

        synchronized (httpClientPost) {

            HttpPost httpPost = new HttpPost(url);
            httpPost.setParams(httpParameters);

            HttpResponse response = null;
            HttpEntity entity = null;
            String jsonData = null;
            InputStream instream = null;

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(mParams, "UTF-8"));
                
                response = httpClientPost.execute(httpPost);
                entity = response.getEntity();

                if (entity != null) {
                    instream = entity.getContent();
                    // instream = new GZIPInputStream(instream);
                    jsonData = convertStreamToString(instream);

                    JSONObject json = new JSONObject(jsonData);
                    if (GlobalData.LOG_REQUEST)
                        Log.d(TAG, "Response JSON of request " + id + " = " + jsonData);

                    // process response data
                    processLabanApiReponse(json);

                } else {
                    if (requestListener != null)
                        requestListener.onRequestFailed(new ErrorMessage(ERROR.UNKNOWN, ERROR.UNKNOWN_MSG));
                }
            } catch (JSONException e) {
                doRetry(e);
                e.printStackTrace();

            } catch (ClientProtocolException e) {
                doRetry(e);
                e.printStackTrace();

            } catch (IOException e) {
                doRetry(e);
                e.printStackTrace();

            } catch (Exception e) {
                doRetry(e);
                e.printStackTrace();

            } finally {
                if (!REUSE_CLIENT) {
                    httpClientPost.getConnectionManager().shutdown();
                    httpClientPost = null;
                }

            }
        }
    }

    private void doRetry(Exception e) {
        if (enableAutoRetryNetwork) {
            if (retryCount < maxValueAutoRetry) {
                retryCount++;
                if (GlobalData.debugViewLogApp)
                    RequestBackgroundWorker.queueRequest(Request.this);
            } else {
                if (requestListener != null)
                    requestListener.onRequestFailed(new ErrorMessage(ERROR.UNKNOWN, ""));
            }

        } else {
            if (requestListener != null)
                requestListener.onRequestFailed(new ErrorMessage(ERROR.UNKNOWN, ""));
        }
    }

    public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        public MySSLSocketFactory(SSLContext context) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
            super(null);
            sslContext = context;
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
