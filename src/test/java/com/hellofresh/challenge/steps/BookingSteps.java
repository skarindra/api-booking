package com.hellofresh.challenge.steps;

import com.hellofresh.challenge.constant.Constant;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.Assert;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.hellofresh.challenge.utils.ApiUtil.getJsonPath;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by sekarayukarindra.
 */
public class BookingSteps {

    private static Response response;
    private static String roomid;
    private static String bookingid;
    private static String firstname;
    private static String lastname;

    /** Multi bookings */
    private static String date_checkin_1;
    private static String date_checkout_1;
    private static String fname_1;
    private static String lname_1;
    private static String email1;
    private static String phone1;
    private static String date_checkin_2;
    private static String date_checkout_2;
    private static String fname_2;
    private static String lname_2;
    private static String email2;
    private static String phone2;

    final static Logger LOGGER = LogManager.getLogger(BookingSteps.class);

    private void getRoom(String roomid){
        response = SerenityRest.given().when().
                get(Constant.BASE_URL +"?roomid="+roomid);
        response.then().log().all().assertThat().spec(new ResponseSpecBuilder().expectStatusCode(200).build());
    }

    @Step
    public void verifyRoomId(String roomId){
        response.then().assertThat()
                .spec(new ResponseSpecBuilder().expectBody("bookings[0].roomid",notNullValue()).build());
        String roomid = getJsonPath("bookings[0]",response).getString("roomid");
        Assert.assertEquals(roomId, roomid);
    }

    @Step
    public void createBooking(String param){
        switch (param){
            case "random" : createRandomBooking();break;
            case "multiple" : createTwoBookingSameRoom();break;
        }
    }

    @Step
    public void verifyRecentlyCreatedBooking(){
        verifyCreatedBooking(roomid, firstname, lastname);
    }

    private void createRandomBooking(){
        roomid = RandomStringUtils.randomNumeric(4);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String date_now = formatter.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH,2);
        String date_checkout = formatter.format(calendar.getTime());
        String timestamp = String.valueOf(new Date().getTime());
        String email = "hf_"+timestamp+"@mail"+timestamp.substring(7)+".com";
        firstname = "first_"+timestamp.substring(5);
        lastname = "last_"+timestamp.substring(5);
        String phone = RandomStringUtils.randomNumeric(11);

        createRoomBooking(roomid, date_now, date_checkout, firstname, lastname, email, phone);
        bookingid = getJsonPath(response).getString("bookingid");
    }

    private void createRoomBooking(String roomId, String checkin, String checkout, String fname, String lname, String email, String phone){
        Map<String, Object> body = new HashMap<>();
        body.put("bookingdates", new HashMap<String, Object>(){{
            put("checkin",checkin);
            put("checkout", checkout);
        }});
        body.put("depositpaid", true);
        body.put("email", email);
        body.put("firstname", fname);
        body.put("lastname", lname);
        body.put("phone", phone);
        body.put("roomid", roomId);

        String jsonBody = String.valueOf(new JSONObject(body));

        response = SerenityRest.given().header("Content-Type","application/json").header("Accept", "application/json")
                .body(jsonBody)
                .log().all().when().post(Constant.BASE_URL);
        response.then().assertThat().spec(new ResponseSpecBuilder().expectStatusCode(201).build());
    }

    private void createTwoBookingSameRoom(){
        roomid = RandomStringUtils.randomNumeric(4);
        date_checkin_1 = "2019-12-18";
        date_checkout_1 = "2019-12-20";
        fname_1 = "hf1_fname"+RandomStringUtils.randomAlphanumeric(3);
        lname_1 = "hf1_lname"+RandomStringUtils.randomAlphanumeric(3);
        email1 = "hf1"+RandomStringUtils.randomAlphanumeric(3)+"@mail.com";
        phone1 = RandomStringUtils.randomNumeric(11);

        date_checkin_2 = "2019-12-20";
        date_checkout_2 = "2019-12-23";
        fname_2 = "hf2_fname"+RandomStringUtils.randomAlphanumeric(3);
        lname_2 = "hf2_lname"+RandomStringUtils.randomAlphanumeric(3);
        email2 = "hf2"+RandomStringUtils.randomAlphanumeric(3)+"@mail.com";
        phone2 = RandomStringUtils.randomNumeric(11);

        createRoomBooking(roomid, date_checkin_1, date_checkout_1, fname_1, lname_1, email1, phone1);
        bookingid = getJsonPath(response).getString("bookingid");
        verifyCreatedBooking(roomid, fname_1, lname_1);
        createRoomBooking(roomid, date_checkin_2, date_checkout_2, fname_2, lname_2, email2, phone2);
        bookingid = getJsonPath(response).getString("bookingid");
        verifyCreatedBooking(roomid, fname_2, lname_2);
    }

    private void verifyCreatedBooking(String roomid, String fname, String lname){
        response = SerenityRest.given().when().
                get(Constant.BASE_URL+bookingid);
        response.then().log().all().assertThat().spec(new ResponseSpecBuilder().expectStatusCode(200).build());
        String recentRoomId = getJsonPath(response).getString("roomid");
        String recentFirstname = getJsonPath(response).getString("firstname");
        String recentLastname = getJsonPath(response).getString("lastname");
        LOGGER.info("recentRoomId : "+ recentRoomId);
        Assert.assertEquals(recentRoomId, roomid);
        Assert.assertEquals(recentFirstname, fname);
        Assert.assertEquals(recentLastname, lname);
    }

    @Step
    public void getRecentBookingByRoomid(){
        getRoom(roomid);
    }

    @Step
    public void verifyMultiBooking(){
        String recentRoomid1 = getJsonPath("bookings", response).getString("roomid[0]");
        String recentRoomid2 = getJsonPath("bookings", response).getString("roomid[1]");
        String recentFirstname1 = getJsonPath("bookings",response).getString("firstname[0]");
        String recentLastname1 = getJsonPath("bookings",response).getString("lastname[0]");
        String recentCheckin1 = getJsonPath("bookings.bookingdates[0]", response).getString("checkin");
        String recentCheckout1 = getJsonPath("bookings.bookingdates[0]", response).getString("checkout");
        String recentEmail1 = getJsonPath("bookings", response).getString("email[0]");
        String recentPhone1 = getJsonPath("bookings", response).getString("phone[0]");

        String recentFirstname2 = getJsonPath("bookings",response).getString("firstname[1]");
        String recentLastname2 = getJsonPath("bookings",response).getString("lastname[1]");
        String recentCheckin2 = getJsonPath("bookings.bookingdates[1]", response).getString("checkin");
        String recentCheckout2 = getJsonPath("bookings.bookingdates[1]", response).getString("checkout");
        String recentEmail2 = getJsonPath("bookings", response).getString("email[1]");
        String recentPhone2 = getJsonPath("bookings", response).getString("phone[1]");

        Assert.assertEquals(recentRoomid1, roomid);
        Assert.assertEquals(recentRoomid2, roomid);
        Assert.assertEquals(recentFirstname1, fname_1);
        Assert.assertEquals(recentLastname1, lname_1);
        Assert.assertEquals(recentFirstname2, fname_2);
        Assert.assertEquals(recentLastname2, lname_2);
        Assert.assertEquals(recentCheckin1, date_checkin_1);
        Assert.assertEquals(recentCheckout1, date_checkout_1);
        Assert.assertEquals(recentCheckin2, date_checkin_2);
        Assert.assertEquals(recentCheckout2, date_checkout_2);
        Assert.assertEquals(recentEmail1, email1);
        Assert.assertEquals(recentEmail2, email2);
        Assert.assertEquals(recentPhone1, phone1);
        Assert.assertEquals(recentPhone2, phone2);
    }
}
