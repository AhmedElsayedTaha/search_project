package apit.net.sa.searchcontactapp.views;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.widget.EditText;
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
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class LocalSearchActivity extends AppCompatActivity implements UserInterface.UserViewInterface {

    public static final String TAG =LocalSearchActivity.ACTIVITY_SERVICE;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.input_search)
    EditText inpuetSearch;
    ContactsAdapterFilter adapterFilter;
    UserInterface.userPresenterInterface presenterInterface;
    Unbinder unbinder;
    ProgressDialog progressDialog;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    List<UserModel> resultList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_search);
        unbinder = ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
         * Define the recyclerView
         */
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

        compositeDisposable.add(RxTextView.textChangeEvents(inpuetSearch)
                .skipInitialValue()
                .distinctUntilChanged()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TextViewTextChangeEvent>() {
                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        Log.e(TAG,"search is "+textViewTextChangeEvent.text());
                        adapterFilter.getFilter().filter(textViewTextChangeEvent.text());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "DONE");
                    }
                })
        );






    }

    @Override
    public void getUserSuccess(List<UserModel> userModels) {
        resultList.clear();
        if(userModels!=null&&userModels.size()>0){
            resultList.addAll(userModels);
            adapterFilter = new ContactsAdapterFilter(LocalSearchActivity.this, resultList
                    , new ContactsAdapterFilter.ContactsAdapterListener() {
                @Override
                public void onContactSelected(UserModel userModel) {
                    Toast.makeText(LocalSearchActivity.this,"item",Toast.LENGTH_LONG).show();
                }
            });
            recyclerView.setAdapter(adapterFilter);

        }
        progressDialog.dismiss();
    }

    @Override
    public void getUserFailed(Throwable t) {
        progressDialog.dismiss();
    }

    @Override
    public void getUserSuccessRemote(List<UserModel> userModels) {

    }

    @Override
    public void getUserFailedRemote(Throwable t) {

    }


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        unbinder.unbind();
        super.onDestroy();
    }
}
