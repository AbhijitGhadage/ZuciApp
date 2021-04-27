package com.zuci.zuciapp.network;

import com.zuci.zuciapp.firebase.notification.RequestNotificaton;
import com.zuci.zuciapp.ui.agoraLive.LiveCallResponse;
import com.zuci.zuciapp.ui.agoraLive.liveVideo.LiveCallCoinsDeductsModel;
import com.zuci.zuciapp.ui.agoraLive.liveVideo.LiveCallCoinsDeductsModelResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.VideoAudioCutCoinsModel;
import com.zuci.zuciapp.ui.transaction.PointsAddCoinsModel;
import com.zuci.zuciapp.ui.transaction.CashWithdrawalModel;
import com.zuci.zuciapp.ui.home.CoinsResponse;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.DeductionModel;
import com.zuci.zuciapp.ui.matchingPartnerQA.MatchingPartnerQueAnsModel;
import com.zuci.zuciapp.ui.matchingPartnerQA.QueAnsModel;
import com.zuci.zuciapp.ui.reels.VideoAddLikeModel;
import com.zuci.zuciapp.ui.transaction.AddCoinsRateModel;
import com.zuci.zuciapp.utils.AppResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.zuci.zuciapp.ui.feedback.FeedbackModel;
import com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory.ChatListModel;
import com.zuci.zuciapp.ui.signUp.SignUpModel;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface TipsGoApiService {

    //    @FormUrlEncoded
    @POST("api/ZucciHome/NewRegistration")
    Single<JsonElement> signUpUser(@Body SignUpModel signUpModel);

    //  Social login
    @POST("api/ZucciHome/socialLogin")
    Single<JsonElement> socialLogin(@Body SignUpModel signUpModel);

    //    @Multipart
//    @FormUrlEncoded
    @POST("api/ZucciHome/Login")
    Single<JsonElement> loginUser(@Query("Email") String emailId,
                                  @Query("password") String password,
                                  @Query("DeviceToken") String deviceToken);


    //    @FormUrlEncoded
    @POST("api/ZucciHome/sendOtpforgotPassword")
    Single<JsonElement> forgotPassword(@Query("EmailId") String emailId);

    @GET("api/ZucciHome/GetuserByTypeOrId")
    Single<JsonElement> getHomePageList(@Query("userId") Integer userId,
                                        @Query("pageNo") Integer pageNo,
                                        @Query("TypeId") Integer TypeId);

    //  profile update or create
    @POST("api/ZucciHome/NewRegistration")
    Single<JsonElement> profileCreate(@Body SignUpModel signUpModel);

    //  contry list
    @GET("api/ZucciHome/GetCountry")
    Single<JsonElement> countryList();

    // otp verify
    @POST("api/ZucciHome/VerifyOtp")
    Single<JsonElement> otpVerifyRegister(@Query("RegistrationId") int registerIdInt,
                                          @Query("EmailId") String emailId,
                                          @Query("Otp") String otp);

    // change Password
    @FormUrlEncoded
    @POST("api/ZucciHome/ResetPassword")
    Single<JsonElement> changePassword(@Field("EmailId") String emailId,
                                       @Field("Oldpassword") String oldPassword,
                                       @Field("NewPassword") String newPassword);

    // otp verify
    @POST("api/ZucciHome/setForgotPassword")
    Single<JsonElement> setNewPassword(@Query("RegistrationId") int registerIdInt,
                                       @Query("EmailId") String emailId,
                                       @Query("Password") String password);

    //  feedback
    @POST("api/ZucciHome/feedback")
    Single<JsonElement> feedback(@Body FeedbackModel feedbackModel);


    // image upload and operations
    @Multipart
    @POST("api/ZucciHome/addMediaFile")
    Single<JsonElement> uploadGalleryImg(
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part imageName);

    // gallery list
    @GET("api/ZucciHome/getMediaListByType")
    Single<JsonElement> getGalleryList(
            @Query("RegistrationId") Integer RegistrationId,
            @Query("MediaType") String MediaType,
            @Query("LoginRegistrationId") Integer LoginRegistrationId);

    // delete image
    @POST("api/ZucciHome/deleteMediaFile")
    Single<JsonElement> deleteImage(@Query("imageId") long imageId);

    // profile update
    @Multipart
    @POST("api/ZucciHome/UpdateProfileImage")
    Single<JsonElement> uploadProfileImg(
            @Part("RegistrationId") RequestBody registrationId,
            @Part MultipartBody.Part imageName);

    @POST("api/ZucciHome/GetAccessTokens")
    Single<CallResponse> getCallResponseApi(
            @Query("SenderUserId") int SenderUserId,
            @Query("ReceiverUserId") int ReceiverUserId,
            @Query("CallType") String CallType);

    @POST("api/ZucciHome/UpdateCallStatus")
    Single<AppResponse> updateCallStatus(@Query("CallId") long CallId);

    @GET("api/ZucciHome/UserInfo")
    Single<List<ChatListModel>> getChatUserList(@Query("UserId") long UserId);

    // notification call
    @Headers({"Authorization: key=AAAA1L2_CcM:APA91bGqeNum1CSpmYLrx-6qrTP8qAFeitiEZkq2PftOs4vshsoAOjp2j8hP16TgdWa748gpWAf92-y5yox9Ra7MGIcFugi4RU50wKKUY5oxASexRf8SqQtojsNdE_doFItSbwuRSElT",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Call<JsonElement> sendChatNotification(@Body RequestNotificaton requestNotificaton);

    //  cash withdrawal
    @POST("api/ZucciHome/Cashwithdrawal")
    Single<JsonElement> cashWithdrawal(@Body CashWithdrawalModel cashWithdrawalModel);

    //  addCoins
    @POST("api/ZucciHome/addCoins")
    Single<JsonElement> addCoins(@Body PointsAddCoinsModel PointsAddCoinsModel);


    // reels video list
    @GET("api/ZucciHome/GetAllVideos")
    Single<JsonElement> getVideoList(@Query("RegistrationId") Integer RegistrationId);

    //  coins rate
    @POST("api/ZucciHome/SetCoins")
    Single<JsonElement> coinsRatesApi(@Body AddCoinsRateModel addCoinsRateModel);

    //  likes api
    @POST("api/ZucciHome/AddMediaLike")
    Single<JsonElement> getLikesApi(@Body VideoAddLikeModel videoAddLikeModel);

    // update media status
//    @FormUrlEncoded
    @POST("api/ZucciHome/UpdateMediaStatus")
    Single<JsonElement> updateMediaStatus(@Query("RegistrationId") int RegistrationId,
                                          @Query("MediaId") int MediaId,
                                          @Query("Status") String Status);

    // Deducts Coins image / video
    @POST("api/ZucciHome/DeductsCoins")
    Single<JsonElement> deductsCoins(@Body DeductionModel deductionModel);

    // get total coins
    @GET("api/ZucciHome/GetUserCoins")
    Single<JsonElement> totalCoins(@Query("RegistrationId") Integer RegistrationId);

    // Deducts Coins call
    @POST("api/ZucciHome/AfterCallDeductCoins")
    Single<JsonElement> deductsCoinsCalls(@Body VideoAudioCutCoinsModel videoAudioCutCoinsModel);

    @GET("api/ZucciHome/GetSelfInfoQuestion")
    Single<List<QueAnsModel>> getQueAnsListApi();

    @POST("api/ZucciHome/matchingPartnerQueAns")
    Single<AppResponse> addMatchingPartnerQueAnsApi(@Body List<MatchingPartnerQueAnsModel> partnerQueAnsModels);

    /*new api call*/
    @GET("api/ZucciHome/GetUserCoins")
    Call<CoinsResponse> getTotalCoins(@Query("RegistrationId") Integer RegistrationId);

    // transaction log list
    @GET("api/ZucciHome/GetCoinHistory")
    Single<JsonElement> transactionLog(@Query("RegistrationId") Integer RegistrationId);


    // following api
    @POST("api/ZucciHome/AddFollowerUser")
    Single<JsonElement> followApi(@Query("LoginId") long LoginId,
                                  @Query("FollowersId") long FollowersId);

    // update media status
//    @FormUrlEncoded
    @POST("api/ZucciHome/AddMediaViews")
    Single<JsonElement> videoViews(@Query("RegistrationId") long RegistrationId,
                                   @Query("MediaRegId") long MediaRegId,
                                   @Query("MediaId") long MediaId);

    // live call
    @GET("api/ZucciHome/LiveUserList")
    Single<LiveCallResponse> liveCallResponseApi(@Query("RegistrationId") long RegistrationId);

    /*new api call*/
    @POST("api/ZucciHome/LiveCallDeductsCoins")
    Call<LiveCallCoinsDeductsModelResponse> getLiveDeductsCoins(@Body LiveCallCoinsDeductsModel liveCallCoinsDeductsModel);
}

