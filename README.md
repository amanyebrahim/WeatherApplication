# WeatherApplication

App demonstrating Clean Architecture using Coroutines and Android Jetpack Components (MVVM , Binding, Live Data)

# ScreenShots

<p float="left">
  <img src="/screenShot/getWeather.png" width="400" />
  <img src="/screenShot/weatherSearch.png" width="400" /> 
</p>


# Tech stack & Open-source libraries

* **Retrofit2 & okHttp3** : For Network calls </br>
* **Architecture** : MVVM - Repository pattern </br>
* **Coroutines** for background operations like fetching network response </br>
* **Live Data** : To notify view for change </br>
* **koin** : For dependency injection </br>
* **Datainding** :for write expression Directly in xml to make certain view related logic </br>
* **Material-Components** :Material design components like ripple cardView.
 

# APIKEY

WeatherProject using the [WeatherApi](https://openweathermap.org/api.) for constructing RESTful API. </br>
Get your api key from here https://home.openweathermap.org/api_keys </br>
Please add your api key to apiKey field in local.properties to run </br>
