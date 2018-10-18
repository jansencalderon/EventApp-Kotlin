package tip.dgts.eventapp.app


import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*
import tip.dgts.eventapp.model.response.*


interface ApiInterface {




    @GET(Endpoints.GET_ALL_EVENTS)
    fun getAllEvents(@Header(Constants.AUTHORIZATION) authorization: String,
                     @Header(Constants.ACCEPT) accept: String,
                     @Query(Constants.USER_ID) userId: Int,
                     @Query("tag_id") tagId: Int): Single<EventListResponse>


    @GET(Endpoints.GET_ALL_LIKED_EVENTS)
    fun getAllLikedEvents(@Header(Constants.AUTHORIZATION) authorization: String,
                          @Header(Constants.ACCEPT) accept: String): Single<EventListResponse>

    @FormUrlEncoded
    @POST(Endpoints.RESERVE_EVENT)
    fun reserveEvent(@Header(Constants.AUTHORIZATION) authorization: String,
                     @Header(Constants.ACCEPT) accept: String,
                     @Field("event_id") eventId: Int,
                     @Field(Constants.USER_ID) user_id: Int): Call<BasicResponse>


    @GET(Endpoints.GET_USER_TICKETS)
    fun getTickets(@Header(Constants.AUTHORIZATION) authorization: String,
                   @Header(Constants.ACCEPT) accept: String,
                   @Path(Constants.USER_ID) user_id: Int): Single<TicketListResponse>


    @FormUrlEncoded
    @POST(Endpoints.LIKE_EVENT)
    fun likeEvent(@Header(Constants.AUTHORIZATION) authorization: String,
                  @Header(Constants.ACCEPT) accept: String,
                  @Path(Constants.USER_ID) user_id: Int,
                  @Field("event_id") eventId: Int): Single<BasicResponse>


    @DELETE(Endpoints.UNLIKE_EVENT)
    fun unlikeEvent(@Header(Constants.AUTHORIZATION) authorization: String,
                    @Header(Constants.ACCEPT) accept: String,
                    @Path(Constants.USER_ID) user_id: Int,
                    @Path("event_id") eventId: Int): Single<BasicResponse>


    @GET(Endpoints.GET_TAGS)
    fun getTags(@Header(Constants.AUTHORIZATION) authorization: String,
                @Header(Constants.ACCEPT) accept: String): Single<TagListResponse>

    @GET(Endpoints.GET_USER_INTERESTS)
    fun getInterests(@Header(Constants.AUTHORIZATION) authorization: String,
                     @Header(Constants.ACCEPT) accept: String,
                     @Path(Constants.USER_ID) user_id: Int): Single<InterestListResponse>

    @FormUrlEncoded
    @POST(Endpoints.SAVE_INTEREST)
    fun saveInterest(@Header(Constants.AUTHORIZATION) authorization: String,
                     @Header(Constants.ACCEPT) accept: String,
                     @Path(Constants.USER_ID) user_id: Int,
                     @Field("tag_id") tagId: Int): Single<BasicResponse>

    @DELETE(Endpoints.DELETE_INTEREST)
    fun deleteInterest(@Header(Constants.AUTHORIZATION) authorization: String,
                       @Header(Constants.ACCEPT) accept: String,
                       @Path(Constants.USER_ID) user_id: Int,
                       @Path("interest_id") interest_id: Int): Single<BasicResponse>


    @FormUrlEncoded
    @POST(Endpoints.USER_LOGIN)
    fun login(@Field(Constants.EMAIL) email: String,
              @Field(Constants.PASSWORD) password: String): Single<LoginResponse>


    @FormUrlEncoded
    @POST(Endpoints.USER_REGISTER)
    fun register(
            @Field(Constants.FIRST_NAME) firstName: String,
            @Field(Constants.LAST_NAME) lastName: String,
            @Field(Constants.EMAIL) username: String,
            @Field(Constants.CONTACT) contact: String,
            @Field(Constants.BIRTHDAY) birthday: String,
            @Field(Constants.ADDRESS) address: String,
            @Field(Constants.PASSWORD) password: String,
            @Field(Constants.PASSWORD_C) password_confirmation: String,
            @Header(Constants.ACCEPT) accept: String): Single<BasicResponse>

    @FormUrlEncoded
    @PUT(Endpoints.USER_UPDATE)
    fun updateUser(@Header(Constants.AUTHORIZATION) authorization: String,
                   @Header(Constants.ACCEPT) accept: String,
                   @Field(Constants.FIRST_NAME) firstName: String,
                   @Field(Constants.LAST_NAME) lastName: String,
                   @Field(Constants.EMAIL) username: String,
                   @Field(Constants.CONTACT) contact: String,
                   @Field(Constants.BIRTHDAY) birthday: String,
                   @Field(Constants.ADDRESS) address: String): Single<BasicResponse>

    @FormUrlEncoded
    @POST(Endpoints.ADD_FEEDBACK)
    fun addFeedback(@Header(Constants.AUTHORIZATION) authorization: String,
                    @Header(Constants.ACCEPT) accept: String,
                    @Path("event_id") eventId: Int,
                    @Field("user_id") userId: Int,
                    @Field("feedback") feedback: String,
                    @Field("rate") rate: Int): Single<BasicResponse>


    @GET(Endpoints.GET_EVENT_FEEDBACK)
    fun getFeedbacks(@Header(Constants.AUTHORIZATION) authorization: String,
                     @Header(Constants.ACCEPT) accept: String,
                     @Path("event_id") event_id: Int): Single<FeedbackListResponse>


    @GET(Endpoints.GET_EVENT_POLLS)
    fun getPolls(@Header(Constants.AUTHORIZATION) authorization: String,
                 @Header(Constants.ACCEPT) accept: String,
                 @Path("event_id") event_id: Int,
                 @Query(Constants.USER_ID) user_id: Int): Single<PollListResponse>


    @GET(Endpoints.GET_EVENT_POLLS_NOMINEE)
    fun getNominees(@Header(Constants.AUTHORIZATION) authorization: String,
                    @Header(Constants.ACCEPT) accept: String,
                    @Path("event_id") event_id: Int,
                    @Path("voting_id") voting_id: Int,
                    @Query(Constants.USER_ID) user_id: Int): Single<NomineeListResponse>

    @FormUrlEncoded
    @POST(Endpoints.ADD_EVENT_POLL_VOTE)
    fun addVote(@Header(Constants.AUTHORIZATION) authorization: String,
                @Header(Constants.ACCEPT) accept: String,
                @Path("event_id") event_id: Int,
                @Path("voting_id") voting_id: Int,
                @Field(Constants.USER_ID) user_id: Int,
                @Field("nominee_id") nominee_id: Int): Single<BasicResponse>

}
