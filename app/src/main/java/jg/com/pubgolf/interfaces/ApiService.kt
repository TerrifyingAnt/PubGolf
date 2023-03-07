package jg.com.pubgolf.interfaces

import jg.com.pubgolf.classes.User
import io.reactivex.rxjava3.core.Single
import jg.com.pubgolf.debugutils.Constants
import jg.com.pubgolf.classes.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST(Constants.base + Constants.registerLink)
    fun registerUser(@Body user: User): Single<UserResponse>

    @POST(Constants.base + Constants.enterLink)
    fun enterUser(@Body user: User): Single<UserResponse>
}