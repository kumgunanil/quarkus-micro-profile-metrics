package com.anilit.resource;

import com.anilit.restclient.JsonPlaceHolderRestClient;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.metrics.Metric;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/")
public class Resource {

    @RestClient
    JsonPlaceHolderRestClient jsonPlaceHolderRestClient;
    private long highestPrimeNumberSoFar = 2;

    @GET
    @Path("hello")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(){
        return Response.ok("Hello from Quarkus").build();
    }

    /**
     * This api will take a number and return whether the input number is prime number or not
     * @param number
     * @return
     */

    @POST
    @Path("{number}")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "CountOf_CheckIfPrime", description = "How many times this method/api is called.")
    @Timed(name = "TimeTaken_CheckIfPrime", description = "How much time api take to respond", unit = MetricUnits.MILLISECONDS)
    @Metered(name = "Metered_CheckIfPrime", description = "how frequent this api is called")
    public String checkIfPrime(@PathParam("number") long number){
        if (number <1){
            return "Only natural numbers can be prime numbers.";
        }
        if (number == 1){
            return "1 is not prime.";
        }
        if (number == 2) {
            return "2 is prime.";
        }
        if (number % 2 == 0) {
            return number + " is not prime, it is divisible by 2.";
        }
        for (int i = 3; i < Math.floor(Math.sqrt(number)) + 1; i = i + 2) {
            if (number % i == 0) {
                return number + " is not prime, is divisible by " + i + ".";
            }
        }
        if (number > highestPrimeNumberSoFar) {
            highestPrimeNumberSoFar = number;
        }
        return number + "  is prime.";
    }

    @Gauge(name = "highestPrimeNumberRequest", description = "What is the highest Prime number calculated so far. ", unit = MetricUnits.NONE)
    public Long getHighestPrimeNumber(){
        return highestPrimeNumberSoFar;
    }
}
