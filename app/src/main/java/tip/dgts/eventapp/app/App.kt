package tip.dgts.eventapp.app

import android.app.Application

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.util.concurrent.TimeUnit

import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tip.dgts.eventapp.model.data.Token
import tip.dgts.eventapp.model.data.User
import tip.dgts.eventapp.model.response.LoginResponse


class App : Application() {



    override fun onCreate() {
        super.onCreate()
        instance = this
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfig)
    }



    companion object {

        @get:Synchronized
        lateinit var instance: App

        val user: User
            get() {
                val realm = Realm.getDefaultInstance()
                return realm.where(User::class.java).findFirst()!!
            }

        /*
        Realm realm = Realm.getDefaultInstance();
        Token token = new Token();
        LoginResponse loginResponse = realm.where(LoginResponse.class).findFirst();*/
        val token: Token
            get() {
                val token = Token()
                token.expiresIn = 0
                token.tokenKey = "none"
                token.tokenType = "none"

                return token
            }
    }


    val apiInterface: ApiInterface get() {
        return getClient().create(ApiInterface::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient.Builder {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY

        httpClient.connectTimeout(120, TimeUnit.SECONDS)
        httpClient.readTimeout(120, TimeUnit.SECONDS)
        httpClient.writeTimeout(120, TimeUnit.SECONDS)

        // add logging as last interceptor
        httpClient.addNetworkInterceptor(logging)
        httpClient.addInterceptor(logging)// <-- this is the important line!

        return httpClient
    }

    private fun getClient(): Retrofit {
        val retrofit: Retrofit

        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
        val url = Endpoints.API_URL
        retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getOkHttpClient().build())
                .build()

        return retrofit
    }

    //upload image

    /*
    private OkHttpClient.Builder getOkHttpClientImage() {
        if (httpClient == null) {
            // setup logs for debugging of http request
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient = new OkHttpClient.Builder();
            // add your other interceptors …

            httpClient.connectTimeout(120, TimeUnit.SECONDS);
            httpClient.readTimeout(120,TimeUnit.SECONDS);
            httpClient.writeTimeout(120,TimeUnit.SECONDS);

            // add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!
        }
        return httpClient;
    }

    private Retrofit getClientUploadImage() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();

            String url = Endpoints.IMAGE_UPLOAD;
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getOkHttpClientImage().build())
                    .build();
        }
        return retrofit;
    }
    public ApiInterface uploadImage() {
        if (apiInterface == null) {
            apiInterface = getClientUploadImage().create(ApiInterface.class);
        }
        return apiInterface;
    }*/


}
