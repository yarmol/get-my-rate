# GET MY RATE Web Service

Main functionality is to provide current exchange rate of Bitcoin, and history of bitcoin rates.

Architecture notes 
--

Application developed as simple Spring Boot Application. There is standart 3 layers inside application. 
 * Controller layer, where client service communicate with
 * Service layer, where data retrieving precessed
 * Persistence layer, where data are stored.

There two about application logic.
First is to integrate with 3-rd party services, which have REST external API. But in this way 
the sence of this application is only to proxy requests to 3-rd party system. 
That's why I decided to have real persistence layer, but in simple way.
I decided to use embedded implementation of MariaDB - MariaDB4j. 
Reasons: 
 * we do not need standalone DB server for simple logic
 * easy application starting
 * easy prototyping
 * in case to switch to standalone DB server - we can do it in few changes
 * MariaDB close to MySQL, and MySQL is close to Aurora MySQL that is used by SearchMetrics

During Spring Boot application start, MariaDB instance is started also. And during start all history 
BTC/USD data are inserted into DB.

For manual testing reasons Swagger GUI also was added. So it is avaliable on host:8080/swagger-ui.html 
when application are started

Running: 
--
Running application is possible with command:
```
gradle run
```
After successful running we will see 
```
	Application 'get-my-rate' is running! Access URLs:
	Local: 		<HOST>:8080
	Swagger: 	<HOST>:8080/swagger-ui.html
	External: 	<HOST>:8080
```

API Methods: 
--


* GET /api/current getting exchange rate (on current date).
Response format is:

```
{
    date: "2018-01-29",
    rate: 11446.54,
    currency_from: "BTC",
    currency_to: "USD"
}
```
**Response code 200** - in case of success, 
              **404** - in case of rate was not found
              
  
* GET /api/history getting exchange rate (on current date).
 
  Query parameters:
      * from - date in format of yyyy-mm-dd
      * to  - date in format of yyyy-mm-dd (max date is is 2019-04-24)
  Response format is:
  
  ```
  [{
      date: "2018-01-29",
      rate: 11446.54,
      currency_from: "BTC",
      currency_to: "USD"
  },
  {
        date: "2018-01-30",
        rate: 10000,
        currency_from: "BTC",
        currency_to: "USD"
   }...
  ]
  ```
  **Response code  200** - in case of success, 
                 **400** - in case of request error, query dates are absent or not valid
                 **422** - in case of non parseble dates
                
                

         
                
    

 