package apit.net.sa.searchcontactapp.Repository;

import android.widget.EditText;

import java.util.List;

import apit.net.sa.searchcontactapp.Models.UserModel;

public interface UserInterface {
    interface userPresenterInterface {
        void requestUserData(String source,String query);
        //Remote
        void requestRemoteData(String source, String query, EditText editText);
    }

    interface UserRepositoryInterface{
        interface OnFinishedListner{
            void onFnished(List<UserModel> userModels);
            void onFailed(Throwable t);

            void onFnishedRemote(List<UserModel> userModels);
            void onFailedRemote(Throwable t);
        }
        void getUsers(OnFinishedListner onFinishedListner,String source,String query);
        void getUsersRemotes(OnFinishedListner onFinishedListner,String source,String query,EditText editText);

    }

    interface UserViewInterface{
        void getUserSuccess(List<UserModel> userModels);
        void getUserFailed(Throwable t);

        void getUserSuccessRemote(List<UserModel> userModels);
        void getUserFailedRemote(Throwable t);
    }
}
