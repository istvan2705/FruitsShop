package stepdefs;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Assert;



import java.io.IOException;

public class StepsDefinition {

    private static final String BASE_URL = "https://api.predic8.de:443";
    private static Logger log = Logger.getLogger(StepsDefinition.class);
    private static JSONObject customersJsonData = null;
    private static String ORDER_URL = null;
    private static String CUSTOMER_URL = null;
    private static Request request;
    private static Response response;
    private static RequestBody body;
    private static long responseTime;
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("text/plain");

    @Given("^user wants to create customer with first name \"([^\"]*)\" and last name \"([^\"]*)\"$")
    public void addParametersToCustomerJsonBody(String firstName, String lastName) {
        JSONObject json = new JSONObject();
        json.put("firstname", firstName);
        json.put("lastname", lastName);
        body = getRequestBody(json.toString());
    }

    @When("users submits POST request to the endpoint$")
    public void createCustomer() throws IOException {
        Request request = getRequest(body, "/shop/customers/", "POST");
        response = client.newCall(request).execute();
        responseTime = getResponseTime(response);
        String jsonData = response.body().string();
        customersJsonData = new JSONObject(jsonData);
        log.info(customersJsonData);
        CUSTOMER_URL = customersJsonData.getString("customer_url");
        log.info("Customer is created with URL " + CUSTOMER_URL);
    }

    @Given("^user submits GET request to the endpoint$")
    public void getCustomerByID() throws IOException {
        Request request = getRequest(null, CUSTOMER_URL, "GET");
        response = client.newCall(request).execute();
        responseTime = getResponseTime(response);
        String jsonData = response.body().string();
        customersJsonData = new JSONObject(jsonData);
        log.info(customersJsonData);
        log.info("Customer is retrieved " + CUSTOMER_URL);
    }

    @Given("^user wants update customers name with new first name \"([^\"]*)\"$")
    public void addParameterToCustomerJsonBody(String firstName) {
        JSONObject json = new JSONObject();
        json.put("firstname", firstName);
        body = getRequestBody(json.toString());
    }

    @When("^user submits PATCH request to the endpoint$")
    public void updateCustomer() throws IOException {
        Request request = getRequest(body, CUSTOMER_URL, "PATCH");
        response = client.newCall(request).execute();
        responseTime = getResponseTime(response);
        String jsonData = response.body().string();
        customersJsonData = new JSONObject(jsonData);
        log.info(customersJsonData);
        log.info("Customer is updated");
    }

    private Request getRequest(RequestBody body, String endpoint, String method) {
        return new Request.Builder()
                .url(BASE_URL + endpoint)
                .method(method, body)
                .addHeader("Content-Type", "text/plain")
                .build();
    }

    private RequestBody getRequestBody(String jsonString) {
        return RequestBody.create(mediaType, jsonString);
    }

    @Given("^user adds parameter \"([^\"]*)\" into POST request body$")
    public void addParameterToOrder(String url) {
        JSONObject json = new JSONObject();
        json.put(url, CUSTOMER_URL);
        body = getRequestBody(json.toString());
    }

    @When("^user sends POST request to the endpoint$")
    public void createOrder() throws Throwable {
        Request request = getRequest(body, CUSTOMER_URL + "/orders/", "POST");
        response = client.newCall(request).execute();
        responseTime = getResponseTime(response);
        String jsonData = response.body().string();
        customersJsonData = new JSONObject(jsonData);
        log.info(customersJsonData);
        ORDER_URL = customersJsonData.getString("items_url").substring(0, 17);
        log.info("Order is created for customer with " + ORDER_URL);
    }

    @Given("^user submits DELETE request to delete the order$")
    public void deleteOrder() throws IOException {
        Request request = getRequest(null, ORDER_URL, "DELETE");
        response = client.newCall(request).execute();
        responseTime = getResponseTime(response);
        log.info("Order is removed");
    }

    @Given("^user submits DELETE request to delete the customer$")
    public void deleteCustomer() throws IOException {
        Request request = getRequest(null, CUSTOMER_URL, "DELETE");
        response = client.newCall(request).execute();
        responseTime = getResponseTime(response);
        log.info("Order is removed");
    }

    @Given("^user wants to get information regarding \"([^\"]*)\" orders with state \"([^\"]*)\" from \"([^\"]*)\" page$")
    public Request addQueryParamsToGetOrders(String amountOfOrders, String state, String page) {
        HttpUrl.Builder httpBuider = HttpUrl.parse(BASE_URL + "/shop/orders/").newBuilder();
        httpBuider.addQueryParameter("page", page);
        httpBuider.addQueryParameter("limit", amountOfOrders);
        httpBuider.addQueryParameter("state", state);
        request = new Request.Builder().url(httpBuider.build()).build();
        return request;
    }

    @When("^user retrieves the order by parameters$")
    public void getOrders() throws IOException {
        response = client.newCall(request).execute();
        responseTime = getResponseTime(response);
        String jsonData = response.body().string();
        customersJsonData = new JSONObject(jsonData);
        log.info(customersJsonData);
    }

    @Then("^the status code should be \"([^\"]*)\"$")
    public void theStatusCodeIs(int statusCode) {
        Assert.assertEquals(statusCode, response.code());
    }

    @Then("^the response time should be less than 2000 ms$")
    public void theResponseTimeIs() {
        log.info("Response time is "+responseTime);
        Assert.assertTrue(responseTime < 2000);
    }

    @Given("user adds \"([^\"]*)\" id as query parameter for GET categories by ID")
    public void userAddsIdAsQueryParameterForGETCategoriesByID(String categoryID) throws IOException {
        request = new Request.Builder()
                .url(BASE_URL + "/shop/categories/" + categoryID)
                .method("GET", null).build();
    }

    @When("user submits GET request to the get category by ID endpoint")
    public void userSubmitsGETRequestToTheGetCategoryByIDEndpoint() throws IOException {
        response = client.newCall(request).execute();
        responseTime = getResponseTime(response);
        String jsonData = response.body().string();
        customersJsonData = new JSONObject(jsonData);
        log.info(customersJsonData);
    }

    public long getResponseTime(Response response) {
        long tx = response.sentRequestAtMillis();
        long rx = response.receivedResponseAtMillis();
        return rx - tx;
    }
}
