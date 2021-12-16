package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import helloworld.data.Forecast;
import helloworld.data.WeatherData;

public class WeatherHandler {

    public Forecast handle(WeatherData weatherData, Context context) {
        if (weatherData.getTemp() > 30 && weatherData.getHumidity() > 40) return Forecast.WARM;
        return Forecast.SUNNY;
    }

}
