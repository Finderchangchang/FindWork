package wai.findwork.method;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import wai.findwork.model.IPAddress;
import wai.findwork.model.Weather;

/**
 * Created by Administrator on 2016/11/21.
 */

public interface BaiDuAPI {

    @GET("iplookup.php?format=json")
    Observable<IPAddress> getIpAddress();

    @GET("weather_mini")
    Observable<Weather> getWeather(@Query("city") String city);
}
