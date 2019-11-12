package apit.net.sa.searchcontactapp.views;

import android.app.ProgressDialog;
import android.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import apit.net.sa.searchcontactapp.Adapters.ContactsAdapterFilter;
import apit.net.sa.searchcontactapp.Models.UserModel;
import apit.net.sa.searchcontactapp.R;
import apit.net.sa.searchcontactapp.Repository.RepositoryImp;
import apit.net.sa.searchcontactapp.Repository.UserInterface;
import apit.net.sa.searchcontactapp.Repository.UserPresenterImp;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RemoteSearchActivity extends AppCompatActivity implements UserInterface.UserViewInterface {

    Unbinder unbinder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.input_search)
    EditText inputSearch;
    List<UserModel> resultList = new ArrayList<>();
    ContactsAdapterFilter adapterFilter;
    UserInterface.userPresenterInterface presenterInterface;
    ProgressDialog progressDialog;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_search);
        unbinder = ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        presenterInterface = new UserPresenterImp(new RepositoryImp(),this);
        presenterInterface.requestUserData("gmail",null);

        compositeDisposable.add(RxTextView.textChangeEvents(inputSearch)
        .skipInitialValue()
        .distinctUntilChanged()
        .debounce(300,TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                Log.e("onNext"," "+textViewTextChangeEvent.text().toString());
                presenterInterface.requestRemoteData(null,textViewTextChangeEvent.text().toString(),inputSearch);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));





    }

    @Override
    public void getUserSuccess(List<UserModel> userModels) {
        resultList.clear();
        if(userModels!=null&&userModels.size()>0){
            resultList.addAll(userModels);
            adapterFilter = new ContactsAdapterFilter(RemoteSearchActivity.this, resultList
                    , new ContactsAdapterFilter.ContactsAdapterListener() {
                @Override
                public void onContactSelected(UserModel userModel) {
                    Toast.makeText(RemoteSearchActivity.this,"item",Toast.LENGTH_LONG).show();
                }
            });
            recyclerView.setAdapter(adapterFilter);

        }
        progressDialog.dismiss();
    }

    @Override
    public void getUserFailed(Throwable t) {

    }

    @Override
    public void getUserSuccessRemote(List<UserModel> userModels) {
        if(userModels!=null&&userModels.size()>0) {
           Toast.makeText(RemoteSearchActivity.this,"remote",Toast.LENGTH_SHORT).show();
           resultList.clear();
           resultList.addAll(userModels);
           adapterFilter.notifyDataSetChanged();
            adapterFilter = new ContactsAdapterFilter(RemoteSearchActivity.this, resultList
                    , new ContactsAdapterFilter.ContactsAdapterListener() {
                @Override
                public void onContactSelected(UserModel userModel) {
                    Toast.makeText(RemoteSearchActivity.this,"item",Toast.LENGTH_LONG).show();
                }
            });
            recyclerView.setAdapter(adapterFilter);
        }
    }

    @Override
    public void getUserFailedRemote(Throwable t) {
        Toast.makeText(RemoteSearchActivity.this,"failed",Toast.LENGTH_SHORT).show();
        Log.e("failedd","error is "+t.getMessage());
    }
}
