(ns ^:skip-wiki cognitect.aws.http
  "Impl, don't call directly."
  (:require [clojure.core.async :as a]))

(defprotocol HttpClient
  (-submit [_ request channel]
    "Submit an http request, channel will be filled with response. Returns ch.

     Request map:

     :scheme                 :http or :https
     :server-name            string
     :server-port            integer
     :uri                    string
     :query-string           string, optional
     :request-method         :get/:post/:put/:head/:delete
     :headers                map from downcased string to string
     :body                   ByteBuffer, optional
     :timeout-msec           opt, total request send/receive timeout
     :meta                   opt, data to be added to the response map

     content-type must be specified in the headers map
     content-length is derived from the ByteBuffer passed to body

     Response map:

     :status            integer HTTP status code
     :body              ByteBuffer, optional
     :headers           map from downcased string to string
     :meta              opt, data from the request

     On error, response map is per cognitect.anomalies.

     Alpha. This will absolutely change.")
  (-stop [_] "Stops the client, releasing resources"))

(defn submit
  ([client request]
   (-submit client request (a/chan 1)))
  ([client request channel]
   (-submit client request channel)))

(defn stop
  "Stops the client, releasing resources.

  Alpha. Subject to change."
  [client]
  (-stop client))
