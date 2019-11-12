package apit.net.sa.searchcontactapp.Network;

import java.util.List;

import apit.net.sa.searchcontactapp.Models.UserModel;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("contacts.php")
    Single<List<UserModel>> getUserData(@Query("source") String source, @Query("search") String query);

    @GET("contacts.php")
    Single<List<UserModel>> getRemotFilterUser(@Query("source") String source, @Query("search") String query);
}
