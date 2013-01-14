# Mobilehitchhiker application

Mobilehitchhiker consists of two applications - a frontent (Android app) and
a backend (web server).

## Backend

The backend is responsible for storing and retrieving trips. Communication with
frontend is done via HTTP protocol (Python Flask web microframework). The
server handles POST and GET HTTP request. For POST request a valid JSON should
be passed as HTTP body parameter. JSON structure is as following:
```javascript
{
    "end_long": 11.0294511,
    "start": "Brixen, South Tyrol, Italy",
    "end": "Rovereto, South Tyrol, Italy",
    "end_lat": 45.8575835,
    "start_lat": 46.7028803,
    "start_long": 11.6929562,
    "start_date": "2015-12-29T12:10:23",
    "contact": "some@email.com"
}
```

When a valid JSON is received, HTTP server stores it into Sqlite3 DB.
To be able to search for trips the server needs to receive following arguments:
* start point lat/long
* end point lat/long
* trip date
Then it iterates over all DB records (trips) of given date and calculates
distance (from start and end points). It returns a list of closest trips
encoded as a JSON (structure is the same as in the POST request).

## Frontend

The frontend (Android app) consists of two activities:
* MainActivity (responsible for request form handling)
* TripMap (responsible for showing trip map and details)

When a user starts the application, a landing window (MainActivity) is going to be opened.
In a landing page there is one checkbox used to use GPS for start point of a
trip. Also there are two text edit fields for names of start/end points.
When a "Create trip" or "Find trip" button is pressed, start/end addresses are
being converted into coordinates by using Geocoder (if GPS checkbox is checked,
then start point address is not used). Also, there is datepicker, for defining
a date.

When user click on one of the button, HTTP request to the backend is being
made. In "Create trip" case - POST request with JSON encoded data and in "Find
trip" - GET request. After this TripMap activity is opened, where trip details
a shown on Google Maps.
