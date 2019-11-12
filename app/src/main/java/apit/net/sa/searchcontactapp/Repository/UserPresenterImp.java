package apit.net.sa.searchcontactapp.Repository;

import android.widget.EditText;

import java.util.List;

import apit.net.sa.searchcontactapp.Models.UserModel;

public class UserPresenterImp implements UserInterface.userPresenterInterface,UserInterface.UserRepositoryInterface.OnFinishedListner {
    private UserInterface.UserRepositoryInterface userRepositoryInterface;
    private UserInterface.UserViewInterface userViewInterface;

    public UserPresenterImp(UserInterface.UserRepositoryInterface userRepositoryInterface, UserInterface.UserViewInterface userViewInterface) {
        this.userRepositoryInterface = userRepositoryInterface;
        this.userViewInterface = userViewInterface;
    }

    @Override
    public void requestUserData(String source, String query) {
        userRepositoryInterface.getUsers(this,source,query);
    }

    @Override
    public void requestRemoteData(String source, String query, EditText editText) {
        userRepositoryInterface.getUsersRemotes(this,source,query,editText);
    }

    @Override
    public void onFnished(List<UserModel> userModels) {
        if(userViewInterface!=null)
            userViewInterface.getUserSuccess(userModels);
    }

    @Override
    public void onFailed(Throwable t) {
        if(userViewInterface!=null)
            userViewInterface.getUserFailed(t);
    }

    @Override
    public void onFnishedRemote(List<UserModel> userModels) {
        if(userViewInterface!=null)
            userViewInterface.getUserSuccessRemote(userModels);
    }

    @Override
    public void onFailedRemote(Throwable t) {
        if(userViewInterface!=null)
            userViewInterface.getUserFailedRemote(t);
    }
}
