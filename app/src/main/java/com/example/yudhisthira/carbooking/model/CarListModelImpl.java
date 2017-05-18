package com.example.yudhisthira.carbooking.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.yudhisthira.carbooking.data.Car;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * This is implementation class of ICarListModel
 */
public class CarListModelImpl implements ICarListModel {

    private static final String             CAR_LIST_URL = "http://www.sebastianf.net/intive/api/v1/cars.json";

    /**
     * List of Car object
     */
    private List<Car> mCarList = null;

    /**
     * Car list response callback object
     */
    private  ICarsResponseCallback mCallback;

    /**
     * @param callback ICarsResponseCallback object where response will be notify
     */
    @Override
    public void fetchCarsData(Context context, ICarsResponseCallback callback) {

        mCallback = callback;

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://www.sebastianf.net/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        MyCarListClient myCarListClient = retrofit.create(MyCarListClient.class);

        Call<List<Car>> carListCall = myCarListClient.fetchCarList();
        carListCall.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                List<Car> list = response.body();

                Collections.sort(list);

                mCallback.onSuccess(list);
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                mCallback.onFailure();
            }
        });

        //new MyAsyncTask(CAR_LIST_URL).execute();
    }

    @Override
    public void getCarDetails(String carID, ICarsResponseCallback callback) {

        mCallback = callback;

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://www.sebastianf.net/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        MyCarListClient myCarListClient = retrofit.create(MyCarListClient.class);

        Call<Car> call = myCarListClient.getCarDetails(carID);

        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {

                List<Car> list = (List<Car>) response.body();

                Collections.sort(list);

                mCallback.onSuccess(list);
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                mCallback.onFailure();
            }
        });

    }

    /**
     *
     * @return List of Car objects.
     *
     * This function will return stored List of Car
     */
    @Override
    public List<Car> getStoredData() {
        return mCarList;
    }


    class MyAsyncTask extends AsyncTask<Void, Void, List<Car>> {
        private String          mStrUrl = "";

        private boolean         mIsSuccess = false;

        public MyAsyncTask(String strUrl) {
            mStrUrl = strUrl;
        }

        @Override
        protected List<Car> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            List<Car> carList = null;

            try {
                URL url = new URL(mStrUrl);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                String strResponse = "[\n" +
                        "  {\n" +
                        "    \"id\" : 3,\n" +
                        "    \"name\" : \"The intive_Kupferwerk car\",\n" +
                        "    \"shortDescription\" : \"Transporter\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"id\" : 1,\n" +
                        "    \"name\" : \"intive_Kupferwerk 1\",\n" +
                        "    \"shortDescription\" : \"Limousine with 5 Seats\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"id\" : 2,\n" +
                        "    \"name\" : \"intive_Kupferwerk car 2\",\n" +
                        "    \"shortDescription\" : \"2 seated car\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"id\" : 4,\n" +
                        "    \"name\" : \"Kupferwerk vehicle\",\n" +
                        "    \"shortDescription\" : \"Limousine\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"id\" : 5,\n" +
                        "    \"name\" : \"Bicycle\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"id\" : 6,\n" +
                        "    \"name\" : \"Skateboard\",\n" +
                        "    \"shortDescription\" : \"With nice stickers on the skateboard to make it fancy\"\n" +
                        "  }\n" +
                        "]";

                //carList = parsResponse(buffer.toString());

                carList = parsResponse(strResponse);

                Collections.sort(carList);

                mIsSuccess = true;
            }
            catch (Exception e) {

            }

            return carList;
        }

        @Override
        protected void onPostExecute(List<Car> carList) {

            if(true == mIsSuccess) {
                mCallback.onSuccess(carList);
            }
            else {
                mCallback.onFailure();
            }
        }

        private List<Car> parsResponse(String strResponse) {
            List<Car> carList = new ArrayList<>();

            try {

                JSONArray jsonArray = new JSONArray(strResponse);
                int l = jsonArray.length();

                for (int i = 0 ; i < l ; ++i ) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Car car = new Car();

                    car.setCarName(parseString(jsonObject, "name"));
                    car.setCarShortDescription(parseString(jsonObject, "shortDescription"));
                    car.setCarID(parseInt(jsonObject, "id"));

                    carList.add(car);
                }

                Log.d("", "");

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return carList;

        }

        public String parseString(JSONObject jsonObject, String strKey) {
            String strValue = "";

            try {
                strValue = jsonObject.getString(strKey);
            }
            catch (Exception e) {

            }

            return strValue;
        }

        public int parseInt(JSONObject jsonObject, String strKey) {
            int value = -1;

            try {
                value = jsonObject.getInt(strKey);
            }
            catch (Exception e) {

            }

            return value;
        }
    }
}
